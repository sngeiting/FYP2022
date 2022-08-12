package com.example.fyp2022;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class summaryDetail extends Fragment {

    TextView tv_totalBudgetAmt, tv_Sum_budgetLeftAmt,tv_budgetUsedAmt, tv_highestSpent, tv_overshotAmt, tv_totalTransactionNum;
    String monthSelected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.fragment_summary_detail, container, false);

        // get data from bundle
        if(getArguments()!=null) {
            monthSelected = getArguments().getString("monthSelected");
        }

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        tv_totalBudgetAmt = view.findViewById(R.id.tv_totalBudgetAmt);
        tv_Sum_budgetLeftAmt = view.findViewById(R.id.tv_Sum_budgetLeftAmt);
        tv_budgetUsedAmt = view.findViewById(R.id.tv_budgetUsedAmt);
        tv_highestSpent = view.findViewById(R.id.tv_highestSpent);
        tv_overshotAmt = view.findViewById(R.id.tv_overshotAmt);
        tv_totalTransactionNum = view.findViewById(R.id.tv_totalTransactionNum);

        // set all to be 0 first
        tv_totalBudgetAmt.setText("0");
        tv_Sum_budgetLeftAmt.setText("0");
        tv_budgetUsedAmt.setText("0");
        tv_highestSpent.setText("0");
        tv_overshotAmt.setText("0");
        tv_totalTransactionNum.setText("0");

        int totalBudgetSet = (int) dbHelper.getBudgetSet(monthSelected);
        tv_totalBudgetAmt.setText(Integer.toString(totalBudgetSet));

        double totalBudgetUsed = dbHelper.getTotalSpendings(monthSelected);
        double budgetLeft = round(totalBudgetSet - totalBudgetUsed,2);
        double highestSpent = dbHelper.getHighestSpending(monthSelected);
        if (totalBudgetUsed == 0) {
            // remove .0
            tv_budgetUsedAmt.setText(Integer.toString((int)totalBudgetUsed));
            tv_Sum_budgetLeftAmt.setText(Integer.toString((int) budgetLeft));
            tv_highestSpent.setText(Integer.toString((int) highestSpent));
        } else {
            tv_budgetUsedAmt.setText(Double.toString(totalBudgetUsed));
            tv_Sum_budgetLeftAmt.setText(Double.toString(budgetLeft));
            tv_highestSpent.setText(Double.toString(highestSpent));
        }

        if(budgetLeft < 0) {
            tv_overshotAmt.setText(Double.toString(budgetLeft));
            tv_overshotAmt.setTextColor(getResources().getColor(R.color.red_error));
            tv_Sum_budgetLeftAmt.setTextColor(getResources().getColor(R.color.red_error));
        } else {
            tv_overshotAmt.setText("0");
        }

        int totalTransactions = dbHelper.getTotalTransactions(monthSelected);
        tv_totalTransactionNum.setText(Integer.toString(totalTransactions));

        return view;
    }
    // method obtained from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public double round(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}