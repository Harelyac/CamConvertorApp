package com.lgvalle.material_animations.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.lgvalle.material_animations.Sample;

public abstract class ActivityAnimations1Binding extends ViewDataBinding {
  @NonNull
  public final Button sample3Button1;

  @NonNull
  public final Button sample3Button2;

  @NonNull
  public final Button sample3Button3;

  @NonNull
  public final LinearLayout sample3Root;

  @NonNull
  public final TextView sampleTitle;

  @NonNull
  public final ImageView squareGreen;

  @NonNull
  public final TextView title;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected Sample mAnimationsSample;

  protected ActivityAnimations1Binding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button sample3Button1, Button sample3Button2, Button sample3Button3,
      LinearLayout sample3Root, TextView sampleTitle, ImageView squareGreen, TextView title,
      Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.sample3Button1 = sample3Button1;
    this.sample3Button2 = sample3Button2;
    this.sample3Button3 = sample3Button3;
    this.sample3Root = sample3Root;
    this.sampleTitle = sampleTitle;
    this.squareGreen = squareGreen;
    this.title = title;
    this.toolbar = toolbar;
  }

  public abstract void setAnimationsSample(@Nullable Sample animationsSample);

  @Nullable
  public Sample getAnimationsSample() {
    return mAnimationsSample;
  }

  @NonNull
  public static ActivityAnimations1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityAnimations1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityAnimations1Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_animations1, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityAnimations1Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityAnimations1Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityAnimations1Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_animations1, null, false, component);
  }

  public static ActivityAnimations1Binding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityAnimations1Binding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityAnimations1Binding)bind(component, view, com.lgvalle.material_animations.R.layout.activity_animations1);
  }
}
