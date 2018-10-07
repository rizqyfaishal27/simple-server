# Update repository 
# Install jdk, curl, git
apt-get update
apt-get -q install default-jre git curl unzip wget

export PATH=$PATH:$JAVA_HOME

# Install Gradle
mkdir /opt/gradle
cd /opt/gradle
wget https://services.gradle.org/distributions/gradle-4.10.2-all.zip
unzip -d /opt/gradle/gradle-4.10.2-all.zip
export PATH=$PATH:/opt/gradle/gradle-4.10.2-all/bin

# Cloning Project Repository
cd ~
rm -rf simple-server
git clone https://github.com/rizqyfaishal27/simple-server.git
cd simple-server
gradle build
gradle installDist
mv -r build/install/simple-server/bin /usr/
mv -r build/install/simple-server/lib /usr/
chmod +x service.sh
./service.sh install

# Starting Service
service simpleServer start