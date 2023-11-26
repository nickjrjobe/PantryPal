#!/bin/bash
JUNIT="https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar"
HAMCREST="https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
OPENJFX="https://download2.gluonhq.com/openjfx/17.0.9/openjfx-17.0.9_linux-x64_bin-sdk.zip"
OPENJFX_ZIP="openjfx-17.0.9_linux-x64_bin-sdk.zip"
CHECKSTYLE="https://github.com/checkstyle/checkstyle/releases/download/checkstyle-10.12.4/checkstyle-10.12.4-all.jar"
JSON="https://search.maven.org/remotecontent?filepath=org/json/json/20231013/json-20231013.jar"
GOOGLE_FORMAT="https://github.com/google/google-java-format/releases/download/v1.18.1/google-java-format-1.18.1-all-deps.jar"
MONGODB="https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/4.11.0/mongodb-driver-sync-4.11.0.jar"
MONGODB_DEP="https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/4.11.0/mongodb-driver-core-4.11.0.jar"
GRADLE="https://services.gradle.org/distributions/gradle-8.4-all.zip"
GRADLE_ZIP="gradle-8.4-all.zip"
mkdir lib
cd lib
curl -O $JUNIT
curl -O $HAMCREST
curl -O $OPENJFX
curl -O $MONGODB
curl -O $MONGODB_DEP
curl -O $JSON -L
wget $CHECKSTYLE
#wget $GOOGLE_FORMAT 
wget $GRADLE 
unzip $GRADLE_ZIP
#cp ../build.gradle .
#./gradle-8.4/bin/gradle wrapper
#./gradlew build
#cp libs/* .
#rm -rf libs
unzip $OPENJFX_ZIP
