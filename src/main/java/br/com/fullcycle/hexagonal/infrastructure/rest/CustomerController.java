package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CreateCustomerUseCase createCustomerUseCase;

  private final GetCustomerByIdUseCase getCustomerByIdUseCase;

  public CustomerController(
    final CreateCustomerUseCase createCustomerUseCase,
    final GetCustomerByIdUseCase getCustomerByIdUseCase
  ) {
    this.createCustomerUseCase = createCustomerUseCase;
    this.getCustomerByIdUseCase = getCustomerByIdUseCase;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody final NewCustomerDTO dto) {
    try {
      final var output = this.createCustomerUseCase.execute(new CreateCustomerUseCase.Input(dto.cpf(), dto.email(), dto.name()));
      return ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);
    }
    catch (final ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable final Long id) {
    return this.getCustomerByIdUseCase.execute(new GetCustomerByIdUseCase.Input(id))
      .map(ResponseEntity::ok)
      .orElseGet(ResponseEntity.notFound()::build);
  }

}
