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

public final class ActivityChoosePdpBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView btnCam;

  @NonNull
  public final ImageView btnGal;

  @NonNull
  public final TextView btnSave;

  @NonNull
  public final ImageView imgChoose;

  @NonNull
  public final ActivityFn5ContentBinding include2;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextView textCam;

  @NonNull
  public final TextView textView15;

  private ActivityChoosePdpBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView btnCam,
      @NonNull ImageView btnGal, @NonNull TextView btnSave, @NonNull ImageView imgChoose,
      @NonNull ActivityFn5ContentBinding include2, @NonNull ConstraintLayout main,
      @NonNull TextView textCam, @NonNull TextView textView15) {
    this.rootView = rootView;
    this.btnCam = btnCam;
    this.btnGal = btnGal;
    this.btnSave = btnSave;
    this.imgChoose = imgChoose;
    this.include2 = include2;
    this.main = main;
    this.textCam = textCam;
    this.textView15 = textView15;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityChoosePdpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityChoosePdpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_choose_pdp, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityChoosePdpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnCam;
      ImageView btnCam = ViewBindings.findChildViewById(rootView, id);
      if (btnCam == null) {
        break missingId;
      }

      id = R.id.btnGal;
      ImageView btnGal = ViewBindings.findChildViewById(rootView, id);
      if (btnGal == null) {
        break missingId;
      }

      id = R.id.btnSave;
      TextView btnSave = ViewBindings.findChildViewById(rootView, id);
      if (btnSave == null) {
        break missingId;
      }

      id = R.id.imgChoose;
      ImageView imgChoose = ViewBindings.findChildViewById(rootView, id);
      if (imgChoose == null) {
        break missingId;
      }

      id = R.id.include2;
      View include2 = ViewBindings.findChildViewById(rootView, id);
      if (include2 == null) {
        break missingId;
      }
      ActivityFn5ContentBinding binding_include2 = ActivityFn5ContentBinding.bind(include2);

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.textCam;
      TextView textCam = ViewBindings.findChildViewById(rootView, id);
      if (textCam == null) {
        break missingId;
      }

      id = R.id.textView15;
      TextView textView15 = ViewBindings.findChildViewById(rootView, id);
      if (textView15 == null) {
        break missingId;
      }

      return new ActivityChoosePdpBinding((ConstraintLayout) rootView, btnCam, btnGal, btnSave,
          imgChoose, binding_include2, main, textCam, textView15);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
