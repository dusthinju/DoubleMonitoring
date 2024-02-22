package com.example.doublemonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doublemonitoring.model.ModuleModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AddModuleActivity extends AppCompatActivity {

    EditText addModuleName;
    EditText addModuleId;
    Button addModuleBtn;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        addModuleName = findViewById(R.id.addModule_edit_Name);
        addModuleId = findViewById(R.id.addModule_edit_id);
        addModuleBtn = findViewById(R.id.addModuleBtn);

        addModuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ModuleModel moduleModel = new ModuleModel();
                moduleModel.moduleName = addModuleName.getText().toString();
                moduleModel.moduleId = addModuleId.getText().toString();
                moduleModel.V=0;
                moduleModel.V2=0;
                moduleModel.T2=25;
                moduleModel.T=26;

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                    // Send token to your backend via HTTPS
                    FirebaseDatabase.getInstance().getReference().child("users").child("labcas").child("module")
                            .child(addModuleName.getText().toString()).setValue(moduleModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(AddModuleActivity.this, "모듈 추가 완료", Toast.LENGTH_SHORT).show();
                                }


                            });

                }

            }
        });

    }
}