package com.lgvalle.material_animations.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.lgvalle.material_animations.Sample;

public abstract class ActivityTransition3Binding extends ViewDataBinding {
  @NonNull
  public final TextView activityTitle;

  @NonNull
  public final Button exitButton;

  @NonNull
  public final ImageView squareRed;

  @NonNull
  public final TextView title;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected Sample mTransition3Sample;

  protected ActivityTransition3Binding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView activityTitle, Button exitButton, ImageView squareRed,
      TextView title, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.activityTitle = activityTitle;
    this.exitButton = exitButton;
    this.squareRed = squareRed;
    this.title = title;
    this.toolbar = toolbar;
  }

  public abstract void setTransition3Sample(@Nullable Sample transition3Sample);

  @Nullable
  public Sample getTransition3Sample() {
    return mTransition3Sample;
  }

  @NonNull
  public static ActivityTransition3Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityTransition3Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityTransition3Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_transition3, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityTransition3Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityTransition3Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityTransition3Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_transition3, null, false, component);
  }

  public static ActivityTransition3Binding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityTransition3Binding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityTransition3Binding)bind(component, view, com.lgvalle.material_animations.R.layout.activity_transition3);
  }
}
