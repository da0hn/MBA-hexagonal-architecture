package br.com.fullcycle.hexagonal.infrastructure.services;

import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository repository;

  @Transactional
  public Customer save(final Customer customer) {
    return this.repository.save(customer);
  }

  public Optional<Customer> findById(final Long id) {
    return this.repository.findById(id);
  }

  public Optional<Customer> findByCpf(final String cpf) {
    return this.repository.findByCpf(cpf);
  }

  public Optional<Customer> findByEmail(final String email) {
    return this.repository.findByEmail(email);
  }

}
