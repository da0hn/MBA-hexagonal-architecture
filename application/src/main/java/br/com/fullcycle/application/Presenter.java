package br.com.fullcycle.application;

public interface Presenter<IN, OUT> {

  OUT present(IN input);

  OUT handleError(Throwable throwable);

  PresenterType type();

}
