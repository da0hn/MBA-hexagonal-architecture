package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.infrastructure.dtos.PartnerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import java.util.UUID;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class PartnerControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private PartnerRepository partnerRepository;

  @Test
  @DisplayName("Deve criar um parceiro")
  public void testCreate() throws Exception {

    final var partner = new PartnerDTO();
    partner.setCnpj("41.536.538/0001-00");
    partner.setEmail("john.doe@gmail.com");
    partner.setName("John Doe");

    final var result = this.mvc.perform(
        MockMvcRequestBuilders.post("/partners")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(partner))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.header().exists("Location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualResponse = this.mapper.readValue(result, PartnerDTO.class);
    Assertions.assertEquals(partner.getName(), actualResponse.getName());
    Assertions.assertEquals(partner.getCnpj(), actualResponse.getCnpj());
    Assertions.assertEquals(partner.getEmail(), actualResponse.getEmail());
  }

  @Test
  @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
  public void testCreateWithDuplicatedCPFShouldFail() throws Exception {

    final var partner = new PartnerDTO();
    partner.setCnpj("41.536.538/0001-00");
    partner.setEmail("john.doe@gmail.com");
    partner.setName("John Doe");

    // Cria o primeiro parceiro
    this.mvc.perform(
        MockMvcRequestBuilders.post("/partners")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(partner))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.header().exists("Location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
      .andReturn().getResponse().getContentAsByteArray();

    partner.setEmail("john2@gmail.com");

    // Tenta criar o segundo parceiro com o mesmo CPF
    this.mvc.perform(
        MockMvcRequestBuilders.post("/partners")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(partner))
      )
      .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
      .andExpect(MockMvcResultMatchers.content().string("Partner already exists"));
  }

  @Test
  @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
  public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

    final var partner = new PartnerDTO();
    partner.setCnpj("41.536.538/0001-00");
    partner.setEmail("john.doe@gmail.com");
    partner.setName("John Doe");

    // Cria o primeiro parceiro
    this.mvc.perform(
        MockMvcRequestBuilders.post("/partners")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(partner))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.header().exists("Location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
      .andReturn().getResponse().getContentAsByteArray();

    partner.setCnpj("66.666.538/0001-00");

    // Tenta criar o segundo parceiro com o mesmo CNPJ
    this.mvc.perform(
        MockMvcRequestBuilders.post("/partners")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(partner))
      )
      .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
      .andExpect(MockMvcResultMatchers.content().string("Partner already exists"));
  }

  @Test
  @DisplayName("Deve obter um parceiro por id")
  public void testGet() throws Exception {

    final var partner = new PartnerDTO();
    partner.setCnpj("41.536.538/0001-00");
    partner.setEmail("john.doe@gmail.com");
    partner.setName("John Doe");

    final var createResult = this.mvc.perform(
        MockMvcRequestBuilders.post("/partners")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(partner))
      )
      .andReturn().getResponse().getContentAsByteArray();

    final UUID partnerId = this.mapper.readValue(createResult, PartnerDTO.class).getId();

    final var result = this.mvc.perform(
        MockMvcRequestBuilders.get("/partners/{id}", partnerId)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualResponse = this.mapper.readValue(result, PartnerDTO.class);
    Assertions.assertEquals(partnerId, actualResponse.getId());
    Assertions.assertEquals(partner.getName(), actualResponse.getName());
    Assertions.assertEquals(partner.getCnpj(), actualResponse.getCnpj());
    Assertions.assertEquals(partner.getEmail(), actualResponse.getEmail());
  }

  @AfterEach
  void tearDown() {
    this.partnerRepository.deleteAll();
  }

}
