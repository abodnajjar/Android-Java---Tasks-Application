package com.example.assigmentone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;


public class AddTask extends AppCompatActivity {
    private Button btnPickDate;
    private  Button btnSave;
    private TextView tvSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_task);

        btnPickDate = findViewById(R.id.btnPickDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPickDate.setOnClickListener(v -> openDatePicker());

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveTask());
    }
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth = selectedMonth + 1;
                    String date = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                    tvSelectedDate.setText(date);
                },
                year, month, day
        );

        dialog.show();
    }


    private void saveTask() {

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDescription = findViewById(R.id.etDescription);
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        CheckBox checkState = findViewById(R.id.checkState);
        RadioGroup rgPriority = findViewById(R.id.rgPriority);

        String title = etTitle.getText().toString();
        String desc = etDescription.getText().toString();
        String date = tvSelectedDate.getText().toString();
        boolean state = checkState.isChecked();

        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            etTitle.requestFocus();
            return;
        }

        if (desc.isEmpty()) {
            etDescription.setError("Description is required");
            etDescription.requestFocus();
            return;
        }

        if (date.isEmpty() || tvSelectedDate.getText().equals("(none)")) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        int checkedId = rgPriority.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "Please select a priority", Toast.LENGTH_SHORT).show();
            return;
        }

        String priority = "";
        if (checkedId == R.id.rbLow) priority = "Low";
        else if (checkedId == R.id.rbMedium) priority = "Medium";
        else if (checkedId == R.id.rbHigh) priority = "High";

        Task task = new Task(priority, date, desc, state, title);
        ArrayList<Task> tasks = Storage.loadTasks(this);

        tasks.add(task);
        Storage.saveTasks(this, tasks);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_saved", true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDescription = findViewById(R.id.etDescription);
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        RadioGroup rgPriority = findViewById(R.id.rgPriority);
        CheckBox checkState = findViewById(R.id.checkState);

        outState.putString("title", etTitle.getText().toString());
        outState.putString("desc", etDescription.getText().toString());
        outState.putString("date", tvSelectedDate.getText().toString());
        outState.putInt("priority", rgPriority.getCheckedRadioButtonId());
        outState.putBoolean("state", checkState.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDescription = findViewById(R.id.etDescription);
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        RadioGroup rgPriority = findViewById(R.id.rgPriority);
        CheckBox checkState = findViewById(R.id.checkState);

        etTitle.setText(savedInstanceState.getString("title"));
        etDescription.setText(savedInstanceState.getString("desc"));
        tvSelectedDate.setText(savedInstanceState.getString("date"));
        rgPriority.check(savedInstanceState.getInt("priority"));
        checkState.setChecked(savedInstanceState.getBoolean("state"));
    }
}


