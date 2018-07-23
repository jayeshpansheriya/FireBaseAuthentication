package com.example.vnurture_android.firebasedemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUploadActivity extends AppCompatActivity {
Button selectBtn,uploadBtn;
TextView fileName,SeeAll;
ImageView imageView;
    Uri mImageUri;
    ProgressBar progressBar;

    StorageReference storageReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        selectBtn=(Button)findViewById(R.id.selectbtn);
        uploadBtn=(Button)findViewById(R.id.submitbtn);
        fileName=(TextView)findViewById(R.id.file_name);
        SeeAll=(TextView)findViewById(R.id.see_all);
        imageView=(ImageView)findViewById(R.id.imageView);
        progressBar=(ProgressBar)findViewById(R.id.progress);

        storageReference= FirebaseStorage.getInstance().getReference("upload");
        databaseReference= FirebaseDatabase.getInstance().getReference("upload");

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageUri!=null){
                    StorageReference fileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtensiotn(mImageUri));

                    fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },5000);
                            Toast.makeText(FileUploadActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            Upload upload=new Upload(fileName.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());

                            String uploadId=databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(upload);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FileUploadActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });

                }else {
                    Toast.makeText(FileUploadActivity.this, "Select File", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode==RESULT_OK)
        {
            mImageUri=data.getData();
            Picasso.with(FileUploadActivity.this).load(mImageUri).into(imageView);
        }


    }

    public String getFileExtensiotn(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }



}
