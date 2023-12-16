package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.infrastructure.dtos.NewCustomerDTO;
import br.com.fullcycle.infrastructure.rest.presenters.PresentersFactory;
import br.com.fullcycle.infrastructure.rest.presenters.PrivateGetCustomerByIdResponseEntity;
import br.com.fullcycle.infrastructure.rest.presenters.PublicGetCustomerByIdString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CreateCustomerUseCase createCustomerUseCase;

  private final GetCustomerByIdUseCase getCustomerByIdUseCase;

  private final PresentersFactory presentersFactory;

  public CustomerController(
    final CreateCustomerUseCase createCustomerUseCase,
    final GetCustomerByIdUseCase getCustomerByIdUseCase,
    final PresentersFactory presentersFactory
  ) {
    this.createCustomerUseCase = createCustomerUseCase;
    this.getCustomerByIdUseCase = getCustomerByIdUseCase;
    this.presentersFactory = presentersFactory;
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
  public Object get(
    @PathVariable final String id,
    @RequestHeader(name = "X-Public", required = false) final String xPublic
  ) {

    Presenter<Optional<GetCustomerByIdUseCase.Output>, Object> presenter = new PrivateGetCustomerByIdResponseEntity();

    if (xPublic != null) {
      presenter = new PublicGetCustomerByIdString();
    }

    return this.getCustomerByIdUseCase.execute(
      new GetCustomerByIdUseCase.Input(id),
      presenter
    );
  }

}
