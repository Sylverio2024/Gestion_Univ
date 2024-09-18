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

public class SalleAdapter extends RecyclerView.Adapter<SalleAdapter.SalleViewHolder> {

    private List<Salle1> salleList;
    private List<Salle1> salleListFull;
    private Context context;

    public SalleAdapter(List<Salle1> salleList, Context context) {
        this.salleList = salleList;
        this.context = context;
        this.salleListFull = new ArrayList<>(salleList);
    }

    @NonNull
    @Override
    public SalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.salle_item, parent, false);
        return new SalleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SalleViewHolder holder, int position) {
        Salle1 currentSalle = salleList.get(position);
        holder.salleStatut.setText(currentSalle.getStatutSalle());
        holder.salleName.setText(currentSalle.getNameSalle());
        holder.salleNumero.setText(currentSalle.getNumeroSalle());
        holder.salleDescription.setText(currentSalle.getDescriptionSalle());

        holder.itemView.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("salle_nom", currentSalle.getNameSalle());
            ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return salleList.size();
    }

    public void filter(String query) {
        List<Salle1> filteredList = new ArrayList<>();

        if (query == null || query.isEmpty()) {
            filteredList.addAll(salleListFull);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (Salle1 item : salleListFull) {
                if (item.getNameSalle().toLowerCase().contains(filterPattern) ||
                        item.getNumeroSalle().toLowerCase().contains(filterPattern) ||
                        item.getStatutSalle().toLowerCase().contains(filterPattern) ||
                        item.getDescriptionSalle().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }

        salleList.clear();
        salleList.addAll(filteredList);
        notifyDataSetChanged();
    }

    static class SalleViewHolder extends RecyclerView.ViewHolder {
        TextView salleStatut, salleName, salleNumero, salleDescription;

        SalleViewHolder(View itemView) {
            super(itemView);
            salleStatut = itemView.findViewById(R.id.salle_statut);
            salleName = itemView.findViewById(R.id.salle_name);
            salleNumero = itemView.findViewById(R.id.salle_numero);
            salleDescription = itemView.findViewById(R.id.salle_description);
        }
    }
}