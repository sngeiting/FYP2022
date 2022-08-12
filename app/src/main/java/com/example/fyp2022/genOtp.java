package com.example.fyp2022;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.LocalDate;

public class genOtp extends Fragment {
    Button btn_genOtp;
    EditText et_requestBudget;
    TextView tv_otp, tv_budgetLeftAmt, tv_budgetLeft;
    DatabaseHelper dbHelper;
    double totalSpendings, budgetSet;
    String month;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.fragment_gen_otp
                                                                    , container, false);

        LocalDate localDate = LocalDate.now();
        //test only
//        LocalDate testDate = LocalDate.now().withMonth(10).withDayOfMonth(1);
//        localDate = testDate;

        int monthInNumeric = localDate.getMonthValue();
        if (monthInNumeric < 10) {
            month = "0" + monthInNumeric;
        } else {
            month = Integer.toString(monthInNumeric);
        }

        btn_genOtp = getActivity().findViewById(R.id.btn_genOtp);
        et_requestBudget = getActivity().findViewById(R.id.et_requestBudget);
        tv_otp = view.findViewById(R.id.tv_otp);
        tv_budgetLeftAmt = getActivity().findViewById(R.id.tv_budgetLeftAmt);
        tv_budgetLeft = getActivity().findViewById(R.id.tv_budgetLeft);

        // disable amount and gen OTP btn.
        btn_genOtp.setEnabled(false);
        et_requestBudget.setEnabled(false);

        tv_otp.setText("0");
        dbHelper = new DatabaseHelper(getActivity());
        tv_otp.setText(dbHelper.getOtpGenerated());

        totalSpendings = dbHelper.getTotalSpendings(month);
        budgetSet = dbHelper.getBudgetSet(month);

        ProgressBar pb_timer = (ProgressBar) view.findViewById(R.id.pb_timer);
        pb_timer.setProgress(0);
            CountDownTimer cdTimer = new CountDownTimer(10000, 10) {
                int i = 0;
                @Override
                public void onTick(long millisUntilFinished) {
                    i++;
                    pb_timer.setProgress((int) i * 100 / (10000 / 10));
                }
                @Override
                public void onFinish() {
                    // close fragment
                    getActivity().getSupportFragmentManager().popBackStack();
                    i++;
                    pb_timer.setProgress(100);

                    //update tv_budgetLeftAmt
                    double remainingBudgetLeft = ((GenOtpHome) getActivity()).round(budgetSet - totalSpendings,2);
                    tv_budgetLeftAmt.setText(Double.toString(remainingBudgetLeft));
                    if (remainingBudgetLeft < 0) {
                        tv_budgetLeftAmt.setTextColor(getResources().getColor(R.color.red_error));
                        tv_budgetLeft.setTextColor(getResources().getColor(R.color.red_error));
                    }

                    int timesFrictionPointTriggered = 0;
                    if (dbHelper.isBudgetFrictionDataCreated(month)) {
                        timesFrictionPointTriggered = dbHelper.countTimesFrictionPointsTriggered(month);
                    }
                    if (timesFrictionPointTriggered > 2) {
                        // if user triggered more than 3 times, dont let user generate OTP anymore.
                        btn_genOtp.setEnabled(false);
                        et_requestBudget.setEnabled(false);
                        et_requestBudget.setText("");
                    } else {
                        // enable genOtp btn and et_requestAmt
                        btn_genOtp.setEnabled(true);
                        et_requestBudget.setEnabled(true);
                        et_requestBudget.setText("");
                    }
                }
            };
            cdTimer.start();

        // Inflate the layout for this fragment
        return view;

    }
}