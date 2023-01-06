# Fabric8 Java to CRD Yaml Generator Demo

Demo project that showcases generating CRD Yaml from java sources using  [Fabric8 CRD Generator Annotation Processor](https://github.com/fabric8io/kubernetes-client/blob/master/doc/CRD-generator.md).

Project contains POJOs for Book CustomResource:

```bash
fabric8-java-crd-yaml-generator-demo : $ tree src/main/java/
src/main/java/
└── io
    └── fabric8
        └── demo
            └── crd
                └── v1alpha1
                    ├── Book.java
                    ├── BookSpec.java
                    └── BookStatus.java

5 directories, 3 files
```

There is [Fabric8 CRD Generator Annotation Processor](https://github.com/fabric8io/kubernetes-client/blob/master/doc/CRD-generator.md) dependency in `pom.xml`:
```xml
<dependency>
    <groupId>io.fabric8</groupId>
    <artifactId>crd-generator-apt</artifactId>
    <version>${fabric8.version}</version>
</dependency>
```

Build Project:
```bash
fabric8-java-crd-yaml-generator-demo : $ mvn clean install
```

You should be able to see generated YAML manifests:
```bash
fabric8-java-crd-yaml-generator-demo : $ ls target/classes/META-INF/fabric8/
books.testing.fabric8.io-v1beta1.yml  books.testing.fabric8.io-v1.yml
```
