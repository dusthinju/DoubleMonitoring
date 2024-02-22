package com.example.doublemonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText id;
    EditText password;
    Button adminBtn;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();;

        id = (EditText) findViewById(R.id.login_edit_id);
        password = (EditText) findViewById(R.id.login_edit_password);
        adminBtn = (Button) findViewById(R.id.adminBtn);

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminOnclick(view);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(LoginActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                }


            }
        };
    }

    void adminOnclick(View view){
        firebaseAuth.signInWithEmailAndPassword("abc@gmail.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override // 로그인이 정상적으로 됬는지 안됬는지 판단해주는 부분
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!(task.isSuccessful())) {
                    // 로그인 실패했을 경우
                    Toast.makeText(LoginActivity.this, "로그인 실패했어요ㅠㅠ", Toast.LENGTH_SHORT).show();
                }

                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, ModuleListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}