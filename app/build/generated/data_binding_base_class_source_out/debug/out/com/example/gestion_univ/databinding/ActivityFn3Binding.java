// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFn3Binding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView backFn2;

  @NonNull
  public final Button btnSingin;

  @NonNull
  public final ConstraintLayout constraintLayout3;

  @NonNull
  public final TextInputEditText eTextEmail;

  @NonNull
  public final TextInputEditText eTextPassword;

  @NonNull
  public final TextInputLayout layoutEmail;

  @NonNull
  public final TextInputLayout layoutPassword;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final ProgressBar proBar;

  @NonNull
  public final TextView textForgot;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView6;

  private ActivityFn3Binding(@NonNull ConstraintLayout rootView, @NonNull ImageView backFn2,
      @NonNull Button btnSingin, @NonNull ConstraintLayout constraintLayout3,
      @NonNull TextInputEditText eTextEmail, @NonNull TextInputEditText eTextPassword,
      @NonNull TextInputLayout layoutEmail, @NonNull TextInputLayout layoutPassword,
      @NonNull ConstraintLayout main, @NonNull ProgressBar proBar, @NonNull TextView textForgot,
      @NonNull TextView textView3, @NonNull TextView textView6) {
    this.rootView = rootView;
    this.backFn2 = backFn2;
    this.btnSingin = btnSingin;
    this.constraintLayout3 = constraintLayout3;
    this.eTextEmail = eTextEmail;
    this.eTextPassword = eTextPassword;
    this.layoutEmail = layoutEmail;
    this.layoutPassword = layoutPassword;
    this.main = main;
    this.proBar = proBar;
    this.textForgot = textForgot;
    this.textView3 = textView3;
    this.textView6 = textView6;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFn3Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFn3Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_fn3, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFn3Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.back_fn2;
      ImageView backFn2 = ViewBindings.findChildViewById(rootView, id);
      if (backFn2 == null) {
        break missingId;
      }

      id = R.id.btnSingin;
      Button btnSingin = ViewBindings.findChildViewById(rootView, id);
      if (btnSingin == null) {
        break missingId;
      }

      id = R.id.constraintLayout3;
      ConstraintLayout constraintLayout3 = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout3 == null) {
        break missingId;
      }

      id = R.id.eTextEmail;
      TextInputEditText eTextEmail = ViewBindings.findChildViewById(rootView, id);
      if (eTextEmail == null) {
        break missingId;
      }

      id = R.id.eTextPassword;
      TextInputEditText eTextPassword = ViewBindings.findChildViewById(rootView, id);
      if (eTextPassword == null) {
        break missingId;
      }

      id = R.id.layoutEmail;
      TextInputLayout layoutEmail = ViewBindings.findChildViewById(rootView, id);
      if (layoutEmail == null) {
        break missingId;
      }

      id = R.id.layoutPassword;
      TextInputLayout layoutPassword = ViewBindings.findChildViewById(rootView, id);
      if (layoutPassword == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.proBar;
      ProgressBar proBar = ViewBindings.findChildViewById(rootView, id);
      if (proBar == null) {
        break missingId;
      }

      id = R.id.textForgot;
      TextView textForgot = ViewBindings.findChildViewById(rootView, id);
      if (textForgot == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.textView6;
      TextView textView6 = ViewBindings.findChildViewById(rootView, id);
      if (textView6 == null) {
        break missingId;
      }

      return new ActivityFn3Binding((ConstraintLayout) rootView, backFn2, btnSingin,
          constraintLayout3, eTextEmail, eTextPassword, layoutEmail, layoutPassword, main, proBar,
          textForgot, textView3, textView6);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
