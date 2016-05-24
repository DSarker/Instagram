package com.industries.sarker.instagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private EditText usernameField;
    private EditText passwordField;
    private Button signButton;
    private TextView toggleText;
    private boolean newUserMode = false;
    private RelativeLayout relativeLayout;
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        usernameField = (EditText) findViewById(R.id.username_edittext);
        usernameField.setOnKeyListener(this);

        passwordField = (EditText) findViewById(R.id.password_edittext);
        passwordField.setOnKeyListener(this);

        signButton = (Button) findViewById(R.id.sign_button);
        signButton.setOnClickListener(this);

        toggleText = (TextView) findViewById(R.id.toggle_textview);
        toggleText.setOnClickListener(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        relativeLayout.setOnKeyListener(this);

        logoImageView = (ImageView) findViewById(R.id.logo);
        logoImageView.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            showUserList();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_textview:
                if (newUserMode) {
                    toggleText.setText("Sign Up");
                    signButton.setText("LOG IN");
                    newUserMode = false;
                } else {
                    toggleText.setText("Log In");
                    signButton.setText("SIGN UP");
                    newUserMode = true;
                }
                break;

            case R.id.sign_button:
                final ProgressDialog dlg = new ProgressDialog(MainActivity.this);

                if (newUserMode) {
                    dlg.setMessage("Signing up. Please wait.");
                    dlg.show();

                    ParseUser user = new ParseUser();
                    user.setUsername(String.valueOf(usernameField.getText()).trim());
                    user.setPassword(String.valueOf(passwordField.getText()).trim());

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.v("**", "Sign up successful");
                                showUserList();

                            } else {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            dlg.dismiss();
                        }
                    });

                } else {
                    dlg.setMessage("Logging in. Please wait.");
                    dlg.show();

                    ParseUser.logInInBackground(String.valueOf(usernameField.getText()).trim(), String.valueOf(passwordField.getText()).trim(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Log.v("**", "Log in successful");
                                showUserList();

                            } else {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            dlg.dismiss();
                        }
                    });
                }
                break;

            case R.id.logo:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;

            case R.id.relative_layout:
                View view2 = this.getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (v != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            signButton.performClick();

        } else {
            View view = this.getCurrentFocus();
            if (view == null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        return false;
    }

    public void showUserList() {
        if(ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, UserList.class);
            startActivity(intent);
            finish();
        }
    }
}
