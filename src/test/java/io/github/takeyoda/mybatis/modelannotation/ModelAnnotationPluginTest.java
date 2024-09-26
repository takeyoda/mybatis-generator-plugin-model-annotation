package io.github.takeyoda.mybatis.modelannotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Properties;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;

@ExtendWith(MockitoExtension.class)
class ModelAnnotationPluginTest {
  private ModelAnnotationPlugin plugin;

  @Mock
  private Context context;

  @Mock
  private TopLevelClass topLevelClass;

  @Mock
  private IntrospectedTable introspectedTable;

  @Mock
  private FullyQualifiedTable fullyQualifiedTable;

  @BeforeEach
  void beforeEach() {
    plugin = new ModelAnnotationPlugin();
    plugin.setContext(context);
  }

  @Test
  void whenAnnotationTypesWasEmpty() {
    Properties properties = new Properties();
    properties.setProperty("annotationTypes", "");
    plugin.setProperties(properties);

    final boolean expect = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

    assertTrue(expect);
    verifyNoInteractions(topLevelClass, introspectedTable, fullyQualifiedTable);
  }

  @Test
  void whenOneAnnotationTypeSpecified() {
    Properties properties = new Properties();
    properties.setProperty("annotationTypes", "some.pkg.SomeAnnotation");
    plugin.setProperties(properties);

    doNothing().when(topLevelClass).addImportedTypes(anySet());
    doNothing().when(topLevelClass).addAnnotation(anyString());
    doReturn(fullyQualifiedTable).when(introspectedTable).getFullyQualifiedTable();
    doReturn("table_x").when(fullyQualifiedTable).getIntrospectedTableName();
    doReturn("DomainObjectX").when(fullyQualifiedTable).getDomainObjectName();

    final boolean expect = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

    assertTrue(expect);

    verify(topLevelClass, times(1)).addImportedTypes(Set.of(new FullyQualifiedJavaType("some.pkg.SomeAnnotation")));
    verify(topLevelClass, times(1).description("should strip package name")).addAnnotation("@SomeAnnotation");
  }

  @Test
  void whenTwoAnnotationTypesSpecified() {
    Properties properties = new Properties();
    properties.setProperty("annotationTypes", "a.b.Foo\tc.d.Bar");
    plugin.setProperties(properties);

    doNothing().when(topLevelClass).addImportedTypes(anySet());
    doNothing().when(topLevelClass).addAnnotation(anyString());
    doReturn(fullyQualifiedTable).when(introspectedTable).getFullyQualifiedTable();
    doReturn("table_x").when(fullyQualifiedTable).getIntrospectedTableName();
    doReturn("DomainObjectX").when(fullyQualifiedTable).getDomainObjectName();

    final boolean expect = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

    assertTrue(expect);

    verify(topLevelClass, times(1)).addImportedTypes(Set.of(new FullyQualifiedJavaType("a.b.Foo"), new FullyQualifiedJavaType("c.d.Bar")));
    verify(topLevelClass, times(1).description("should strip package name")).addAnnotation("@Foo");
    verify(topLevelClass, times(1).description("should strip package name")).addAnnotation("@Bar");
  }

  @Test
  void whenAnnotationArgumentSpecified() {
    Properties properties = new Properties();
    properties.setProperty("annotationTypes", """
    a.b.Foo(a=1, b="zzz")   \t   c.d.Bar(entityName="${TABLE_NAME}")   \t   e.f.Baz(objName="${DOMAIN_OBJECT_NAME}")
    """);
    plugin.setProperties(properties);

    doNothing().when(topLevelClass).addImportedTypes(anySet());
    doNothing().when(topLevelClass).addAnnotation(anyString());
    doReturn(fullyQualifiedTable).when(introspectedTable).getFullyQualifiedTable();
    doReturn("table_x").when(fullyQualifiedTable).getIntrospectedTableName();
    doReturn("DomainObjectX").when(fullyQualifiedTable).getDomainObjectName();

    final boolean expect = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

    assertTrue(expect);

    verify(topLevelClass, times(1)).addImportedTypes(Set.of(new FullyQualifiedJavaType("a.b.Foo"), new FullyQualifiedJavaType("c.d.Bar"), new FullyQualifiedJavaType("e.f.Baz")));
    verify(topLevelClass, times(1).description("should strip package name")).addAnnotation("@Foo(a=1, b=\"zzz\")");
    verify(topLevelClass, times(1).description("should replace TABLE_NAME placeholder")).addAnnotation("@Bar(entityName=\"table_x\")");
    verify(topLevelClass, times(1).description("should replace DOMAIN_OBJECT_NAME placeholder")).addAnnotation("@Baz(objName=\"DomainObjectX\")");
  }
}
