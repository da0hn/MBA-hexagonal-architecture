package br.com.fullcycle.infrastructure.rest.presenters;

import br.com.fullcycle.application.Presenter;
import br.com.fullcycle.application.PresenterType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PresentersFactory {

  private final Map<PresenterType, Presenter<?, ?>> presentersMap;

  public PresentersFactory(final Collection<Presenter<?, ?>> presenters) {
    this.presentersMap = presenters.stream().distinct()
      .collect(Collectors.toMap(Presenter::type, Function.identity()));
  }

  public Presenter<?, ?> getPresenter(final PresenterType presenterType) {
    return this.presentersMap.get(presenterType);
  }

}
