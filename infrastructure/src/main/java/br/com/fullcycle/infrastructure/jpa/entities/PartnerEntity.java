package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.person.Name;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity(name = "Partner")
@Table(name = "partners")
public class PartnerEntity {

  @Id
  private UUID id;

  private String name;

  private String cnpj;

  private String email;

  public PartnerEntity() {
  }

  public PartnerEntity(final UUID id, final String name, final String cnpj, final String email) {
    this.id = id;
    this.name = name;
    this.cnpj = cnpj;
    this.email = email;
  }

  public static PartnerEntity of(final Partner partner) {
    return new PartnerEntity(
      partner.partnerId().value(),
      partner.name().value(),
      partner.cnpj().value(),
      partner.email().value()
    );
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getCnpj() {
    return this.cnpj;
  }

  public void setCnpj(final String cnpj) {
    this.cnpj = cnpj;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public Partner toPartner() {
    return new Partner(new PartnerId(this.id), new Name(this.name), new Cnpj(this.cnpj), new Email(this.email));
  }

}
