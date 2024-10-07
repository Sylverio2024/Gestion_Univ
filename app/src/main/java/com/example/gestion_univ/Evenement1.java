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
    RecyclerView recyclerViewEvent;
    List<DataClass4> dataList4;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    AdapterEvenement adapter4;
    SearchView searchViewEvent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evenement1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewEvent = findViewById(R.id.recyclerViewEvent);
        buttonRetourEvent = findViewById(R.id.BackTeach);
        searchViewEvent = findViewById(R.id.searchEvent);
        searchViewEvent.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Evenement1.this, 1);
        recyclerViewEvent.setLayoutManager(gridLayoutManager);

        // Progress Dialog Setup
        AlertDialog.Builder builder = new AlertDialog.Builder(Evenement1.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Close Dialog button
        Button bntQ = dialog.findViewById(R.id.btnQuitterDialog);
        bntQ.setOnClickListener(v -> dialog.dismiss());

        // Data and Adapter setup
        dataList4 = new ArrayList<>();
        adapter4 = new AdapterEvenement(Evenement1.this, dataList4);
        recyclerViewEvent.setAdapter(adapter4);

        // Fetching data from Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList4.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass4 dataClass4 = itemSnapshot.getValue(DataClass4.class);
                    if (dataClass4 != null) {
                        dataClass4.setKey4(itemSnapshot.getKey());
                        dataList4.add(dataClass4);
                    }
                }
                adapter4.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        // Search functionality
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

        // Back button action
        buttonRetourEvent.setOnClickListener(v -> {
            Intent intent = new Intent(Evenement1.this, MainEtudiant.class);
            startActivity(intent);
            finish();
        });
    }

    // Override Back button press to return to the main activity
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

    // Search logic for filtering events
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