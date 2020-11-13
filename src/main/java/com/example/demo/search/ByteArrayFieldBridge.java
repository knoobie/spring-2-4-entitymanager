package com.example.demo.search;

import org.hibernate.search.bridge.StringBridge;
import org.springframework.stereotype.Component;

@Component
public class ByteArrayFieldBridge implements StringBridge {

  @Override
  public String objectToString(Object o) {
    if (null == o) {
      return "";
    }
    if (o instanceof String) {
      return (String) o;
    }

    return new String((byte[]) o);
  }
}
