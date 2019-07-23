package com.example.mycredentialinfo;

public class EduDetailsData {
    private EduData data;

    public EduData getData() {
        return data;
    }

    public void setData(EduData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EducationDetailsData{" +
                "data=" + data +
                '}';
    }
}
