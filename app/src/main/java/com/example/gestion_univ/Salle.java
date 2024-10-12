package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Salle extends AppCompatActivity {

    ImageButton buttonRetourE;
    FloatingActionButton fabSalle;
    RecyclerView recyclerView3;
    List<DataClass3> dataList3,filteredDataList3;
    DatabaseReference databaseReference;
    ValueEventListener eventListener3;
    MyAdapter3 adapter3;
    SearchView searchView3;
    AlertDialog dialog;
    SwipeRefreshLayout swipeRefreshLayoutSalle; // SwipeRefreshLayout

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_salle);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonRetourE = findViewById(R.id.BackTeach);
        fabSalle = findViewById(R.id.fabSalle);
        recyclerView3 = findViewById(R.id.recyclerViewSalle);
        searchView3 = findViewById(R.id.searchSalle);
        swipeRefreshLayoutSalle = findViewById(R.id.swipeRefreshLayoutSalle); // Initialisation du SwipeRefreshLayout
        searchView3.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Salle.this, 1);
        recyclerView3.setLayoutManager(gridLayoutManager);

        // Setup loading dialog
        setupLoadingDialog();

        dataList3 = new ArrayList<>();
        filteredDataList3 = new ArrayList<>();
        adapter3 = new MyAdapter3(Salle.this, filteredDataList3);
        recyclerView3.setAdapter(adapter3);

        databaseReference = FirebaseDatabase.getInstance().getReference("Salle");

        // Load data initially
        loadData();

        // SwipeRefresh setup
        swipeRefreshLayoutSalle.setOnRefreshListener(this::loadData);

        // Configuration du bouton de retour
        buttonRetourE.setOnClickListener(v -> {
            Intent intent = new Intent(Salle.this, fn5.class);
            startActivity(intent);
            finish();
        });

        // Bouton flottant pour ajouter une salle
        fabSalle.setOnClickListener(v -> {
            Intent intent = new Intent(Salle.this, UploadSalle.class);
            startActivity(intent);
        });

        // Écouteur pour la barre de recherche
        searchView3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }
    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Salle.this);
        View dialogView = getLayoutInflater().inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialog = builder.create();

        Button buttonQuitter = dialogView.findViewById(R.id.btnQuitterDialog);
        buttonQuitter.setOnClickListener(v -> dialog.dismiss());
    }

    private void filterData(String query) {
        String lowerCaseQuery = query.toLowerCase();
        filteredDataList3.clear();
        for (DataClass3 dataClass3 : dataList3) {
            if (dataClass3.getNumeroSalle().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass3.getNameSalle().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass3.getStatutSalle().toLowerCase().contains(lowerCaseQuery)) {
                filteredDataList3.add(dataClass3);
            }
        }
        adapter3.notifyDataSetChanged();  // Update RecyclerView with filtered data
    }

    private void loadData() {
        dialog.show();  // Show the loading dialog when fetching data

        eventListener3 = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList3.clear();  // Clear the existing list before updating
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass3 dataClass3 = itemSnapshot.getValue(DataClass3.class);
                    if (dataClass3 != null) {
                        dataClass3.setKey3(itemSnapshot.getKey());
                        dataList3.add(dataClass3);
                    }
                }
                filteredDataList3.clear();  // Update the filtered list
                filteredDataList3.addAll(dataList3);

                adapter3.notifyDataSetChanged();  // Notify adapter about data change
                dialog.dismiss();
                swipeRefreshLayoutSalle.setRefreshing(false);  // Stop refreshing animation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fnEnseignant", "Error fetching data", error.toException());
                dialog.dismiss();
                swipeRefreshLayoutSalle.setRefreshing(false);  // Stop refreshing even in case of error
            }
        });
    }

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