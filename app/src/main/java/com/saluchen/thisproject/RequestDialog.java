package com.saluchen.thisproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RequestDialog extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    private EditText itemNameText;
    private EditText itemDetailsText;
    private EditText expectedDateText;
    SharedPreferences sharedPreferences;
    android.widget.Button btn_drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_dialog);

        sharedPreferences = getSharedPreferences(Config.sharedPrefs,
                Context.MODE_PRIVATE);

        itemNameText = findViewById(R.id.request_item_name);
        itemDetailsText = findViewById(R.id.request_item_details);
        expectedDateText = findViewById(R.id.request_expected_date);

        itemNameText.setText(sharedPreferences.getString(Config.itemName, ""));
        itemDetailsText.setText(sharedPreferences.getString(Config.itemDetail, ""));
        expectedDateText.setText(sharedPreferences.getString(Config.expectedDate, ""));

        expectedDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RequestDialog.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_drop = (Button) findViewById(R.id.drop_location_button);
        btn_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button", "Drop Pressed");
                String itemName = itemNameText.getText().toString();
                String itemDetails = itemDetailsText.getText().toString();
                String date = expectedDateText.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.itemName, itemName);
                editor.putString(Config.itemDetail, itemDetails);
                editor.putString(Config.expectedDate, date);
                editor.apply();

                String message = "hello ";
                Intent intent = new Intent(RequestDialog.this, HomeActivity.class);
                Bundle extras = new Bundle();
                extras.putString("itemName", itemName);
                extras.putString("itemDetails", itemDetails);
                extras.putString("expectedDate", date);
                for (String key : extras.keySet()) {
                    Log.d("Bundle Debug", key + " = \"" + extras.get(key) + "\"");
                }
                intent.putExtras(extras);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(2, intent);
                finish();
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

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hour);
            myCalendar.set(Calendar.MINUTE, minute);
        }
    };

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        expectedDateText.setText(sdf.format(myCalendar.getTime()));
    }

    /*    public void onRequestDropLocationButton(View view) {

            Log.d("Button","Drop Pressed");
            String itemName = itemNameText.getText().toString();
            String itemDetails = itemDetailsText.getText().toString();
            String date = expectedDateText.getText().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Config.itemName, itemName);
            editor.putString(Config.itemDetail, itemDetails);
            editor.putString(Config.expectedDate, date);
            editor.apply();


            String message="hello ";
            Intent intent=new Intent(RequestDialog.this,HomeActivity.class);
            intent.putExtra("MESSAGE",message);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setResult(2,intent);
            finish();
        }
    */
    @Override
    public void onBackPressed() {
        String message = "FromRequestDialog ";
        Log.d("Button", "Back Pressed");
        Intent intent = new Intent(RequestDialog.this, HomeActivity.class);
        intent.putExtra("MESSAGE", message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(1, intent);
        finish();
    }
}