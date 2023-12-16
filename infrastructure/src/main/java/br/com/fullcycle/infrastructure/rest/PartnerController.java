package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.infrastructure.dtos.PartnerDTO;
import br.com.fullcycle.infrastructure.http.HttpRouter;
import br.com.fullcycle.infrastructure.http.HttpRouter.HttpRequest;
import br.com.fullcycle.infrastructure.http.HttpRouter.HttpResponse;

import java.net.URI;

import static br.com.fullcycle.application.partner.CreatePartnerUseCase.Input;

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

  public HttpRouter bind(HttpRouter httpRouter) {
    httpRouter.GET("/partners/{id}", this::get);
    httpRouter.POST("/partners", this::create);
    return httpRouter;
  }

  public HttpResponse<?> create(final HttpRequest request) {
    try {
      final var dto = request.body(PartnerDTO.class);
      final var output = this.createPartnerUseCase.execute(new Input(
        dto.getCnpj(),
        dto.getEmail(),
        dto.getName()
      ));
      return HttpResponse.created(URI.create("/partners/" + output.id())).body(output);
    }
    catch (final ValidationException e) {
      return HttpResponse.unprocessableEntity().body(e.getMessage());
    }
  }

  public HttpResponse<?> get(final HttpRequest request) {
    final var id = request.pathParameter("id");
    return this.getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id))
      .map(HttpResponse::ok)
      .orElseGet(HttpResponse.notFound()::build);
  }

}
