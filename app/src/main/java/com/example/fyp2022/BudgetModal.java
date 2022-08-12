package com.example.fyp2022;

public class BudgetModal {
    private final int budgetID;
    private final double budgetUsed;
    private final String budgetDate;

    public BudgetModal(int budgetID, double budgetUsed, String budgetDate) {
        this.budgetID = budgetID;
        this.budgetUsed = budgetUsed;
        this.budgetDate = budgetDate;
    }

    public int getBudgetID() {
        return budgetID;
    }
    public double getBudgetUsed() {
        return budgetUsed;
    }
    public String getBudgetDate() {
        return budgetDate;
    }

    @Override
    public String toString() {
        return "BudgetModal{" +
                "budgetID=" + budgetID +
                ", budgetUsed=" + budgetUsed +
                ", budgetDate='" + budgetDate + '\'' +
                '}';
    }
}



