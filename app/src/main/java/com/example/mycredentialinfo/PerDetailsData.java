package com.example.mycredentialinfo;

public class PerDetailsData {
    private PerData data;

    public PerDetailsData() {
    }

    public PerData getData() {
        return data;
    }

    public void setData(PerData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PersonalDetailsData{" +
                "data=" + data +
                '}';
    }
}
