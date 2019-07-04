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

public abstract class ActivityTransition2Binding extends ViewDataBinding {
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
  protected Sample mTransition2Sample;

  protected ActivityTransition2Binding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView activityTitle, Button exitButton, ImageView squareRed,
      TextView title, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.activityTitle = activityTitle;
    this.exitButton = exitButton;
    this.squareRed = squareRed;
    this.title = title;
    this.toolbar = toolbar;
  }

  public abstract void setTransition2Sample(@Nullable Sample transition2Sample);

  @Nullable
  public Sample getTransition2Sample() {
    return mTransition2Sample;
  }

  @NonNull
  public static ActivityTransition2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityTransition2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityTransition2Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_transition2, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityTransition2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityTransition2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityTransition2Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_transition2, null, false, component);
  }

  public static ActivityTransition2Binding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityTransition2Binding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityTransition2Binding)bind(component, view, com.lgvalle.material_animations.R.layout.activity_transition2);
  }
}
