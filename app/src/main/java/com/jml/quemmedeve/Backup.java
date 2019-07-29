package com.jml.quemmedeve;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;
import com.jml.quemmedeve.ultility.DateUltility;
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
    private Button btnSendBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        btnBackupLocal = findViewById(R.id.btnBackupLocal);
        btnRestaurarBackup = findViewById(R.id.btnRestaurarBackup);
        btnSendBackup = findViewById(R.id.btnSendBackup);

        makeLocalBackup();
        restoreLocalBackup();
        sendBackup();

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


    private void sendBackup() {
        btnSendBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent();
                File fileDb = new File(Environment.getExternalStorageDirectory() + "/Quemmedeve/backup/qmd.db");
                it.setAction(Intent.ACTION_SEND);

                if(fileDb.exists()){
                    it.setType("application/x-sqlite3");
                    it.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileDb));
                    it.putExtra(Intent.EXTRA_SUBJECT, "Backup-"+ DateUltility.getCurrentData("USA")+".db");
                    startActivity(Intent.createChooser(it, "Enviando arquivo"));
                }else{
                    Toast.makeText(getApplicationContext(), "Não Foi localizado o arquivo de backup. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void restoreLocalBackup() {
        btnRestaurarBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder modal = new AlertDialog.Builder(Backup.this);
                modal.setTitle("Tem certeza que deseja fazer isso ?");

                modal.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent();
                        it.setType("application/x-sqlite3");
                        it.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(it, "Selecione o arquivo"), 1);
                    }
                });

                modal.setNegativeButton("NÃO", null);

                AlertDialog dialog = modal.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(MimeTypeMap.getFileExtensionFromUrl(data.getData().getPath()));

        if(requestCode == 1 && resultCode == RESULT_OK){
            if(MimeTypeMap.getFileExtensionFromUrl(data.getData().getPath()).equals("db")){
                executeRestore(data.getData().getPath());
            }else{
               Toast.makeText(getApplicationContext(), "Arquivo Inválido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void executeBackup() {
        try {
                File fileDbOrigin = new File(Environment.getDataDirectory() + "/data/com.jml.quemmedeve/databases/qmd.db");
                File folderDest = new File(Environment.getExternalStorageDirectory() + "/Quemmedeve/backup");

                if(!folderDest.isDirectory()){
                    folderDest.mkdirs();
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

                Toast.makeText(getApplicationContext(), "Backup feito com sucesso", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            Log.i("Erro: ", e.getMessage());
        } catch (IOException e) {
            Log.i("Erro: ", e.getMessage());
        }
    }


    private void executeRestore(String filePath){

        try {
            InputStream in = new FileInputStream(new File(filePath));
            OutputStream out = new FileOutputStream(new File(Environment.getDataDirectory() + "/data/com.jml.quemmedeve/databases/qmd.db"));

            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

         Toast.makeText(getApplicationContext(), "Restauração Feita com sucesso!", Toast.LENGTH_SHORT).show();


        }catch (FileNotFoundException e){
            Log.i("Erro: ", e.getMessage());
        } catch (IOException e) {
            Log.i("Erro: ", e.getMessage());
        }
    }
}
