package com.example.gestion_univ;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileOutputStream;

public class editPDP1 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseStorage storage;
    String userId1;
    ImageView imageLRG1,imgShare1,imgDownload1;
    ImageButton btnRetourPDP1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_pdp1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.blue));
        }
        btnRetourPDP1=findViewById(R.id.BackTeach);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user=mAuth.getCurrentUser();
        userId1=mAuth.getCurrentUser().getUid();
        imageLRG1=findViewById(R.id.imageLRG1);
        imgDownload1=findViewById(R.id.imgDownload1);
        imgShare1=findViewById(R.id.imgShare1);
        showProfileImageLarge1();

        ActivityCompat.requestPermissions(editPDP1.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(editPDP1.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(editPDP1.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PackageManager.PERMISSION_GRANTED);
        imgDownload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery1();
            }
        });
        imgShare1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bit= (BitmapDrawable)imageLRG1.getDrawable();
                Bitmap map=bit.getBitmap();
                String path= MediaStore.Images.Media.insertImage(editPDP1.this.getContentResolver(), map,"Share Image",null);
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                startActivity(Intent.createChooser(i,"Share Image ..."));
            }
        });
        btnRetourPDP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent( editPDP1.this, ProfileUser.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void showProfileImageLarge1(){

        if(user !=null){
            String userId=user.getUid();
            db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document=task.getResult();
                        if(document.exists()){
                            String imageUrl=document.getString("imageUrl");
                            if(imageUrl != null && !imageUrl.isEmpty()){
                                Glide.with(editPDP1.this).load(imageUrl).into(imageLRG1);

                            }
                        }
                    }else{
                        Log.d("ProfileAdmin", "get failed with ", task.getException());
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            // Si c'est la première activité, retournez à l'écran d'accueil ou quittez l'application
            Intent intent = new Intent(this, ProfileUser.class);
            startActivity(intent);
            finish();
        } else {
            // Si ce n'est pas la première activité, poursuivez avec le comportement par défaut du bouton de retour
            super.onBackPressed();
        }
    }
    private void saveToGallery1(){
        BitmapDrawable bit= (BitmapDrawable)imageLRG1.getDrawable();
        Bitmap map=bit.getBitmap();
        FileOutputStream out=null;
        File file= Environment.getExternalStorageDirectory();
        File dir= new File(file.getAbsolutePath()+"/MyPics");
        dir.mkdirs();
        String filename= String.format("%d.png",System.currentTimeMillis());
        File outFile=new File(dir,filename);
        try {
            out=new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        map.compress(Bitmap.CompressFormat.PNG,100,out);
        try {
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            out.close();
            Toast.makeText(getApplicationContext(), "Photo enregistré!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}