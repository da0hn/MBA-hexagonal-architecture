package br.com.fullcycle.hexagonal.infrastructure.services;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerService {

  @Autowired
  private CustomerJpaRepository repository;

  @Transactional
  public CustomerEntity save(final CustomerEntity customer) {
    return this.repository.save(customer);
  }

  public Optional<CustomerEntity> findById(final Long id) {
    return this.repository.findById(id);
  }

  public Optional<CustomerEntity> findByCpf(final String cpf) {
    return this.repository.findByCpf(cpf);
  }

  public Optional<CustomerEntity> findByEmail(final String email) {
    return this.repository.findByEmail(email);
  }

}
