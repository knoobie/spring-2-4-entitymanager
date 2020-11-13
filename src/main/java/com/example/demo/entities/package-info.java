@AnalyzerDef(name = "questionAnalyzer",
  charFilters = {
    @CharFilterDef(factory = HTMLStripCharFilterFactory.class)
  },
  tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
  filters = {
    @TokenFilterDef(factory = LowerCaseFilterFactory.class),
    @TokenFilterDef(factory = GermanStemFilterFactory.class),
    @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
      @Parameter(name = "language", value = "German")
    })
  })
package com.example.demo.entities;

import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.de.GermanStemFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.CharFilterDef;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;