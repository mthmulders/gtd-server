package com.infosupport.training.reactjs.gtdserver.contexts;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Context {
    private final long id;
    private final String name;
}
