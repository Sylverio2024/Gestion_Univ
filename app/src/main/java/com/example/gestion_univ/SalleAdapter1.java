package com.example.gestion_univ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SalleAdapter1 extends RecyclerView.Adapter<SalleAdapter1.SalleViewHolder1> {

    private List<Salle2> salleList1;
    private List<Salle2> salleListFull1;
    private Context context;

    public SalleAdapter1(List<Salle2> salleList1, Context context) {
        this.salleList1 = salleList1;
        this.context = context;
        this.salleListFull1 = new ArrayList<>(salleList1);
    }

    @NonNull
    @Override
    public SalleViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.salle_item1, parent, false);
        return new SalleViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SalleViewHolder1 holder, int position) {
        Salle2 currentSalle1 = salleList1.get(position);
        holder.salleStatut1.setText(currentSalle1.getStatutSalle());
        holder.salleName1.setText(currentSalle1.getNameSalle());
        holder.salleNumero1.setText(currentSalle1.getNumeroSalle());
        holder.salleDescription1.setText(currentSalle1.getDescriptionSalle());

        holder.itemView.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("salle_nom", currentSalle1.getNameSalle());
            ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return salleList1.size();
    }

    public void filter(String query) {
        List<Salle2> filteredList1 = new ArrayList<>();

        if (query == null || query.isEmpty()) {
            filteredList1.addAll(salleListFull1);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (Salle2 item : salleListFull1) {
                if (item.getNameSalle().toLowerCase().contains(filterPattern) ||
                        item.getNumeroSalle().toLowerCase().contains(filterPattern) ||
                        item.getStatutSalle().toLowerCase().contains(filterPattern) ||
                        item.getDescriptionSalle().toLowerCase().contains(filterPattern)) {
                    filteredList1.add(item);
                }
            }
        }

        salleList1.clear();
        salleList1.addAll(filteredList1);
        notifyDataSetChanged();
    }

    static class SalleViewHolder1 extends RecyclerView.ViewHolder {
        TextView salleStatut1, salleName1, salleNumero1, salleDescription1;

        SalleViewHolder1(View itemView) {
            super(itemView);
            salleStatut1 = itemView.findViewById(R.id.salle_statut1);
            salleName1 = itemView.findViewById(R.id.salle_name1);
            salleNumero1 = itemView.findViewById(R.id.salle_numero1);
            salleDescription1 = itemView.findViewById(R.id.salle_description1);
        }
    }
}