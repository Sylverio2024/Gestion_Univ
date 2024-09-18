// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUploadStudentBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final Button bntSaveS;

  @NonNull
  public final NestedScrollView main;

  @NonNull
  public final ScrollView scrollView1;

  @NonNull
  public final EditText txtAdresseS;

  @NonNull
  public final EditText txtDateNaissanceS;

  @NonNull
  public final EditText txtMentionS;

  @NonNull
  public final EditText txtNiveauS;

  @NonNull
  public final EditText txtNomS;

  @NonNull
  public final EditText txtNumInscriptionS;

  @NonNull
  public final EditText txtNumeroID1;

  @NonNull
  public final EditText txtParcoursS;

  @NonNull
  public final EditText txtPrenomS;

  @NonNull
  public final EditText txtTelephoneS;

  @NonNull
  public final ImageView updloadImage1;

  private ActivityUploadStudentBinding(@NonNull NestedScrollView rootView, @NonNull Button bntSaveS,
      @NonNull NestedScrollView main, @NonNull ScrollView scrollView1,
      @NonNull EditText txtAdresseS, @NonNull EditText txtDateNaissanceS,
      @NonNull EditText txtMentionS, @NonNull EditText txtNiveauS, @NonNull EditText txtNomS,
      @NonNull EditText txtNumInscriptionS, @NonNull EditText txtNumeroID1,
      @NonNull EditText txtParcoursS, @NonNull EditText txtPrenomS, @NonNull EditText txtTelephoneS,
      @NonNull ImageView updloadImage1) {
    this.rootView = rootView;
    this.bntSaveS = bntSaveS;
    this.main = main;
    this.scrollView1 = scrollView1;
    this.txtAdresseS = txtAdresseS;
    this.txtDateNaissanceS = txtDateNaissanceS;
    this.txtMentionS = txtMentionS;
    this.txtNiveauS = txtNiveauS;
    this.txtNomS = txtNomS;
    this.txtNumInscriptionS = txtNumInscriptionS;
    this.txtNumeroID1 = txtNumeroID1;
    this.txtParcoursS = txtParcoursS;
    this.txtPrenomS = txtPrenomS;
    this.txtTelephoneS = txtTelephoneS;
    this.updloadImage1 = updloadImage1;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUploadStudentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUploadStudentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_upload_student, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUploadStudentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bntSaveS;
      Button bntSaveS = ViewBindings.findChildViewById(rootView, id);
      if (bntSaveS == null) {
        break missingId;
      }

      NestedScrollView main = (NestedScrollView) rootView;

      id = R.id.scrollView1;
      ScrollView scrollView1 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView1 == null) {
        break missingId;
      }

      id = R.id.txtAdresseS;
      EditText txtAdresseS = ViewBindings.findChildViewById(rootView, id);
      if (txtAdresseS == null) {
        break missingId;
      }

      id = R.id.txtDate_naissanceS;
      EditText txtDateNaissanceS = ViewBindings.findChildViewById(rootView, id);
      if (txtDateNaissanceS == null) {
        break missingId;
      }

      id = R.id.txtMentionS;
      EditText txtMentionS = ViewBindings.findChildViewById(rootView, id);
      if (txtMentionS == null) {
        break missingId;
      }

      id = R.id.txtNiveauS;
      EditText txtNiveauS = ViewBindings.findChildViewById(rootView, id);
      if (txtNiveauS == null) {
        break missingId;
      }

      id = R.id.txtNomS;
      EditText txtNomS = ViewBindings.findChildViewById(rootView, id);
      if (txtNomS == null) {
        break missingId;
      }

      id = R.id.txtNum_inscriptionS;
      EditText txtNumInscriptionS = ViewBindings.findChildViewById(rootView, id);
      if (txtNumInscriptionS == null) {
        break missingId;
      }

      id = R.id.txtNumeroID1;
      EditText txtNumeroID1 = ViewBindings.findChildViewById(rootView, id);
      if (txtNumeroID1 == null) {
        break missingId;
      }

      id = R.id.txtParcoursS;
      EditText txtParcoursS = ViewBindings.findChildViewById(rootView, id);
      if (txtParcoursS == null) {
        break missingId;
      }

      id = R.id.txtPrenomS;
      EditText txtPrenomS = ViewBindings.findChildViewById(rootView, id);
      if (txtPrenomS == null) {
        break missingId;
      }

      id = R.id.txtTelephoneS;
      EditText txtTelephoneS = ViewBindings.findChildViewById(rootView, id);
      if (txtTelephoneS == null) {
        break missingId;
      }

      id = R.id.updloadImage1;
      ImageView updloadImage1 = ViewBindings.findChildViewById(rootView, id);
      if (updloadImage1 == null) {
        break missingId;
      }

      return new ActivityUploadStudentBinding((NestedScrollView) rootView, bntSaveS, main,
          scrollView1, txtAdresseS, txtDateNaissanceS, txtMentionS, txtNiveauS, txtNomS,
          txtNumInscriptionS, txtNumeroID1, txtParcoursS, txtPrenomS, txtTelephoneS, updloadImage1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
