
package com.example.fyp2022;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GenOtpHome extends AppCompatActivity {
    TextView tv_budgetLeftAmt, tv_budgetLeft;
    EditText et_requestBudget;
    String month, currentDate;
    boolean success;
    double totalSpending, budgetSet, budgetLeft;
    int timesFrictionPointTriggered;
    Button btn_genOtp;
    DatabaseHelper dbHelper;

    ActivityResultLauncher activityResultLauncher, popUpActivityResultLauncher;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gen_otp_home);

        LocalDate localDate = LocalDate.now();

        //testing
//        LocalDate testDate = LocalDate.now().withMonth(10).withDayOfMonth(1);
//        localDate = testDate;

        currentDate = localDate.toString();

        int monthInNumeric = localDate.getMonthValue();
        if (monthInNumeric < 10) {
            month = "0" + monthInNumeric;
        } else {
            month = Integer.toString(monthInNumeric);
        }

        tv_budgetLeft = findViewById(R.id.tv_budgetLeft);
        tv_budgetLeftAmt = findViewById(R.id.tv_budgetLeftAmt);
        et_requestBudget = findViewById(R.id.et_requestBudget);

        dbHelper = new DatabaseHelper(GenOtpHome.this);
        String userDate = dbHelper.getLatestUserDate();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String otpDateTime = dateTimeFormat.format(new Date());

        budgetSet = dbHelper.getBudgetSet(month);
        totalSpending = dbHelper.getTotalSpendings(month);
        budgetLeft = round(budgetSet - totalSpending,2);
        if (totalSpending == 0) {
            tv_budgetLeftAmt.setText(Double.toString(budgetSet));
        } else {

            tv_budgetLeftAmt.setText(Double.toString(budgetLeft));
            if (budgetLeft < 0) {
                tv_budgetLeftAmt.setTextColor(getResources().getColor(R.color.red_error));
                tv_budgetLeft.setTextColor(getResources().getColor(R.color.red_error));
            }
        }
        success = false;
        btn_genOtp = findViewById(R.id.btn_genOtp);

        checkNumOfTimesUserDidFrictionPoints();

        et_requestBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // below code obtained is copied from https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
                String str = et_requestBudget.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 5, 2);

                if (!str2.equals(str)) {
                    et_requestBudget.setText(str2);
                    et_requestBudget.setSelection(str2.length());
                }
            }
        });
        btn_genOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String requestAmt = et_requestBudget.getText().toString();
                    if(!(Double.parseDouble(requestAmt) <= 0)) {
                        // starting of popup
                        // check if budget requested is more than  budget remaining.
                        totalSpending = dbHelper.getTotalSpendings(month);
                        budgetLeft = budgetSet - totalSpending;
                        if (Double.parseDouble(requestAmt) > budgetLeft) {
                            if (dbHelper.isBudgetFrictionDataCreated(month)) {
                                timesFrictionPointTriggered = dbHelper.countTimesFrictionPointsTriggered(month);
                            } else {
                                timesFrictionPointTriggered = 0;
                            }
                            // open popup class.
                            String message = "You can only overshot 3 times!";
                            String count = timesFrictionPointTriggered + "/3";
                            Intent popupIntent = new Intent(GenOtpHome.this, PopUp.class);
                            popupIntent.putExtra("message",message);
                            popupIntent.putExtra("overshotTimes",count);
                            popUpActivityResultLauncher.launch(popupIntent);

                        } else {
                            // create a budgetModal object
                            int budgetID = dbHelper.getNextBudgetID();
                            BudgetModal newSpending = new BudgetModal(budgetID, Double.parseDouble(requestAmt), currentDate);
                            dbHelper.addBudgetDetail(newSpending, 1, userDate);

                            // create a Otp object
                            int otpID = dbHelper.getNextOtpID();
                            OtpModal newOtp = new OtpModal(otpID, randomNumGen(), otpDateTime);
                            dbHelper.addOtpDetails(newOtp, newSpending.getBudgetID());

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction ft = manager.beginTransaction();
                            //check if fragment exists
                            Fragment fragmentA = manager.findFragmentById(R.id.layout_genOtp);
                            if (fragmentA == null) {
                                genOtp genOtp = new genOtp();
                                // call fragment_genOtp
                                ft.replace(R.id.layout_genOtp, genOtp);
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        }
                    } else {
                        Toast.makeText(GenOtpHome.this, "Amount cannot be 0",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(GenOtpHome.this,"Amount field cannot be empty.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            // get data from PopUp class
            popUpActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                try {
                                    // launch FrictionPopUp class upon confirmation.
                                    Intent intent = new Intent(GenOtpHome.this, FrictionPopUp.class);
                                    activityResultLauncher.launch(intent);
                                } catch (Exception e) {
                                    Toast.makeText(GenOtpHome.this, "Unable to retrieve data from PopUp.java",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } catch(Exception e) { }

        try {
            // get data from FrictionPopUp class
            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                boolean success = data.getBooleanExtra("success", false);
                                List<Integer> successID = data.getIntegerArrayListExtra("successID");
                                List<Integer> failureID = data.getIntegerArrayListExtra("failureID");
                                if (success) {
                                    // create a budgetModal object
                                    int budgetID = dbHelper.getNextBudgetID();
                                    BudgetModal newSpending = new BudgetModal(budgetID, Double.parseDouble(et_requestBudget.getText().toString()), currentDate);
                                    dbHelper.addBudgetDetail(newSpending, 1, userDate);

                                    // create a budgetFriction object
                                    for (int item : successID) {
                                        int budgetFrictionID = dbHelper.getNextBudgetFrictionID();
                                        BudgetFrictionModal successValidation = new BudgetFrictionModal(budgetFrictionID,newSpending.getBudgetID()
                                                ,item,1,currentDate);
                                        dbHelper.addBudgetFrictionDetail(successValidation);
                                    }
                                    for(int item: failureID) {
                                        int budgetFrictionID = dbHelper.getNextBudgetFrictionID();
                                        BudgetFrictionModal failureValidation = new BudgetFrictionModal(budgetFrictionID,newSpending.getBudgetID()
                                                ,item,0,currentDate);
                                        dbHelper.addBudgetFrictionDetail(failureValidation);
                                    }
                                    // create a Otp object
                                    int otpID = dbHelper.getNextOtpID();
                                    OtpModal newOtp = new OtpModal(otpID, randomNumGen(), otpDateTime);
                                    dbHelper.addOtpDetails(newOtp, newSpending.getBudgetID());
                                    FragmentManager manager = getSupportFragmentManager();
                                    FragmentTransaction ft = manager.beginTransaction();
                                    //check if fragment exists
                                    Fragment fragmentA = manager.findFragmentById(R.id.layout_genOtp);
                                    if (fragmentA == null) {
                                        genOtp genOtp = new genOtp();
                                        // call fragment_genOtp
                                        ft.replace(R.id.layout_genOtp, genOtp);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                    }
                                }
                            }
                        }
                    });
        } catch(Exception e) { }


        // check if budget is set. If not, open SetBudget.class
        boolean isBudgetSet = dbHelper.isBudgetSet(month);
        if(!isBudgetSet){
            startActivity(new Intent(GenOtpHome.this, SetBudget.class));
        }

        // Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_genOtp);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_budget:
                        startActivity(new Intent(GenOtpHome.this, Budget.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_genOtp:
                        return true;
                    case R.id.menu_summary:
                        startActivity(new Intent(GenOtpHome.this, Summary.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_awareness:
                        startActivity(new Intent(GenOtpHome.this, Awareness.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

    public String randomNumGen() {
        Random random = new Random();
        // generate 6 digits code from 000000 to 999999
        return String.format("%06d",random.nextInt(999999));
    }

    public void checkNumOfTimesUserDidFrictionPoints() {
        // set default to be 1
        if (dbHelper.isBudgetFrictionDataCreated(month)) {
            timesFrictionPointTriggered = dbHelper.countTimesFrictionPointsTriggered(month);
        } else {
            timesFrictionPointTriggered =0;
        }

        if (timesFrictionPointTriggered > 2) {
            btn_genOtp.setEnabled(false);
            et_requestBudget.setEnabled(false);
        } else {
            btn_genOtp.setEnabled(true);
            et_requestBudget.setEnabled(true);
        }
    }

    // below method is copied from https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
    }

    // method obtained from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public double round(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

