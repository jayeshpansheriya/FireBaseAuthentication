package com.example.vnurture_android.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity implements ImageListAdapter.onItemClickListener{
RecyclerView recyclerView;
ImageListAdapter adapter;
ProgressBar progressBar;
FirebaseStorage mStorage;
DatabaseReference databaseReference;
ValueEventListener mValueEventListener;
List<Upload> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        progressBar=(ProgressBar)findViewById(R.id.progress_circle);
        recyclerView=(RecyclerView)findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();

        adapter=new ImageListAdapter(ImageListActivity.this,list);
        adapter.setonItemClickListener(ImageListActivity.this);
        recyclerView.setAdapter(adapter);

        mStorage=FirebaseStorage.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("upload");
       mValueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Upload upload=snapshot.getValue(Upload.class);
                        upload.setmKey(snapshot.getKey());
                        list.add(upload);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImageListActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal Click: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever Click: "+position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "Delete Click: "+position, Toast.LENGTH_SHORT).show();
        Upload selectItem=list.get(position);
        final String selectkey=selectItem.getmKey();

        StorageReference imgRefe=mStorage.getReferenceFromUrl(selectItem.getmImageurl());
        imgRefe.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectkey).removeValue();
                Toast.makeText(ImageListActivity.this, "Image Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mValueEventListener);
    }
}
