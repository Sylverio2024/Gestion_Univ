// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEditProfil1Binding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnSave1;

  @NonNull
  public final EditText editName1;

  @NonNull
  public final ActivityFn5ContentBinding include2;

  @NonNull
  public final ConstraintLayout main;

  private ActivityEditProfil1Binding(@NonNull ConstraintLayout rootView, @NonNull Button btnSave1,
      @NonNull EditText editName1, @NonNull ActivityFn5ContentBinding include2,
      @NonNull ConstraintLayout main) {
    this.rootView = rootView;
    this.btnSave1 = btnSave1;
    this.editName1 = editName1;
    this.include2 = include2;
    this.main = main;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEditProfil1Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEditProfil1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_edit_profil1, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEditProfil1Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnSave1;
      Button btnSave1 = ViewBindings.findChildViewById(rootView, id);
      if (btnSave1 == null) {
        break missingId;
      }

      id = R.id.editName1;
      EditText editName1 = ViewBindings.findChildViewById(rootView, id);
      if (editName1 == null) {
        break missingId;
      }

      id = R.id.include2;
      View include2 = ViewBindings.findChildViewById(rootView, id);
      if (include2 == null) {
        break missingId;
      }
      ActivityFn5ContentBinding binding_include2 = ActivityFn5ContentBinding.bind(include2);

      ConstraintLayout main = (ConstraintLayout) rootView;

      return new ActivityEditProfil1Binding((ConstraintLayout) rootView, btnSave1, editName1,
          binding_include2, main);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
