package br.com.fullcycle.hexagonal.infrastructure.configurations;

import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  private final CustomerService customerService;

  private final EventService eventService;

  private final PartnerService partnerService;

  public UseCaseConfig(
    final CustomerService customerService,
    final EventService eventService,
    final PartnerService partnerService
  ) {
    this.customerService = customerService;
    this.eventService = eventService;
    this.partnerService = partnerService;
  }

  @Bean
  public CreateCustomerUseCase createCustomerUseCase() {
    return new CreateCustomerUseCase(this.customerService);
  }

  @Bean
  public CreateEventUseCase createEventUseCase() {
    return new CreateEventUseCase(this.eventService, this.partnerService);
  }

  @Bean
  public CreatePartnerUseCase createPartnerUseCase() {
    return new CreatePartnerUseCase(this.partnerService);
  }

  @Bean
  public GetCustomerByIdUseCase getCustomerByIdUseCase() {
    return new GetCustomerByIdUseCase(this.customerService);
  }

  @Bean
  public GetPartnerByIdUseCase getPartnerByIdUseCase() {
    return new GetPartnerByIdUseCase(this.partnerService);
  }

  @Bean
  public SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase() {
    return new SubscribeCustomerToEventUseCase(this.eventService, this.customerService);
  }

}
