# mybatis-generator-plugin-model-annotation

A mybatis-generator plugin that adds any annotations to model classes.

## Usage
mybatis-generator-config.xml:
```xml
<generatorConfiguration>
  <context>
    ...

    <plugin type="io.github.takeyoda.mybatis.modelannotation.ModelAnnotationPlugin">
      <property name="annotationTypes" value="jakarta.persistence.Entity, some.other.pkg.FooAnnotation" />
    </plugin>

    ...
  </context>
</generatorConfiguration>
```
