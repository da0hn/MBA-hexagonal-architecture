package br.com.fullcycle.hexagonal.dtos;

public class SubscribeDTO {

  private Long customerId;

  public Long getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(final Long customerId) {
    this.customerId = customerId;
  }

}
