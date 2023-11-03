package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.dtos.CustomerDTO;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody final CustomerDTO dto) {
    try {
      final var createCustomerUseCase = new CreateCustomerUseCase(this.customerService);
      final var output = createCustomerUseCase.execute(new CreateCustomerUseCase.Input(dto.getCpf(), dto.getEmail(), dto.getName()));
      return ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);
    }
    catch (final ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable final Long id) {
    final var customer = this.customerService.findById(id);
    if (customer.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(customer.get());
  }

}
