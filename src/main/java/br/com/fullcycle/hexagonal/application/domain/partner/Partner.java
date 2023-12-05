package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.domain.Entities;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.domain.person.Name;

public class Partner {

  private final PartnerId partnerId;

  private final Name name;

  private final Cnpj cnpj;

  private final Email email;

  public Partner(final PartnerId partnerId, final Name name, final Cnpj cnpj, final Email email) {
    this.partnerId = Entities.requireNonNull(partnerId, "Partner id cannot be null");
    this.name = Entities.requireNonNull(name, "Partner name cannot be null");
    this.cnpj = Entities.requireNonNull(cnpj, "Partner cnpj cannot be null");
    this.email = Entities.requireNonNull(email, "Partner email cannot be null");
  }

  public static Partner newPartner(final String name, final String cnpj, final String email) {
    return new Partner(PartnerId.unique(), new Name(name), new Cnpj(cnpj), new Email(email));
  }

  public PartnerId partnerId() {
    return this.partnerId;
  }

  public Name name() {
    return this.name;
  }

  public Cnpj cnpj() {
    return this.cnpj;
  }

  public Email email() {
    return this.email;
  }

  @Override
  public int hashCode() {
    return this.partnerId.hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof final Partner partner)) return false;

    return this.partnerId.equals(partner.partnerId);
  }

}
