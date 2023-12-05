package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PartnerRepositoryAdapter implements PartnerRepository {

  private final PartnerJpaRepository partnerJpaRepository;

  public PartnerRepositoryAdapter(final PartnerJpaRepository partnerJpaRepository) {
    this.partnerJpaRepository = partnerJpaRepository;
  }

  @Override
  public Optional<Partner> partnerOfId(final PartnerId partnerId) {
    return this.partnerJpaRepository.findById(partnerId.value())
      .map(PartnerEntity::toPartner);
  }

  @Override
  public Optional<Partner> partnerOfCnpj(final Cnpj cnpj) {
    return this.partnerJpaRepository.findByCnpj(cnpj.value())
      .map(PartnerEntity::toPartner);
  }

  @Override
  public Optional<Partner> partnerOfEmail(final Email email) {
    return this.partnerJpaRepository.findByEmail(email.value())
      .map(PartnerEntity::toPartner);
  }

  @Override
  @Transactional
  public Partner create(final Partner partner) {
    return this.partnerJpaRepository.save(PartnerEntity.of(partner)).toPartner();
  }

  @Override
  public Partner update(final Partner partner) {
    return this.partnerJpaRepository.save(PartnerEntity.of(partner)).toPartner();
  }

  @Override
  public void deleteAll() {
    this.partnerJpaRepository.deleteAll();
  }

}
