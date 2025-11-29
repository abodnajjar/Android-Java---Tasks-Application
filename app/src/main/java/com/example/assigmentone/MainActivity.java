package com.example.assigmentone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button AddButton,sortbtn;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;
    private EditText searchBar;
    private static final int REQUEST_ADD_TASK = 100;
    private static final int REQUEST_VIEW_TASK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_bage_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycleview);
        sortbtn=findViewById(R.id.sortbtn);
        sortbtn.setOnClickListener(v -> showSortDialog());

        taskList = Storage.loadTasks(this);
        taskAdapter = new TaskAdapter(this, taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        AddButton = findViewById(R.id.addButton);
        searchBar=findViewById(R.id.searchBar);

        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        AddButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTask.class);
            startActivityForResult(intent, REQUEST_ADD_TASK);
        });
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_TASK && resultCode == RESULT_OK) {
            taskList = Storage.loadTasks(this);
            taskAdapter.updateList(taskList);
        }

        if (requestCode == REQUEST_VIEW_TASK && resultCode == RESULT_OK) {

            boolean update = data.getBooleanExtra("update", false);
            boolean delete = data.getBooleanExtra("delete", false);

            int pos = data.getIntExtra("position", -1);
            if (delete && pos != -1) {
                taskList.remove(pos);
                taskAdapter.notifyItemRemoved(pos);
                Storage.saveTasks(this, taskList);
            }

            if (update && pos != -1) {
                Task updated = (Task) data.getSerializableExtra("updatedTask");

                taskList.set(pos, updated);
                taskAdapter.notifyItemChanged(pos);
                Storage.saveTasks(this, taskList);
            }
        }
    }
    private void showSortDialog() {
        String[] options = {
                "Priority (High → Low)",
                "Priority (Low → High)",
                "Date (Newest First)",
                "Date (Oldest First)",
                "Title (A → Z)",
                "Title (Z → A)"
        };

        new AlertDialog.Builder(this)
                .setTitle("Sort by")
                .setItems(options, (dialog, which) -> {

                    switch (which) {
                        case 0:
                            sortByPriorityHighLow();
                            break;
                        case 1:
                            sortByPriorityLowHigh();
                            break;
                        case 2:
                            sortByDateNewest();
                            break;
                        case 3:
                            sortByDateOldest();
                            break;
                        case 4:
                            sortByTitleAZ();
                            break;
                        case 5:
                            sortByTitleZA();
                            break;
                    }
                })
                .show();
    }
    private void sortByTitleAZ() {
        Collections.sort(taskList, (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        taskAdapter.updateList(taskList);
    }
    private void sortByTitleZA() {
        Collections.sort(taskList, (a, b) -> b.getTitle().compareToIgnoreCase(a.getTitle()));
        taskAdapter.updateList(taskList);
    }
    private void sortByPriorityHighLow() {
        Collections.sort(taskList, (a, b) -> b.getPriorityValue() - a.getPriorityValue());
        taskAdapter.updateList(taskList);
    }
    private void sortByPriorityLowHigh() {
        Collections.sort(taskList, (a, b) -> a.getPriorityValue() - b.getPriorityValue());
        taskAdapter.updateList(taskList);
    }
    private void sortByDateNewest() {
        Collections.sort(taskList, (a, b) ->
                parseDate(b.getDate()).compareTo(parseDate(a.getDate()))
        );
        taskAdapter.updateList(taskList);
    }
    private void sortByDateOldest() {
        Collections.sort(taskList, (a, b) ->
                parseDate(a.getDate()).compareTo(parseDate(b.getDate()))
        );
        taskAdapter.updateList(taskList);
    }
    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString);
        } catch (Exception e) {
            return new Date(0);
        }
    }
}