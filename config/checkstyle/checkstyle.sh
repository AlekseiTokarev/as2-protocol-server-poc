#!/bin/bash
mkdir -p build/reports
java -Dconfig_loc=config/checkstyle -Dbasedir=$(pwd) \
  -jar config/checkstyle/checkstyle-10.3-all.jar \
  -c config/checkstyle/checkstyle.xml \
  -f sarif \
  -o build/reports/checkstyle-analysis.sarif \
  $@
