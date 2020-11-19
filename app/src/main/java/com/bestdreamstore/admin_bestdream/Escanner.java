package com.bestdreamstore.admin_bestdream;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.squareup.picasso.Picasso;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import java.util.Timer;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Cart_Controller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.util.TimerTask;
public class Escanner extends AppCompatActivity {



    Functions userFunctions;

    Cart_Controller cart;

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private TextView tvCodigoLeido;

    EditText escanner_edittext;
    TextView response_server;
    ImageView image_prod, image_confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escanner);
        verificarYPedirPermisosDeCamara();


        userFunctions = new Functions();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_escanner);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String id_order_fin = userFunctions.get_id_order_admin(getApplicationContext());
        this.setTitle("Pedido: "+id_order_fin);







        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        image_prod = (ImageView)findViewById(R.id.image_prod);
        image_confirmation = (ImageView)findViewById(R.id.image_confirmation);


        tvCodigoLeido = findViewById(R.id.tvCodigoLeido);


        response_server = (TextView) findViewById(R.id.response_server);

        escanner_edittext = (EditText)findViewById(R.id.escanner_edittext);
        escanner_edittext.addTextChangedListener(new TextWatcher() {

            boolean isTyping = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private final long DELAY = 700; // milliseconds

            @Override
            public void afterTextChanged(final Editable s) {
                Log.d("", "");
                if(!isTyping) {
                    //Log.d(TAG, "started typing");
                    // Send notification for start typing event
                    isTyping = true;
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {


                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        search_server();
                                        isTyping = false;
                                        escanner_edittext.setText("");

                                    }
                                });




                            }
                        },
                        DELAY
                );
            }
        });






        Button ver_pedido = findViewById(R.id.ver_pedido);
        ver_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cart_Controller.SHOW_POOP_UP_CART(Escanner.this);
                Intent i = new Intent(Escanner.this, Comparar_Pedidos_2.class);
                startActivityForResult(i, 1);


            }
        });








        ImageButton btnEscanear = (ImageButton)findViewById(R.id.btnEscanear);
        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permisoCamaraConcedido) {
                    Toast.makeText(Escanner.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                escanear();
            }
        });










    }







    private void escanear() {
        Intent i = new Intent(Escanner.this, ActivityEscanear.class);
        startActivityForResult(i, CODIGO_INTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {


                    String codigo = data.getStringExtra("codigo");
                    escanner_edittext.setText(codigo);



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
        int estadoDePermiso = ContextCompat.checkSelfPermission(Escanner.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(Escanner.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }




    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(Escanner.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }











    public void search_server(){


        /*
        response_server.setText("Espere Unoa Momentos......");
        Picasso.get().load("https://bestdream.store/Views/Default/img/wait.png").into(image_prod);
        Picasso.get().load("https://bestdream.store/Views/Default/img/checked.png").into(image_confirmation);

        */


        String result = escanner_edittext.getText().toString();

        if (result.trim().equals("")) { }else{


            String URL_ENCODE = "https://bestdream.store/WS/bar_code/"+result;


            RequestQueue requestQueue = Volley.newRequestQueue(this);

           // Initialize a new JsonObjectRequest instance
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_ENCODE,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{



                            JSONArray feed_todos = response.getJSONArray("feed_todos");
                            JSONObject json_fin = feed_todos.getJSONObject(0);

                            //Log.i("----feed_todos---", String.valueOf(feed_todos));

                            int numrows = json_fin.getInt("numrows");

                            Log.i("----NUMROWS---", String.valueOf(numrows));

                            if(numrows > 0){

                                JSONObject json_json = json_fin.getJSONObject("json");

                                int precio_mayoreo_url = json_json.getInt("precio_mayoreo");
                                int decimales_mayoreo_url = json_json.getInt("decimales_mayoreo");
                                String PRECIO_PREMIUM_GLOBAL = String.valueOf(precio_mayoreo_url)+"."+String.valueOf(decimales_mayoreo_url);


                                Log.i("----PRECIO PREMIUM---", PRECIO_PREMIUM_GLOBAL);


                                int costo_producto = json_json.getInt("costo_producto");
                                int decimales_costo = json_json.getInt("decimales_costo");
                                String COSTO_PRODUCTO_GLOBAL = String.valueOf(costo_producto)+"."+String.valueOf(decimales_costo);

                                Log.i("----COSTO PRODCUTO---", COSTO_PRODUCTO_GLOBAL);


                                DatabaseHandler db3 = new DatabaseHandler(getApplicationContext());





                                response_server.setText(json_json.getString("nombre"));
                                Picasso.get().load(json_json.getString("imagen")).into(image_prod);
                                Picasso.get().load("https://bestdream.store/Views/Default/img/checked.png").into(image_confirmation);



                            }else{

                                response_server.setText("");
                                Picasso.get().load("https://bestdream.store/Views/Default/img/pago_error.png").into(image_prod);
                                Picasso.get().load("https://bestdream.store/Views/Default/img/pago_error.png").into(image_confirmation);


                            }




                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){


                    }
                }
        );

        requestQueue.add(jsonObjectRequest);








        }



    }






}