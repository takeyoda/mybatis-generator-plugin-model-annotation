package io.github.takeyoda.mybatis.modelannotation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

class AnnotationEntryTest {

  @Test
  void parse_withoutArgument() {
    AnnotationEntry actual = AnnotationEntry.parse("some.pkg.Foo");

    assertEquals(new FullyQualifiedJavaType("some.pkg.Foo"), actual.annotationType());
    assertNull(actual.arguments());
  }

  @Test
  void parse_withArgument() {
    AnnotationEntry actual = AnnotationEntry.parse("some.pkg.Bar(arg1=\"(zzz)\", arg2=9999)");

    assertEquals(new FullyQualifiedJavaType("some.pkg.Bar"), actual.annotationType());
    assertEquals("(arg1=\"(zzz)\", arg2=9999)", actual.arguments());
  }

  @Test
  void annotationCode_withoutArgument() {
    AnnotationEntry actual = AnnotationEntry.parse("some.pkg.Foo");

    assertEquals("@Foo", actual.annotationCode());
  }

  @Test
  void annotationCode_withArgument() {
    AnnotationEntry actual = AnnotationEntry.parse("some.pkg.Bar(arg1=\"(zzz)\", arg2=9999)");

    assertEquals("@Bar(arg1=\"(zzz)\", arg2=9999)", actual.annotationCode());
  }
}