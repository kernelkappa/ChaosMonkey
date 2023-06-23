#!/bin/bash
set -euox
source env.sh
cd "${K8S_SOURCE_PATH}"
status=$?
if [[ ! $status -eq 0 ]]; then
  echo -e "\e[1m\e[31mERROR - Can't change directory to ../k8s\e[0m"
  exit $status
else
  echo -e "\e[1m\e[32mINFO - Changed directory to ../k8s\e[0m"
fi
if [[ $1 == "init" ]]; then
  kubectl create namespace "${DEVOPS_NAMESPACE}"
  status=$?
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't create namespace ${DEVOPS_NAMESPACE}\e[0m"
    exit $status
  fi
  kubectl create namespace "${TARGET_NAMESPACE}"
  status=$?
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't create namespace ${TARGET_NAMESPACE}\e[0m"
    exit $status
  fi
  kubectl apply -f service-account.yaml
  status=$?
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't apply manifest service-account.yaml\e[0m"
    exit $status
  fi
  kubectl apply -f target-deployment.yaml
  status=$?
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't apply manifest target-deployment.yaml\e[0m"
    exit $status
  fi
  kubectl apply -f chaos-monkey-pod-deployment.yaml
  status=$?
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't apply manifest chaos-monkey-pod-deployment.yaml\e[0m"
    exit $status
  fi
elif [[ $1 == "redeploy" ]]; then
  kubectl delete -f chaos-monkey-pod-deployment.yaml
  status=$?
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't delete manifest chaos-monkey-pod-deployment.yaml\e[0m"
    exit $status
  fi
  kubectl apply -f chaos-monkey-pod-deployment.yaml
  status=$?
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't apply manifest chaos-monkey-pod-deployment.yaml\e[0m"
    exit $status
  fi
elif [[ $1 == "clean" ]]; then
  kubectl delete -f chaos-monkey-pod-deployment.yaml
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't delete manifest chaos-monkey-pod-deployment.yaml\e[0m"
    exit $status
  fi
  kubectl delete -f target-deployment.yaml
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't delete manifest target-deployment.yaml\e[0m"
    exit $status
  fi
  kubectl delete namespace "${DEVOPS_NAMESPACE}"
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't delete namespace ${DEVOPS_NAMESPACE}\e[0m"
    exit $status
  fi
  kubectl delete namespace "${TARGET_NAMESPACE}"
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't delete namespace ${TARGET_NAMESPACE}\e[0m"
    exit $status
  fi
  kubectl delete -f service-account.yaml
  if [[ ! $status -eq 0 ]]; then
    echo -e "\e[1m\e[31mERROR - Can't delete manifest service-account.yaml\e[0m"
    exit $status
  fi
fi
cd "../${AUTOMATION_SOURCE_DIR}"
exit 0

