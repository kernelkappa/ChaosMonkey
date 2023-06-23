# ChaosMonkeyPod
ChaosMonkeyPod is a simple Java application which runs inside a Kubernetes container and implements the concept of chaos engineering. <br/>
It uses Quartz framework to schedule a job which looks for pods with a specific label and chooses randomly which one will be deleted. <br/>
All interactions with Kubernets are executed with REST API. 

## Repository structure
Repository is made of three directories:
### docker folder
Contains the Dockerfile and the start.sh files.
In addiction during installation the jar file will be added here.

More information in _Installation_ section.
### k8s folder
Contains the Kubernetes manifests for the chaos-monkey-pod, the nginx deployment (used to create the set of pods to delete) and a file named service-account which contains the ClusterRole, ClusterRoleBinding and the ServiceAccount manifests.
### sources folder
Contains the directory with the Java project with the classical Maven structure.
### automation folder
Contains script for automating build with maven, docker build image and push image on registry, creation and update of namespaces and application of manifests in Kubernetes.  
## Installation and use
To install with automation scripts:<br/>
 - open bash in your linux distro or WSL in Windows
 - change directory to automation
 - change variables value in env.sh file where needed (DOCKER_REGISTRY is mandatory to be changed)
 - give execution permission to scripts
```bash
chmod +x maven.sh
chmod +x dockerAutomation.sh
chmod +x k8sAutomation.sh
```
 - execute maven packaging using script
```bash
./maven.sh
```
 - execute docker build and push using script
```bash
./dockerAutomation.sh
```
- execute Kubernetes configuration for first time: 
```bash
./k8sAutomation.sh init
```
You can check running pod for ChaosMonkeyPod application executing
```bash
kubectl get pod -n devops-tools
```
You can check running pods which will be randomly deleted executing 
```bash
kubectl get pod -n testing
```
In case you want to clean all Kubernetes configuration just run
```bash
./k8sAutomation.sh clean
```
In case you want to apply a new configuration for chaos-monkey-pod-deployment.yaml manifest just run
```bash
./k8sAutomation.sh redeploy
```

If you don't want to use automation scripts:
 - change directory to sources/ChaosMonkeyPod
 - execute 
```bash
mvn package
```
 - copy from generated target directory file chaos-monkey-pod-1.0.0.jar to docker directory
 - change directory to docker and execute docker build
```bash
docker build . -t _${YOUR_REGISTRY}_/chaos-monkey-pod:_${IMAGE_TAG}_
```
 - push generated image to your registry 
```bash
docker push _${YOUR_REGISTRY}_/chaos-monkey-pod:_${IMAGE_TAG}_
```
 - configure Kubernetes namespaces (currently is devops-tools for ChaosMonkeyPod application and testing for pods which will be randomly deleted by it)
```bash
kubectl create namespace devops-tools
kubectl create namespace testing
```
***You are free to change namespaces names but pay attention to change Deployment and ServiceAccount manifests too!!***<br/>
***This consideration applies also if you use automation scripts!!***

 -  configure Kubernetes applying manifest fo ClusterRole, ClusterRoleBinding and ServiceAccount (service-account.yaml file contains all of them) 
```bash
kubectl apply -f service-account.yaml
```
 - Apply Deployment manifest for pods destinated to be randomly deleted by ChaosMonkeyPod  
```bash
kubectl apply -f target-deployment.yaml
```
 - Apply Deployment manifest for ChaosMonkeyPod
```bash
kubectl apply -f chaos-monkey-pod-deployment.yaml
```
As done for automation you can check running pod for ChaosMokeyPod application executing
```bash
kubectl get pod -n devops-tools
``` 
and 
```bash
kubectl get pod -n testing
``` 
to check status of pods which will be randomly deleted by it.

You're done!!

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)