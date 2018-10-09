# Update repository 
# Install jdk, curl, git
apt-get update
# apt-get install -y software-properties-common
# add-apt-repository -y ppa:webupd8team/java
apt-get -y install openjdk-8-jdk git curl unzip wget nginx

touch /etc/environment
echo JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64" > /etc/environment
source /etc/environment

# Install Gradle
rm -rf /opt/gradle
mkdir /opt/gradle
cd /opt/gradle
wget https://services.gradle.org/distributions/gradle-4.10.2-all.zip
unzip -q /opt/gradle/gradle-4.10.2-all.zip
export PATH=$PATH:/opt/gradle/gradle-4.10.2/bin
echo $PATH

# Cloning Project Repository
cd ~
rm -rf simple-server
git clone https://github.com/rizqyfaishal27/simple-server.git
cd simple-server
gradle build
gradle installDist
mv build/install/simple-server/bin/* /usr/bin/
mv build/install/simple-server/lib/* /usr/lib/

# Running service simple-server
chmod +x service.sh
./service.sh start
sleep 3
./service.sh status
