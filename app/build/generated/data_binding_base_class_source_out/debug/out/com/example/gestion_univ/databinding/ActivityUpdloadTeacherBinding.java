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

public final class ActivityUpdloadTeacherBinding implements ViewBinding {
  @NonNull
  private final NestedScrollView rootView;

  @NonNull
  public final Button bntSaveT;

  @NonNull
  public final NestedScrollView main;

  @NonNull
  public final ScrollView scrollView;

  @NonNull
  public final EditText txtAdresseT;

  @NonNull
  public final EditText txtCategorieT;

  @NonNull
  public final EditText txtNameT;

  @NonNull
  public final EditText txtNumeroID;

  @NonNull
  public final EditText txtPrenomT;

  @NonNull
  public final EditText txtSpecialiteT;

  @NonNull
  public final EditText txtTelephoneT;

  @NonNull
  public final ImageView updloadImage;

  private ActivityUpdloadTeacherBinding(@NonNull NestedScrollView rootView,
      @NonNull Button bntSaveT, @NonNull NestedScrollView main, @NonNull ScrollView scrollView,
      @NonNull EditText txtAdresseT, @NonNull EditText txtCategorieT, @NonNull EditText txtNameT,
      @NonNull EditText txtNumeroID, @NonNull EditText txtPrenomT, @NonNull EditText txtSpecialiteT,
      @NonNull EditText txtTelephoneT, @NonNull ImageView updloadImage) {
    this.rootView = rootView;
    this.bntSaveT = bntSaveT;
    this.main = main;
    this.scrollView = scrollView;
    this.txtAdresseT = txtAdresseT;
    this.txtCategorieT = txtCategorieT;
    this.txtNameT = txtNameT;
    this.txtNumeroID = txtNumeroID;
    this.txtPrenomT = txtPrenomT;
    this.txtSpecialiteT = txtSpecialiteT;
    this.txtTelephoneT = txtTelephoneT;
    this.updloadImage = updloadImage;
  }

  @Override
  @NonNull
  public NestedScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUpdloadTeacherBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUpdloadTeacherBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_updload_teacher, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUpdloadTeacherBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bntSaveT;
      Button bntSaveT = ViewBindings.findChildViewById(rootView, id);
      if (bntSaveT == null) {
        break missingId;
      }

      NestedScrollView main = (NestedScrollView) rootView;

      id = R.id.scrollView;
      ScrollView scrollView = ViewBindings.findChildViewById(rootView, id);
      if (scrollView == null) {
        break missingId;
      }

      id = R.id.txtAdresseT;
      EditText txtAdresseT = ViewBindings.findChildViewById(rootView, id);
      if (txtAdresseT == null) {
        break missingId;
      }

      id = R.id.txtCategorieT;
      EditText txtCategorieT = ViewBindings.findChildViewById(rootView, id);
      if (txtCategorieT == null) {
        break missingId;
      }

      id = R.id.txtNameT;
      EditText txtNameT = ViewBindings.findChildViewById(rootView, id);
      if (txtNameT == null) {
        break missingId;
      }

      id = R.id.txtNumeroID;
      EditText txtNumeroID = ViewBindings.findChildViewById(rootView, id);
      if (txtNumeroID == null) {
        break missingId;
      }

      id = R.id.txtPrenomT;
      EditText txtPrenomT = ViewBindings.findChildViewById(rootView, id);
      if (txtPrenomT == null) {
        break missingId;
      }

      id = R.id.txtSpecialiteT;
      EditText txtSpecialiteT = ViewBindings.findChildViewById(rootView, id);
      if (txtSpecialiteT == null) {
        break missingId;
      }

      id = R.id.txtTelephoneT;
      EditText txtTelephoneT = ViewBindings.findChildViewById(rootView, id);
      if (txtTelephoneT == null) {
        break missingId;
      }

      id = R.id.updloadImage;
      ImageView updloadImage = ViewBindings.findChildViewById(rootView, id);
      if (updloadImage == null) {
        break missingId;
      }

      return new ActivityUpdloadTeacherBinding((NestedScrollView) rootView, bntSaveT, main,
          scrollView, txtAdresseT, txtCategorieT, txtNameT, txtNumeroID, txtPrenomT, txtSpecialiteT,
          txtTelephoneT, updloadImage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
