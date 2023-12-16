package br.com.fullcycle.infrastructure.configurations;

import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.infrastructure.rest.PartnerController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

  @Bean
  public PartnerController partnerController(final CreatePartnerUseCase createPartnerUseCase, final GetPartnerByIdUseCase getPartnerByIdUseCase) {
    return new PartnerController(createPartnerUseCase, getPartnerByIdUseCase);
  }

}
