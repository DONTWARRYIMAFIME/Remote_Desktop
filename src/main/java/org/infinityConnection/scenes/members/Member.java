package org.infinityConnection.scenes.members;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class Member extends RecursiveTreeObject<Member> {

    final StringProperty hostName;
    final StringProperty ip;
    final StringProperty connectionTime;
    final StringProperty sessionTime;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Timer timer = new Timer();
    private int seconds = 0;

    private TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                seconds++;

                int s = seconds % 60;
                int m = seconds / 60;
                int h = m / 60;

                sessionTime.setValue(h + " : " + m + " : " + s);
            }
        };
    }

    public Member(String hostName, String ip) {
        this.hostName = new SimpleStringProperty(hostName);
        this.ip = new SimpleStringProperty(ip);
        this.connectionTime = new SimpleStringProperty(LocalTime.now().format(dtf));
        this.sessionTime = new SimpleStringProperty("0 : 0 : 0");
        timer.scheduleAtFixedRate(getTask(), 1000, 1000);
    }

    public void stopTimer() {
        timer.cancel();
    }


}
