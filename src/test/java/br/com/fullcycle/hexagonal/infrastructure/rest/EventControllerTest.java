package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
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
  private CustomerJpaRepository customerRepository;

  @Autowired
  private PartnerJpaRepository partnerRepository;

  @Autowired
  private EventJpaRepository eventRepository;

  private CustomerEntity johnDoe;

  private PartnerEntity disney;

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {

    final var event = new NewEventDTO("Disney on Ice", "2021-01-01", 100, null);

    final var result = this.mvc.perform(
        MockMvcRequestBuilders.post("/events")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(event))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualResponse = this.mapper.readValue(result, CreateEventUseCase.Output.class);
    Assertions.assertEquals(event.date(), actualResponse.date());
    Assertions.assertEquals(event.totalSpots(), actualResponse.totalSpots());
    Assertions.assertEquals(event.name(), actualResponse.name());
  }

  @Test
  @Transactional
  @DisplayName("Deve comprar um ticket de um evento")
  public void testReserveTicket() throws Exception {

    final var event = new NewEventDTO(
      "Disney on Ice",
      "2021-01-01",
      100,
      null
    );

    final var createResult = this.mvc.perform(
        MockMvcRequestBuilders.post("/events")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(event))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
      .andReturn().getResponse().getContentAsByteArray();

    final var eventId = this.mapper.readValue(createResult, CreateEventUseCase.Output.class).id();

    final var subscribeDTO = new SubscribeDTO(this.johnDoe.getId().toString(), null);

    this.mvc.perform(
        MockMvcRequestBuilders.post("/events/{id}/subscribe", eventId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(subscribeDTO))
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualEvent = this.eventRepository.findById(null).get();
    Assertions.assertEquals(1, actualEvent.getTickets().size());
  }

  @BeforeEach
  void setUp() {
    this.johnDoe = this.customerRepository.save(new CustomerEntity(null, "John Doe", "123", "john@gmail.com"));
    this.disney = this.partnerRepository.save(new PartnerEntity(null, "Disney", "456", "disney@gmail.com"));
  }

  @AfterEach
  void tearDown() {
    this.eventRepository.deleteAll();
    this.customerRepository.deleteAll();
    this.partnerRepository.deleteAll();
  }

}
