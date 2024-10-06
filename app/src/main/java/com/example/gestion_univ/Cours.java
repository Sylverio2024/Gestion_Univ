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

public class Cours extends AppCompatActivity {
    ImageButton buttonRetourE;
    FloatingActionButton fabCours;
    RecyclerView recyclerView2;
    List<DataClass2> dataList2;
    DatabaseReference databaseReference;
    ValueEventListener eventListener2;
    MyAdapter2 adapter2;
    SearchView searchView2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cours);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        buttonRetourE=findViewById(R.id.BackTeach);
        fabCours=findViewById(R.id.fabCours);
        recyclerView2=findViewById(R.id.recyclerViewCours);

        searchView2=findViewById(R.id.searchCours);
        searchView2.clearFocus();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(Cours.this,1);
        recyclerView2.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder=new AlertDialog.Builder(Cours.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();
        Button bntQ = dialog.findViewById(R.id.btnQuitterDialog);
        bntQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dataList2=new ArrayList<>();
        adapter2=new MyAdapter2(Cours.this,dataList2);
        recyclerView2.setAdapter(adapter2);

        databaseReference= FirebaseDatabase.getInstance().getReference("Cours");
        dialog.show();

        eventListener2=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList2.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass2 dataClass2=itemSnapshot.getValue(DataClass2.class);
                    dataClass2.setKey2(itemSnapshot.getKey());
                    dataList2.add(dataClass2);
                }
                adapter2.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList2(newText);
                return true;
            }
        });


        buttonRetourE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent( Cours.this, fn5.class);
                startActivity(intent);
                finish();

            }
        });
        fabCours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Cours.this, UploadCours.class);
                startActivity(intent);
                //finish();
            }
        });
    }
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, fn5.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
    public void searchList2(String text){
        ArrayList<DataClass2> searchList2 = new ArrayList<>();
        for(DataClass2 dataClass2:dataList2){
            if(dataClass2.getNumeroCours().toLowerCase().contains(text.toLowerCase()) || dataClass2.getNameCours().toLowerCase().contains(text.toLowerCase()) || dataClass2.getParcoursCours().toLowerCase().contains(text.toLowerCase()) || dataClass2.getNiveauCours().toLowerCase().contains(text.toLowerCase()) || dataClass2.getDescriptionCours().toLowerCase().contains(text.toLowerCase())){
                searchList2.add(dataClass2);
            }
        }
        adapter2.searchDataList2(searchList2);
    }
}