package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPartnerRepository implements PartnerRepository {

  private final Map<String, Partner> partners;

  private final Map<String, Partner> partnersByEmail;

  private final Map<String, Partner> partnersByCnpj;

  public InMemoryPartnerRepository() {
    this.partners = new HashMap<>();
    this.partnersByEmail = new HashMap<>();
    this.partnersByCnpj = new HashMap<>();
  }

  @Override
  public Optional<Partner> partnerOfId(final PartnerId partnerId) {
    return Optional.ofNullable(this.partners.getOrDefault(partnerId.asString(), null));

  }

  @Override
  public Optional<Partner> partnerOfCnpj(final String cnpj) {
    return Optional.ofNullable(this.partnersByCnpj.getOrDefault(cnpj, null));
  }

  @Override
  public Optional<Partner> partnerOfEmail(final String email) {
    return Optional.ofNullable(this.partnersByEmail.getOrDefault(email, null));
  }

  @Override
  public Partner create(final Partner partner) {
    this.partners.put(partner.partnerId().asString(), partner);
    this.partnersByEmail.put(partner.email().value(), partner);
    this.partnersByCnpj.put(partner.cnpj().value(), partner);
    return partner;
  }

  @Override
  public Partner update(final Partner partner) {
    this.partners.put(partner.partnerId().asString(), partner);
    this.partnersByEmail.put(partner.email().value(), partner);
    this.partnersByCnpj.put(partner.cnpj().value(), partner);
    return partner;
  }

}
