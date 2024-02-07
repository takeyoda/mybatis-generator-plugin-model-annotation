package io.github.takeyoda.mybatis.modelannotation;

import java.util.Objects;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

record AnnotationEntry(
    FullyQualifiedJavaType annotationType,
 String arguments
) {

  // some.pkg.Foo(bar=1, baz=2)
  //   -> "some.pkg.Foo" ... annotationType
  //   -> "(bar=1, baz=2) ... arguments
  static AnnotationEntry parse(String line) {
    int startArgument = line.indexOf("(");
    if (startArgument == -1) {
      return new AnnotationEntry(new FullyQualifiedJavaType(line), null);
    }
    String fqcn = line.substring(0, startArgument);
    String arguments = line.substring(startArgument);
    return new AnnotationEntry(new FullyQualifiedJavaType(fqcn), arguments);
  }

  public String annotationCode() {
    return "@" + annotationType.getShortName() + Objects.requireNonNullElse(arguments, "");
  }
}
