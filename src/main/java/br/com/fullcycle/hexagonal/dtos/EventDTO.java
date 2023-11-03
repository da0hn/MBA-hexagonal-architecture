package br.com.fullcycle.hexagonal.dtos;

import br.com.fullcycle.hexagonal.models.Event;

import java.time.format.DateTimeFormatter;

public class EventDTO {

  private Long id;

  private String name;

  private String date;

  private int totalSpots;

  private PartnerDTO partner;

  public EventDTO() {
  }

  public EventDTO(final Event event) {
    this.id = event.getId();
    this.name = event.getName();
    this.date = event.getDate().format(DateTimeFormatter.ISO_DATE);
    this.totalSpots = event.getTotalSpots();
    this.partner = new PartnerDTO(event.getPartner());
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

  public String getDate() {
    return this.date;
  }

  public void setDate(final String date) {
    this.date = date;
  }

  public int getTotalSpots() {
    return this.totalSpots;
  }

  public void setTotalSpots(final int totalSpots) {
    this.totalSpots = totalSpots;
  }

  public PartnerDTO getPartner() {
    return this.partner;
  }

  public void setPartner(final PartnerDTO partner) {
    this.partner = partner;
  }

}
