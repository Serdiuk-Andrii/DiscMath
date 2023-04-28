package com.example.discmath.ui.util.keyboard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.discmath.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogicKeyboard extends LinearLayout implements View.OnClickListener {

    // constructors
    public LogicKeyboard(Context context) {
        this(context, null, 0);
    }

    public LogicKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogicKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    Map<View, String> keyValues = new HashMap<>();

    // Our communication link to the EditText
    InputConnection inputConnection;

    private void init(Context context, AttributeSet attrs) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.logic_keyboard, this, true);
        List<View> result = getAllViewsOfGivenType(this, Button.class);
        result.forEach(view -> {
            view.setOnClickListener(this);
            keyValues.put(view, ((Button) view).getText().toString() );
        });
    }

    @Override
    public void onClick(View v) {

        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return;

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v.getId() == R.id.button_delete) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        } else {
            String value = keyValues.get(v);
            inputConnection.commitText(value, 1);
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }

    private static List<View> getAllViewsOfGivenType(ViewGroup view, Class filterClass) {
        List<View> result = new ArrayList<>();
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);
            if (child instanceof ViewGroup) {
                result.addAll(getAllViewsOfGivenType((ViewGroup) child, filterClass));
            } else if (filterClass.isInstance(child)) {
                result.add(child);
            }
        }
        return result;
    }

}
