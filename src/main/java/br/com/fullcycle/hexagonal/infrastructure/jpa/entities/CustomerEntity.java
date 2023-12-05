package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "customers")
public class CustomerEntity {

  @Id
  private String id;

  private String name;

  private String cpf;

  private String email;

  public CustomerEntity() {
  }

  public CustomerEntity(final String id, final String name, final String cpf, final String email) {
    this.id = id;
    this.name = name;
    this.cpf = cpf;
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

  public String getCpf() {
    return this.cpf;
  }

  public void setCpf(final String cpf) {
    this.cpf = cpf;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final CustomerEntity customer = (CustomerEntity) o;
    return Objects.equals(this.id, customer.id);
  }

}
