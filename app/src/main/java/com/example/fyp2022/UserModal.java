package com.example.fyp2022;

import java.io.Serializable;

public class UserModal implements Serializable {
    private final int userID;
    private final String userDate;
    private final String budgetType;
    private final double budgetSet;
    private final double budgetPercent;

    public UserModal(int userID, String userDate, String budgetType, double budgetSet, double budgetPercent) {
        this.userID = userID;
        this.userDate = userDate;
        this.budgetType = budgetType;
        this.budgetSet = budgetSet;
        this.budgetPercent = budgetPercent;
    }

    public int getUserID() {
        return userID;
    }
    public String getUserDate() {
        return userDate;
    }
    public String getBudgetType() {
        return budgetType;
    }
    public double getBudgetSet() {
        return budgetSet;
    }
    public double getBudgetPercent() {
        return budgetPercent;
    }

    @Override
    public String toString() {
        return "UserModal{" +
                "userID=" + userID +
                ", userDate='" + userDate + '\'' +
                ", budgetType='" + budgetType + '\'' +
                ", budgetSet=" + budgetSet +
                ", budgetPercent=" + budgetPercent +
                '}';
    }
}
