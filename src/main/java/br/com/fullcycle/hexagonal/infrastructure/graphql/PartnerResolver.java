package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.PartnerDTO;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerResolver {

  private final PartnerService partnerService;

  public PartnerResolver(final PartnerService partnerService) { this.partnerService = partnerService; }

  @MutationMapping
  public CreatePartnerUseCase.Output createPartner(@Argument final PartnerDTO input) {
    final var createPartnerUseCase = new CreatePartnerUseCase(this.partnerService);
    return createPartnerUseCase.execute(new CreatePartnerUseCase.Input(
      input.getCnpj(),
      input.getEmail(),
      input.getName()
    ));
  }

  @QueryMapping
  public GetPartnerByIdUseCase.Output partnerOfId(@Argument final Long id) {
    final var getPartnerByIdUseCase = new GetPartnerByIdUseCase(this.partnerService);
    return getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id))
      .orElse(null);
  }

}
