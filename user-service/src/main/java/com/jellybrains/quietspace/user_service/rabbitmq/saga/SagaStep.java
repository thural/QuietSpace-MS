package com.jellybrains.quietspace.user_service.rabbitmq.saga;

public interface SagaStep<T, U> {

    void process(T event);

    void rollback(U event);

}
