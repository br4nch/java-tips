package com.tip.br4nch.function;

import javax.net.ssl.SSLHandshakeException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public final class ExponentialBackOff {
    private static final int[] FIBONACCI = new int[]{1, 1, 2, 3, 5, 8, 13};
    private static final List<Class<? extends Exception>> EXPECTED_COMMUNICATION_ERRORS = Arrays.asList(
            SSLHandshakeException.class, SocketTimeoutException.class);

    private ExponentialBackOff() {

    }

    public static <T> T execute(ExponentialBackOffFunction<T> fn) {
        for (int attempt = 0; attempt < FIBONACCI.length; attempt++) {
            try {
                return fn.execute();
            } catch (Exception e) {
                handleFailure(attempt, (RemoteException) e);
            }
        }
        throw new RuntimeException("Failed to communicate.");
    }

    private static void handleFailure(int attempt, RemoteException e) {
        if (e.getCause() != null && !EXPECTED_COMMUNICATION_ERRORS.contains(e.getCause().getClass()))
            throw new RuntimeException(e);
        doWait(attempt);
    }

    private static void doWait(int attempt) {
        try {
            Thread.sleep(FIBONACCI[attempt] * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
