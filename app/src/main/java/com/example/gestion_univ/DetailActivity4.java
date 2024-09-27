package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

public class DetailActivity4 extends AppCompatActivity {

    private TextView detailNumero, detailTitre, detailDate, detailTime, detailDescription;
    private RecyclerView imageRecyclerView;
    private List<String> imageUrls;
    private Adapter1 Adapter1;
    FloatingActionButton deleteButton, editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail4);

        // Initialisation des vues
        detailNumero = findViewById(R.id.detailNumero);
        detailTitre = findViewById(R.id.detailTitre);
        detailDate = findViewById(R.id.detailDate);
        detailTime = findViewById(R.id.detailTime);
        detailDescription = findViewById(R.id.detailDescription);
        imageRecyclerView = findViewById(R.id.imageRecyclerView);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        // Récupération des données transmises par l'intent
        String numeroEvent = getIntent().getStringExtra("NumeroIDEvent");
        String titreEvent = getIntent().getStringExtra("titreEvent");
        String dateEvent = getIntent().getStringExtra("dateEvent");
        String timeEvent = getIntent().getStringExtra("heureEvent");
        String descriptionEvent = getIntent().getStringExtra("descriptionEvent");
        imageUrls = getIntent().getStringArrayListExtra("imagesEvent");

        // Mise à jour des TextViews avec les données reçues
        detailNumero.setText(numeroEvent);
        detailTitre.setText(titreEvent);
        detailDate.setText(dateEvent);
        detailTime.setText(timeEvent);
        detailDescription.setText(descriptionEvent);
        // Configuration du RecyclerView pour les images avec défilement vertical
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);
        // Adapter avec listener pour l'affichage plein écran
        Adapter1 = new Adapter1(this, imageUrls, new Adapter1.OnItemClickListener() {
            @Override
            public void onItemClick(String imageUrl) {
                // Ouvre une nouvelle activité pour afficher l'image en plein écran
                Intent intent = new Intent(DetailActivity4.this, FullScreenImageActivity.class);
                intent.putExtra("imageUrl", imageUrl);
                startActivity(intent);
            }
        });
        imageRecyclerView.setAdapter(Adapter1);
    }
}