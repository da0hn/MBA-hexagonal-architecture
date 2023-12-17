package br.com.fullcycle.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Main {

  public static void main(final String[] args) {
    SpringApplication.run(Main.class, args);
  }

}
