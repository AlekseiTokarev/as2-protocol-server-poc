#!/bin/bash
mkdir -p build/reports
java -Dconfig_loc=.github/checkstyle -Dbasedir=$(pwd) \
  -jar .github/checkstyle/checkstyle-10.3-all.jar \
  -c .github/checkstyle/checkstyle.xml \
  -f sarif \
  -o build/reports/checkstyle-analysis.sarif \
  $@
