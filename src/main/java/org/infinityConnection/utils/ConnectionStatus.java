package org.infinityConnection.utils;

public enum ConnectionStatus {
    CONNECTING("Connecting to the server..."),
    UNKNOWN_HOST("Unknown host"),
    TIME_OUT("Timeout exceeded"),
    WRONG_PASSWORD("Wrong password"),
    DROPPED_CONNECTION("Connection dropped"),
    CONNECTED("Successfully connected!");

    private final String statusName;

    ConnectionStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
