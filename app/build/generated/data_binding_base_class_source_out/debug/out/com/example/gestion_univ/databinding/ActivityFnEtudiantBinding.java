// Generated by view binder compiler. Do not edit!
package com.example.gestion_univ.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gestion_univ.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFnEtudiantBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton fab1;

  @NonNull
  public final ActivityFn5ContentBinding include2;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final RecyclerView recyclerView1;

  @NonNull
  public final SearchView search1;

  @NonNull
  public final SwipeRefreshLayout swipeRefreshLayout1;

  private ActivityFnEtudiantBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton fab1, @NonNull ActivityFn5ContentBinding include2,
      @NonNull ConstraintLayout main, @NonNull RecyclerView recyclerView1,
      @NonNull SearchView search1, @NonNull SwipeRefreshLayout swipeRefreshLayout1) {
    this.rootView = rootView;
    this.fab1 = fab1;
    this.include2 = include2;
    this.main = main;
    this.recyclerView1 = recyclerView1;
    this.search1 = search1;
    this.swipeRefreshLayout1 = swipeRefreshLayout1;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFnEtudiantBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFnEtudiantBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_fn_etudiant, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFnEtudiantBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fab1;
      FloatingActionButton fab1 = ViewBindings.findChildViewById(rootView, id);
      if (fab1 == null) {
        break missingId;
      }

      id = R.id.include2;
      View include2 = ViewBindings.findChildViewById(rootView, id);
      if (include2 == null) {
        break missingId;
      }
      ActivityFn5ContentBinding binding_include2 = ActivityFn5ContentBinding.bind(include2);

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.recyclerView1;
      RecyclerView recyclerView1 = ViewBindings.findChildViewById(rootView, id);
      if (recyclerView1 == null) {
        break missingId;
      }

      id = R.id.search1;
      SearchView search1 = ViewBindings.findChildViewById(rootView, id);
      if (search1 == null) {
        break missingId;
      }

      id = R.id.swipeRefreshLayout1;
      SwipeRefreshLayout swipeRefreshLayout1 = ViewBindings.findChildViewById(rootView, id);
      if (swipeRefreshLayout1 == null) {
        break missingId;
      }

      return new ActivityFnEtudiantBinding((ConstraintLayout) rootView, fab1, binding_include2,
          main, recyclerView1, search1, swipeRefreshLayout1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
