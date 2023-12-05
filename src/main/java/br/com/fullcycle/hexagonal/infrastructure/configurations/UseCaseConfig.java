package br.com.fullcycle.hexagonal.infrastructure.configurations;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.SubscribeCustomerToEventUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  private final PartnerRepository partnerRepository;

  private final CustomerRepository customerRepository;

  private final EventRepository eventRepository;

  private final TicketRepository ticketRepository;

  public UseCaseConfig(
    final PartnerRepository partnerRepository,
    final CustomerRepository customerRepository,
    final EventRepository eventRepository,
    final TicketRepository ticketRepository
  ) {
    this.partnerRepository = partnerRepository;
    this.customerRepository = customerRepository;
    this.eventRepository = eventRepository;
    this.ticketRepository = ticketRepository;
  }

  @Bean
  public CreateCustomerUseCase createCustomerUseCase() {
    return new CreateCustomerUseCase(this.customerRepository);
  }

  @Bean
  public CreateEventUseCase createEventUseCase() {
    return new CreateEventUseCase(this.eventRepository, this.partnerRepository);
  }

  @Bean
  public CreatePartnerUseCase createPartnerUseCase() {
    return new CreatePartnerUseCase(this.partnerRepository);
  }

  @Bean
  public GetCustomerByIdUseCase getCustomerByIdUseCase() {
    return new GetCustomerByIdUseCase(this.customerRepository);
  }

  @Bean
  public GetPartnerByIdUseCase getPartnerByIdUseCase() {
    return new GetPartnerByIdUseCase(this.partnerRepository);
  }

  @Bean
  public SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase() {
    return new SubscribeCustomerToEventUseCase(this.eventRepository, this.customerRepository, this.ticketRepository);
  }

}
