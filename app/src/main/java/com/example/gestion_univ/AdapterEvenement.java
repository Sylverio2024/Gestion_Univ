package com.example.gestion_univ;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterEvenement extends RecyclerView.Adapter<MyViewHolderEvent> {

    private Context context;
    private List<DataClass4> dataList4;

    public AdapterEvenement(Context context, List<DataClass4> dataList4) {
        this.context = context;
        this.dataList4 = dataList4;
    }

    @NonNull
    @Override
    public MyViewHolderEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_event, parent, false);
        return new MyViewHolderEvent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderEvent holder, int position) {
        DataClass4 dataClass4 = dataList4.get(position);

        holder.NumeroIDEvent.setText(dataClass4.getNumeroEvent());
        holder.titreEvent.setText(dataClass4.getTitreEvent());
        holder.dateEvent.setText(dataClass4.getDateEvent());
        holder.heureEvent.setText(dataClass4.getTimeEvent());
        holder.descriptionEvent.setText(dataClass4.getDescriptionEvent());

        // Configuration du RecyclerView pour les images
        holder.imageRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        ImageAdapter imageAdapter = new ImageAdapter(context, dataClass4.getImagesEvent()); // Utiliser l'adaptateur d'images
        holder.imageRecyclerView.setAdapter(imageAdapter);

        holder.recCard4.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivityEvent.class);
            intent.putExtra("numeroEvent", dataClass4.getNumeroEvent());
            intent.putExtra("titreEvent", dataClass4.getTitreEvent());
            intent.putExtra("dateEvent", dataClass4.getDateEvent());
            intent.putExtra("heureEvent", dataClass4.getTimeEvent());
            intent.putExtra("descriptionEvent", dataClass4.getDescriptionEvent());
            intent.putExtra("key4", dataClass4.getKey4());
            intent.putStringArrayListExtra("imagesEvent", new ArrayList<>(dataClass4.getImagesEvent())); // Passer la liste d'images
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList4.size();
    }

    public void searchDataList4(List<DataClass4> searchList4) {
        dataList4 = searchList4;
        notifyDataSetChanged();
    }
}

class MyViewHolderEvent extends RecyclerView.ViewHolder {
    RecyclerView imageRecyclerView;
    TextView NumeroIDEvent, titreEvent, dateEvent, heureEvent, descriptionEvent;
    CardView recCard4;

    public MyViewHolderEvent(@NonNull View itemView) {
        super(itemView);
        imageRecyclerView = itemView.findViewById(R.id.imageRecyclerView); // RecyclerView pour afficher les images
        recCard4 = itemView.findViewById(R.id.recCard4);
        NumeroIDEvent = itemView.findViewById(R.id.NumeroIDEvent);
        titreEvent = itemView.findViewById(R.id.titreEvent);
        dateEvent = itemView.findViewById(R.id.dateEvent);
        heureEvent = itemView.findViewById(R.id.heureEvent);
        descriptionEvent = itemView.findViewById(R.id.descriptionEvent);
    }
}