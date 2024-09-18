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

public class Salle extends AppCompatActivity {
    ImageButton buttonRetourE;
    FloatingActionButton fabSalle;
    RecyclerView recyclerView3;
    List<DataClass3> dataList3;
    DatabaseReference databaseReference;
    ValueEventListener eventListener3;
    MyAdapter3 adapter3;
    SearchView searchView3;
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
        buttonRetourE=findViewById(R.id.BackTeach);
        fabSalle=findViewById(R.id.fabSalle);
        recyclerView3=findViewById(R.id.recyclerViewSalle);

        searchView3=findViewById(R.id.searchSalle);
        searchView3.clearFocus();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(Salle.this,1);
        recyclerView3.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder=new AlertDialog.Builder(Salle.this);
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
        dataList3=new ArrayList<>();
        adapter3=new MyAdapter3(Salle.this,dataList3);
        recyclerView3.setAdapter(adapter3);

        databaseReference= FirebaseDatabase.getInstance().getReference("Salle");
        dialog.show();

        eventListener3=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList3.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass3 dataClass3=itemSnapshot.getValue(DataClass3.class);
                    dataClass3.setKey3(itemSnapshot.getKey());
                    dataList3.add(dataClass3);
                }
                adapter3.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchView3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList3(newText);
                return true;
            }
        });


        buttonRetourE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent( Salle.this, fn5.class);
                startActivity(intent);
                finish();

            }
        });
        fabSalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Salle.this, UploadSalle.class);
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
    public void searchList3(String text){
        ArrayList<DataClass3> searchList3 = new ArrayList<>();
        for(DataClass3 dataClass3:dataList3){
            if(dataClass3.getNumeroSalle().toLowerCase().contains(text.toLowerCase()) || dataClass3.getNameSalle().toLowerCase().contains(text.toLowerCase()) || dataClass3.getStatutSalle().toLowerCase().contains(text.toLowerCase())){
                searchList3.add(dataClass3);
            }
        }
        adapter3.searchDataList3(searchList3);
    }
}