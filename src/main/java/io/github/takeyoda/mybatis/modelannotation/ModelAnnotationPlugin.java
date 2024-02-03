package io.github.takeyoda.mybatis.modelannotation;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

public class ModelAnnotationPlugin extends PluginAdapter {

  private Set<FullyQualifiedJavaType> annotationTypes;
  private Set<String> annotations;

  @Override
  public boolean validate(List<String> warnings) {
    return true;
  }

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
    // An annotation class FQCNs
    this.annotationTypes = StringUtility.tokenize(properties.getProperty("annotationTypes")) //
        .stream() //
        .map(FullyQualifiedJavaType::new) //
        .collect(Collectors.toSet());
    this.annotations = annotationTypes.stream() //
        .map(fqcn -> "@" + fqcn.getShortNameWithoutTypeArguments()) //
        .collect(Collectors.toSet());
  }

  @Override
  public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    if (annotations.isEmpty()) {
      return true;
    }
    topLevelClass.addImportedTypes(this.annotationTypes);
    annotations.forEach(topLevelClass::addAnnotation);
    return true;
  }
}
