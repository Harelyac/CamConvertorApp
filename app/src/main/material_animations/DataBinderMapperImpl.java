package com.lgvalle.material_animations;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.lgvalle.material_animations.databinding.ActivityAnimations1BindingImpl;
import com.lgvalle.material_animations.databinding.ActivityAnimations2BindingImpl;
import com.lgvalle.material_animations.databinding.ActivityRevealBindingImpl;
import com.lgvalle.material_animations.databinding.ActivitySharedelementBindingImpl;
import com.lgvalle.material_animations.databinding.ActivityTransition1BindingImpl;
import com.lgvalle.material_animations.databinding.ActivityTransition2BindingImpl;
import com.lgvalle.material_animations.databinding.ActivityTransition3BindingImpl;
import com.lgvalle.material_animations.databinding.RowSampleBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYANIMATIONS1 = 1;

  private static final int LAYOUT_ACTIVITYANIMATIONS2 = 2;

  private static final int LAYOUT_ACTIVITYREVEAL = 3;

  private static final int LAYOUT_ACTIVITYSHAREDELEMENT = 4;

  private static final int LAYOUT_ACTIVITYTRANSITION1 = 5;

  private static final int LAYOUT_ACTIVITYTRANSITION2 = 6;

  private static final int LAYOUT_ACTIVITYTRANSITION3 = 7;

  private static final int LAYOUT_ROWSAMPLE = 8;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(8);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.activity_animations1, LAYOUT_ACTIVITYANIMATIONS1);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.activity_animations2, LAYOUT_ACTIVITYANIMATIONS2);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.activity_reveal, LAYOUT_ACTIVITYREVEAL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.activity_sharedelement, LAYOUT_ACTIVITYSHAREDELEMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.activity_transition1, LAYOUT_ACTIVITYTRANSITION1);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.activity_transition2, LAYOUT_ACTIVITYTRANSITION2);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.activity_transition3, LAYOUT_ACTIVITYTRANSITION3);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lgvalle.material_animations.R.layout.row_sample, LAYOUT_ROWSAMPLE);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYANIMATIONS1: {
          if ("layout/activity_animations1_0".equals(tag)) {
            return new ActivityAnimations1BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_animations1 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYANIMATIONS2: {
          if ("layout/activity_animations2_0".equals(tag)) {
            return new ActivityAnimations2BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_animations2 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYREVEAL: {
          if ("layout/activity_reveal_0".equals(tag)) {
            return new ActivityRevealBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_reveal is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSHAREDELEMENT: {
          if ("layout/activity_sharedelement_0".equals(tag)) {
            return new ActivitySharedelementBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_sharedelement is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYTRANSITION1: {
          if ("layout/activity_transition1_0".equals(tag)) {
            return new ActivityTransition1BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_transition1 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYTRANSITION2: {
          if ("layout/activity_transition2_0".equals(tag)) {
            return new ActivityTransition2BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_transition2 is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYTRANSITION3: {
          if ("layout/activity_transition3_0".equals(tag)) {
            return new ActivityTransition3BindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_transition3 is invalid. Received: " + tag);
        }
        case  LAYOUT_ROWSAMPLE: {
          if ("layout/row_sample_0".equals(tag)) {
            return new RowSampleBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for row_sample is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(9);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "transition2Sample");
      sKeys.put(2, "reveal1Sample");
      sKeys.put(3, "transition3Sample");
      sKeys.put(4, "transition1Sample");
      sKeys.put(5, "sharedSample");
      sKeys.put(6, "sample");
      sKeys.put(7, "animationsSample");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(8);

    static {
      sKeys.put("layout/activity_animations1_0", com.lgvalle.material_animations.R.layout.activity_animations1);
      sKeys.put("layout/activity_animations2_0", com.lgvalle.material_animations.R.layout.activity_animations2);
      sKeys.put("layout/activity_reveal_0", com.lgvalle.material_animations.R.layout.activity_reveal);
      sKeys.put("layout/activity_sharedelement_0", com.lgvalle.material_animations.R.layout.activity_sharedelement);
      sKeys.put("layout/activity_transition1_0", com.lgvalle.material_animations.R.layout.activity_transition1);
      sKeys.put("layout/activity_transition2_0", com.lgvalle.material_animations.R.layout.activity_transition2);
      sKeys.put("layout/activity_transition3_0", com.lgvalle.material_animations.R.layout.activity_transition3);
      sKeys.put("layout/row_sample_0", com.lgvalle.material_animations.R.layout.row_sample);
    }
  }
}
