# Fabric8 CRD Yaml to Java Generator Demo
Demo project that showcases generating java POJO from CRD YAML using  [Fabric8 Java Generator Maven Plugin](https://github.com/fabric8io/kubernetes-client/blob/master/doc/java-generation-from-CRD.md).

Project contains YAML for Book CustomResourceDefinition:
```shell
fabric8-crd-java-generator-demo : $ tree src/main/resources/
src/main/resources/
└── crd
    └── book-crd.yaml
```

Add Fabric8 Java Generator Maven Plugin to `pom.xml`:
```xml
<plugin>
    <groupId>io.fabric8</groupId>
    <artifactId>java-generator-maven-plugin</artifactId>
    <version>${fabric8.version}</version>
    <configuration>
        <!-- 1 -->
        <source>${project.basedir}/src/main/resources/crd/book-crd.yaml</source> 
         <!-- 2 -->
        <extraAnnotations>true</extraAnnotations>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
1. Source file or directory from which to generate POJOs
2. Add extra Sundrio annotations for generating builder, fluent classes.

Build project:
```shell
fabric8-crd-java-generator-demo : $ mvn clean install
```

POJOs should be generated in `target/generated-sources` folder:

```shell
fabric8-crd-java-generator-demo : $ tree target/generated-sources/java/
target/generated-sources/java/
└── io
    └── fabric8
        └── demo
            └── generator
                └── v1alpha1
                    ├── Book.java
                    ├── BookSpec.java
                    └── BookStatus.java

5 directories, 3 files

fabric8-crd-java-generator-demo : $ tree target/generated-sources/annotations/
target/generated-sources/annotations/
└── io
    └── fabric8
        └── demo
            └── generator
                └── v1alpha1
                    ├── BookBuilder.java
                    ├── BookFluentImpl.java
                    ├── BookFluent.java
                    ├── BookSpecBuilder.java
                    ├── BookSpecFluentImpl.java
                    ├── BookSpecFluent.java
                    ├── BookStatusBuilder.java
                    ├── BookStatusFluentImpl.java
                    └── BookStatusFluent.java

5 directories, 9 files

```

