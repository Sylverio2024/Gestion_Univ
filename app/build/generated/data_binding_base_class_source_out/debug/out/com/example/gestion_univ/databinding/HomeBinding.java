// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class HomeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView cours;

  @NonNull
  public final ImageView evenement;

  @NonNull
  public final ImageView imageView12;

  @NonNull
  public final ImageView imageView18;

  @NonNull
  public final ImageView imageView19;

  @NonNull
  public final ImageView imageView5;

  @NonNull
  public final ImageView notification;

  @NonNull
  public final ImageView salle;

  @NonNull
  public final TextView textCours;

  @NonNull
  public final TextView textEvent;

  @NonNull
  public final TextView textNotification;

  @NonNull
  public final TextView textSalle;

  private HomeBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView cours,
      @NonNull ImageView evenement, @NonNull ImageView imageView12, @NonNull ImageView imageView18,
      @NonNull ImageView imageView19, @NonNull ImageView imageView5,
      @NonNull ImageView notification, @NonNull ImageView salle, @NonNull TextView textCours,
      @NonNull TextView textEvent, @NonNull TextView textNotification,
      @NonNull TextView textSalle) {
    this.rootView = rootView;
    this.cours = cours;
    this.evenement = evenement;
    this.imageView12 = imageView12;
    this.imageView18 = imageView18;
    this.imageView19 = imageView19;
    this.imageView5 = imageView5;
    this.notification = notification;
    this.salle = salle;
    this.textCours = textCours;
    this.textEvent = textEvent;
    this.textNotification = textNotification;
    this.textSalle = textSalle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static HomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static HomeBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
      boolean attachToParent) {
    View root = inflater.inflate(R.layout.home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static HomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cours;
      ImageView cours = ViewBindings.findChildViewById(rootView, id);
      if (cours == null) {
        break missingId;
      }

      id = R.id.evenement;
      ImageView evenement = ViewBindings.findChildViewById(rootView, id);
      if (evenement == null) {
        break missingId;
      }

      id = R.id.imageView12;
      ImageView imageView12 = ViewBindings.findChildViewById(rootView, id);
      if (imageView12 == null) {
        break missingId;
      }

      id = R.id.imageView18;
      ImageView imageView18 = ViewBindings.findChildViewById(rootView, id);
      if (imageView18 == null) {
        break missingId;
      }

      id = R.id.imageView19;
      ImageView imageView19 = ViewBindings.findChildViewById(rootView, id);
      if (imageView19 == null) {
        break missingId;
      }

      id = R.id.imageView5;
      ImageView imageView5 = ViewBindings.findChildViewById(rootView, id);
      if (imageView5 == null) {
        break missingId;
      }

      id = R.id.notification;
      ImageView notification = ViewBindings.findChildViewById(rootView, id);
      if (notification == null) {
        break missingId;
      }

      id = R.id.salle;
      ImageView salle = ViewBindings.findChildViewById(rootView, id);
      if (salle == null) {
        break missingId;
      }

      id = R.id.textCours;
      TextView textCours = ViewBindings.findChildViewById(rootView, id);
      if (textCours == null) {
        break missingId;
      }

      id = R.id.textEvent;
      TextView textEvent = ViewBindings.findChildViewById(rootView, id);
      if (textEvent == null) {
        break missingId;
      }

      id = R.id.textNotification;
      TextView textNotification = ViewBindings.findChildViewById(rootView, id);
      if (textNotification == null) {
        break missingId;
      }

      id = R.id.textSalle;
      TextView textSalle = ViewBindings.findChildViewById(rootView, id);
      if (textSalle == null) {
        break missingId;
      }

      return new HomeBinding((ConstraintLayout) rootView, cours, evenement, imageView12,
          imageView18, imageView19, imageView5, notification, salle, textCours, textEvent,
          textNotification, textSalle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
