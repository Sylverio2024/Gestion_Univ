package com.example.gestion_univ;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User1> userList;
    private Context context;
    private String currentUserEmail; // Email de l'utilisateur connecté

    public UserAdapter(List<User1> userList, Context context, String currentUserEmail) {
        this.userList = userList;
        this.context = context;
        this.currentUserEmail = currentUserEmail;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User1 user1 = userList.get(position);
        holder.nomComplet.setText(user1.getNomComplet());
        holder.email.setText(user1.getEmail());
        holder.telephone.setText(user1.getTelephone());
        holder.role.setText(user1.getRole());

        // Afficher le statut en ligne uniquement si l'email correspond à l'utilisateur connecté
       /* if (user1.getEmail().equals(currentUserEmail)) {
            holder.setStatus.setVisibility(View.VISIBLE);
        } else {
            holder.setStatus.setVisibility(View.GONE);
        }*/

        // Charger l'image depuis Firebase Storage avec Glide
        Glide.with(context)
                .load(user1.getImageUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            // Naviguer vers les détails de l'utilisateur (modifier, supprimer)
            Intent intent = new Intent(context, detailUser.class);
            intent.putExtra("nomComplet", user1.getNomComplet());
            intent.putExtra("email", user1.getEmail());
            intent.putExtra("telephone", user1.getTelephone());
            intent.putExtra("imageUrl", user1.getImageUrl());
            intent.putExtra("role", user1.getRole());
            // Ajoutez le statut ici
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nomComplet, email, telephone, role;
        ImageView imageView, setStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nomComplet = itemView.findViewById(R.id.nomCompletU);
            email = itemView.findViewById(R.id.emailU);
            telephone = itemView.findViewById(R.id.telephoneU);
            role = itemView.findViewById(R.id.role);
            imageView = itemView.findViewById(R.id.imageViewU);
            setStatus = itemView.findViewById(R.id.setStatus); // Initialement invisible, sera modifié dynamiquement
        }
    }
}