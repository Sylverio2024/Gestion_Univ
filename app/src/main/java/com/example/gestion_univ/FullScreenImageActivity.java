package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullImageView = findViewById(R.id.fullImageView);

        // Récupérer l'URL de l'image depuis l'intent
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Charger l'image en plein écran avec Glide
        Glide.with(this).load(imageUrl).into(fullImageView);
    }
}