package com.example.demo.entities;

import com.example.demo.search.CustomDiscriminator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.AnalyzerDiscriminator;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

@Data
@Entity
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question")
@AnalyzerDiscriminator(impl = CustomDiscriminator.class)
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(name = "question", nullable = false, unique = true, length = 1000)
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  private String question;

  @IndexedEmbedded(depth = 1)
  @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
  private Answer answer;
}
