// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUpdateTeacherBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final Button bntUpdateT;

  @NonNull
  public final ScrollView main;

  @NonNull
  public final EditText updateAdresseT;

  @NonNull
  public final EditText updateCategorieT;

  @NonNull
  public final EditText updateNameT;

  @NonNull
  public final EditText updateNumeroID;

  @NonNull
  public final EditText updatePrenomT;

  @NonNull
  public final EditText updateSpecialiteT;

  @NonNull
  public final EditText updateTelephoneT;

  private ActivityUpdateTeacherBinding(@NonNull ScrollView rootView, @NonNull Button bntUpdateT,
      @NonNull ScrollView main, @NonNull EditText updateAdresseT,
      @NonNull EditText updateCategorieT, @NonNull EditText updateNameT,
      @NonNull EditText updateNumeroID, @NonNull EditText updatePrenomT,
      @NonNull EditText updateSpecialiteT, @NonNull EditText updateTelephoneT) {
    this.rootView = rootView;
    this.bntUpdateT = bntUpdateT;
    this.main = main;
    this.updateAdresseT = updateAdresseT;
    this.updateCategorieT = updateCategorieT;
    this.updateNameT = updateNameT;
    this.updateNumeroID = updateNumeroID;
    this.updatePrenomT = updatePrenomT;
    this.updateSpecialiteT = updateSpecialiteT;
    this.updateTelephoneT = updateTelephoneT;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUpdateTeacherBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUpdateTeacherBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_update_teacher, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUpdateTeacherBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bntUpdateT;
      Button bntUpdateT = ViewBindings.findChildViewById(rootView, id);
      if (bntUpdateT == null) {
        break missingId;
      }

      ScrollView main = (ScrollView) rootView;

      id = R.id.updateAdresseT;
      EditText updateAdresseT = ViewBindings.findChildViewById(rootView, id);
      if (updateAdresseT == null) {
        break missingId;
      }

      id = R.id.updateCategorieT;
      EditText updateCategorieT = ViewBindings.findChildViewById(rootView, id);
      if (updateCategorieT == null) {
        break missingId;
      }

      id = R.id.updateNameT;
      EditText updateNameT = ViewBindings.findChildViewById(rootView, id);
      if (updateNameT == null) {
        break missingId;
      }

      id = R.id.updateNumeroID;
      EditText updateNumeroID = ViewBindings.findChildViewById(rootView, id);
      if (updateNumeroID == null) {
        break missingId;
      }

      id = R.id.updatePrenomT;
      EditText updatePrenomT = ViewBindings.findChildViewById(rootView, id);
      if (updatePrenomT == null) {
        break missingId;
      }

      id = R.id.updateSpecialiteT;
      EditText updateSpecialiteT = ViewBindings.findChildViewById(rootView, id);
      if (updateSpecialiteT == null) {
        break missingId;
      }

      id = R.id.updateTelephoneT;
      EditText updateTelephoneT = ViewBindings.findChildViewById(rootView, id);
      if (updateTelephoneT == null) {
        break missingId;
      }

      return new ActivityUpdateTeacherBinding((ScrollView) rootView, bntUpdateT, main,
          updateAdresseT, updateCategorieT, updateNameT, updateNumeroID, updatePrenomT,
          updateSpecialiteT, updateTelephoneT);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
