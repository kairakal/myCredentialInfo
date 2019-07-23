package com.example.mycredentialinfo;

public class ProfDetailsData {
    private ProfData data;

    public ProfData getData() {
        return data;
    }

    public void setData(ProfData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProfessionalDetailsData{" +
                "data=" + data +
                '}';
    }
}
