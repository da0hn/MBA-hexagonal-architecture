package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

  private final CustomerJpaRepository customerJpaRepository;

  public CustomerRepositoryAdapter(final CustomerJpaRepository customerJpaRepository) {
    this.customerJpaRepository =
      customerJpaRepository;
  }

  @Override
  public Optional<Customer> customerOfId(final CustomerId customerId) {
    return this.customerJpaRepository.findById(customerId.value())
      .map(CustomerEntity::toCustomer);
  }

  @Override
  public Optional<Customer> customerOfCPF(final Cpf cpf) {
    return this.customerJpaRepository.findByCpf(cpf.value())
      .map(CustomerEntity::toCustomer);
  }

  @Override
  public Optional<Customer> customerOfEmail(final Email email) {
    return this.customerJpaRepository.findByEmail(email.value())
      .map(CustomerEntity::toCustomer);
  }

  @Override
  @Transactional
  public Customer create(final Customer customer) {
    return this.customerJpaRepository.save(CustomerEntity.of(customer)).toCustomer();
  }

  @Override
  @Transactional
  public Customer update(final Customer customer) {
    return this.customerJpaRepository.save(CustomerEntity.of(customer)).toCustomer();
  }

}
