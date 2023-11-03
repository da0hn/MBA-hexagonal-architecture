package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.services.PartnerService;
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
@RequestMapping(value = "partners")
public class PartnerController {

  @Autowired
  private PartnerService partnerService;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody final PartnerDTO dto) {
    try {
      final var createPartnerUseCase = new CreatePartnerUseCase(this.partnerService);
      final var output = createPartnerUseCase.execute(new CreatePartnerUseCase.Input(dto.getCnpj(), dto.getEmail(), dto.getName()));
      return ResponseEntity.created(URI.create("/partners/" + output.id())).body(output);
    }
    catch (final ValidationException e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable final Long id) {
    final var getPartnerByIdUseCase = new GetPartnerByIdUseCase(this.partnerService);
    return getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id))
      .map(ResponseEntity::ok)
      .orElseGet(ResponseEntity.notFound()::build);
  }

}
