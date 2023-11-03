package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.dtos.CustomerDTO;
import br.com.fullcycle.hexagonal.models.Customer;
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
    if (this.customerService.findByCpf(dto.getCpf()).isPresent()) {
      return ResponseEntity.unprocessableEntity().body("Customer already exists");
    }
    if (this.customerService.findByEmail(dto.getEmail()).isPresent()) {
      return ResponseEntity.unprocessableEntity().body("Customer already exists");
    }

    var customer = new Customer();
    customer.setName(dto.getName());
    customer.setCpf(dto.getCpf());
    customer.setEmail(dto.getEmail());

    customer = this.customerService.save(customer);

    return ResponseEntity.created(URI.create("/customers/" + customer.getId())).body(customer);
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
