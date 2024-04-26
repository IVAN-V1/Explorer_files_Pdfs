package com.ministudio.explorer_files_pdfs.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.ministudio.explorer_files_pdfs.R;

public class Permiso_ad_documentos extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final int MANAGE_STORAGE_PERMISSION_REQUEST_CODE = 2;
    MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiso_ad_documentos);



        button=findViewById(R.id.permiso_archivos);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                requestStoragePermission();


            }
        });





    }



    private void requestStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestManageStoragePermission();

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE
            );
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestManageStoragePermission() {

        if (!Environment.isExternalStorageManager()) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, MANAGE_STORAGE_PERMISSION_REQUEST_CODE);

        } else {

            redirectToAnotherActivity(); // El usuario ya tiene el permiso, redirigir a otra actividad
        }
    }

    private void redirectToAnotherActivity() {
        // Redirigir a la actividad deseada
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Opcional: puedes finalizar esta actividad si no quieres que el usuario vuelva atrás
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Verificar si el usuario tiene el permiso al volver a la actividad
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            redirectToAnotherActivity();
        }
    }



}