package com.mukeshdev.quizappsqlite.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mukeshdev.quizappsqlite.Utility.Constants;
import com.mukeshdev.quizappsqlite.databinding.ActivityCategoryBinding;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHistory = new Intent(CategoryActivity.this, MainActivity.class);
                intentHistory.putExtra("Category", Constants.HISTORY);
                startActivity(intentHistory);
            }
        });

        binding.cvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEnglish = new Intent(CategoryActivity.this,MainActivity.class);
                intentEnglish.putExtra("Category",Constants.ENGLISH);
                startActivity(intentEnglish);
            }
        });

        binding.cvMaths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaths = new Intent(CategoryActivity.this,MainActivity.class);
                intentMaths.putExtra("Category",Constants.MATHS);
                startActivity(intentMaths);
            }
        });

        binding.cvComputers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComputers = new Intent(CategoryActivity.this,MainActivity.class);
                intentComputers.putExtra("Category",Constants.COMPUTERS);
                startActivity(intentComputers );
            }
        });

        binding.cvGraphics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGraphics = new Intent(CategoryActivity.this,MainActivity.class);
                intentGraphics.putExtra("Category",Constants.GRAPHICS);
                startActivity(intentGraphics );
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            new AlertDialog.Builder(this)
                    .setTitle("Do you  want to exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                            finish();
                        }
                    }).create().show();

        }else  {

            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BUG","onStop() in Category");
        finish();

    }
}