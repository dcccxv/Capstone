package com.google.sample.cloudvision;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    boolean is_end = false;
    boolean output = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ((EditText) findViewById(R.id.et_lng)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                findViewById(R.id.btn_signup).performClick();
                return false;
            }
        });

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storeID = ((EditText) findViewById(R.id.et_store_id)).getText().toString();
                String pw = ((EditText) findViewById(R.id.et_pw)).getText().toString();
                String storeName = ((EditText) findViewById(R.id.et_store_name)).getText().toString();
                String seat = ((EditText) findViewById(R.id.et_numebr_of_seats)).getText().toString();
                String lat = ((EditText) findViewById(R.id.et_lat)).getText().toString();
                String lng = ((EditText) findViewById(R.id.et_lng)).getText().toString();

                if (storeID.equals("") || pw.equals("") || storeName.equals("") || seat.equals("") || lat.equals("") || lng.equals(""))
                    Toast.makeText(SignupActivity.this, "하나 이상의 빈칸이 존재합니다.", Toast.LENGTH_SHORT).show();
                else if ( Integer.parseInt(seat) < 1) //else if (seat.equals("0"))
                    Toast.makeText(SignupActivity.this, "좌석 수는 0이 될수 없습니다.", Toast.LENGTH_SHORT).show();
                else if (storeID.equals(".") || storeID.equals("#") || storeID.equals("$") || storeID.equals("[") || storeID.equals("]")
                        || pw.equals(".") || pw.equals("#") || pw.equals("$") || pw.equals("[") || pw.equals("]")
                        || storeName.equals(".") || storeName.equals("#") || storeName.equals("$") || storeName.equals("[") || storeName.equals("]")
                        || seat.equals(".") || seat.equals("#") || seat.equals("$") || seat.equals("[") || seat.equals("]")
                        || lat.equals(".") || lat.equals("#") || lat.equals("$") || lat.equals("[") || lat.equals("]")
                        || lng.equals(".") || lng.equals("#") || lng.equals("$") || lng.equals("[") || lng.equals("]")){
                    Toast.makeText(SignupActivity.this, ". # $ [ ] 는 사용할 수 없는 문자입니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations");

                    myRef.child(storeID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                HashMap<String, Object> result = new HashMap<>();
                                result.put("pw", pw);
                                result.put("name", storeName);
                                result.put("seat", Integer.parseInt(seat));
                                result.put("lat", Double.parseDouble(lat));
                                result.put("lng", Double.parseDouble(lng));
                                result.put("time", Integer.parseInt(seat));
                                is_end = true;
                                myRef.child(storeID).setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignupActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {
                                if (!is_end)
                                    Toast.makeText(SignupActivity.this, "존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
