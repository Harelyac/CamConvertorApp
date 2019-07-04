package com.lgvalle.material_animations.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.lgvalle.material_animations.Sample;

public abstract class RowSampleBinding extends ViewDataBinding {
  @NonNull
  public final ImageView sampleIcon;

  @NonNull
  public final LinearLayout sampleLayout;

  @NonNull
  public final TextView sampleName;

  @Bindable
  protected Sample mSample;

  protected RowSampleBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView sampleIcon, LinearLayout sampleLayout, TextView sampleName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.sampleIcon = sampleIcon;
    this.sampleLayout = sampleLayout;
    this.sampleName = sampleName;
  }

  public abstract void setSample(@Nullable Sample sample);

  @Nullable
  public Sample getSample() {
    return mSample;
  }

  @NonNull
  public static RowSampleBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static RowSampleBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<RowSampleBinding>inflate(inflater, com.lgvalle.material_animations.R.layout.row_sample, root, attachToRoot, component);
  }

  @NonNull
  public static RowSampleBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static RowSampleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<RowSampleBinding>inflate(inflater, com.lgvalle.material_animations.R.layout.row_sample, null, false, component);
  }

  public static RowSampleBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static RowSampleBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (RowSampleBinding)bind(component, view, com.lgvalle.material_animations.R.layout.row_sample);
  }
}
