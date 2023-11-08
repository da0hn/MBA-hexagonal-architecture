package br.com.fullcycle.hexagonal.infrastructure.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "partners")
public class Partner {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String name;

  private String cnpj;

  private String email;

  public Partner() {
  }

  public Partner(final Long id, final String name, final String cnpj, final String email) {
    this.id = id;
    this.name = name;
    this.cnpj = cnpj;
    this.email = email;
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
