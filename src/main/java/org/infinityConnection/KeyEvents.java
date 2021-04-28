package org.infinityConnection;

public enum KeyEvents {
    UNKNOWN_EVENT(0),
    PRESS_MOUSE(1),
    RELEASE_MOUSE(2),
    PRESS_KEY(3),
    RELEASE_KEY(4),
    MOVE_MOUSE(5);

    private int eventID;

    KeyEvents(int eventID){
        this.eventID = eventID;
    }

    public int getEventID(){
        return eventID;
    }
}
