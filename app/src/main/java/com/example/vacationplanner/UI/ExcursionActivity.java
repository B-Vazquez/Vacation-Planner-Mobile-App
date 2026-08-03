package com.example.vacationplanner.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacationplanner.R;
import com.example.vacationplanner.utilities.AlarmUtil;
import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.utilities.ExcursionSaveUtil;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ExcursionActivity extends AppCompatActivity {
    String title;
    String date;
    int excursionID;
    int vacationID;
    EditText editTitle;
    EditText editDate;
    Repository repository;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar excursionCalender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTitle = findViewById(R.id.excursiontitle);
        editDate = findViewById(R.id.excursiondate);
        title = getIntent().getStringExtra("name");
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        date = getIntent().getStringExtra("date");
        editTitle.setText(title);
        editDate.setText(date);
        editDate.setEnabled(false);
        repository = new Repository(getApplication());

        Button saveButton = findViewById(R.id.excursionSaveButton);
        saveButton.setOnClickListener(view -> {
            String date = editDate.getText().toString();
            String title = editTitle.getText().toString();
            // Validate excursion date against parent vacation dates
            if (ExcursionSaveUtil.validateExcursionToSave(ExcursionActivity.this,
                    title, date, vacationID, repository)){
                ExcursionSaveUtil.saveExcursionToRepository(excursionID, vacationID,
                        title, date, repository);
                finish();
            }
        });

        Button deleteButton = findViewById(R.id.excursionDeleteButton);
        deleteButton.setOnClickListener(view -> {
            if(excursionID != -1){
                String title = editTitle.getText().toString();
                String date = editDate.getText().toString();
                Excursion excursion = new Excursion(excursionID, title, vacationID, date);
                repository.delete(excursion);
            }
            finish();
        });

        Button dateButton = findViewById(R.id.excursiondatebutton);
        dateButton.setOnClickListener(view -> {
            String info;
            String test = editDate.getText().toString();
            if(test.isEmpty()){
                info = sdf.format(new Date());
            } else info = test;
            try{
                excursionCalender.setTime(Objects.requireNonNull(sdf.parse(info)));
            } catch (ParseException e){
                Log.e("Excursion", "Error parsing date from date picker.");
            }
            new DatePickerDialog(ExcursionActivity.this, excursionDate, excursionCalender.get(Calendar.YEAR),
                    excursionCalender.get(Calendar.MONTH), excursionCalender.get(Calendar.DAY_OF_MONTH)).show();
        });
        excursionDate = (datePicker, year, month, day) -> {
            excursionCalender.set(Calendar.YEAR, year);
            excursionCalender.set(Calendar.MONTH, month);
            excursionCalender.set(Calendar.DAY_OF_MONTH, day);
            editDate.setText(sdf.format(excursionCalender.getTime()));
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.excursion_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.excursionNotify){
            String title = editTitle.getText().toString();
            String date = editDate.getText().toString();
            if(title.isEmpty() || date.isEmpty()){
                Toast.makeText(ExcursionActivity.this, "All fields must be filled to set an alert.", Toast.LENGTH_LONG).show();
                return true;
            }
            // Use AlarmUtil for standardized notification scheduling
            AlarmUtil.setAlarm(this, date, "Your excursion '" + title + "' is today.");
            return true;
        }
        return true;
    }
}