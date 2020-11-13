package com.example.demo.search;

import org.hibernate.search.analyzer.Discriminator;

public class CustomDiscriminator implements Discriminator {

  @Override
  public String getAnalyzerDefinitionName(Object value, Object entity, String field) {
    if ("question".equals(field) || "answer.answer".equals(field)) {
      return "questionAnalyzer"; // always use the questionAnalyzer
    }
    return null; // use default;
  }
}
