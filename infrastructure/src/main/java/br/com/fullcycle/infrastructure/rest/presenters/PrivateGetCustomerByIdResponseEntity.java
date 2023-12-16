package br.com.fullcycle.infrastructure.rest.presenters;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.PresenterType;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PrivateGetCustomerByIdResponseEntity implements Presenter<Optional<GetCustomerByIdUseCase.Output>, Object> {

  private static final Logger log = LoggerFactory.getLogger(PrivateGetCustomerByIdResponseEntity.class);

  @Override
  public ResponseEntity<?> present(final Optional<GetCustomerByIdUseCase.Output> input) {
    return input.map(ResponseEntity::ok)
      .orElseGet(ResponseEntity.notFound()::build);

  }

  @Override
  public ResponseEntity<?> handleError(final Throwable throwable) {
    log.error("Error while getting customer by id", throwable);
    return ResponseEntity.notFound().build();
  }

  @Override
  public PresenterType type() {
    return PresenterType.PRIVATE_GET_CUSTOMER_BY_ID;
  }

}
