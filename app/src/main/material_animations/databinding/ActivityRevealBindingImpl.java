package com.lgvalle.material_animations.databinding;
import com.lgvalle.material_animations.R;
import com.lgvalle.material_animations.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityRevealBindingImpl extends ActivityRevealBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 3);
        sViewsWithIds.put(R.id.reveal_root, 4);
        sViewsWithIds.put(R.id.sample_body, 5);
        sViewsWithIds.put(R.id.square_green, 6);
        sViewsWithIds.put(R.id.square_red, 7);
        sViewsWithIds.put(R.id.square_blue, 8);
        sViewsWithIds.put(R.id.square_yellow, 9);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityRevealBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private ActivityRevealBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[4]
            , (android.widget.TextView) bindings[5]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.TextView) bindings[2]
            , (androidx.appcompat.widget.Toolbar) bindings[3]
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.sharedTarget.setTag(null);
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
        if (BR.reveal1Sample == variableId) {
            setReveal1Sample((com.lgvalle.material_animations.Sample) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setReveal1Sample(@Nullable com.lgvalle.material_animations.Sample Reveal1Sample) {
        this.mReveal1Sample = Reveal1Sample;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.reveal1Sample);
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
        java.lang.String reveal1SampleName = null;
        com.lgvalle.material_animations.Sample reveal1Sample = mReveal1Sample;
        int reveal1SampleColor = 0;

        if ((dirtyFlags & 0x3L) != 0) {



                if (reveal1Sample != null) {
                    // read reveal1Sample.name
                    reveal1SampleName = reveal1Sample.getName();
                    // read reveal1Sample.color
                    reveal1SampleColor = reveal1Sample.getColor();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            com.lgvalle.material_animations.Sample.setColorTint(this.sharedTarget, reveal1SampleColor);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.title, reveal1SampleName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): reveal1Sample
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}