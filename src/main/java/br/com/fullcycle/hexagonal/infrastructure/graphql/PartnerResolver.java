package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.PartnerDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerResolver {

  private final CreatePartnerUseCase createPartnerUseCase;

  private final GetPartnerByIdUseCase getPartnerByIdUseCase;

  public PartnerResolver(
    final CreatePartnerUseCase createPartnerUseCase,
    final GetPartnerByIdUseCase getPartnerByIdUseCase
  ) {
    this.createPartnerUseCase = createPartnerUseCase;
    this.getPartnerByIdUseCase = getPartnerByIdUseCase;
  }

  @MutationMapping
  public CreatePartnerUseCase.Output createPartner(@Argument final PartnerDTO input) {
    return this.createPartnerUseCase.execute(new CreatePartnerUseCase.Input(
      input.getCnpj(),
      input.getEmail(),
      input.getName()
    ));
  }

  @QueryMapping
  public GetPartnerByIdUseCase.Output partnerOfId(@Argument final Long id) {
    return this.getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id))
      .orElse(null);
  }

}
