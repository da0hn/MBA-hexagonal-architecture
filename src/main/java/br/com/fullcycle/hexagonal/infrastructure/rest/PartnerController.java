package br.com.fullcycle.hexagonal.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.PartnerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/partners")
public class PartnerController {

  private final CreatePartnerUseCase createPartnerUseCase;

  private final GetPartnerByIdUseCase getPartnerByIdUseCase;

  public PartnerController(
    final CreatePartnerUseCase createPartnerUseCase,
    final GetPartnerByIdUseCase getPartnerByIdUseCase
  ) {
    this.createPartnerUseCase = createPartnerUseCase;
    this.getPartnerByIdUseCase = getPartnerByIdUseCase;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody final PartnerDTO dto) {
    try {
      final var output = this.createPartnerUseCase.execute(new CreatePartnerUseCase.Input(
        dto.getCnpj(),
        dto.getEmail(),
        dto.getName()
      ));
      return ResponseEntity.created(URI.create("/partners/" + output.id())).body(output);
    }
    catch (final ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable final String id) {
    return this.getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id))
      .map(ResponseEntity::ok)
      .orElseGet(ResponseEntity.notFound()::build);
  }

}
