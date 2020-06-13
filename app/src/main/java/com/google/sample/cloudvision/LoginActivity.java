package com.google.sample.cloudvision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        findViewById(R.id.btn_guest_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, GuestActivity.class));
                finish();
            }
        });

        ((EditText) findViewById(R.id.et_passwd)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                findViewById(R.id.btn_login).performClick();
                return false;
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = ((EditText)findViewById(R.id.et_id)).getText().toString();
                String pw = ((EditText)findViewById(R.id.et_passwd)).getText().toString();
                String approval = "0";

                if(id!=null && !id.equals("") && !id.equals(".") && !id.equals("#") && !id.equals("$") && !id.equals("[") && !id.equals("]")) {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations");
                    myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (pw != null && !pw.equals("")&& !pw.equals(".") && !pw.equals("#") && !pw.equals("$") && !pw.equals("[") && !pw.equals("]") && dataSnapshot.exists() && dataSnapshot.child("pw").getValue().equals(pw)) {
                                if (dataSnapshot.child("approval").getValue().equals(approval)) {
                                    Toast.makeText(LoginActivity.this, "관리자의 승인이 필요합니다", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn_signup_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }
}
