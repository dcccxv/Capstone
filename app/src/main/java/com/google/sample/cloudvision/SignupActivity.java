package com.google.sample.cloudvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    boolean is_end = false;
    Bitmap img;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                // 선택한 이미지에서 비트맵 생성
                InputStream in = getContentResolver().openInputStream(data.getData());
                img = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ((EditText) findViewById(R.id.et_lng)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                findViewById(R.id.select_img).performClick();
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

                if (storeID.equals("") || pw.equals("") || storeName.equals("") || seat.equals("") || lat.equals("") || lng.equals("") || img == null) {
                    Toast.makeText(SignupActivity.this, "하나 이상의 빈칸이 존재합니다.", Toast.LENGTH_SHORT).show();
                } else if (seat.equals("0")) {
                    Toast.makeText(SignupActivity.this, "좌석 수는 0이 될수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations");
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference ref = mStorageRef.child(storeID+".jpg");

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();



                myRef.child(storeID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            UploadTask uploadTask = ref.putBytes(data);
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "사진 저장 성공", Toast.LENGTH_SHORT).show();
                                            HashMap<String, Object> result = new HashMap<>();
                                            result.put("pw", pw);
                                            result.put("name", storeName);
                                            result.put("seat", Integer.parseInt(seat));
                                            result.put("lat", Double.parseDouble(lat));
                                            result.put("lng", Double.parseDouble(lng));
                                            result.put("time", Integer.parseInt(seat));
                                            result.put("approval", "0");
                                            result.put("shopimgUrl", downloadUri.toString());
                                            is_end = true;
                                            myRef.child(storeID).setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(SignupActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    } else {
                                    }
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

    findViewById(R.id.select_img).

    setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);
        }
    });
}
}
