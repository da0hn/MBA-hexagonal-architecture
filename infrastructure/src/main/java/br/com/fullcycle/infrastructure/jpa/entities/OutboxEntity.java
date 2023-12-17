package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.DomainEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;
import java.util.function.Function;

@Entity(name = "Outbox")
@Table(name = "outbox")
public class OutboxEntity {

  @Id
  private UUID id;

  @Column(name = "content", length = 4_000, columnDefinition = "JSON")
  private String content;

  @Column(name = "is_published")
  private Boolean published;

  public OutboxEntity() {
  }

  public OutboxEntity(final UUID id, final String content, final Boolean published) {
    this.id = id;
    this.content = content;
    this.published = published;
  }

  public static OutboxEntity of(final DomainEvent domainEvent, final Function<? super DomainEvent, String> toJson) {
    return new OutboxEntity(
      UUID.fromString(domainEvent.domainEventId()),
      toJson.apply(domainEvent),
      false
    );
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public Boolean getPublished() {
    return this.published;
  }

  public void setPublished(final Boolean published) {
    this.published = published;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof final OutboxEntity that)) return false;

    return this.id.equals(that.id);
  }

  public OutboxEntity notePublished() {
    this.published = true;
    return this;
  }

}
