package com.infosupport.training.reactjs.gtdserver.contexts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Builder
@Getter
@Table("CONTEXTS")
@With
public class Context {
    @Id
    private final UUID id;
    @Column("USER_ID")
    private final UUID userId;
    private final String name;

    @JsonCreator
    public Context(@JsonProperty("id") UUID id,
                   @JsonProperty("user_id") UUID userId,
                   @JsonProperty("name") String name) {
        this.id = id;
        this.userId = userId;
        this.name = requireNonNull(name);
    }

    @JsonIgnore
    public UUID getUserId() {
        return this.userId;
    }
}
