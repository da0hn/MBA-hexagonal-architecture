package br.com.fullcycle.hexagonal.infrastructure.services;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PartnerService {

  @Autowired
  private PartnerJpaRepository repository;

  @Transactional
  public PartnerEntity save(final PartnerEntity customer) {
    return this.repository.save(customer);
  }

  public Optional<PartnerEntity> findById(final Long id) {
    return this.repository.findById(id);
  }

  public Optional<PartnerEntity> findByCnpj(final String cnpj) {
    return this.repository.findByCnpj(cnpj);
  }

  public Optional<PartnerEntity> findByEmail(final String email) {
    return this.repository.findByEmail(email);
  }

}
