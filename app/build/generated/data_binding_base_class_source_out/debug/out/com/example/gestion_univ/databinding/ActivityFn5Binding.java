// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import com.google.android.material.navigation.NavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFn5Binding implements ViewBinding {
  @NonNull
  private final DrawerLayout rootView;

  @NonNull
  public final DrawerLayout main;

  @NonNull
  public final NavigationView navigationView;

  private ActivityFn5Binding(@NonNull DrawerLayout rootView, @NonNull DrawerLayout main,
      @NonNull NavigationView navigationView) {
    this.rootView = rootView;
    this.main = main;
    this.navigationView = navigationView;
  }

  @Override
  @NonNull
  public DrawerLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFn5Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFn5Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_fn5, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFn5Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      DrawerLayout main = (DrawerLayout) rootView;

      id = R.id.navigationView;
      NavigationView navigationView = ViewBindings.findChildViewById(rootView, id);
      if (navigationView == null) {
        break missingId;
      }

      return new ActivityFn5Binding((DrawerLayout) rootView, main, navigationView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
