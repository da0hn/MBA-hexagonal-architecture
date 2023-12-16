package br.com.fullcycle.application;

public abstract class NullaryUseCase<OUTPUT> {

  public abstract OUTPUT execute();

  public <T> T execute(final Presenter<OUTPUT, T> presenter) {
    try {
      return presenter.present(this.execute());
    }
    catch (final Exception e) {
      return presenter.handleError(e);
    }
  }

}
