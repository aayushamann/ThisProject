package com.saluchen.thisproject;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RequestDialog extends AppCompatActivity {

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


        expectedDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RequestDialog.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
        Log.d("Button","Drop Pressed");
        String itemName = itemNameText.getText().toString();
        String itemDetails = itemDetailsText.getText().toString();
        String date = expectedDateText.getText().toString();
        String message="hello ";
        Intent intent=new Intent(RequestDialog.this,HomeActivity.class);
        intent.putExtra("MESSAGE",message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(2,intent);
    }
}