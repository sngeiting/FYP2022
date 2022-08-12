package com.example.fyp2022;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class frictionText extends Fragment {

    TextView tv_question, tv_hint;
    EditText et_input;
    List<FrictionModal> frictionList;
    DatabaseHelper dbHelper;
    Button btn_submit;
    boolean success;
    int successIDs, failureIDs;

    // create an instance of PassDataInterface and assigning value via a Constructor
    PassDataInterface passDataInterface;

    public frictionText(PassDataInterface passDataInterface) {
        this.passDataInterface = passDataInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.fragment_friction_text, container, false);

        tv_question = view.findViewById(R.id.tv_question);
        tv_hint = view.findViewById(R.id.tv_hint);
        et_input = view.findViewById(R.id.et_input);
        btn_submit = view.findViewById(R.id.btn_submit);

        successIDs = 0;
        failureIDs = 0;

        Random random = new Random();
        dbHelper = new DatabaseHelper(getActivity());
        // get list of frictions with type = text.
        String frictionTypeToGet = "Text";
        frictionList = dbHelper.getFrictionDetailsWithSpecificType(frictionTypeToGet);

        // generate a random int from 0 to list size.
        int num = random.nextInt(frictionList.size());
        int frictionID = frictionList.get(num).getFrictionID();
        String questionSet = frictionList.get(num).getFrictionDesc();
        String questionValidation = frictionList.get(num).getFrictionValidation();
        tv_question.setText(questionSet);
        tv_hint.setText("Type: ' "+questionValidation+" '");
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_input.getText().toString().equals(questionValidation)) {
                    // either close fragment or move to next qn depending on how many times it overshot
                    et_input.getText().clear();
                    successIDs = frictionID;
                    success = true;
                } else {
                    // show message is wrong, and move on to next qn.
                    Toast.makeText(getActivity(), "You did not type correctly!", Toast.LENGTH_SHORT).show();
                    et_input.getText().clear();
                    failureIDs = frictionID;
                    success = false;
                }
                passDataInterface.onDataReceived(success, successIDs, failureIDs);
            }
        });
        return view;
    }
}