package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class choixSalle extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SalleAdapter salleAdapter;
    private List<Salle1> salleList;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private ImageButton buttonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_salle);

        recyclerView = findViewById(R.id.recyclerViewSalleChoix);
        searchView = findViewById(R.id.searchSalleChoix);
        buttonRetour = findViewById(R.id.BackTeach);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        salleList = new ArrayList<>();
        salleAdapter = new SalleAdapter(salleList, this);
        recyclerView.setAdapter(salleAdapter);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Salle");

        // Fetch available rooms from Firebase
        fetchAvailableSalles();

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                salleAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                salleAdapter.filter(newText);
                return false;
            }
        });

        // Set up buttonRetour click listener
        buttonRetour.setOnClickListener(v -> {
            // Pass any necessary data back to the previous activity
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void fetchAvailableSalles() {
        databaseReference.orderByChild("statutSalle").equalTo("Disponible").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                salleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Salle1 salle = snapshot.getValue(Salle1.class);
                    if (salle != null) {
                        salleList.add(salle);
                    }
                }
                salleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(choixSalle.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}