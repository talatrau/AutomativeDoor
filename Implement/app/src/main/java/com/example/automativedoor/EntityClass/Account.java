package com.example.automativedoor.EntityClass;

import java.util.List;

public class Account {
    private String email;
    private String pass;
    private String pin;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean pinVerify(String pin) {
        return this.pin == pin;
    }

    public boolean passVerify(String pass) {
        return this.pass == pass;
    }
}
