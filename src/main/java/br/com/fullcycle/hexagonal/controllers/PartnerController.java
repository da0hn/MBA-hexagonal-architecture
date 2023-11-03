package br.com.fullcycle.hexagonal.controllers;

import br.com.fullcycle.hexagonal.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.models.Partner;
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
    if (this.partnerService.findByCnpj(dto.getCnpj()).isPresent()) {
      return ResponseEntity.unprocessableEntity().body("Partner already exists");
    }
    if (this.partnerService.findByEmail(dto.getEmail()).isPresent()) {
      return ResponseEntity.unprocessableEntity().body("Partner already exists");
    }

    var partner = new Partner();
    partner.setName(dto.getName());
    partner.setCnpj(dto.getCnpj());
    partner.setEmail(dto.getEmail());

    partner = this.partnerService.save(partner);

    return ResponseEntity.created(URI.create("/partners/" + partner.getId())).body(partner);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable final Long id) {
    final var partner = this.partnerService.findById(id);
    if (partner.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(partner.get());
  }

}
