package br.com.fullcycle.infrastructure.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Optional;

public class SpringHttpRouter implements HttpRouter {

  private static final Logger log = LoggerFactory.getLogger(SpringHttpRouter.class);

  private final RouterFunctions.Builder router;

  public SpringHttpRouter() {
    this.router = RouterFunctions.route();
  }

  public RouterFunctions.Builder router() {
    return this.router;
  }

  @Override
  public <T> HttpRouter POST(final String pattern, final HttpHandler<T> handler) {

    this.router.POST(pattern, serverRequest -> {
      try {
        final var request = new SpringHttpRequest(serverRequest);
        final var response = handler.handle(request);
        return ServerResponse.status(response.statusCode())
          .headers(headers -> response.headers().forEach(headers::add))
          .body(response.body());
      }
      catch (final Throwable e) {
        log.error("Unexpected error was observed at %s".formatted(pattern), e);
        return ServerResponse.status(500).body("Unexpected error was observed");
      }
    });

    return this;
  }

  @Override
  public <T> HttpRouter GET(final String pattern, final HttpHandler<T> handler) {
    return null;
  }

  public record SpringHttpRequest(ServerRequest request) implements HttpRequest {

    @Override
    public <T> T body(final Class<T> tClass) {
      try {
        return this.request.body(tClass);
      }
      catch (final Throwable e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public String pathParameter(final String parameter) {
      return this.request.pathVariable(parameter);
    }

    @Override
    public Optional<String> queryParameter(final String parameter) {
      return this.request.param(parameter);
    }

  }

}
