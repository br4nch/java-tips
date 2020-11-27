package com.tip.br4nch.function;

@FunctionalInterface
public interface ExponentialBackOffFunction<T> {
    T execute();
}
