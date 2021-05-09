package com.example.automativedoor.Control;

// reading data interface
public interface IDatabaseDriver {
    public void writeJson();

    public String readJson();

    public void writeDatabase();

    public String readDatabase();
}
