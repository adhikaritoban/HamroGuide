package com.example.sajak.hamroguide.Emergency;

public class EmergencyGetSet {

    private int number;
    private String name;

    public EmergencyGetSet(int number, String name) {
        this.setNumber(number);
        this.setName(name);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
