package com.easyapps.singerpro.presentation.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.easyapps.singerpro.presentation.helper.ActivityUtils;
import com.easyapps.singerpro.R;

/**
 * A {@link android.preference.Preference} that displays a number picker as a dialog.
 * Copyright rhmeeuwisse (https://github.com/rhmeeuwisse)
 */
public class NumberPickerPreference extends DialogPreference {

    private static final int DEFAULT_MAX_VALUE = 50;
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final boolean DEFAULT_WRAP_SELECTOR_WHEEL = true;

    private int minValue;
    private int maxValue;
    private final boolean wrapSelectorWheel;

    private NumberPicker picker;
    private int value;

    public NumberPickerPreference(Context context, int minValue, int maxValue){
        this(context, null, android.R.attr.dialogPreferenceStyle);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public NumberPickerPreference(Context context){
        this(context, null, android.R.attr.dialogPreferenceStyle);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    private NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);
        minValue = a.getInteger(R.styleable.NumberPickerPreference_minValue, DEFAULT_MIN_VALUE);
        maxValue = a.getInteger(R.styleable.NumberPickerPreference_maxValue, DEFAULT_MAX_VALUE);
        wrapSelectorWheel = a.getBoolean(R.styleable.NumberPickerPreference_wrapSelectorWheel, DEFAULT_WRAP_SELECTOR_WHEEL);
        a.recycle();

        Activity parent = ((Activity)context);
        String fileName = ActivityUtils.getLyricFileNameParameter(parent.getIntent());
        setKey(getKey() + (fileName == null ? "" : fileName));
    }

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setWrapSelectorWheel(wrapSelectorWheel);
        picker.setValue(getValue());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, minValue);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(minValue) : (Integer) defaultValue);
    }

    public void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    public int getValue() {
        return this.value;
    }
}