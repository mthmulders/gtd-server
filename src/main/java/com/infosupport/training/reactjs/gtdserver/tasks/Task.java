package com.infosupport.training.reactjs.gtdserver.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Task {
    private final long id;
    private final String text;
    @JsonProperty("context_id")
    private final long contextId;
}
