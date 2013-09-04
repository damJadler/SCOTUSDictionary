package com.damjadler;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

//This class lifted wholesale from http://savagelook.com/blog/android/android-quick-tip-edittext-with-done-button-that-closes-the-keyboard
//Used to change keyboard layout to add "Done" button
public class DoneOnEditorActionListener implements OnEditorActionListener {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;	
        }
        return false;
    }
}
