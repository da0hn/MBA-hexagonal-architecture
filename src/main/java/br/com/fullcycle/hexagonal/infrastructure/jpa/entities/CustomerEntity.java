package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class CustomerEntity {

  @Id
  private UUID id;

  private String name;

  private String cpf;

  private String email;

  public CustomerEntity() {
  }

  public CustomerEntity(final UUID id, final String name, final String cpf, final String email) {
    this.id = id;
    this.name = name;
    this.cpf = cpf;
    this.email = email;
  }

  public static CustomerEntity of(final Customer customer) {
    return new CustomerEntity(
      customer.customerId().value(),
      customer.name().value(),
      customer.cpf().value(),
      customer.email().value()
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

  public Customer toCustomer() {
    return new Customer(
      new CustomerId(this.id),
      new Name(this.name),
      new Cpf(this.cpf),
      new Email(this.email)
    );
  }

}
