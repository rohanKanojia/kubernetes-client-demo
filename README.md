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

### `kubectl` to Kubernetes Client Mapping:
| kubectl                                        | Fabric8 Kubernetes Client                           |
| ---------------------------------------------- | ------------------------------------- |
| `kubectl get pods`                             | [PodListTest.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/PodListTest.java) |
| `kubectl get pods -w`                          | [PodWatch.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/PodWatch.java)       |
| `kubectl create pods`                          | [PodDemo.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/PodDemo.java)         |
| `kubectl create svc -f test-svc.yaml`          | [LoadAndCreateService.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/LoadAndCreateService.java) |
| `kubectl exec`                                 | [ExecDemo.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/ExecDemo.java) |
| `kubectl delete pod test-pod1`                 | [PodDelete.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/PodDelete.java) |
| `kubectl create deploy`                        | [DeploymentDemo.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/DeploymentDemo.java) |
| `kubectl create -f customresource.yaml`        | [CustomResourceCreateDemo.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/CustomResourceCreateDemo.java) |
| `kubectl create -f customresource.yaml`        | [CustomResourceCreateDemoTypeless.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/CustomResourceCreateDemoTypeless.java) |
| `kubectl get ns`                               | [NamespaceListDemo.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/NamespaceListDemo.java) |
| `kubectl create job`                           | [JobCreate.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/JobCreate.java) |
| `kubectl logs job/pi`                          | [JobFetchLogs.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/JobFetchLogs.java) |
| `kubectl create -f test-list.yml`              | [CreateOrReplaceResourceList.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/CreateOrReplaceResourceList.java) |
| `kubectl create -f test-ing.yml`               | [CreateOrReplaceIngress.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/CreateOrReplaceIngress.java) |
| `kubectl create validatingwebhookconfiguration`| [ValidatingWebhookConfigurationTest.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/ValidatingWebhookConfigurationTest.java) 
| `kubectl get events`                           | [EventsExample.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/EventsExample.java) |
| `kubectl top nodes`                            | [TopExample.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/TopExample.java) |
| `kubectl auth can-i create deployment.apps`    | [CanITest.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/CanITest.java) |                           
| `kubectl cp /foo_dir <some-pod>:/bar_dir`      | [UploadDirectoryToPod.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/UploadDirectoryToPod.java) |   
| `kubectl cp <pod>:/tmp/foo /tmp/bar`           | [DownloadFileFromPod.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/DownloadFileFromPod.java) | 
| `kubectl cp <pod>:/tmp/foo -c c1 /tmp/bar`     | [DownloadFileFromMultiContainerPod.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/DownloadFileFromMultiContainerPod.java) | 
| `kubectl cp /foo_dir <pod>:/tmp/bar_dir`       | [UploadFileToPod.java](https://github.com/rohanKanojia/kubernetes-client-demo/blob/master/src/main/java/io/fabric8/UploadFileToPod.java) | 
