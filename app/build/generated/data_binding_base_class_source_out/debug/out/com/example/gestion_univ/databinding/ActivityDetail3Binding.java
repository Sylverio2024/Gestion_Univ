// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import com.github.clans.fab.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDetail3Binding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final FloatingActionButton deleteButtonSalle;

  @NonNull
  public final TextView detailDescriptionSalle;

  @NonNull
  public final TextView detailNomSalle;

  @NonNull
  public final TextView detailNumeroSalle;

  @NonNull
  public final TextView detailStatutSalle;

  @NonNull
  public final FloatingActionButton editButtonSalle;

  @NonNull
  public final TextView labelDescription;

  @NonNull
  public final TextView labelNom;

  @NonNull
  public final TextView labelNumeroID;

  @NonNull
  public final TextView labelStatut;

  @NonNull
  public final RelativeLayout main;

  private ActivityDetail3Binding(@NonNull RelativeLayout rootView,
      @NonNull FloatingActionButton deleteButtonSalle, @NonNull TextView detailDescriptionSalle,
      @NonNull TextView detailNomSalle, @NonNull TextView detailNumeroSalle,
      @NonNull TextView detailStatutSalle, @NonNull FloatingActionButton editButtonSalle,
      @NonNull TextView labelDescription, @NonNull TextView labelNom,
      @NonNull TextView labelNumeroID, @NonNull TextView labelStatut,
      @NonNull RelativeLayout main) {
    this.rootView = rootView;
    this.deleteButtonSalle = deleteButtonSalle;
    this.detailDescriptionSalle = detailDescriptionSalle;
    this.detailNomSalle = detailNomSalle;
    this.detailNumeroSalle = detailNumeroSalle;
    this.detailStatutSalle = detailStatutSalle;
    this.editButtonSalle = editButtonSalle;
    this.labelDescription = labelDescription;
    this.labelNom = labelNom;
    this.labelNumeroID = labelNumeroID;
    this.labelStatut = labelStatut;
    this.main = main;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDetail3Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDetail3Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_detail3, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDetail3Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.deleteButtonSalle;
      FloatingActionButton deleteButtonSalle = ViewBindings.findChildViewById(rootView, id);
      if (deleteButtonSalle == null) {
        break missingId;
      }

      id = R.id.detailDescriptionSalle;
      TextView detailDescriptionSalle = ViewBindings.findChildViewById(rootView, id);
      if (detailDescriptionSalle == null) {
        break missingId;
      }

      id = R.id.detailNomSalle;
      TextView detailNomSalle = ViewBindings.findChildViewById(rootView, id);
      if (detailNomSalle == null) {
        break missingId;
      }

      id = R.id.detailNumeroSalle;
      TextView detailNumeroSalle = ViewBindings.findChildViewById(rootView, id);
      if (detailNumeroSalle == null) {
        break missingId;
      }

      id = R.id.detailStatutSalle;
      TextView detailStatutSalle = ViewBindings.findChildViewById(rootView, id);
      if (detailStatutSalle == null) {
        break missingId;
      }

      id = R.id.editButtonSalle;
      FloatingActionButton editButtonSalle = ViewBindings.findChildViewById(rootView, id);
      if (editButtonSalle == null) {
        break missingId;
      }

      id = R.id.labelDescription;
      TextView labelDescription = ViewBindings.findChildViewById(rootView, id);
      if (labelDescription == null) {
        break missingId;
      }

      id = R.id.labelNom;
      TextView labelNom = ViewBindings.findChildViewById(rootView, id);
      if (labelNom == null) {
        break missingId;
      }

      id = R.id.labelNumeroID;
      TextView labelNumeroID = ViewBindings.findChildViewById(rootView, id);
      if (labelNumeroID == null) {
        break missingId;
      }

      id = R.id.labelStatut;
      TextView labelStatut = ViewBindings.findChildViewById(rootView, id);
      if (labelStatut == null) {
        break missingId;
      }

      RelativeLayout main = (RelativeLayout) rootView;

      return new ActivityDetail3Binding((RelativeLayout) rootView, deleteButtonSalle,
          detailDescriptionSalle, detailNomSalle, detailNumeroSalle, detailStatutSalle,
          editButtonSalle, labelDescription, labelNom, labelNumeroID, labelStatut, main);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
