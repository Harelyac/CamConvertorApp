package com.lgvalle.material_animations.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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

public abstract class ActivityAnimations2Binding extends ViewDataBinding {
  @NonNull
  public final LinearLayout buttonsGroup;

  @NonNull
  public final Button sample3Button1;

  @NonNull
  public final Button sample3Button2;

  @NonNull
  public final Button sample3Button3;

  @NonNull
  public final Button sample3Button4;

  @NonNull
  public final LinearLayout sample3Root;

  @NonNull
  public final FrameLayout sceneRoot;

  @NonNull
  public final TextView title;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected Sample mAnimationsSample;

  protected ActivityAnimations2Binding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, LinearLayout buttonsGroup, Button sample3Button1, Button sample3Button2,
      Button sample3Button3, Button sample3Button4, LinearLayout sample3Root, FrameLayout sceneRoot,
      TextView title, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.buttonsGroup = buttonsGroup;
    this.sample3Button1 = sample3Button1;
    this.sample3Button2 = sample3Button2;
    this.sample3Button3 = sample3Button3;
    this.sample3Button4 = sample3Button4;
    this.sample3Root = sample3Root;
    this.sceneRoot = sceneRoot;
    this.title = title;
    this.toolbar = toolbar;
  }

  public abstract void setAnimationsSample(@Nullable Sample animationsSample);

  @Nullable
  public Sample getAnimationsSample() {
    return mAnimationsSample;
  }

  @NonNull
  public static ActivityAnimations2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityAnimations2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityAnimations2Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_animations2, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityAnimations2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityAnimations2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityAnimations2Binding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_animations2, null, false, component);
  }

  public static ActivityAnimations2Binding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityAnimations2Binding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityAnimations2Binding)bind(component, view, com.lgvalle.material_animations.R.layout.activity_animations2);
  }
}
