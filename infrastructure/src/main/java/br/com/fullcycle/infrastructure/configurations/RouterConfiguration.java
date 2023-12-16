package br.com.fullcycle.infrastructure.configurations;

import br.com.fullcycle.infrastructure.http.SpringHttpRouter;
import br.com.fullcycle.infrastructure.rest.PartnerController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;

@Configuration
public class RouterConfiguration {

  @Bean
  public RouterFunction<?> routes(
    final PartnerController partnerController
  ) {
    final var router = new SpringHttpRouter();

    partnerController.bind(router);

    return router.router()
      .build();
  }

}
