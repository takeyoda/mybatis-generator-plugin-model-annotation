# mybatis-generator-plugin-model-annotation

A mybatis-generator plugin that adds any annotations to model classes.

## Usage
### pom.xml:
```xml
<project>
  ...

  <!-- Add this -->
  <pluginRepositories>
    <pluginRepository>
      <id>github</id>
      <name>GitHub Repository</name>
      <url>https://maven.pkg.github.com/takeyoda/mybatis-generator-plugin-model-annotation</url>
    </pluginRepository>
  </pluginRepositories>

  <build>
    <plugins>
      <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        ...

        <!-- Add this -->
        <dependencies>
          <dependency>
            <groupId>io.github.takeyoda</groupId>
            <artifactId>mybatis-generator-plugin-model-annotation</artifactId>
            <version>0.0.4</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>

  ...
</project>
```

### mybatis-generator-config.xml:
```xml
<generatorConfiguration>
  <context>
    ...

    <!-- Add this -->
    <plugin type="io.github.takeyoda.mybatis.modelannotation.ModelAnnotationPlugin">
      <property name="annotationTypes" value="jakarta.persistence.Entity, some.other.pkg.FooAnnotation" />
    </plugin>

    ...
  </context>
</generatorConfiguration>
```
