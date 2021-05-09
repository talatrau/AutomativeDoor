package com.example.automativedoor.Control;

import java.time.LocalDateTime;

public class UserController implements ILogInOut, IDatabaseDriver {

    public void setSpeaker() {

    }

    public boolean turnOnSensor() {
        return true;
    }

    public boolean turnOffSensor() {
        return true;
    }

    public boolean openDoor() {
        return true;
    }

    public boolean closeDoor() {
        return true;
    }

    // must start a new thread in this method
    // to always tracking sensor and alarm
    public void trackingSensor() {

    }

    public void sendResponse() {

    }

    public void scheduleSensor(LocalDateTime time) {

    }

    // must start a new thread in this method
    // to always tracking real time to turn on or off sensor
    public void trackingTime() {

    }

    public void viewHistory() {

    }

    private void sendMail() {

    }


    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void writeJson() {

    }

    @Override
    public String readJson() {
        return null;
    }

    @Override
    public void writeDatabase() {

    }

    @Override
    public String readDatabase() {
        return null;
    }
}
