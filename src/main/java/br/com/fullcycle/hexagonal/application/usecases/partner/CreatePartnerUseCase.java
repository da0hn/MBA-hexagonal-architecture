package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

public class CreatePartnerUseCase extends UseCase<CreatePartnerUseCase.Input, CreatePartnerUseCase.Output> {

  private final PartnerRepository partnerRepository;

  public CreatePartnerUseCase(final PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  @Override
  public Output execute(final Input input) {
    if (this.partnerRepository.partnerOfCnpj(new Cnpj(input.cnpj())).isPresent()) {
      throw new ValidationException("Partner already exists");
    }
    if (this.partnerRepository.partnerOfEmail(new Email(input.email())).isPresent()) {
      throw new ValidationException("Partner already exists");
    }

    var partner = Partner.newPartner(input.name(), input.cnpj(), input.email());

    partner = this.partnerRepository.create(partner);

    return new Output(
      partner.partnerId().asString(),
      partner.cnpj().value(),
      partner.email().value(),
      partner.name().value()
    );
  }

  public record Input(String cnpj, String email, String name) { }

  public record Output(String id, String cnpj, String email, String name) { }

}
