package com.infosupport.training.reactjs.gtdserver;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Context {
    private final long id;
    private final String name;
}
