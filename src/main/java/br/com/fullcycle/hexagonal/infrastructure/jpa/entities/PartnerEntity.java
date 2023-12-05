package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "partners")
public class PartnerEntity {

  @Id
  private String id;

  private String name;

  private String cnpj;

  private String email;

  public PartnerEntity() {
  }

  public PartnerEntity(final String id, final String name, final String cnpj, final String email) {
    this.id = id;
    this.name = name;
    this.cnpj = cnpj;
    this.email = email;
  }

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
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
