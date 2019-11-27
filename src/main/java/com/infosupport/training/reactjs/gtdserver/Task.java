package com.infosupport.training.reactjs.gtdserver;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Task {
    private final long id;
    private final String text;
    private final long context_id;
}
