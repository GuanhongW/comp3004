package com.comp3004groupx.smartaccount.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.comp3004groupx.smartaccount.R;
import com.comp3004groupx.smartaccount.module.DAO.PurchaseTypeDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuguanhong on 2017-10-31.
 */

public class Edit_Purchase_Type extends AppCompatActivity {

    PurchaseTypeDAO purchaseTypeDAO;
    Switch swith;
    Spinner purchaseSpinner;
    EditText newTypeName;
    Button deleteButton;
    Button createButton;
    int status = 0;//0 is expense type, 1 is income type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_purchase_type);

//        //This code is for refresh
//        Intent intent = getIntent();
//        status = intent.getIntExtra("key", 0);

        //Set switch status
        setSwitchStatus();

        //Spinner UI design
        spinnerUI();

        //Delete or Create
        deletePurchaseType();
        createPurchaseType();


    }

    private void spinnerUI(){
        purchaseSpinner = (Spinner) findViewById(R.id.purchaseSpinner);
        newTypeName = (EditText) findViewById(R.id.newTypeName);
        purchaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (purchaseSpinner.getSelectedItem().toString().equals("Create a new expense type") || purchaseSpinner.getSelectedItem().toString().equals("Create a new income type")){
                    newTypeName.setVisibility(View.VISIBLE);
                }
                else {
                    newTypeName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setExpenseSpinner() {
        purchaseSpinner = (Spinner) findViewById(R.id.purchaseSpinner);
        purchaseTypeDAO = new PurchaseTypeDAO(getApplicationContext());
        List<String> typeList = purchaseTypeDAO.getALLExpenseType();
        List<String> typeSpinnerList = new ArrayList<>();
        typeSpinnerList.add("Create a new expense type");
        for (int i = 0; i < typeList.size(); i++) {
            typeSpinnerList.add(typeList.get(i));
        }
        ArrayAdapter<String> typeDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeSpinnerList);
        typeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purchaseSpinner.setAdapter(typeDataAdapter);
    }

    private void setIncomeSpinner() {
        purchaseSpinner = (Spinner) findViewById(R.id.purchaseSpinner);
        purchaseTypeDAO = new PurchaseTypeDAO(getApplicationContext());
        List<String> typeList = purchaseTypeDAO.getAllIncomeType();
        List<String> typeSpinnerList = new ArrayList<>();
        typeSpinnerList.add("Create a new income type");
        for (int i = 0; i < typeList.size(); i++) {
            typeSpinnerList.add(typeList.get(i));
        }
        ArrayAdapter<String> typeDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeSpinnerList);
        typeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purchaseSpinner.setAdapter(typeDataAdapter);
    }

    private void setSwitchStatus() {
        swith = (Switch) findViewById(R.id.switch1);

        //Default is Expense Spinner
        setExpenseSpinner();

        if (status == 0) {
            setExpenseSpinner();
        } else {
            setIncomeSpinner();
        }

        swith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    setIncomeSpinner();
                    status = 1;
                } else {
                    setExpenseSpinner();
                    status = 0;
                }
            }
        });

    }


    private void createPurchaseType() {
        createButton = (Button) findViewById(R.id.createButton);
        newTypeName = (EditText) findViewById(R.id.newTypeName);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 0) {
                    purchaseTypeDAO.addPurchaseType(newTypeName.getText().toString());
                    toast("Success");
                    setExpenseSpinner();
                    newTypeName.setText("");
                } else {
                    purchaseTypeDAO.addIncomeType(newTypeName.getText().toString());
                    toast("Success");
                    setIncomeSpinner();
                    newTypeName.setText("");
                }
            }
        });
    }

    private void deletePurchaseType() {
        deleteButton = (Button) findViewById(R.id.editButton);
        purchaseSpinner = (Spinner) findViewById(R.id.purchaseSpinner);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteErrorChecking()) {
                    if (status == 0) {
                        purchaseTypeDAO.removeType(purchaseSpinner.getSelectedItem().toString());
                        toast("Success");
                        setExpenseSpinner();
                    } else {
                        purchaseTypeDAO.removeType(purchaseSpinner.getSelectedItem().toString());
                        toast("Success");
                        setIncomeSpinner();
                    }
                }
            }
        });

    }

    private boolean deleteErrorChecking() {
        boolean noError = true;
        purchaseSpinner = (Spinner) findViewById(R.id.purchaseSpinner);
        if (purchaseSpinner.getSelectedItem().toString().equals("Create a new expense type") || purchaseSpinner.getSelectedItem().toString().equals("Create a new income type")) {
            noError = false;
            toast("Please choose one type which you want to delete");
        }
        return noError;
    }

    private void toast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
