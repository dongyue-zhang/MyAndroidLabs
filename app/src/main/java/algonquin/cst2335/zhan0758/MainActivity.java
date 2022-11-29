package algonquin.cst2335.zhan0758;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.zhan0758.databinding.ActivityMainBinding;

/**
 * Main page of the app that contains a password validation program
 * @author Dongyue Zhang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected RequestQueue queue = null;
    Bitmap image;
    String url = null;
    String imgUrl = "https://openweathermap.org/img/w/";
//    /** Holds the text at the centre of the screen*/
//    TextView passwordMessage = null;
//    /** Holds the password input from user*/
//    EditText passwordFiled = null;
//    /** Holds the button that checks the input password when clicked*/
//    Button loginButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getForecastButton.setOnClickListener(click -> {
            cityName = binding.cityEditView.getText().toString();

            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cityName, "UTF-8") + "&appid=69b4a40df6f3b050bb7a22edfd16a495&units=metric";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, (response) -> {
                    try {
                        JSONObject coord = response.getJSONObject("coord");
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject position0 = weatherArray.getJSONObject(0);
                        String description = position0.getString("description");
                        String iconName = position0.getString("icon");
                        JSONObject mainObject = response.getJSONObject("main");
                        double current = mainObject.getDouble("temp");
                        double min = mainObject.getDouble("temp_min");
                        double max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");

                        String pathname = getFilesDir() + "/" + iconName + ".png";
                        File file = new File(pathname);
                        if (file.exists()) {
                            image = BitmapFactory.decodeFile(pathname);
                        } else {
                            ImageRequest imgReq = new ImageRequest(imgUrl + iconName + ".png", new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    image = bitmap;
                                    FileOutputStream fOut = null;
                                    try {
                                        fOut = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                        fOut.flush();
                                        fOut.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                            });

                            queue.add(imgReq);
                        }

                        runOnUiThread(() -> {

                            binding.tempText.setText("The current temperature is " + current);
                            binding.tempText.setVisibility(View.VISIBLE);

                            binding.minText.setText("The min temperature is " + min);
                            binding.minText.setVisibility(View.VISIBLE);

                            binding.maxText.setText("The max temperature is " + max);
                            binding.maxText.setVisibility(View.VISIBLE);

                            binding.humidityText.setText("The humidity is " + humidity + "%");
                            binding.humidityText.setVisibility(View.VISIBLE);

                            binding.icon.setImageBitmap(image);
                            binding.icon.setVisibility(View.VISIBLE);

                            binding.descriptionText.setText(description);
                            binding.descriptionText.setVisibility(View.VISIBLE);

                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, (error) -> { });

                queue.add(request);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });


//        setContentView(R.layout.activity_main);
//        passwordMessage = findViewById(R.id.password_message);
//        passwordFiled = findViewById(R.id.password_filed);
//        loginButton = findViewById(R.id.login_button);
//
//        loginButton.setOnClickListener( clk -> {
//            String password = passwordFiled.getText().toString();
//
//            if (checkPasswordComplexity( password )) {
//                passwordMessage.setText("Your password meets the requirements");
//            } else {
//                passwordMessage.setText("You shall not pass");
//            }
//        });
    }

    /**
     * Validates given password for at least one uppercase, one lowercase, one number, and one special character.
     * Gives out a message if it doesn't meet a certain requirement
     * @param password password that is grabbed from the EditText
     * @return ture if password meets the requirements; false if not
     */
    boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = false;
        foundLowerCase = false;
        foundNumber = false;
        foundSpecial = false;
        char[] chars = password.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                foundNumber = true;
            }
            if (Character.isUpperCase(chars[i])) {
                foundUpperCase = true;
            }

            if (Character.isLowerCase(chars[i])) {
                foundLowerCase = true;
            }
            if (isSpecialCharacter(chars[i])) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            Toast.makeText(this, "Password must contain at lest one uppercase!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundLowerCase) {
            Toast.makeText(this, "Password must contain at lest one lowercase!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundNumber) {
            Toast.makeText(this, "Password must contain at lest one number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!foundSpecial) {
            Toast.makeText(this, "Password must contain at lest one special character!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Checks if a given character is a special character
     * @param c a character
     * @return true if it's a special character; false if not
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case '*':
            case '!':
            case '%':
            case '^':
            case '@':
            case '$':
            case '&':
                return true;
            default:
                return false;
        }
    }
}