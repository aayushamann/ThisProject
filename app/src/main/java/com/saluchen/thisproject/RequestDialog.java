package com.saluchen.thisproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RequestDialog extends Activity {

    Calendar myCalendar = Calendar.getInstance();
    private EditText itemNameText;
    private EditText itemDetailsText;
    private EditText expectedDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_dialog);

        itemNameText = findViewById(R.id.request_item_name);
        itemDetailsText = findViewById(R.id.request_item_details);
        expectedDateText = findViewById(R.id.request_expected_date);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        expectedDateText.setText(sdf.format(myCalendar.getTime()));
    }

    public void onRequestDropLocationButton(View view) {
        String itemName = itemNameText.getText().toString();
        String itemDetails = itemDetailsText.getText().toString();
        String date = expectedDateText.getText().toString();
        String message="hello ";
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",message);
        setResult(2,intent);
        finish();//finishing activity
    }
}
