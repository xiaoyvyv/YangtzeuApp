package com.yangtzeu.ui.view;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.Toolbar;

public interface ChangePassView {
    Toolbar getToolbar();

    TextInputEditText ACC();

    TextInputEditText PassOld();

    TextInputEditText PassNew();

    TextInputEditText PassDone();
}
