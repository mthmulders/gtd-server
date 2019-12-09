package com.infosupport.training.reactjs.gtdserver.tasks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Builder
@Getter
@Table("TASKS")
@With
public class Task {
    @Id
    private final UUID id;
    @Column("USER_ID")
    private final UUID userId;
    @JsonProperty("context_id")
    private final UUID contextId;
    private final String text;

    @JsonIgnore
    public UUID getUserId() {
        return this.userId;
    }
}
