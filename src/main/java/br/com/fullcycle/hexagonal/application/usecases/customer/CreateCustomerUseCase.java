package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.usecases.UseCase;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

public class CreateCustomerUseCase extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

  private final CustomerRepository customerRepository;

  public CreateCustomerUseCase(final CustomerRepository customerRepository) { this.customerRepository = customerRepository; }

  public Output execute(final Input input) {
    if (this.customerRepository.customerOfCPF(input.cpf()).isPresent()) {
      throw new ValidationException("Customer already exists");
    }
    if (this.customerRepository.customerOfEmail(input.email()).isPresent()) {
      throw new ValidationException("Customer already exists");
    }

    var customer = Customer.newCustomer(input.name(), input.cpf(), input.email());

    customer = this.customerRepository.create(customer);

    return new Output(
      customer.customerId().asString(),
      customer.cpf().value(),
      customer.email().value(),
      customer.name().value()
    );

  }

  public record Input(String cpf, String email, String name) { }

  public record Output(String id, String cpf, String email, String name) { }

}
