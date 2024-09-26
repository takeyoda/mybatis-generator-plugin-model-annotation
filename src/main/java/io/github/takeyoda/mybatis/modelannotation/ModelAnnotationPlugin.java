package io.github.takeyoda.mybatis.modelannotation;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

public class ModelAnnotationPlugin extends PluginAdapter {

  private static final String PLACEHOLDER_TABLE_NAME = "TABLE_NAME";
  private static final String PLACEHOLDER_DOMAIN_OBJECT_NAME = "DOMAIN_OBJECT_NAME";

  private Set<FullyQualifiedJavaType> importClasses;
  private Set<String> annotations;

  @Override
  public boolean validate(List<String> warnings) {
    return true;
  }

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
    // A tab separated annotation class FQCNs.
    // - some.pkg.Foo
    // - some.pkg.Bar(tableName="${TABLE_NAME}")
    final List<AnnotationEntry> annotationEntries = Splitter.on("\t") //
        .splitToStream(properties.getProperty("annotationTypes")) //
        .map(String::trim)
        .filter(s -> !Strings.isNullOrEmpty(s))
        .map(AnnotationEntry::parse) //
        .toList();

    this.importClasses = annotationEntries.stream() //
        .map(AnnotationEntry::annotationType) //
        .collect(Collectors.toSet());
    this.annotations = annotationEntries.stream() //
        .map(AnnotationEntry::annotationCode)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    if (annotations.isEmpty()) {
      return true;
    }
    final Map<String, String> placeholders = Map.of( //
        PLACEHOLDER_TABLE_NAME, introspectedTable.getFullyQualifiedTable().getIntrospectedTableName(), //
        PLACEHOLDER_DOMAIN_OBJECT_NAME, introspectedTable.getFullyQualifiedTable().getDomainObjectName() //
    );
    final Replacer replacer = new Replacer(placeholders);

    topLevelClass.addImportedTypes(this.importClasses);
    annotations.stream().map(replacer::replaceAll).forEach(topLevelClass::addAnnotation);
    return true;
  }
}
