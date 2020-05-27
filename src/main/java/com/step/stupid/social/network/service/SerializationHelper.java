package com.step.stupid.social.network.service;

public interface SerializationHelper<T, R> {

    String serialize(T t);

    T deserialize(R r, Class<T> cls);
}
