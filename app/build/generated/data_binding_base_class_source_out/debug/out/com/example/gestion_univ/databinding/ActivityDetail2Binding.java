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

public final class ActivityDetail2Binding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final FloatingActionButton deleteButtonCours;

  @NonNull
  public final TextView detailDate;

  @NonNull
  public final TextView detailDescriptionCours;

  @NonNull
  public final TextView detailHeure;

  @NonNull
  public final TextView detailNiveauCours;

  @NonNull
  public final TextView detailNomCours;

  @NonNull
  public final TextView detailNumeroCours;

  @NonNull
  public final TextView detailParcoursCours;

  @NonNull
  public final TextView detailSalleCours;

  @NonNull
  public final FloatingActionButton editButtonCours;

  @NonNull
  public final TextView labelDateHeure;

  @NonNull
  public final TextView labelDescriptionCours;

  @NonNull
  public final TextView labelNiveauCours;

  @NonNull
  public final TextView labelNom;

  @NonNull
  public final TextView labelNumeroID;

  @NonNull
  public final TextView labelParcoursCours;

  @NonNull
  public final TextView labelSalleCours;

  @NonNull
  public final RelativeLayout main;

  private ActivityDetail2Binding(@NonNull RelativeLayout rootView,
      @NonNull FloatingActionButton deleteButtonCours, @NonNull TextView detailDate,
      @NonNull TextView detailDescriptionCours, @NonNull TextView detailHeure,
      @NonNull TextView detailNiveauCours, @NonNull TextView detailNomCours,
      @NonNull TextView detailNumeroCours, @NonNull TextView detailParcoursCours,
      @NonNull TextView detailSalleCours, @NonNull FloatingActionButton editButtonCours,
      @NonNull TextView labelDateHeure, @NonNull TextView labelDescriptionCours,
      @NonNull TextView labelNiveauCours, @NonNull TextView labelNom,
      @NonNull TextView labelNumeroID, @NonNull TextView labelParcoursCours,
      @NonNull TextView labelSalleCours, @NonNull RelativeLayout main) {
    this.rootView = rootView;
    this.deleteButtonCours = deleteButtonCours;
    this.detailDate = detailDate;
    this.detailDescriptionCours = detailDescriptionCours;
    this.detailHeure = detailHeure;
    this.detailNiveauCours = detailNiveauCours;
    this.detailNomCours = detailNomCours;
    this.detailNumeroCours = detailNumeroCours;
    this.detailParcoursCours = detailParcoursCours;
    this.detailSalleCours = detailSalleCours;
    this.editButtonCours = editButtonCours;
    this.labelDateHeure = labelDateHeure;
    this.labelDescriptionCours = labelDescriptionCours;
    this.labelNiveauCours = labelNiveauCours;
    this.labelNom = labelNom;
    this.labelNumeroID = labelNumeroID;
    this.labelParcoursCours = labelParcoursCours;
    this.labelSalleCours = labelSalleCours;
    this.main = main;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDetail2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDetail2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_detail2, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDetail2Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.deleteButtonCours;
      FloatingActionButton deleteButtonCours = ViewBindings.findChildViewById(rootView, id);
      if (deleteButtonCours == null) {
        break missingId;
      }

      id = R.id.detailDate;
      TextView detailDate = ViewBindings.findChildViewById(rootView, id);
      if (detailDate == null) {
        break missingId;
      }

      id = R.id.detailDescriptionCours;
      TextView detailDescriptionCours = ViewBindings.findChildViewById(rootView, id);
      if (detailDescriptionCours == null) {
        break missingId;
      }

      id = R.id.detailHeure;
      TextView detailHeure = ViewBindings.findChildViewById(rootView, id);
      if (detailHeure == null) {
        break missingId;
      }

      id = R.id.detailNiveauCours;
      TextView detailNiveauCours = ViewBindings.findChildViewById(rootView, id);
      if (detailNiveauCours == null) {
        break missingId;
      }

      id = R.id.detailNomCours;
      TextView detailNomCours = ViewBindings.findChildViewById(rootView, id);
      if (detailNomCours == null) {
        break missingId;
      }

      id = R.id.detailNumeroCours;
      TextView detailNumeroCours = ViewBindings.findChildViewById(rootView, id);
      if (detailNumeroCours == null) {
        break missingId;
      }

      id = R.id.detailParcoursCours;
      TextView detailParcoursCours = ViewBindings.findChildViewById(rootView, id);
      if (detailParcoursCours == null) {
        break missingId;
      }

      id = R.id.detailSalleCours;
      TextView detailSalleCours = ViewBindings.findChildViewById(rootView, id);
      if (detailSalleCours == null) {
        break missingId;
      }

      id = R.id.editButtonCours;
      FloatingActionButton editButtonCours = ViewBindings.findChildViewById(rootView, id);
      if (editButtonCours == null) {
        break missingId;
      }

      id = R.id.labelDateHeure;
      TextView labelDateHeure = ViewBindings.findChildViewById(rootView, id);
      if (labelDateHeure == null) {
        break missingId;
      }

      id = R.id.labelDescriptionCours;
      TextView labelDescriptionCours = ViewBindings.findChildViewById(rootView, id);
      if (labelDescriptionCours == null) {
        break missingId;
      }

      id = R.id.labelNiveauCours;
      TextView labelNiveauCours = ViewBindings.findChildViewById(rootView, id);
      if (labelNiveauCours == null) {
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

      id = R.id.labelParcoursCours;
      TextView labelParcoursCours = ViewBindings.findChildViewById(rootView, id);
      if (labelParcoursCours == null) {
        break missingId;
      }

      id = R.id.labelSalleCours;
      TextView labelSalleCours = ViewBindings.findChildViewById(rootView, id);
      if (labelSalleCours == null) {
        break missingId;
      }

      RelativeLayout main = (RelativeLayout) rootView;

      return new ActivityDetail2Binding((RelativeLayout) rootView, deleteButtonCours, detailDate,
          detailDescriptionCours, detailHeure, detailNiveauCours, detailNomCours, detailNumeroCours,
          detailParcoursCours, detailSalleCours, editButtonCours, labelDateHeure,
          labelDescriptionCours, labelNiveauCours, labelNom, labelNumeroID, labelParcoursCours,
          labelSalleCours, main);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
