package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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

public class Evenement extends AppCompatActivity {
    ImageButton buttonRetourEvent;
    FloatingActionButton fabEvent;
    RecyclerView recyclerViewEvent;
    List<DataClass4> dataList4,filteredDataList4;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter4 adapter4;
    SearchView searchViewEvent;
    AlertDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evenement);

        recyclerViewEvent = findViewById(R.id.recyclerViewEvent);
        buttonRetourEvent = findViewById(R.id.BackTeach);
        fabEvent = findViewById(R.id.fabEvent);
        searchViewEvent = findViewById(R.id.searchEvent);
        searchViewEvent.clearFocus();

        // Initialisation du SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout); // Assurez-vous que cet ID existe dans votre layout

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Evenement.this, 1);
        recyclerViewEvent.setLayoutManager(gridLayoutManager);



        // Setup loading dialog
        setupLoadingDialog();

        dataList4 = new ArrayList<>();
        filteredDataList4 = new ArrayList<>();
        adapter4 = new MyAdapter4(Evenement.this, filteredDataList4);
        recyclerViewEvent.setAdapter(adapter4);

        // Récupération des données depuis Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");

        // Load data initially
        loadData();

        // SwipeRefresh setup
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        // Fonctionnalité de recherche
        searchViewEvent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });

        // Action pour le bouton flottant
        fabEvent.setOnClickListener(v -> {
            Intent intent = new Intent(Evenement.this, uploadEvent.class);
            startActivity(intent);
        });

        // Action pour le bouton de retour
        buttonRetourEvent.setOnClickListener(v -> {
            Intent intent = new Intent(Evenement.this, fn5.class);
            startActivity(intent);
            finish();
        });
    }
    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Evenement.this);
        View dialogView = getLayoutInflater().inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialog = builder.create();

        Button buttonQuitter = dialogView.findViewById(R.id.btnQuitterDialog);
        buttonQuitter.setOnClickListener(v -> dialog.dismiss());
    }
    private void loadData() {
        dialog.show();  // Show the loading dialog when fetching data

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList4.clear();  // Clear the existing list before updating
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass4 dataClass4 = itemSnapshot.getValue(DataClass4.class);
                    if (dataClass4 != null) {
                        dataClass4.setKey4(itemSnapshot.getKey());
                        dataList4.add(dataClass4);
                    }
                }
                filteredDataList4.clear();  // Update the filtered list
                filteredDataList4.addAll(dataList4);

                adapter4.notifyDataSetChanged();  // Notify adapter about data change
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);  // Stop refreshing animation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fnEnseignant", "Error fetching data", error.toException());
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);  // Stop refreshing even in case of error
            }
        });
    }

    private void filterData(String query) {
        String lowerCaseQuery = query.toLowerCase();
        filteredDataList4.clear();
        for (DataClass4 dataClass4 : dataList4) {
            if (dataClass4.getNumeroEvent().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass4.getTitreEvent().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass4.getDateEvent().toLowerCase().contains(lowerCaseQuery)) {
                filteredDataList4.add(dataClass4);
            }
        }
        adapter4.notifyDataSetChanged();  // Update RecyclerView with filtered data
    }

    // Surcharger l'action du bouton retour
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, fn5.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}