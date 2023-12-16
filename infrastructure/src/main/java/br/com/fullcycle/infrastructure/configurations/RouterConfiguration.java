package br.com.fullcycle.infrastructure.configurations;

import br.com.fullcycle.infrastructure.http.SpringHttpRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;

@Configuration
public class RouterConfiguration {

  @Bean
  public RouterFunction<?> routes() {
    final var router = new SpringHttpRouter();
    return router.router()
      .
      .build();
  }

}
