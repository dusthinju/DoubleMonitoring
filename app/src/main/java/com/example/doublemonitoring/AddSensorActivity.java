package com.example.doublemonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.doublemonitoring.model.SensorModel;
import com.example.doublemonitoring.model.TempModel;
import com.example.doublemonitoring.model.VoltModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AddSensorActivity extends AppCompatActivity {

    RadioGroup radioGroup;

    public String sensorType;
    EditText sensorAddress;

    EditText sensorName;

    Button addBtn;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        sensorAddress = (EditText) findViewById(R.id.addsensor_edit_address);
        sensorName = (EditText) findViewById(R.id.addSensor_edit_name);
        addBtn = (Button) findViewById(R.id.addSensorBtn);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radio_button_temp){
                    sensorType = "T";
                }

                if(checkedId == R.id.radio_button_volt){
                    sensorType = "V";
                }

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addOnClick(view);

            }
        });

    }

    public void addOnClick(View view){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            uid= user. getUid();

            SensorModel sensorModel = new SensorModel();
            sensorModel.sensorAddress = sensorAddress.getText().toString();
            sensorModel.sensorValue = 0;
            sensorModel.sensorName = sensorName.getText().toString();

            if(sensorType == "T"){ sensorModel.sensorType = "T";

                FirebaseDatabase.getInstance().getReference().child("users").child("test").child("temp").child(sensorAddress.getText().toString()).setValue(sensorModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddSensorActivity.this, "센서 추가 완료222", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            if(sensorType == "V"){
                sensorModel.sensorType = "V";
                FirebaseDatabase.getInstance().getReference().child("users").child("test").child("volt").child(sensorAddress.getText().toString()).setValue(sensorModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddSensorActivity.this, "센서 추가 완료222", Toast.LENGTH_SHORT).show();
                            }
                        });
            }




        }


    }
}