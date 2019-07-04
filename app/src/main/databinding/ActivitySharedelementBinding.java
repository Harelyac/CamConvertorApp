package com.lgvalle.material_animations.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.lgvalle.material_animations.Sample;

public abstract class ActivitySharedelementBinding extends ViewDataBinding {
  @NonNull
  public final FrameLayout sample2Content;

  @NonNull
  public final TextView title;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected Sample mSharedSample;

  protected ActivitySharedelementBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, FrameLayout sample2Content, TextView title, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.sample2Content = sample2Content;
    this.title = title;
    this.toolbar = toolbar;
  }

  public abstract void setSharedSample(@Nullable Sample sharedSample);

  @Nullable
  public Sample getSharedSample() {
    return mSharedSample;
  }

  @NonNull
  public static ActivitySharedelementBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivitySharedelementBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivitySharedelementBinding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_sharedelement, root, attachToRoot, component);
  }

  @NonNull
  public static ActivitySharedelementBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivitySharedelementBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivitySharedelementBinding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_sharedelement, null, false, component);
  }

  public static ActivitySharedelementBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivitySharedelementBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivitySharedelementBinding)bind(component, view, com.lgvalle.material_animations.R.layout.activity_sharedelement);
  }
}
