package br.com.fullcycle.application.customer;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.person.Cpf;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.customer.Customer;

public class CreateCustomerUseCase extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

  private final CustomerRepository customerRepository;

  public CreateCustomerUseCase(final CustomerRepository customerRepository) { this.customerRepository = customerRepository; }

  public Output execute(final Input input) {
    if (this.customerRepository.customerOfCPF(new Cpf(input.cpf())).isPresent()) {
      throw new ValidationException("Customer already exists");
    }
    if (this.customerRepository.customerOfEmail(new Email(input.email())).isPresent()) {
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
