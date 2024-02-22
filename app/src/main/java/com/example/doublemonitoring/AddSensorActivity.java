package com.example.doublemonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.doublemonitoring.model.ModuleModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSensorActivity extends AppCompatActivity {

    EditText moduleType;
    EditText moduleAddress;
    Button addBtn;
    List<ModuleModel> moduleModels;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        moduleType = (EditText) findViewById(R.id.addsensor_edit_type);
        moduleAddress = (EditText) findViewById(R.id.addsensor_edit_address);
        addBtn = (Button) findViewById(R.id.addSensorBtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();
        moduleModels = new ArrayList<>();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOnclick(view);
            }
        });

    }

    public void addOnclick(View view){



    }
}