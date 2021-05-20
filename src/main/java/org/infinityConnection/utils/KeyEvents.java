package org.infinityConnection.utils;

public enum KeyEvents {
    UNKNOWN_EVENT(0),
    MOUSE_PRESS(1),
    MOUSE_RELEASED(2),
    KEY_RELEASED(3),
    MOUSE_MOVE(4);

    private final int eventID;

    KeyEvents(int eventID){
        this.eventID = eventID;
    }

    public int getEventID(){
        return eventID;
    }
}
