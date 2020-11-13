package com.example.demo;

import com.example.demo.search.MyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

  @Autowired
  private MyService service;

  @Test
  void contextLoads() {

    service.create("My Question", "My Answer");
    // this fails since 2.4.0
    service.load();

    Assertions.assertThat(service.search("question")).hasSize(1);
    Assertions.assertThat(service.search("answer")).hasSize(1);
  }
}
