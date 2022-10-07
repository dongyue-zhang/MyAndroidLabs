package algonquin.cst2335.zhan0758;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SecondActivity extends AppCompatActivity {
    TextView title;
    Button callNumberButton, changePictureButton;
    EditText editTextPhone;
    ImageView profileImage;

    String phoneNumber;

    ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap thumbnail = data.getParcelableExtra("data");
//                        profileImage.setImageBitmap(thumbnail);

                        FileOutputStream fOut = null;

                        try {
                            fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromPrevious = getIntent();
//        String emailAddress = fromPrevious.getStringExtra("emailAddress");
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        String password = fromPrevious.getStringExtra("password");
        phoneNumber = prefs.getString("PhoneNumber", "");
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPhone.setText(phoneNumber);

        title = findViewById(R.id.title);
        title.setText("Welcome back " + emailAddress);

        callNumberButton = findViewById(R.id.callNumberButton);
        callNumberButton.setOnClickListener(click -> {
            editTextPhone = findViewById(R.id.editTextPhone);

            phoneNumber = editTextPhone.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        changePictureButton = findViewById(R.id.changePictureButton);
        profileImage = findViewById(R.id.profileImage);

        changePictureButton.setOnClickListener( click -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });

        File file = new File(getFilesDir(),"Picture.png" );
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(getFilesDir() +"/Picture.png");
            profileImage.setImageBitmap(theImage);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editTextPhone = findViewById(R.id.editTextPhone);
        editor.putString("PhoneNumber", editTextPhone.getText().toString());
        editor.apply();


    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "In Resume() - Responding to user input");
        File file = new File(getFilesDir(),"Picture.png" );
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(getFilesDir() +"/Picture.png");
            profileImage.setImageBitmap(theImage);
        }
    }
}