package br.com.fullcycle.hexagonal.graphql;

import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.dtos.CustomerDTO;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerResolver {

  private final CustomerService customerService;

  public CustomerResolver(final CustomerService customerService) { this.customerService = customerService; }

  @MutationMapping
  public CreateCustomerUseCase.Output createCustomer(@Argument final CustomerDTO input) {
    final var createCustomerUseCase = new CreateCustomerUseCase(this.customerService);
    final var output = createCustomerUseCase.execute(new CreateCustomerUseCase.Input(
      input.getCpf(),
      input.getEmail(),
      input.getName()
    ));
    return output;
  }

  @QueryMapping
  public GetCustomerByIdUseCase.Output customerOfId(@Argument final Long id) {
    final var getCustomerByIdUseCase = new GetCustomerByIdUseCase(this.customerService);
    return getCustomerByIdUseCase.execute(new GetCustomerByIdUseCase.Input(id))
      .orElse(null);
  }

}
