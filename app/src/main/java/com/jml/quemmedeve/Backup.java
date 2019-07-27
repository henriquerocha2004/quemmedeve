package com.jml.quemmedeve;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.mortbay.jetty.servlet.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Backup extends AppCompatActivity {

    private Button btnBackupLocal;
    private Button btnRestaurarBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        btnBackupLocal = findViewById(R.id.btnBackupLocal);
        btnRestaurarBackup = findViewById(R.id.btnRestaurarBackup);

        makeLocalBackup();

    }

    private void makeLocalBackup() {

        btnBackupLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder modal = new AlertDialog.Builder(Backup.this);
                modal.setTitle("O Arquivo de backup será Salvo em QuemMeDeve/Backups. Confirma?");

                modal.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermissions();
                    }
                });

                modal.setNegativeButton("NÂO", null);
                AlertDialog dialog = modal.create();
                dialog.show();

            }
        });
    }

    private void checkPermissions(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            executeBackup();
        }else{
            ActivityCompat.requestPermissions(Backup.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case 1:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    executeBackup();
                }
            }
        }
    }



    private void executeBackup() {

        try {
                File fileDbOrigin = new File(Environment.getDataDirectory() + "/data/com.jml.quemmedeve/databases/qmd.db");
                File folderDest = new File(Environment.getExternalStorageDirectory() + "/Quemmedeve/");

                if(!folderDest.isDirectory()){
                    folderDest.mkdir();
                }

                File fileDbDest = new File(folderDest.getAbsolutePath()+ "/qmd.db");

                InputStream in = new FileInputStream(fileDbOrigin);
                OutputStream out = new FileOutputStream(fileDbDest);

                byte[] buffer = new byte[1024];
                int len;

                while((len = in.read(buffer)) > 0){
                    out.write(buffer, 0,len);
                }

                in.close();
                out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
