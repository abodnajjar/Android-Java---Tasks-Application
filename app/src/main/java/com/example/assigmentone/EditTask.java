package com.example.assigmentone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class EditTask extends AppCompatActivity {
    private Task task;
    private int position;
    private EditText etTitle,etDescription;
    private TextView tvSelectedDate;
    private RadioGroup rgPriority;
    private CheckBox checkState;
    private Button btnedit,btnPickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.edit_task);

        task = (Task) getIntent().getSerializableExtra("task");
        position = getIntent().getIntExtra("position", -1);

         etTitle = findViewById(R.id.etTitle);
         etDescription = findViewById(R.id.etDescription);
         tvSelectedDate = findViewById(R.id.tvSelectedDate);
         rgPriority = findViewById(R.id.rgPriority);
         checkState = findViewById(R.id.checkState);
         btnedit=findViewById(R.id.btnedit);
        btnPickDate=findViewById(R.id.btnPickDate);

        etTitle.setText(task.getTitle());
        etDescription.setText(task.getDescription());
        tvSelectedDate.setText(task.getDate());
        checkState.setChecked(task.isState());

        switch (task.getPriority()) {
            case "Low":
                rgPriority.check(R.id.rbLow);
                break;
            case "Medium":
                rgPriority.check(R.id.rbMedium);
                break;
            case "High":
                rgPriority.check(R.id.rbHigh);
                break;
        }
        btnPickDate.setOnClickListener(v -> {
            showDatePicker();
        });

        btnedit.setOnClickListener(v -> {

            String newTitle = etTitle.getText().toString();
            String newDesc = etDescription.getText().toString();
            String newDate = tvSelectedDate.getText().toString();
            boolean newState = checkState.isChecked();

            String newPriority = "";
            int checked = rgPriority.getCheckedRadioButtonId();
            if (checked == R.id.rbLow) newPriority = "Low";
            else if (checked == R.id.rbMedium) newPriority = "Medium";
            else if (checked == R.id.rbHigh) newPriority = "High";

            Task updatedTask = new Task(newPriority, newDate, newDesc, newState, newTitle);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedTask", updatedTask);
            resultIntent.putExtra("position", position);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    tvSelectedDate.setText(date);
                    tvSelectedDate.setTextColor(Color.BLACK);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}