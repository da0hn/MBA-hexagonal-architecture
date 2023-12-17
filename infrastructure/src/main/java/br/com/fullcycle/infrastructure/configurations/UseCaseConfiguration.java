package br.com.fullcycle.infrastructure.configurations;

import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.application.ticket.CreateTicketForCustomerUseCase;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.domain.ticket.TicketRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

  private final PartnerRepository partnerRepository;

  private final CustomerRepository customerRepository;

  private final EventRepository eventRepository;

  private final TicketRepository ticketRepository;

  public UseCaseConfiguration(
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

  @Bean
  public CreateTicketForCustomerUseCase createTicketForCustomerUseCase() {
    return new CreateTicketForCustomerUseCase(this.ticketRepository);
  }

}
