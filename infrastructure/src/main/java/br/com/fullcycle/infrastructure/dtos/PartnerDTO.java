package br.com.fullcycle.infrastructure.dtos;

import br.com.fullcycle.infrastructure.jpa.entities.PartnerEntity;

import java.util.UUID;

public class PartnerDTO {

  private UUID id;

  private String name;

  private String cnpj;

  private String email;

  public PartnerDTO() {
  }

  public PartnerDTO(final UUID id) {
    this.id = id;
  }

  public PartnerDTO(final PartnerEntity partner) {
    this.id = partner.getId();
    this.name = partner.getName();
    this.cnpj = partner.getCnpj();
    this.email = partner.getEmail();
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

}
