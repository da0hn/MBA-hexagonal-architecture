package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.infrastructure.dtos.NewCustomerDTO;
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

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class CustomerControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Test
  @DisplayName("Deve criar um cliente")
  public void testCreate() throws Exception {

    final var customer = new NewCustomerDTO("John Doe", "123.456.789-01", "john.doe@gmail.com");

    final var result = this.mvc.perform(
        MockMvcRequestBuilders.post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(customer))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.header().exists("Location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualResponse = this.mapper.readValue(result, NewCustomerDTO.class);
    Assertions.assertEquals(customer.name(), actualResponse.name());
    Assertions.assertEquals(customer.cpf(), actualResponse.cpf());
    Assertions.assertEquals(customer.email(), actualResponse.email());
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
  public void testCreateWithDuplicatedCPFShouldFail() throws Exception {

    final var customer = new NewCustomerDTO("John Doe", "123.456.789-01", "john.doe@gmail.com");

    // Cria o primeiro cliente
    this.mvc.perform(
        MockMvcRequestBuilders.post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(customer))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.header().exists("Location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
      .andReturn().getResponse().getContentAsByteArray();

    final var otherCustomer = new NewCustomerDTO("John Doe", "123.456.789-01", "john2@gmail.com");

    // Tenta criar o segundo cliente com o mesmo CPF
    this.mvc.perform(
        MockMvcRequestBuilders.post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(otherCustomer))
      )
      .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
      .andExpect(MockMvcResultMatchers.content().string("Customer already exists"));
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
  public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

    final var customer = new NewCustomerDTO("John Doe", "123.456.789-01", "john.doe@gmail.com");

    // Cria o primeiro cliente
    this.mvc.perform(
        MockMvcRequestBuilders.post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(customer))
      )
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.header().exists("Location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
      .andReturn().getResponse().getContentAsByteArray();

    final var otherCustomer = new NewCustomerDTO("John Doe", "999.999.189-01", "john.doe@gmail.com");

    // Tenta criar o segundo cliente com o mesmo CPF
    this.mvc.perform(
        MockMvcRequestBuilders.post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(otherCustomer))
      )
      .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
      .andExpect(MockMvcResultMatchers.content().string("Customer already exists"));
  }

  @Test
  @DisplayName("Deve obter um cliente por id")
  public void testGet() throws Exception {

    final var customer = new NewCustomerDTO("John Doe", "123.456.789-01", "john.doe@gmail.com");

    final var createResult = this.mvc.perform(
        MockMvcRequestBuilders.post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(customer))
      )
      .andReturn().getResponse().getContentAsByteArray();

    final var customerId = this.mapper.readValue(createResult, CreateCustomerUseCase.Output.class).id();

    final var result = this.mvc.perform(
        MockMvcRequestBuilders.get("/customers/{id}", customerId)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn().getResponse().getContentAsByteArray();

    final var actualResponse = this.mapper.readValue(result, GetCustomerByIdUseCase.Output.class);
    Assertions.assertEquals(customerId, actualResponse.id());
    Assertions.assertEquals(customer.name(), actualResponse.name());
    Assertions.assertEquals(customer.cpf(), actualResponse.cpf());
    Assertions.assertEquals(customer.email(), actualResponse.email());
  }

  @Test
  @DisplayName("Deve obter um cliente por id com X-Public")
  public void testGetXPublic() throws Exception {

    final var customer = new NewCustomerDTO("John Doe", "123.456.789-01", "john.doe@gmail.com");

    final var createResult = this.mvc.perform(
        MockMvcRequestBuilders.post("/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(this.mapper.writeValueAsString(customer))
      )
      .andReturn().getResponse().getContentAsByteArray();

    final var customerId = this.mapper.readValue(createResult, CreateCustomerUseCase.Output.class).id();

    final var result = this.mvc.perform(
        MockMvcRequestBuilders.get("/customers/{id}", customerId)
          .header("X-Public", true)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn().getResponse().getContentAsByteArray();

    Assertions.assertEquals(customerId, new String(result));
  }

  @AfterEach
  void tearDown() {
    this.customerRepository.deleteAll();
  }

}
