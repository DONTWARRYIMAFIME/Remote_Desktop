package org.infinityConnection;

public enum ConnectionStatus {
    UNKNOWN("Unknown status"),
    CONNECTING("Connecting to the server..."),
    UNKNOWN_HOST("Unknown host"),
    TIME_OUT("Timeout exceeded"),
    WRONG_PASSWORD("Wrong password"),
    CONNECTED("Successfully connected!");

    private String statusName;

    ConnectionStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
