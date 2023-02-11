package com.example.androidcrudapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText name,email,contact;
    private TextView title;
    private Button add,view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextPersonName2);
        contact = findViewById(R.id.editTextTextPersonName3);
        add = findViewById(R.id.button);
        view = findViewById(R.id.button2);


        add.setOnClickListener(v->{
            String data1 = name.getText().toString();
            String data2 = email.getText().toString();
            String data3 = contact.getText().toString();

            if(data1.equals("") || data2.equals("") || data3.equals(""))
            {
                Toast.makeText(this, "Enter full details", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Database database = new Database(this);
                ModelClass modelClass = new ModelClass(data1,data2,data3);
                database.addEmployee(modelClass);
                Toast.makeText(this, "Details added Successfully", Toast.LENGTH_SHORT).show();
            }
        });


        view.setOnClickListener(v->{
            Intent intent =new Intent(MainActivity.this,ViewEmployeeActivity.class);
            startActivity(intent);
        });
    }
}