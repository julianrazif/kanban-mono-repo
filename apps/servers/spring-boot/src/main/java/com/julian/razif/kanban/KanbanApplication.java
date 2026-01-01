package com.julian.razif.kanban;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KanbanApplication {

  static void main(String[] args) {
    SpringApplication.run(KanbanApplication.class, args);
  }

}
