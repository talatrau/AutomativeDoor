package com.example.automativedoor.EntityClass;

public class Account {
    private String email;
    private String pass;
    private String pin;

    public Account(String email, String pass, String pin) {
        this.email = email;
        this.pass = pass;
        this.pin = pin;
    }

    public boolean pinVerify(String pin) {
        return this.pin == pin;
    }

    public boolean passVerify(String pass) {
        return this.pass == pass;
    }
}
