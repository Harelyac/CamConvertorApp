package com.lgvalle.material_animations.databinding;
import com.lgvalle.material_animations.R;
import com.lgvalle.material_animations.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityAnimations1BindingImpl extends ActivityAnimations1Binding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 3);
        sViewsWithIds.put(R.id.sample_title, 4);
        sViewsWithIds.put(R.id.sample3_button1, 5);
        sViewsWithIds.put(R.id.sample3_button2, 6);
        sViewsWithIds.put(R.id.sample3_button3, 7);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityAnimations1BindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds));
    }
    private ActivityAnimations1BindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.Button) bindings[5]
            , (android.widget.Button) bindings[6]
            , (android.widget.Button) bindings[7]
            , (android.widget.LinearLayout) bindings[0]
            , (android.widget.TextView) bindings[4]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (androidx.appcompat.widget.Toolbar) bindings[3]
            );
        this.sample3Root.setTag(null);
        this.squareGreen.setTag(null);
        this.title.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.animationsSample == variableId) {
            setAnimationsSample((com.lgvalle.material_animations.Sample) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setAnimationsSample(@Nullable com.lgvalle.material_animations.Sample AnimationsSample) {
        this.mAnimationsSample = AnimationsSample;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.animationsSample);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.lgvalle.material_animations.Sample animationsSample = mAnimationsSample;
        int animationsSampleColor = 0;
        java.lang.String animationsSampleName = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (animationsSample != null) {
                    // read animationsSample.color
                    animationsSampleColor = animationsSample.getColor();
                    // read animationsSample.name
                    animationsSampleName = animationsSample.getName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            com.lgvalle.material_animations.Sample.setColorTint(this.squareGreen, animationsSampleColor);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.title, animationsSampleName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): animationsSample
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}