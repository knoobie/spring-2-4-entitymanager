package com.example.demo.search;

import com.example.demo.entities.Answer;
import com.example.demo.entities.Question;
import com.example.demo.repo.QuestionRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MyService {

  @Autowired
  private QuestionRepository repository;

  @PersistenceContext
  private EntityManager entityManager;

  public void create(String question1, String answer1) {
    Question question = new Question();
    question.setQuestion(question1);
    question.setAnswer(new Answer(answer1));
    question.getAnswer().setQuestion(question);

    repository.saveAndFlush(question);
  }

  @Transactional
  public void load() {
    log.debug("Start lucene indexing...");
    try {
      FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
      fullTextEntityManager.createIndexer().startAndWait();
    } catch (InterruptedException e) {
      log.warn("Error occurred trying to build Lucene Search indexes " + e.getMessage(), e);
    } finally {
      log.debug("Finished lucene indexing...");
    }
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public List<Question> search(String searchTerm) {
    val luceneEntityManager = Search.getFullTextEntityManager(entityManager);
    val queryBuilder = luceneEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Question.class).get();

    Map<Query, Occur> queryList = new LinkedHashMap<>();
    queryList.put(queryBuilder.keyword().fuzzy().withEditDistanceUpTo(2).withPrefixLength(1).onFields("question").boostedTo(2f).andField("answer.answer")
      .matching(searchTerm.toLowerCase()).createQuery(), BooleanClause.Occur.SHOULD);

    val combinedQuery = combineQueries(queryList);
    FullTextQuery jpaQuery = luceneEntityManager.createFullTextQuery(combinedQuery, Question.class);
    jpaQuery.setSort(new Sort(SortField.FIELD_SCORE));
    jpaQuery.setMaxResults(10);
    jpaQuery.setTimeout(5, TimeUnit.SECONDS);

    return (List<Question>) jpaQuery.getResultList();
  }

  private org.apache.lucene.search.Query combineQueries(Map<org.apache.lucene.search.Query, BooleanClause.Occur> queries) {

    BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
    for (Entry<Query, Occur> query : queries.entrySet()) {
      booleanQuery.add(query.getKey(), query.getValue());
    }
    return booleanQuery.build();
  }
}
