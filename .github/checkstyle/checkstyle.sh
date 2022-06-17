#!/bin/bash
VERSION=10.3
mkdir -p build/reports

JAR_FILE=.github/checkstyle/checkstyle-${VERSION}-all.jar
if [ ! -f "$JAR_FILE" ]; then
  echo "Downloading checkstyle jar"
  curl --output ${JAR_FILE} -LO https://github.com/checkstyle/checkstyle/releases/download/checkstyle-${VERSION}/checkstyle-${VERSION}-all.jar
fi

java -Dconfig_loc=.github/checkstyle -Dbasedir=$(pwd) \
  -jar ${JAR_FILE} \
  -c .github/checkstyle/checkstyle.xml \
  -f sarif \
  -o build/reports/checkstyle-analysis.sarif \
  $@
