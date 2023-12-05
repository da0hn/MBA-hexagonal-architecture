package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

  private final Map<String, Customer> customers;

  private final Map<String, Customer> customersByEmail;

  private final Map<String, Customer> customersByCpf;

  public InMemoryCustomerRepository() {
    this.customers = new HashMap<>();
    this.customersByEmail = new HashMap<>();
    this.customersByCpf = new HashMap<>();
  }

  @Override
  public Optional<Customer> customerOfId(final CustomerId customerId) {
    return Optional.ofNullable(this.customers.getOrDefault(customerId.asString(), null));
  }

  @Override
  public Optional<Customer> customerOfCPF(final Cpf cpf) {
    return Optional.ofNullable(this.customersByCpf.getOrDefault(cpf.value(), null));
  }

  @Override
  public Optional<Customer> customerOfEmail(final Email email) {
    return Optional.ofNullable(this.customersByEmail.getOrDefault(email.value(), null));
  }

  @Override
  public Customer create(final Customer customer) {
    this.customers.put(customer.customerId().asString(), customer);
    this.customersByEmail.put(customer.email().value(), customer);
    this.customersByCpf.put(customer.cpf().value(), customer);
    return customer;
  }

  @Override
  public Customer update(final Customer customer) {
    this.customers.put(customer.customerId().asString(), customer);
    this.customersByEmail.put(customer.email().value(), customer);
    this.customersByCpf.put(customer.cpf().value(), customer);
    return customer;
  }

  @Override
  public void deleteAll() {
    this.customers.clear();
    this.customersByEmail.clear();
    this.customersByCpf.clear();
  }

}
