#!/bin/sh
#
# Reference: http://www.source-code.biz/snippets/java/javaDaemonTest.sh.txt (editted)
#


### BEGIN INIT INFO
# Provides:                   SimpleServer
# Required-Start:             $network $local_fs $remote_fs $syslog
# X-UnitedLinux-Should-Start: $named sendmail
# Required-Stop:              $network $local_fs $remote_fs syslog
# X-UnitedLinux-Should-Stop:  $named sendmail
# Default-Start:              3 5
# Default-Stop:               0 1 2 6
# Short-Description:          JavaDaemonServer
# Description:                Java server daemon
### END INIT INFO

if [ -z "$JAVA_HOME" ]; then                               # if JAVA_HOME is undefined
   if [ -f /usr/share/java-utils/java-functions ]; then
      . /usr/share/java-utils/java-functions; set_jvm      # JPackage standard method to set JAVA_HOME
    elif [ -n "$(type -t setJava)" ]; then
      . setJava ""; fi                                     # old SUSE method to set JAVA_HOME
   if [ -z "$JAVA_HOME" ]; then echo Unable to set JAVA_HOME environment variable; exit 1; fi
   fi

scriptFile=$(readlink -fn $(type -p $0))                   # the absolute, dereferenced path of this script file
scriptDir=$(dirname $scriptFile)                           # absolute path of the script directory
applDir="$scriptDir"                                       # home directory of the service application
serviceName="JavaDaemonTest"                               # service name
serviceNameLo="javaDaemonTest"                             # service name with the first letter in lowercase
serviceUser="jdmntest"                                     # OS user name for the service
serviceUserHome="$applDir"                                 # home directory of the service user
serviceGroup="jdmntest"                                    # OS group name for the service
serviceLogFile="/var/log/$serviceNameLo.log"               # log file for StdOut/StdErr
maxShutdownTime=15                                         # maximum number of seconds to wait for the daemon to terminate normally
pidFile="/var/run/$serviceNameLo.pid"                      # name of PID file (PID = process ID number)
javaCommand="java"                                         # name of the Java launcher without the path
javaExe="$JAVA_HOME/bin/$javaCommand"                      # file name of the Java application launcher executable
javaArgs="JavaDaemonTest"                                  # arguments for Java launcher
javaCommandLine="$javaExe $javaArgs"                       # command line to start the Java service application
javaCommandLineKeyword="JavaDaemonTest"                    # a keyword that occurs on the commandline, used to detect an already running service process and to distinguish it from others
rcFileBaseName="rc$serviceNameLo"                          # basename of the "rc" symlink file for this script
rcFileName="/usr/local/sbin/$rcFileBaseName"               # full path of the "rc" symlink file for this script
etcInitDFile="/etc/init.d/$serviceNameLo"                  # symlink to this script from /etc/init.d

# Makes the file $1 writable by the group $serviceGroup.
function makeFileWritable {
   local filename="$1"
   touch $filename || return 1
   chgrp $serviceGroup $filename || return 1
   chmod g+w $filename || return 1
   return 0; }

# Returns 0 if the process with PID $1 is running.
function checkProcessIsRunning {
   local pid="$1"
   if [ -z "$pid" -o "$pid" == " " ]; then return 1; fi
   if [ ! -e /proc/$pid ]; then return 1; fi
   return 0; }

# Returns 0 if the process with PID $1 is our Java service process.
function checkProcessIsOurService {
   local pid="$1"
   local cmd="$(ps -p $pid --no-headers -o comm)"
   if [ "$cmd" != "$javaCommand" -a "$cmd" != "$javaCommand.bin" ]; then return 1; fi
   grep -q --binary -F "$javaCommandLineKeyword" /proc/$pid/cmdline
   if [ $? -ne 0 ]; then return 1; fi
   return 0; }

# Returns 0 when the service is running and sets the variable $servicePid to the PID.
function getServicePid {
   if [ ! -f $pidFile ]; then return 1; fi
   servicePid="$(<$pidFile)"
   checkProcessIsRunning $servicePid || return 1
   checkProcessIsOurService $servicePid || return 1
   return 0; }

function startServiceProcess {
   cd $applDir || return 1
   rm -f $pidFile
   makeFileWritable $pidFile || return 1
   makeFileWritable $serviceLogFile || return 1
   local cmd="setsid $javaCommandLine >>$serviceLogFile 2>&1 & echo \$! >$pidFile"
   sudo -u $serviceUser $SHELL -c "$cmd" || return 1
   sleep 0.1
   servicePid="$(<$pidFile)"
   if checkProcessIsRunning $servicePid; then :; else
      echo -ne "\n$serviceName start failed, see logfile."
      return 1
      fi
   return 0; }

function stopServiceProcess {
   kill $servicePid || return 1
   for ((i=0; i<maxShutdownTime*10; i++)); do
      checkProcessIsRunning $servicePid
      if [ $? -ne 0 ]; then
         rm -f $pidFile
         return 0
         fi
      sleep 0.1
      done
   echo -e "\n$serviceName did not terminate within $maxShutdownTime seconds, sending SIGKILL..."
   kill -s KILL $servicePid || return 1
   local killWaitTime=15
   for ((i=0; i<killWaitTime*10; i++)); do
      checkProcessIsRunning $servicePid
      if [ $? -ne 0 ]; then
         rm -f $pidFile
         return 0
         fi
      sleep 0.1
      done
   echo "Error: $serviceName could not be stopped within $maxShutdownTime+$killWaitTime seconds!"
   return 1; }

function runInConsoleMode {
   getServicePid
   if [ $? -eq 0 ]; then echo "$serviceName is already running"; return 1; fi
   cd $applDir || return 1
   sudo -u $serviceUser $javaCommandLine || return 1
   return 0; }

function startService {
   getServicePid
   if [ $? -eq 0 ]; then echo -n "$serviceName is already running"; rc_failed 0; rc_status -v; return 0; fi
   echo -n "Starting $serviceName   "
   startServiceProcess
   if [ $? -ne 0 ]; then rc_failed 1; rc_status -v; return 1; fi
   rc_failed 0
   rc_status -v
   return 0; }

function stopService {
   getServicePid
   if [ $? -ne 0 ]; then echo -n "$serviceName is not running"; rc_failed 0; rc_status -v; return 0; fi
   echo -n "Stopping $serviceName   "
   stopServiceProcess
   if [ $? -ne 0 ]; then rc_failed 1; rc_status -v; return 1; fi
   rc_failed 0
   rc_status -v
   return 0; }

function checkServiceStatus {
   echo -n "Checking for $serviceName:   "
   if getServicePid; then
      rc_failed 0
    else
      rc_failed 3
      fi
   rc_status -v
   return 0; }

function installService {
   getent group $serviceGroup >/dev/null 2>&1
   if [ $? -ne 0 ]; then
      echo Creating group $serviceGroup
      groupadd -r $serviceGroup || return 1
      fi
   id -u $serviceUser >/dev/null 2>&1
   if [ $? -ne 0 ]; then
      echo Creating user $serviceUser
      useradd -r -c "user for $serviceName service" -g $serviceGroup -G users -d $serviceUserHome $serviceUser
      fi
   ln -s $scriptFile $rcFileName || return 1
   ln -s $scriptFile $etcInitDFile || return 1
   insserv $serviceNameLo || return 1
   echo $serviceName installed.
   echo You may now use $rcFileBaseName to call this script.
   return 0; }

function uninstallService {
   insserv -r $serviceNameLo || return 1
   rm -f $rcFileName
   rm -f $etcInitDFile
   echo $serviceName uninstalled.
   return 0; }

function main {
   rc_reset
   case "$1" in
      console)                                             # runs the Java program in console mode
         runInConsoleMode
         ;;
      start)                                               # starts the Java program as a Linux service
         startService
         ;;
      stop)                                                # stops the Java program service
         stopService
         ;;
      restart)                                             # stops and restarts the service
         stopService && startService
         ;;
      status)                                              # displays the service status
         checkServiceStatus
         ;;
      install)                                             # installs the service in the OS
         installService
         ;;
      uninstall)                                           # uninstalls the service in the OS
         uninstallService
         ;;
      *)
         echo "Usage: $0 {console|start|stop|restart|status|install|uninstall}"
         exit 1
         ;;
      esac
   rc_exit; }

main $1