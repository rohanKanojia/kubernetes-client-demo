# Fabric8 Kubernetes Client Demo
![License](https://img.shields.io/github/license/rohanKanojia/kubernetes-client-demo)

This project contains various samples for different usages of [Fabric8 Kubernetes Client](https://github.com/fabric8io/kubernetes-client). I generally use them in my blogs or for testing.

### How to Build?
Just need to run `mvn clean install` to compile project

### How to run a specific sample?
In order to run a sample, you need to be logged into a Kubernetes/OpenShift cluster. You can either use minikube or minishift if you don't have access to a remote cluster.

You can load project as maven project in your IDE and can run sample from IDE. You can also run it from maven using exec-maven-plugin. For example, here is how you would run `DeploymentDemo`:
```
mvn exec:java -Dexec.mainClass="io.fabric8.DeploymentDemo"
```