# EmployeeContactBook
An Android application that will be able to store employee information, such as their name, email, address, and phone number with unique IDs, as well as perform CRUD operations on them.
![screen1](https://user-images.githubusercontent.com/121970119/218272957-aa08ce3d-1a4b-4431-8245-9ef8ae15af66.JPG)
![screen2](https://user-images.githubusercontent.com/121970119/218272969-5d735aa1-465c-4370-813f-3512831732d4.JPG)
![screen3](https://user-images.githubusercontent.com/121970119/218272974-c8dee00d-ba44-4321-9c22-e888cbb63a0d.JPG)
![screen4](https://user-images.githubusercontent.com/121970119/218272985-ae290152-c1cb-4c9d-a203-696f9dd8aa80.JPG)
#1  MainActivity.java

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

#2 Database.java

package com.example.androidcrudapplication2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="EmployeeDetails";
    public static final String TABLE_NAME = "Employee";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String EMAIL="email";
    public static final String CONTACT="contact";

    private SQLiteDatabase sqLiteDatabase;

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT NOT NULL," + EMAIL + " TEXT NOT NULL," + CONTACT + " TEXT NOT NULL);";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    public void addEmployee(ModelClass modelClass)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.NAME, modelClass.getName());
        contentValues.put(Database.EMAIL, modelClass.getEmail());
        contentValues.put(Database.CONTACT, modelClass.getContact());
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(Database.TABLE_NAME, null, contentValues);
    }

    public List<ModelClass> viewEmployee()
    {
        String sql = "select * from " + TABLE_NAME;
        sqLiteDatabase = this.getReadableDatabase();
        List<ModelClass> storeEmployee = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String contact = cursor.getString(3);
                storeEmployee.add(new ModelClass(id,name,email,contact));

            }while(cursor.moveToNext());
        }

        cursor.close();
        return storeEmployee;

    }

    public void updateEmployee(ModelClass modelClass)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.NAME, modelClass.getName());
        contentValues.put(Database.EMAIL, modelClass.getEmail());
        contentValues.put(Database.CONTACT, modelClass.getContact());
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update(TABLE_NAME, contentValues, ID + " =?", new String[] {String.valueOf(modelClass.getId())});
    }

    public void deleteEmployee(int id)
    {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, ID + " =?", new String[] {String.valueOf(id)});
    }


}


#3 ModelClass.java

package com.example.androidcrudapplication2;

public class ModelClass {

    private Integer id;
    private String name;
    private String email;
    private String contact;

    public ModelClass(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    public ModelClass(Integer id, String name, String email, String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

#4 EmployeeAdapter.java

    package com.example.androidcrudapplication2;


    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{

        List<ModelClass> modelClassList;
        Context context;
        Database database;

        public EmployeeAdapter(List<ModelClass> modelClassList, Context context) {
            this.modelClassList = modelClassList;
            this.context = context;
            database = new Database(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ModelClass modelClass = modelClassList.get(position);
            holder.id.setText(Integer.toString(modelClass.getId()));
            holder.name.setText(modelClass.getName());
            holder.email.setText(modelClass.getEmail());
            holder.contact.setText(modelClass.getContact());

            holder.edit.setOnClickListener(v->{
                String editName = holder.name.getText().toString();
                String editEmail = holder.email.getText().toString();
                String editContact = holder.contact.getText().toString();

                if(editName.length()>0 || editEmail.length()>0 || editContact.length()>0)
                {
                    database.updateEmployee(new ModelClass(modelClass.getId(),editName,editEmail,editContact));
                    Toast.makeText(context, "Data updated Successfully", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(context, "Enter data", Toast.LENGTH_SHORT).show();
                }



            });


            holder.delete.setOnClickListener(v->{

                database.deleteEmployee(modelClass.getId());
                modelClassList.remove(position);
                notifyDataSetChanged();

            });

        }

        @Override
        public int getItemCount() {
            return modelClassList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView id;
            EditText name,email,contact;
            Button edit,delete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                id = itemView.findViewById(R.id.textView3);
                name = itemView.findViewById(R.id.editTextTextPersonName4);
                email = itemView.findViewById(R.id.editTextTextPersonName5);
                contact = itemView.findViewById(R.id.editTextTextPersonName6);
                edit = itemView.findViewById(R.id.edit);
                delete = itemView.findViewById(R.id.delete);

            }
        }
    }

#5 ViewEmployeeActivity.java

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

#6 activity_main.xml

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">


    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="196dp"
        android:text="Employee Contact Book"
        android:textColor="@color/black"
        android:textSize="35dp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.542"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/editTextTextPersonName"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="76dp"
        android:ems="10"
        android:hint="Enter your name"
        android:inputType="textPersonName"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView" />

    <EditText
        android:id="@+id/editTextTextPersonName2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="48dp"
        android:ems="10"
        android:hint="Enter your email"
        android:inputType="textPersonName"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editTextTextPersonName" />

    <EditText
        android:id="@+id/editTextTextPersonName3"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="48dp"
        android:ems="10"
        android:hint="Enter your contact number"
        android:inputType="number"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editTextTextPersonName2" />

    <LinearLayout
        android:layout_width="397dp"
        android:layout_height="wrap_content"
        android:layout_marginBottom="88dp"
        android:orientation="horizontal"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent">

        <Button
            android:id="@+id/button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="20dp"
            android:layout_marginRight="10dp"
            android:layout_weight="1"
            android:backgroundTint="@color/green"
            android:text="Add Employee"
            android:textColor="@color/black" />

        <Button
            android:id="@+id/button2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="10dp"
            android:layout_marginRight="25dp"
            android:layout_weight="1"
            android:backgroundTint="@color/yellow"
            android:text="View Employee"
            android:textColor="@color/black" />
    </LinearLayout>

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="211dp"
        android:layout_height="152dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/contact_book" />


</androidx.constraintlayout.widget.ConstraintLayout>

#7 activity_view_employee.xml

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ViewEmployeeActivity">


    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginStart="1dp"
        android:layout_marginTop="1dp"
        android:layout_marginEnd="1dp"
        android:layout_marginBottom="1dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>

#8 listitems.xml

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="400dp">


    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="28dp"
        android:text="Employee Details"
        android:textColor="@color/black"
        android:textSize="25dp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/textView3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="TextView"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView2" />

    <EditText
        android:id="@+id/editTextTextPersonName4"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:ems="10"
        android:inputType="textPersonName"
        android:text="Name"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView3" />

    <EditText
        android:id="@+id/editTextTextPersonName5"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:ems="10"
        android:inputType="textPersonName"
        android:text="Email"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editTextTextPersonName4" />

    <EditText
        android:id="@+id/editTextTextPersonName6"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:ems="10"
        android:inputType="number"
        android:text="Contact"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editTextTextPersonName5" />

    <Button
        android:id="@+id/edit"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="88dp"
        android:layout_marginBottom="28dp"
        android:text="Edit"
        android:backgroundTint="@color/purple"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

    <Button
        android:id="@+id/delete"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="96dp"
        android:layout_marginBottom="28dp"
        android:text="delete"
        android:backgroundTint="@color/red"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />


</androidx.constraintlayout.widget.ConstraintLayout>
