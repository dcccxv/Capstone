package com.google.sample.cloudvision;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {
    ListView listview ;
    ListviewAdapter adapter;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        myRef = FirebaseDatabase.getInstance().getReference().child("locations").child(getIntent().getStringExtra("id")).child("comment");

        adapter = new ListviewAdapter();
        listview = findViewById(R.id.comment_list);
        listview.setAdapter(adapter);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ListviewItem comment = postSnapshot.getValue(ListviewItem.class);
                    adapter.addItem(comment.getComment(), comment.getRating());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((EditText) findViewById(R.id.commnet)).getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "댓글을 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                }
                else{
                    HashMap<String, Object> comment = new HashMap<>();
                    comment.put("comment", ((EditText) findViewById(R.id.commnet)).getText().toString());
                    comment.put("rating", ((RatingBar) findViewById(R.id.rating)).getRating());
                    myRef.push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            adapter.addItem(((EditText) findViewById(R.id.commnet)).getText().toString(), ((RatingBar) findViewById(R.id.rating)).getRating());
                            adapter.notifyDataSetChanged();

                            ((EditText) findViewById(R.id.commnet)).setText("");
                            ((RatingBar) findViewById(R.id.rating)).setRating(0);
                        }
                    });
                }
            }
        });

    }

}
