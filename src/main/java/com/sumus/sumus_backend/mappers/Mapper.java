package com.sumus.sumus_backend.mappers;

import java.io.IOException;

public interface Mapper<A, B> {
    
    A mapFrom(B b) throws IOException;

    B mapTo(A a);

    void updateEntityFromDto(A a, B b);
    
}
