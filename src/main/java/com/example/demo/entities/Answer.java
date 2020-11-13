package com.example.demo.entities;

import com.example.demo.search.ByteArrayFieldBridge;
import com.example.demo.search.CustomDiscriminator;
import java.nio.charset.StandardCharsets;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.AnalyzerDiscriminator;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer")
@EqualsAndHashCode(exclude = {"question"})
@ToString(callSuper = true, exclude = {"question"})
@AnalyzerDiscriminator(impl = CustomDiscriminator.class)
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "answer", nullable = false)
  @FieldBridge(impl = ByteArrayFieldBridge.class)
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  private byte[] answer;

  @ContainedIn
  @JoinColumn(name = "question_id")
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private Question question;

  public Answer(String answer) {
    this.answer = answer.getBytes(StandardCharsets.UTF_8);
  }
}
