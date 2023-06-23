#!/bin/bash
set -euox
source env.sh
cd "${DOCKER_SOURCE_PATH}"
status=$?
if [[ ! $status -eq 0 ]]; then
  echo -e "\e[1m\e[31mERROR - Can't change directory to ../docker\e[0m"
  exit $status
else
  echo -e "\e[1m\e[32mINFO - Changed directory to ../k8s\e[0m"
fi
cp "${JAVA_SOURCE_PATH}/target/chaos-monkey-pod-${JAR_VERSION}.jar" .
status=$?
if [[ ! $status -eq 0 ]]; then
  echo -e "\e[1m\e[31mERROR - Can't copy file ${JAVA_SOURCE_PATH}/target/chaos-monkey-pod-${JAR_VERSION}\e[0m"
  exit $status
fi
docker build . -t ${DOCKER_HUB_REGITRY}/chaos-monkey-pod:${IMAGE_TAG}
status=$?
if [[ ! $status -eq 0 ]]; then
  echo -e "\e[1m\e[31mERROR - Can't build image ${DOCKER_HUB_REGITRY}/chaos-monkey-pod:${IMAGE_TAG}\e[0m"
  exit $status
fi
docker push ${DOCKER_HUB_REGITRY}/chaos-monkey-pod:1.2.0
status=$?
if [[ ! $status -eq 0 ]]; then
  echo -e "\e[1m\e[31mERROR - Can't push image ${DOCKER_HUB_REGITRY}/chaos-monkey-pod:${IMAGE_TAG}\e[0m"
  exit $status
fi
cd "../${AUTOMATION_SOURCE_DIR}"
exit 0
