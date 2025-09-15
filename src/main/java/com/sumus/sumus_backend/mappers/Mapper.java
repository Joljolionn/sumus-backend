package com.sumus.sumus_backend.mappers;

public interface Mapper<A, B> {
    
    A mapFrom(B b);

    B mapTo(A a);

    void updateEntityFromDto(A a, B b);
    
}
