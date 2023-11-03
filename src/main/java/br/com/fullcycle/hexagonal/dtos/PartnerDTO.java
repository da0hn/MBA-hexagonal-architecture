package br.com.fullcycle.hexagonal.dtos;

import br.com.fullcycle.hexagonal.models.Partner;

public class PartnerDTO {

  private Long id;

  private String name;

  private String cnpj;

  private String email;

  public PartnerDTO() {
  }

  public PartnerDTO(final Long id) {
    this.id = id;
  }

  public PartnerDTO(final Partner partner) {
    this.id = partner.getId();
    this.name = partner.getName();
    this.cnpj = partner.getCnpj();
    this.email = partner.getEmail();
  }

  public Long getId() {
    return this.id;
  }

  public void setId(final Long id) {
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
