package br.com.fullcycle.domain.customer;

import br.com.fullcycle.domain.Entities;
import br.com.fullcycle.domain.person.Cpf;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.person.Name;

public class Customer {

  private final CustomerId customerId;

  private final Name name;

  private final Cpf cpf;

  private final Email email;

  public Customer(final CustomerId customerId, final Name name, final Cpf cpf, final Email email) {
    this.customerId = Entities.requireNonNull(customerId, "Customer id cannot be null");
    this.name = Entities.requireNonNull(name, "Customer name cannot be null");
    this.cpf = Entities.requireNonNull(cpf, "Customer cpf cannot be null");
    this.email = Entities.requireNonNull(email, "Customer email cannot be null");
  }

  public static Customer newCustomer(final String name, final String cpf, final String email) {
    return new Customer(CustomerId.unique(), new Name(name), new Cpf(cpf), new Email(email));
  }

  public CustomerId customerId() {
    return this.customerId;
  }

  public Name name() {
    return this.name;
  }

  public Cpf cpf() {
    return this.cpf;
  }

  public Email email() {
    return this.email;
  }

  @Override
  public int hashCode() {
    return this.customerId.hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof final Customer customer)) return false;

    return this.customerId.equals(customer.customerId);
  }

}
