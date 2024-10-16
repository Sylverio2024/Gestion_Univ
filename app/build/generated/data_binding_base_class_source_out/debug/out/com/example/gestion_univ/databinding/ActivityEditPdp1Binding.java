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

public final class ActivityEditPdp1Binding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView imageLRG1;

  @NonNull
  public final ImageView imgDownload1;

  @NonNull
  public final ImageView imgShare1;

  @NonNull
  public final ActivityFn5ContentBinding include2;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextView textView11;

  private ActivityEditPdp1Binding(@NonNull ConstraintLayout rootView, @NonNull ImageView imageLRG1,
      @NonNull ImageView imgDownload1, @NonNull ImageView imgShare1,
      @NonNull ActivityFn5ContentBinding include2, @NonNull ConstraintLayout main,
      @NonNull TextView textView11) {
    this.rootView = rootView;
    this.imageLRG1 = imageLRG1;
    this.imgDownload1 = imgDownload1;
    this.imgShare1 = imgShare1;
    this.include2 = include2;
    this.main = main;
    this.textView11 = textView11;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEditPdp1Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEditPdp1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_edit_pdp1, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEditPdp1Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageLRG1;
      ImageView imageLRG1 = ViewBindings.findChildViewById(rootView, id);
      if (imageLRG1 == null) {
        break missingId;
      }

      id = R.id.imgDownload1;
      ImageView imgDownload1 = ViewBindings.findChildViewById(rootView, id);
      if (imgDownload1 == null) {
        break missingId;
      }

      id = R.id.imgShare1;
      ImageView imgShare1 = ViewBindings.findChildViewById(rootView, id);
      if (imgShare1 == null) {
        break missingId;
      }

      id = R.id.include2;
      View include2 = ViewBindings.findChildViewById(rootView, id);
      if (include2 == null) {
        break missingId;
      }
      ActivityFn5ContentBinding binding_include2 = ActivityFn5ContentBinding.bind(include2);

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.textView11;
      TextView textView11 = ViewBindings.findChildViewById(rootView, id);
      if (textView11 == null) {
        break missingId;
      }

      return new ActivityEditPdp1Binding((ConstraintLayout) rootView, imageLRG1, imgDownload1,
          imgShare1, binding_include2, main, textView11);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
