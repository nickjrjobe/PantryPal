#!/bin/bash
JUNIT="https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar"
HAMCREST="https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
OPENJFX="https://download2.gluonhq.com/openjfx/17.0.9/openjfx-17.0.9_osx-aarch64_bin-sdk.zip"
OPENJFX_ZIP="openjfx-17.0.9_osx-aarch64_bin-sdk.zip"
CHECKSTYLE="https://github.com/checkstyle/checkstyle/releases/download/checkstyle-10.12.4/checkstyle-10.12.4-all.jar"
JSON="https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar"
GOOGLE_FORMAT="https://github.com/google/google-java-format/releases/download/v1.18.1/google-java-format-1.18.1-all-deps.jar"
mkdir lib
cd lib
curl -O $JUNIT
curl -O $HAMCREST
curl -O $OPENJFX
wget $CHECKSTYLE
wget $JSON
#wget $GOOGLE_FORMAT
unzip $OPENJFX_ZIP

