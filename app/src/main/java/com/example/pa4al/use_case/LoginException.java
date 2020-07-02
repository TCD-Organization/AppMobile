package com.example.pa4al.use_case;

import android.widget.EditText;

public class LoginException extends Exception {
    private EditText field;

    public LoginException(final String message, final EditText field, final Throwable cause) {
        super(message, cause);
        this.field = field;
    }
    public LoginException(final String message, final EditText field) {
        super(message);
        this.field = field;
    }

    public LoginException(final Throwable cause, final EditText field) {
        super(cause);
        this.field = field;
    }

    public EditText getField() {
        return field;
    }
}
