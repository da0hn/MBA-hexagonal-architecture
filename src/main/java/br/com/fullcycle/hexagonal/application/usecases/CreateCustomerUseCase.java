package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;

public class CreateCustomerUseCase extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

  private final CustomerService customerService;

  public CreateCustomerUseCase(final CustomerService customerService) { this.customerService = customerService; }

  public Output execute(final Input input) {
    if (this.customerService.findByCpf(input.cpf()).isPresent()) {
      throw new ValidationException("Customer already exists");
    }
    if (this.customerService.findByEmail(input.email()).isPresent()) {
      throw new ValidationException("Customer already exists");
    }

    var customer = new Customer();
    customer.setName(input.name());
    customer.setCpf(input.cpf());
    customer.setEmail(input.email());

    customer = this.customerService.save(customer);

    return new Output(customer.getId(), customer.getCpf(), customer.getEmail(), customer.getName());

  }

  public record Input(String cpf, String email, String name) { }

  public record Output(Long id, String cpf, String email, String name) { }

}
