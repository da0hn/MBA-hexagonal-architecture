package br.com.fullcycle.hexagonal.infrastructure.services;

import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PartnerService {

  @Autowired
  private PartnerRepository repository;

  @Transactional
  public Partner save(final Partner customer) {
    return this.repository.save(customer);
  }

  public Optional<Partner> findById(final Long id) {
    return this.repository.findById(id);
  }

  public Optional<Partner> findByCnpj(final String cnpj) {
    return this.repository.findByCnpj(cnpj);
  }

  public Optional<Partner> findByEmail(final String email) {
    return this.repository.findByEmail(email);
  }

}
