package com.example.fyp2022;

public class FrictionModal {
    int frictionID;
    String frictionType;
    String frictionDesc;
    String frictionValidation;

    public FrictionModal(int frictionID, String frictionType, String frictionDesc, String frictionValidation) {
        this.frictionID = frictionID;
        this.frictionType = frictionType;
        this.frictionDesc = frictionDesc;
        this.frictionValidation = frictionValidation;
    }

    public int getFrictionID() {
        return frictionID;
    }
    public String getFrictionType() {
        return frictionType;
    }
    public String getFrictionDesc() {
        return frictionDesc;
    }
    public String getFrictionValidation() {
        return frictionValidation;
    }

    @Override
    public String toString() {
        return "FrictionModal{" +
                "frictionID=" + frictionID +
                ", frictionType='" + frictionType + '\'' +
                ", frictionDesc='" + frictionDesc + '\'' +
                ", frictionValidation='" + frictionValidation + '\'' +
                '}';
    }
}

