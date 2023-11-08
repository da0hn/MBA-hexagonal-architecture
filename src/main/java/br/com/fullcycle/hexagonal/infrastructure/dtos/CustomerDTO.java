package br.com.fullcycle.hexagonal.infrastructure.dtos;

import br.com.fullcycle.hexagonal.infrastructure.models.Customer;

public class CustomerDTO {

  private Long id;

  private String name;

  private String cpf;

  private String email;

  public CustomerDTO() {
  }

  public CustomerDTO(final Customer customer) {
    this.id = customer.getId();
    this.name = customer.getName();
    this.cpf = customer.getCpf();
    this.email = customer.getEmail();
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

}
