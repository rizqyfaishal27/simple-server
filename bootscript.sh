# Update repository 
# Install jdk, curl, git
apt-get update
apt-get install default-jre git curl unzip

export PATH=$PATH:$JAVA_HOME

# Install Gradle
mkdir /opt/gradle
cd /opt/gradle
wget https://services.gradle.org/distributions/gradle-4.10.2-all.zip
unzip -d /opt/gradle gradle-4.10.2-bin.zip
export PATH=$PATH:/opt/gradle/gradle-4.10.2/bin

# Cloning Project Repository
cd ~
git clone https://github.com/rizqyfaishal27/simple-server.git
cd simple-server
gradle build
gradle installDist
mv -r build/install/simple-server/bin /usr/
mv -r build/install/simple-server/lib /usr/
chmod +x service.sh
./service install

# Starting Service
service simpleServer start