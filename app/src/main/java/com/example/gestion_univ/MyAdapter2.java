package com.example.gestion_univ;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyViewHolder2> {

    private Context context2;
    private List<DataClass2> dataList2;

    public MyAdapter2(Context context2, List<DataClass2> dataList2) {
        this.context2 = context2;
        this.dataList2 = dataList2;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item2, parent, false);
        return new MyViewHolder2(view2);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        holder.NumeroIDCours.setText(dataList2.get(position).getNumeroCours());
        holder.NomCours.setText(dataList2.get(position).getNameCours());
        holder.SalleCours.setText(dataList2.get(position).getSalleCours());
        holder.ParcoursCours.setText(dataList2.get(position).getParcoursCours());
        holder.NiveauCours.setText(dataList2.get(position).getNiveauCours());
        holder.DescriptionCours.setText(dataList2.get(position).getDescriptionCours());

        // Ajout des champs date et heure
        holder.DateCours.setText(dataList2.get(position).getDateCours());
        holder.HeureCours.setText(dataList2.get(position).getTimeCours());

        holder.recCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context2, DetailActivity2.class);

                intent.putExtra("numeroCours", dataList2.get(holder.getAdapterPosition()).getNumeroCours());
                intent.putExtra("nameCours", dataList2.get(holder.getAdapterPosition()).getNameCours());
                intent.putExtra("salleCours", dataList2.get(holder.getAdapterPosition()).getSalleCours());
                intent.putExtra("parcoursCours", dataList2.get(holder.getAdapterPosition()).getParcoursCours());
                intent.putExtra("niveauCours", dataList2.get(holder.getAdapterPosition()).getNiveauCours());
                intent.putExtra("descriptionCours", dataList2.get(holder.getAdapterPosition()).getDescriptionCours());

                // Ajout des champs date et heure dans l'intent
                intent.putExtra("dateCours", dataList2.get(holder.getAdapterPosition()).getDateCours());
                intent.putExtra("heureCours", dataList2.get(holder.getAdapterPosition()).getTimeCours());

                intent.putExtra("key2", dataList2.get(holder.getAdapterPosition()).getKey2());

                context2.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList2.size();
    }

    public void searchDataList2(ArrayList<DataClass2> searchList2) {
        dataList2 = searchList2;
        notifyDataSetChanged();
    }
}

class MyViewHolder2 extends RecyclerView.ViewHolder {

    TextView NumeroIDCours, NomCours, SalleCours, ParcoursCours, NiveauCours, DescriptionCours, DateCours, HeureCours;
    CardView recCard2;

    public MyViewHolder2(@NonNull View itemView) {
        super(itemView);

        recCard2 = itemView.findViewById(R.id.recCard2);
        NumeroIDCours = itemView.findViewById(R.id.NumeroIDCours);
        NomCours = itemView.findViewById(R.id.NomCours);
        SalleCours = itemView.findViewById(R.id.SalleCours);
        ParcoursCours = itemView.findViewById(R.id.ParcoursCours);
        NiveauCours = itemView.findViewById(R.id.NiveauCours);
        DescriptionCours = itemView.findViewById(R.id.DescriptionCours);

        // Initialisation des nouveaux champs date et heure
        DateCours = itemView.findViewById(R.id.dateCours);
        HeureCours = itemView.findViewById(R.id.heureCours);
    }
}