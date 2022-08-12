package com.example.fyp2022;

public class BudgetFrictionModal {
    private final int budgetFrictionID;
    private final int budgetID;
    private final int frictionID;
    private final int budgetFrictionValidation;
    private final String budgetFrictionDate;

    public BudgetFrictionModal(int budgetFrictionID, int budgetID, int frictionID, int budgetFrictionValidation, String budgetFrictionDate) {
        this.budgetFrictionID = budgetFrictionID;
        this.budgetID = budgetID;
        this.frictionID = frictionID;
        this.budgetFrictionValidation = budgetFrictionValidation;
        this.budgetFrictionDate = budgetFrictionDate;
    }

    public String getBudgetFrictionDate() {
        return budgetFrictionDate;
    }
    public int getBudgetFrictionID() {
        return budgetFrictionID;
    }
    public int getBudgetID() {
        return budgetID;
    }
    public int getFrictionID() {
        return frictionID;
    }
    public int getBudgetFrictionValidation() {
        return budgetFrictionValidation;
    }

    @Override
    public String toString() {
        return "BudgetFrictionModal{" +
                "budgetFrictionID=" + budgetFrictionID +
                ", budgetID=" + budgetID +
                ", frictionID=" + frictionID +
                ", budgetFrictionValidation=" + budgetFrictionValidation +
                '}';
    }
}
