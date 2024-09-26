package io.github.takeyoda.mybatis.modelannotation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReplacerTest {

  @Test
  void replaceAll() {
    final Map<String, String> placeholders = Map.of(
        "foo", "fooValue", //
        "bar", "barValue" //
    );
    final String template = "START ${foo} ${bar} END";
    final Replacer replacer = new Replacer(placeholders);
    final String actual = replacer.replaceAll(template);

    assertEquals("START fooValue barValue END", actual);
  }

  @Test
  void replaceEmptyPlaceholder() {
    final Map<String, String> placeholders = Collections.emptyMap();
    final String template = "START ${foo} ${bar} END";
    final Replacer replacer = new Replacer(placeholders);
    final String actual = replacer.replaceAll(template);

    assertEquals("START ${foo} ${bar} END", actual, "keep original string");
  }
}