#!/bin/bash
set euox
source env.sh
cd "${JAVA_SOURCE_PATH}"
status=$?
if [[ ! $status -eq 0 ]]; then
  echo -e "\e[1m\e[31mERROR - Can't change directory to ${JAVA_SOURCE_PATH}\e[0m"
  exit $status
else
  echo -e "\e[1m\e[32mINFO - Changed directory to ${JAVA_SOURCE_PATH}\e[0m"
fi
mvn package
status=$?
if [[ ! $status -eq 0 ]]; then
  echo -e "\e[1m\e[31mERROR - Maven package failed\e[0m"
  exit $status
fi
cd "../../${AUTOMATION_SOURCE_DIR}"
exit 0