package com.bestdreamstore.admin_bestdream;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Add_Bar_Code_ID extends AppCompatActivity {

    EditText escaner_id_edittext, product_id_edittext;
    Button add_bar_code;
    ImageButton Escanear;
    String id_producto, imagen, bar_code, id_prod_string;
    ImageView image_prod;
    TextView response_server;
    Functions userFunctions;

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;

    final Activity activity = this;



    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bar_code_id);

        verificarYPedirPermisosDeCamara();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        userFunctions = new Functions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ADD_BAR_ID);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        Escanear = (ImageButton) findViewById(R.id.btnEscanear);
        Escanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!permisoCamaraConcedido) {
                    Toast.makeText(Add_Bar_Code_ID.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                escanear();


            }
        });






        add_bar_code = (Button)findViewById(R.id.add_bar_code);

        response_server = (TextView)findViewById(R.id.response_server);
        escaner_id_edittext = (EditText)findViewById(R.id.escaner_id);
        escaner_id_edittext.setHint("Bar Code");


        product_id_edittext = (EditText)findViewById(R.id.product_id);
        product_id_edittext.setHint("ID Producto");






        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {



            id_producto = extras.getString("ID_PRODUCTO");
            product_id_edittext.setText(id_producto);

            imagen = extras.getString("IMAGEN");

            add_bar_code.setText("Agregar a: "+id_producto );

            this.setTitle("Producto: "+id_producto);

            image_prod = (ImageView)findViewById(R.id.image_prod);
            Picasso.get().load(imagen).into(image_prod);





            new AsyncTask<Object, Void, JSONObject>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();



                }


                @SuppressLint("WrongThread")
                @Override
                protected JSONObject doInBackground(Object... params) {
                    JSONObject resultados_json = new JSONObject();

                    final JSONObject json;

                    return userFunctions.check_if_bar_code_exist(id_producto);


                }


                @Override
                protected void onPostExecute(final JSONObject params) {
                    super.onPostExecute(params);


                    try {


                        String bar_code = params.getString("bar_code");
                        escaner_id_edittext.setText(bar_code);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                } }.execute();












        }else{


            add_bar_code.setText("Agregar Ahora" );

            id_producto = product_id_edittext.getText().toString();
            imagen = "null";




        }





        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/




        add_bar_code.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {


                //response_server.setText("id_producto:: "+ id_prod_string + " ---  bar_code:"+bar_code );


                bar_code = escaner_id_edittext.getText().toString();
                id_prod_string = product_id_edittext.getText().toString();


                if(bar_code.equals("")){

                    response_server.setText("ERROR  ::: AGREGUE CODIGO DE BARRAS");

                }else if(id_prod_string.equals("")){

                    response_server.setText("ERROR  ::: AGREGUE ID_PRODUCTO");

                }else{


                    new AsyncTask<Object, Void, JSONObject>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            response_server.setText("ESPERE UN MOMENTO.....");


                        }


                        @SuppressLint("WrongThread")
                        @Override
                        protected JSONObject doInBackground(Object... params) {
                            JSONObject resultados_json = new JSONObject();

                            final JSONObject json;



                            String permisos_user = userFunctions.get_permisos_user(getApplicationContext());
                            return userFunctions.add_bar_code_id_server(id_prod_string, bar_code, permisos_user);


                        }


                        @Override
                        protected void onPostExecute(final JSONObject params) {
                            super.onPostExecute(params);

                            //Log.d("PARAMS_PEDIDO:", String.valueOf(params));

                            try {

                                String success = params.getString("success");

                                if(success.equals("1")){


                                    response_server.setText("Listo!!! Generado con Exito");


                                }else{

                                    response_server.setText("ERROR  ::: "+success);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        } }.execute();





                }




            }
        });




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
        int estadoDePermiso = ContextCompat.checkSelfPermission(Add_Bar_Code_ID.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(Add_Bar_Code_ID.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }



    private void escanear() {
        Intent i = new Intent(Add_Bar_Code_ID.this, ActivityEscanear.class);
        startActivityForResult(i, CODIGO_INTENT);
    }




    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(Add_Bar_Code_ID.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {


                    String codigo = data.getStringExtra("codigo");
                    escaner_id_edittext.setText(codigo);



                }
            }
        }
    }




}
