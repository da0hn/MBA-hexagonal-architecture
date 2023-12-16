package br.com.fullcycle.application;

public abstract class UseCase<INPUT, OUTPUT> {

  public abstract OUTPUT execute(INPUT input);

  public <T> T execute(final INPUT input, final Presenter<OUTPUT, T> presenter) {
    try {
      return presenter.present(this.execute(input));
    }
    catch (final Exception e) {
      return presenter.handleError(e);
    }
  }

}
