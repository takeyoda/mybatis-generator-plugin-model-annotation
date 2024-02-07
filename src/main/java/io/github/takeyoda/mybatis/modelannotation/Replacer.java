package io.github.takeyoda.mybatis.modelannotation;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Replacer {

  private static final String PLACEHOLDER_OPEN = "${";
  private static final String PLACEHOLDER_CLOSE = "}";
  private final Pattern pattern;
  // Map<placeholder, replacement>
  private final Map<String, String> placeholders;

  public Replacer(Map<String, String> placeholders) {
    // ${(?<placeholder>placeholder1|placeholder2|...)}
    final String findPlaceholderRegex = placeholders.keySet().stream() //
        .map(Pattern::quote)
        .collect(Collectors.joining("|", Pattern.quote(PLACEHOLDER_OPEN) + "(?<placeholder>",
            ")" + Pattern.quote(PLACEHOLDER_CLOSE)));
    this.pattern = Pattern.compile(findPlaceholderRegex);
    this.placeholders = placeholders;
  }

  public String replaceAll(String target) {
    final Matcher matcher = pattern.matcher(target);
    final StringBuilder buff = new StringBuilder();
    while (matcher.find()) {
      final String placeholder = matcher.group("placeholder");
      String value = placeholders.get(placeholder);
      if (value == null) {
        // keep original string
        value = PLACEHOLDER_OPEN + placeholder + PLACEHOLDER_CLOSE;
      }
      matcher.appendReplacement(buff, Matcher.quoteReplacement(value));
    }
    matcher.appendTail(buff);
    return buff.toString();
  }
}
