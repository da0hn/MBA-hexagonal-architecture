package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.dtos.EventDTO;
import br.com.fullcycle.hexagonal.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.dtos.SubscribeDTO;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.repositories.EventRepository;
import br.com.fullcycle.hexagonal.repositories.PartnerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PartnerRepository partnerRepository;

  @Autowired
  private EventRepository eventRepository;

  private Customer johnDoe;

  private Partner disney;

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {

    final var event = new EventDTO();
    event.setDate("2021-01-01");
    event.setName("Disney on Ice");
    event.setTotalSpots(100);
    event.setPartner(new PartnerDTO(this.disney.getId()));

    final var result = this.mvc.perform(
        MockMvcRequestBuilders.post("/events")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(event))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualResponse = this.mapper.readValue(result, EventDTO.class);
    Assertions.assertEquals(event.getDate(), actualResponse.getDate());
    Assertions.assertEquals(event.getTotalSpots(), actualResponse.getTotalSpots());
    Assertions.assertEquals(event.getName(), actualResponse.getName());
  }

  @Test
  @Transactional
  @DisplayName("Deve comprar um ticket de um evento")
  public void testReserveTicket() throws Exception {

    final var event = new EventDTO();
    event.setDate("2021-01-01");
    event.setName("Disney on Ice");
    event.setTotalSpots(100);
    event.setPartner(new PartnerDTO(this.disney.getId()));

    final var createResult = this.mvc.perform(
        MockMvcRequestBuilders.post("/events")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(event))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
      .andReturn().getResponse().getContentAsByteArray();

    final var eventId = this.mapper.readValue(createResult, EventDTO.class).getId();

    final var sub = new SubscribeDTO();
    sub.setCustomerId(this.johnDoe.getId());

    this.mvc.perform(
        MockMvcRequestBuilders.post("/events/{id}/subscribe", eventId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(sub))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualEvent = this.eventRepository.findById(eventId).get();
    Assertions.assertEquals(1, actualEvent.getTickets().size());
  }

  @BeforeEach
  void setUp() {
    this.johnDoe = this.customerRepository.save(new Customer(null, "John Doe", "123", "john@gmail.com"));
    this.disney = this.partnerRepository.save(new Partner(null, "Disney", "456", "disney@gmail.com"));
  }

  @AfterEach
  void tearDown() {
    this.eventRepository.deleteAll();
    this.customerRepository.deleteAll();
    this.partnerRepository.deleteAll();
  }

}
