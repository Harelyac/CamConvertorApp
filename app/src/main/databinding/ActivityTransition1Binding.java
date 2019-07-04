package com.lgvalle.material_animations.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.lgvalle.material_animations.Sample;

public abstract class ActivityTransition1Binding extends ViewDataBinding {
  @NonNull
  public final Button sample1Button1;

  @NonNull
  public final Button sample1Button2;

  @NonNull
  public final Button sample1Button3;

  @NonNull
  public final Button sample1Button4;

  @NonNull
  public final Button sample1Button5;

  @NonNull
  public final Button sample1Button6;

  @NonNull
  public final TextView title;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected Sample mTransition1Sample;

  protected ActivityTransition1Binding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button sample1Button1, Button sample1Button2, Button sample1Button3,
      Button sample1Button4, Button sample1Button5, Button sample1Button6, TextView title,
      Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.sample1Button1 = sample1Button1;
    this.sample1Button2 = sample1Button2;
    this.sample1Button3 = sample1Button3;
    this.sample1Button4 = sample1Button4;
    this.sample1Button5 = sample1Button5;
    this.sample1Button6 = sample1Button6;
    this.title = title;
    this.toolbar = toolbar;
  }

  public abstract void setTransition1Sample(@Nullable Sample transition1Sample);

  @Nullable
  public Sample getTransition1Sample() {
    return mTransition1Sample;
  }

  @NonNull
  public static ActivityTransition1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityTransition1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityTransition1Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_transition1, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityTransition1Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityTransition1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityTransition1Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_transition1, null, false, component);
  }

  public static ActivityTransition1Binding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityTransition1Binding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityTransition1Binding)bind(component, view, com.lgvalle.material_animations.R.layout.activity_transition1);
  }
}
