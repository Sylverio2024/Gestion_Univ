package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Evenement1 extends AppCompatActivity {
    ImageButton buttonRetourEvent;
    FloatingActionButton fabEvent;
    RecyclerView recyclerViewEvent;
    List<DataClass4> dataList4;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter4 adapter4;
    SearchView searchViewEvent;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evenement1);

        recyclerViewEvent = findViewById(R.id.recyclerViewEvent);
        buttonRetourEvent = findViewById(R.id.BackTeach);
        fabEvent = findViewById(R.id.fabEvent);
        searchViewEvent = findViewById(R.id.searchEvent);
        searchViewEvent.clearFocus();

        // Initialisation du SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout); // Assurez-vous que cet ID existe dans votre layout

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Evenement1.this, 1);
        recyclerViewEvent.setLayoutManager(gridLayoutManager);

        // Configuration de la boîte de dialogue de chargement
        AlertDialog.Builder builder = new AlertDialog.Builder(Evenement1.this);
        View dialogView = getLayoutInflater().inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        // Récupérer la référence au bouton Quitter du Dialog
        Button buttonQuitter = dialogView.findViewById(R.id.btnQuitterDialog);
        buttonQuitter.setOnClickListener(v -> dialog.dismiss());

        // Initialisation des données et de l'adaptateur
        dataList4 = new ArrayList<>();
        adapter4 = new MyAdapter4(Evenement1.this, dataList4);
        recyclerViewEvent.setAdapter(adapter4);

        // Récupération des données depuis Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");
        loadData(dialog); // Appel à la méthode pour charger les données

        // Configurer le rafraîchissement par balayage
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadData(null); // Recharger les données lors du tirage vers le bas
        });

        // Fonctionnalité de recherche
        searchViewEvent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList4(newText);
                return true;
            }
        });

        // Action pour le bouton flottant
        fabEvent.setOnClickListener(v -> {
            Intent intent = new Intent(Evenement1.this, uploadEvent.class);
            startActivity(intent);
        });

        // Action pour le bouton de retour
        buttonRetourEvent.setOnClickListener(v -> {
            Intent intent = new Intent(Evenement1.this, MainEtudiant.class);
            startActivity(intent);
            finish();
        });
    }

    // Méthode pour charger les données depuis Firebase
    private void loadData(AlertDialog dialog) {
        if (dialog != null) {
            dialog.show(); // Afficher la boîte de dialogue
        }

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList4.clear(); // Effacer la liste avant de récupérer de nouvelles données
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass4 dataClass4 = itemSnapshot.getValue(DataClass4.class);
                    if (dataClass4 != null) {
                        dataClass4.setKey4(itemSnapshot.getKey());
                        dataList4.add(dataClass4);
                    }
                }
                adapter4.notifyDataSetChanged();
                if (dialog != null) {
                    dialog.dismiss(); // Masquer la boîte de dialogue
                }
                swipeRefreshLayout.setRefreshing(false); // Arrêter l'animation de rafraîchissement
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (dialog != null) {
                    dialog.dismiss(); // Masquer la boîte de dialogue en cas d'erreur
                }
                swipeRefreshLayout.setRefreshing(false); // Arrêter l'animation de rafraîchissement
            }
        });
    }

    // Surcharger l'action du bouton retour
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainEtudiant.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    // Logique de recherche pour filtrer les événements
    public void searchList4(String text) {
        ArrayList<DataClass4> searchList4 = new ArrayList<>();
        for (DataClass4 dataClass4 : dataList4) {
            if (dataClass4.getNumeroEvent().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass4.getTitreEvent().toLowerCase().contains(text.toLowerCase()) ||
                    dataClass4.getDateEvent().toLowerCase().contains(text.toLowerCase())) {
                searchList4.add(dataClass4);
            }
        }
        adapter4.searchDataList4(searchList4);
    }
}