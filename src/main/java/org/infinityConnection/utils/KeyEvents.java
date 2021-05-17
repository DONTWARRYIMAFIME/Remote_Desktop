package org.infinityConnection.utils;

public enum KeyEvents {
    UNKNOWN_EVENT(0),
    PRESS_MOUSE(1),
    RELEASE_MOUSE(2),
    RELEASE_KEY(3),
    MOVE_MOUSE(4);

    private final int eventID;

    KeyEvents(int eventID){
        this.eventID = eventID;
    }

    public int getEventID(){
        return eventID;
    }
}
