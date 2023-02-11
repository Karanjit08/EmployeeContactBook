package com.example.androidcrudapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class ViewEmployeeActivity extends AppCompatActivity {
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        Database database = new Database(this);
        List<ModelClass> list = database.viewEmployee();
        if(list.size()>0)
        {
            EmployeeAdapter employeeAdapter = new EmployeeAdapter(list,ViewEmployeeActivity.this);
            recyclerView.setAdapter(employeeAdapter);
        }
        else
        {
            Toast.makeText(this, "No data Available", Toast.LENGTH_SHORT).show();
        }

    }
}