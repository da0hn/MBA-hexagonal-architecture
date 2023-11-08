package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;

import java.util.Optional;

public class GetPartnerByIdUseCase extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {

  private final PartnerService partnerService;

  public GetPartnerByIdUseCase(final PartnerService partnerService) { this.partnerService = partnerService; }

  @Override
  public Optional<Output> execute(final Input input) {
    return this.partnerService.findById(input.id())
      .map(c -> new Output(c.getId(), c.getCnpj(), c.getEmail(), c.getName()));
  }

  public record Input(Long id) { }

  public record Output(Long id, String cnpj, String email, String name) { }

}
