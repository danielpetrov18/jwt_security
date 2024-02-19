package com.daniel.security_jwt.mapper;

public interface IMapper<A,B> {

    B mapTo(A a);

    A mapFrom(B b);

}