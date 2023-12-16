package br.com.fullcycle.infrastructure.rest.presenters;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.PresenterType;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PublicGetCustomerByIdString implements Presenter<Optional<GetCustomerByIdUseCase.Output>, Object> {

  private static final Logger log = LoggerFactory.getLogger(PublicGetCustomerByIdString.class);

  @Override
  public String present(final Optional<GetCustomerByIdUseCase.Output> input) {
    return input.map(GetCustomerByIdUseCase.Output::id)
      .orElse("Not Found");
  }

  @Override
  public String handleError(final Throwable throwable) {
    log.error("Error while getting customer by id", throwable);
    return "Not Found";
  }

  @Override
  public PresenterType type() {
    return PresenterType.PUBLIC_GET_CUSTOMER_BY_ID;
  }

}
