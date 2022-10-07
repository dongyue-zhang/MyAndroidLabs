package algonquin.cst2335.zhan0758;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    Switch sw;
    Button loginButton;
    EditText emailEditText, passwordEditText;
    String emailAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        imgView = findViewById(R.id.flagView);
        sw = findViewById(R.id.spinSwitch);
        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailAddress = prefs.getString("LoginName", "");
        emailEditText.setText(emailAddress);

        sw.setOnCheckedChangeListener( (btn, isChecked) -> {
            if (isChecked) {
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(5000);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setInterpolator(new LinearInterpolator());

                imgView.startAnimation(rotate);
            } else {
                imgView.clearAnimation();
            }
        });

        Log.w("MainActivity", "In onCreate() - Loading Widgets");
        loginButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            SharedPreferences.Editor editor = prefs.edit();
//            nextPage.putExtra("emailAddress", emailEditText.getText().toString());
            nextPage.putExtra("password", passwordEditText.getText().toString());
            editor.putString("LoginName", emailEditText.getText().toString());
            editor.apply();
            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity", "In onStart() - Visible on screen");
    }

    @Override
    protected void onStop() {
        super.onStop();
    Log.w("MainActivity", "In onStop() - No longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "In onDestroy() - Any memory used by the application is freed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onPause() - No longer responds to user input");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "In Resume() - Responding to user input");
    }


}