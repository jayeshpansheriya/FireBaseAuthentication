package com.example.vnurture_android.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataStoreActivity extends AppCompatActivity {
EditText mail,name;
Button submit;
ListView listView;

ArrayList<DataModel> list=new ArrayList<>();
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_store);

        databaseReference= FirebaseDatabase.getInstance().getReference("Data");

        mail=(EditText)findViewById(R.id.edt_mail);
        name=(EditText)findViewById(R.id.edt_name);
        submit=(Button)findViewById(R.id.btn_submit);

        listView=(ListView)findViewById(R.id.list);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreData();
            }
        });

    }

    private void StoreData() {

        String m=mail.getText().toString();
        String n=name.getText().toString();

        String id=databaseReference.push().getKey();

        DataModel model=new DataModel(id,n,m);

        databaseReference.child(id).setValue(model);

        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    DataModel model = postSnapshot.getValue(DataModel.class);
                    //adding artist to the list
                    list.add(model);

                }
                //creating adapter
                DataAdapter adapter = new DataAdapter(DataStoreActivity.this, list);
                //attaching adapter to the listview
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
