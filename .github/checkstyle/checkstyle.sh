#!/bin/bash
mkdir -p build/reports

FILE=.github/checkstyle/checkstyle-10.3-all.jar
if ! [[ -f "$FILE" ]]; then
  echo "Downloading checkstyle jar"
  curl --output .github/checkstyle/checkstyle-10.3-all.jar -LO https://github.com/checkstyle/checkstyle/releases/download/checkstyle-10.3/checkstyle-10.3-all.jar
fi

java -Dconfig_loc=.github/checkstyle -Dbasedir=$(pwd) \
  -jar .github/checkstyle/checkstyle-10.3-all.jar \
  -c .github/checkstyle/checkstyle.xml \
  -f sarif \
  -o build/reports/checkstyle-analysis.sarif \
  $@
