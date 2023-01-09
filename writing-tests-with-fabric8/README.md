# Writing tests using Fabric8 Kubernetes Client

This is a demo project that has a simple [PodGroupService](./src/main/java/io/fabric8/demos/tests/mockserver/PodGroupService.java) which manages lifecycle of a group of Pods sharing common labels. It has common operations like add, list, watch, delete, etc.

We write tests for this service using various options provided to us by Fabric8 Kubernetes Client:

## Kubernetes Mock Server Tests
Based on [Fabric8 Kubernetes Mock Server](https://search.maven.org/artifact/io.fabric8/kubernetes-server-mock/6.3.1/jar)

- Using CRUD Mode : [PodGroupServiceCrudTest](./src/test/java/io/fabric8/demos/tests/mockserver/PodGroupServiceCrudTest.java)
- Using Expectations Mode : [PodGroupServiceTest](./src/test/java/io/fabric8/demos/tests/mockserver/PodGroupServiceTest.java)

## Real Kubernetes Tests 
Based on [Fabric8 Kubernetes JUnit Jupiter](https://search.maven.org/artifact/io.fabric8/kubernetes-junit-jupiter/6.3.1/jar)

- Using Fabric8 JUnit5 Annotations : [PodGroupServiceIT](./src/test/java/io/fabric8/demos/tests/e2e/PodGroupServiceIT.java)

These tests require a Kubernetes instance to be running. They will execute in `k8s-e2e` profile:
```shell
$ mvn clean install -Pk8s-e2e
```

