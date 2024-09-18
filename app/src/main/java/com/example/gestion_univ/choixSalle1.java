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

public class choixSalle1 extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView recyclerView1;
    private SalleAdapter1 salleAdapter1;
    private List<Salle2> salleList1;
    private SearchView searchView1;
    private DatabaseReference databaseReference;
    private ImageButton buttonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_salle1);

        recyclerView1 = findViewById(R.id.recyclerViewSalleChoix1);
        searchView1 = findViewById(R.id.searchSalleChoix1);
        buttonRetour = findViewById(R.id.BackTeach);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        salleList1 = new ArrayList<>();
        salleAdapter1 = new SalleAdapter1(salleList1, this);
        recyclerView1.setAdapter(salleAdapter1);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Salle");

        // Fetch available rooms from Firebase
        fetchAvailableSalles();

        // Set up SearchView listener
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                salleAdapter1.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                salleAdapter1.filter(newText);
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
                salleList1.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Salle2 salle2 = snapshot.getValue(Salle2.class);
                    if (salle2 != null) {
                        salleList1.add(salle2);
                    }
                }
                salleAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(choixSalle1.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}