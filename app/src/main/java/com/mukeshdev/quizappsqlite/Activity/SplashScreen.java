package com.mukeshdev.quizappsqlite.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mukeshdev.quizappsqlite.R;
import com.mukeshdev.quizappsqlite.databinding.ActivitySplashScreenBinding;
import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    TextView  tvLogoName;
    ImageView imgLogo;
    private final static int EXIT_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.trasnsition);
        binding.textviewLogoText.setAnimation(animation);
        binding.imgviewLogo.setAnimation(animation);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(3000);

                }catch (Exception e){
                    e.printStackTrace();

                }finally {
                    GotoNextActivity();

                }


            }
        });
        thread.start();

    }



    private void GotoNextActivity() {

        startActivityForResult( new Intent(SplashScreen.this, CategoryActivity.class),EXIT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXIT_CODE){

            if (resultCode == RESULT_OK){
                if (data.getBooleanExtra("EXIT",true)){
                    finish();
                }
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Stop","onStop() in SplashActivity");
        finish();

    }

}