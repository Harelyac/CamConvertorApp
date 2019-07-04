package com.lgvalle.material_animations.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.lgvalle.material_animations.Sample;

public abstract class ActivityRevealBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout revealRoot;

  @NonNull
  public final TextView sampleBody;

  @NonNull
  public final ImageView sharedTarget;

  @NonNull
  public final ImageView squareBlue;

  @NonNull
  public final ImageView squareGreen;

  @NonNull
  public final ImageView squareRed;

  @NonNull
  public final ImageView squareYellow;

  @NonNull
  public final TextView title;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected Sample mReveal1Sample;

  protected ActivityRevealBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, RelativeLayout revealRoot, TextView sampleBody, ImageView sharedTarget,
      ImageView squareBlue, ImageView squareGreen, ImageView squareRed, ImageView squareYellow,
      TextView title, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.revealRoot = revealRoot;
    this.sampleBody = sampleBody;
    this.sharedTarget = sharedTarget;
    this.squareBlue = squareBlue;
    this.squareGreen = squareGreen;
    this.squareRed = squareRed;
    this.squareYellow = squareYellow;
    this.title = title;
    this.toolbar = toolbar;
  }

  public abstract void setReveal1Sample(@Nullable Sample reveal1Sample);

  @Nullable
  public Sample getReveal1Sample() {
    return mReveal1Sample;
  }

  @NonNull
  public static ActivityRevealBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRevealBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRevealBinding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_reveal, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityRevealBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRevealBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRevealBinding>inflate(inflater, com.lgvalle.material_animations.R.layout.activity_reveal, null, false, component);
  }

  public static ActivityRevealBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityRevealBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityRevealBinding)bind(component, view, com.lgvalle.material_animations.R.layout.activity_reveal);
  }
}
