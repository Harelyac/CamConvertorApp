package com.lgvalle.material_animations.databinding;
import com.lgvalle.material_animations.R;
import com.lgvalle.material_animations.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityTransition2BindingImpl extends ActivityTransition2Binding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 3);
        sViewsWithIds.put(R.id.activity_title, 4);
        sViewsWithIds.put(R.id.exit_button, 5);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityTransition2BindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private ActivityTransition2BindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[4]
            , (android.widget.Button) bindings[5]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (androidx.appcompat.widget.Toolbar) bindings[3]
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.squareRed.setTag(null);
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
        if (BR.transition2Sample == variableId) {
            setTransition2Sample((com.lgvalle.material_animations.Sample) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setTransition2Sample(@Nullable com.lgvalle.material_animations.Sample Transition2Sample) {
        this.mTransition2Sample = Transition2Sample;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.transition2Sample);
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
        java.lang.String transition2SampleName = null;
        int transition2SampleColor = 0;
        com.lgvalle.material_animations.Sample transition2Sample = mTransition2Sample;

        if ((dirtyFlags & 0x3L) != 0) {



                if (transition2Sample != null) {
                    // read transition2Sample.name
                    transition2SampleName = transition2Sample.getName();
                    // read transition2Sample.color
                    transition2SampleColor = transition2Sample.getColor();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            com.lgvalle.material_animations.Sample.setColorTint(this.squareRed, transition2SampleColor);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.title, transition2SampleName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): transition2Sample
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}