package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class fen2 extends AppCompatActivity {
     private ImageView view;
     private Button btnEtudiant,btnEnseignant,btnScolarite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fen2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
         view= findViewById(R.id.back);
         view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent packageContext;
                 Intent intent = new Intent( fen2.this, MainActivity.class);
                 startActivity(intent);
                 finish();

             }
         });
    }
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            // Si c'est la première activité, retournez à l'écran d'accueil ou quittez l'application
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Si ce n'est pas la première activité, poursuivez avec le comportement par défaut du bouton de retour
            super.onBackPressed();
        }
    }
}