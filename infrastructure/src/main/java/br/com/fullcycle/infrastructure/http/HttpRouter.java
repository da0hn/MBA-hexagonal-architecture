package br.com.fullcycle.infrastructure.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface HttpRouter {

  <T> HttpRouter POST(String pattern, HttpHandler<T> handler);

  <T> HttpRouter GET(String pattern, HttpHandler<T> handler);

  @FunctionalInterface
  interface HttpHandler<T> {

    HttpResponse<T> handle(HttpRequest request);

  }

  interface HttpRequest {

    <T> T body(Class<T> tClass);

    String pathParameter(String parameter);

    Optional<String> queryParameter(String parameter);

  }

  record HttpResponse<T>(int statusCode, Map<String, String> headers, T body) {

    public static HttpResponse.Builder created(final URI uri) {
      return new Builder(201).location(uri);
    }

    public static HttpResponse.Builder unprocessableEntity() {
      return new Builder(422);
    }

    public static <T> HttpResponse<T> ok(final T body) {
      return new HttpResponse<>(200, Map.of(), body);
    }

    public static HttpResponse.Builder notFound() {
      return new Builder(404);
    }

    public static class Builder {

      private final int statusCode;

      private final Map<String, String> headers;

      public Builder(final int statusCode) {
        this.statusCode = statusCode;
        this.headers = new HashMap<>();
      }

      public <T> HttpResponse<T> build() {
        return new HttpResponse<>(this.statusCode, this.headers, null);
      }

      public <T> HttpResponse<T> body(final T body) {
        return new HttpResponse<>(this.statusCode, this.headers, body);
      }

      public Builder location(final URI uri) {
        this.headers.put("Location", uri.toASCIIString());
        return this;
      }

    }

  }

}
