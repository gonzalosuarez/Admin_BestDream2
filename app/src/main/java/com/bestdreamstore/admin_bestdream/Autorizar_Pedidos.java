package com.bestdreamstore.admin_bestdream;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.bestdreamstore.admin_bestdream.Get_Image_Pedido.userFunctions;

public class Autorizar_Pedidos extends AppCompatActivity{




    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private EditText id_encaje;
    private ImageButton button_autorizar, scanner_main;
    private TextView response_server;
    Functions userFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorizar_pedidos);


        button_autorizar = (ImageButton) findViewById(R.id.button_autorizar);
        button_autorizar.setVisibility(View.INVISIBLE);


        scanner_main = (ImageButton) findViewById(R.id.scanner_main);
        scanner_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificarYPedirPermisosDeCamara();

            }
        });


        userFunctions = new Functions();





        id_encaje = (EditText) findViewById(R.id.id_encaje);
        response_server = (TextView) findViewById(R.id.response_server);















    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {


                    button_autorizar.setVisibility(View.VISIBLE);
                    scanner_main.setVisibility(View.INVISIBLE);


                    final String codigo_id_pedido = data.getStringExtra("codigo");

                    id_encaje.setText(codigo_id_pedido);
                    button_autorizar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new AsyncTask<Object, Void, String>() {

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    response_server.setText("Cambiando..........");
                                }
                                @Override
                                protected String doInBackground(Object... params) {

                                    return userFunctions.autorizar_pedido(codigo_id_pedido);


                                }

                                @Override
                                protected void onPostExecute(final String params) {
                                    super.onPostExecute(params);

                                    response_server.setText(params+ " - " +codigo_id_pedido);

                                    button_autorizar.setVisibility(View.INVISIBLE);
                                    scanner_main.setVisibility(View.VISIBLE);

                                } }.execute();


                        }
                    });



                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        escanear();
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void verificarYPedirPermisosDeCamara() {

        int estadoDePermiso = ContextCompat.checkSelfPermission(Autorizar_Pedidos.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
            escanear();
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(Autorizar_Pedidos.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }



        private void escanear() {



            Intent intent = new Intent(Autorizar_Pedidos.this, ActivityEscanear.class);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 2);





        }



        private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(Autorizar_Pedidos.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }
}