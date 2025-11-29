package com.example.assigmentone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TaskDetails extends AppCompatActivity {
    private  TextView etTitle, etDescription,tvSelectedDate,checkText;
    private RadioGroup rgPriority;
    private RadioButton rbLow,rbMedium,rbHigh;
    private CheckBox checkState;
    private Button btndelete,btnedit;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.show_task_details);

        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        tvSelectedDate=findViewById(R.id.tvSelectedDate);
        rgPriority=findViewById(R.id.rgPriority);
        checkState=findViewById(R.id.checkState);
        rbLow=findViewById(R.id.rbLow);
        rbMedium=findViewById(R.id.rbMedium);
        rbHigh=findViewById(R.id.rbHigh);
        checkText=findViewById(R.id.checkText);
        btndelete=findViewById(R.id.btndelete);
        btnedit=findViewById(R.id.btnedit);

        Task task = (Task) getIntent().getSerializableExtra("task");
        position = getIntent().getIntExtra("position", -1);
        etTitle.setEnabled(false);
        etDescription.setEnabled(false);
        rgPriority.setEnabled(false);
        checkState.setEnabled(false);
        checkState.setClickable(false);

        rbLow.setEnabled(false);
        rbMedium.setEnabled(false);
        rbHigh.setEnabled(false);

        etTitle.setText(task.getTitle());
        etDescription.setText(task.getDescription());
        tvSelectedDate.setText(task.getDate());
        String priority=task.getPriority();
        if ( priority!= null) {
            switch (priority) {
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
        }
        boolean state=task.isState();
        checkState.setChecked(state);
        if (state) {
            checkText.setText("Done");
        } else {
            checkText.setText("Not Done");
        }

        btndelete.setOnClickListener(v-> {
            showDeleteConfirmation();
        });
        btnedit.setOnClickListener(v -> {
            Intent intent = new Intent(TaskDetails.this, EditTask.class);
            intent.putExtra("task", task);
            intent.putExtra("position", position);
            startActivityForResult(intent, 300);
        });
    }
    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("delete", true);
                    resultIntent.putExtra("position", position);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300 && resultCode == RESULT_OK) {
            Task updatedTask = (Task) data.getSerializableExtra("updatedTask");

            etTitle.setText(updatedTask.getTitle());
            etDescription.setText(updatedTask.getDescription());
            tvSelectedDate.setText(updatedTask.getDate());
            checkState.setChecked(updatedTask.isState());

            if (updatedTask.isState()) {
                checkText.setText("Done");
            } else {
                checkText.setText("Not Done");
            }

            switch (updatedTask.getPriority()) {
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
            Intent resultIntent = new Intent();
            resultIntent.putExtra("update", true);
            resultIntent.putExtra("updatedTask", updatedTask);
            resultIntent.putExtra("position", position);

            setResult(RESULT_OK, resultIntent);
        }
    }

}