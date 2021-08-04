package com.bestdreamstore.admin_bestdream;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Faltantes_Adapter;
import com.bestdreamstore.admin_bestdream.CONTROLLER.RecyclerView_List_Faltantes_Adapter;
import com.bestdreamstore.admin_bestdream.Funciones.AsincTask_Values;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_QR_SCAN = 101;

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_CAMARA = 2, CODIGO_VOZ = 3;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;


    Functions userFunctions;

    EditText id_pedido_edittext, id_encaje;
    Button ver_pedido_button, id_lista_pedidos, encajar_pedido, button_encaje;
    TextView id_order_result;
    LinearLayout encaje_linearlayout;
    Spinner usuarios_spinner;
    String get_encajador_db;
    String  get_cliente_name, id_encaje_txt, nombre_user, permisos_user;
    ImageView comando_voz;


    java.util.ArrayList<String> users_string = new java.util.ArrayList<>();



    private static String[] usuarios = {"Selecciona Usuario"};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userFunctions = new Functions();

        verificarYPedirPermisosDeCamara();


        /*
        try {

            String resultadoAsynctask =  new AsincTask_Values(getApplicationContext()).execute("lalossssss").get();

            Toast.makeText(MainActivity.this, "VALOR:"+ resultadoAsynctask, Toast.LENGTH_SHORT).show();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */



        usuarios_spinner = (Spinner)findViewById(R.id.usuarios);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        button_encaje = (Button)findViewById(R.id.button_encaje);
        id_encaje = (EditText)findViewById(R.id.id_encaje);
        id_encaje.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        encaje_linearlayout = (LinearLayout) findViewById(R.id.encaje_linearlayout);
        encaje_linearlayout.setVisibility(View.INVISIBLE);


        comando_voz = (ImageView)findViewById(R.id.comando_voz);
        comando_voz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                capturarVoz();

            }
        });






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);



        nombre_user = userFunctions.get_name_user(getApplicationContext());
        permisos_user = userFunctions.get_permisos_user(getApplicationContext());






        View headerView = navigationView.getHeaderView(0);
        TextView NameUsername = (TextView) headerView.findViewById(R.id.NOMBRE_USER);
        TextView PermisosUsername = (TextView) headerView.findViewById(R.id.PERMISOS_USER);
        ImageView logo_header = (ImageView)headerView.findViewById(R.id.logo_header);

        Picasso.get().load("https://bestdream.store/Views/Default/img/logo_bestdream_clear.png").into(logo_header);

        NameUsername.setText(nombre_user);
        PermisosUsername.setText("Permisos :: "+permisos_user);



        navigationView.setNavigationItemSelectedListener(this);






        id_pedido_edittext = (EditText)findViewById(R.id.id_order_edittext);
        //id_pedido_edittext.setText(id_order_fin);



        id_order_result = (TextView) findViewById(R.id.id_order_result);
        id_order_result.setText("Bienvenido: "+nombre_user);



         if(nombre_user.trim().equals("gonzalo") || nombre_user.trim().equals("estanis") || nombre_user.trim().equals("gerardo") || nombre_user.trim().equals("fran")){

             encaje_linearlayout.setVisibility(View.VISIBLE);
             //Toast.makeText(MainActivity.this, "SI ES IGUAL AUNO DE ELLOS:"+ nombre_user, Toast.LENGTH_SHORT).show();



             new AsyncTask<Object, Void, JSONArray>() {

                 @Override
                 protected void onPreExecute() {
                     super.onPreExecute();
                 }


                 @SuppressLint("WrongThread")
                 @Override
                 protected JSONArray doInBackground(Object... params) {
                     return userFunctions.get_all_users_admin();

                 }


                 @SuppressLint("StaticFieldLeak")
                 @Override
                 protected void onPostExecute(final JSONArray params) {
                     super.onPostExecute(params);


                     List<String> namesList = new ArrayList<String>();

                     for (int i=0; i<params.length(); i++) {

                         try {

                             JSONObject jsonObject = params.getJSONObject(i);
                             namesList.add(jsonObject.optString("nombre").toString());

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                     }

                     @SuppressLint("StaticFieldLeak") ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, namesList);
                     usuarios_spinner.setAdapter(adapter_2);




                 } }.execute();






             ImageButton scanner =(ImageButton)findViewById(R.id.scanner_main);
             scanner.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {



                     if (!permisoCamaraConcedido) {
                         Toast.makeText(MainActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                         permisoSolicitadoDesdeBoton = true;
                         verificarYPedirPermisosDeCamara();
                         return;
                     }
                     escanear();



                 }
             });


             button_encaje = (Button) findViewById(R.id.button_encaje);
             button_encaje.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     id_encaje_txt = id_encaje.getText().toString();



                     int add_id_order_int = userFunctions.add_Id_Order(id_encaje_txt, getApplicationContext());

                     if(add_id_order_int == 1){


                         new AsyncTask<Object, Void, String>() {

                             @Override
                             protected void onPreExecute() {
                                 super.onPreExecute();
                             }


                             @SuppressLint("WrongThread")
                             @Override
                             protected String doInBackground(Object... params) {
                                 return usuarios_spinner.getSelectedItem().toString();

                             }


                             @SuppressLint("StaticFieldLeak")
                             @Override
                             protected void onPostExecute(final String USUARIO_FIN) {
                                 super.onPostExecute(USUARIO_FIN);


                                                     get_cliente_name = userFunctions.get_name_cliente(id_encaje_txt);


                                                     AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                                     alertDialog.setCanceledOnTouchOutside(false);
                                                     alertDialog.setTitle("Bienvenido.");
                                                     alertDialog.setMessage("El Usuario:"+USUARIO_FIN+" encajara el pedido de: "+get_cliente_name);
                                                     alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ACEPTAR",
                                                             new DialogInterface.OnClickListener() {
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     dialog.dismiss();


                                                                     Log.i("USUARIO_ENCAJE", USUARIO_FIN);




                                                                     new AsyncTask<Object, Void, Integer>() {

                                                                         @Override
                                                                         protected void onPreExecute() {
                                                                             super.onPreExecute();
                                                                             //response_server.setText("Espere Un Momento......");

                                                                         }

                                                                         @Override
                                                                         protected Integer doInBackground(Object... params) {
                                                                             return userFunctions.encajar_pedido_func(USUARIO_FIN, id_encaje_txt);

                                                                         }

                                                                         @SuppressLint("WrongConstant")
                                                                         @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                                         @Override
                                                                         protected void onPostExecute(final Integer params) {
                                                                             super.onPostExecute(params);



                                                                             if(params == 1){

                                                                                 Toast.makeText(MainActivity.this, "Listo! Redireccionando", 6000).show(); //Incorrecto

                                                                             }else if(params == 2){

                                                                                 Toast.makeText(MainActivity.this, "Ya ha sido encajado previamente.....", 6000).show(); //Incorrecto

                                                                             }else if(params == 3){
                                                                                 Toast.makeText(MainActivity.this, "ERROR DE SISTEMA. REPORTAR", 6000).show(); //Incorrecto


                                                                             }

                                                                             Intent i = new Intent(MainActivity.this, Faltantes_Productos.class);
                                                                             startActivityForResult(i, 2);



                                                                         } }.execute();





                                                                 }
                                                             });
                                                     alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancelar",
                                                             new DialogInterface.OnClickListener() {
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     dialog.dismiss();

                                                                 }
                                                             });
                                                     alertDialog.show();







                                                 } }.execute();






                     }


                 }
             });



         }





        if(!permisos_user.equals("0")){


            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        }




        id_lista_pedidos = (Button) findViewById(R.id.id_lista_pedidos);
        id_lista_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, Searchs.class);
                startActivity(i);

            }
        });






        ver_pedido_button = (Button) findViewById(R.id.id_order_button);
        ver_pedido_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String id_order_fin = id_pedido_edittext.getText().toString();
                //id_order_result.setText(id_order_fin);

                int add_id_order_int = userFunctions.add_Id_Order(id_order_fin, getApplicationContext());

                if(add_id_order_int == 1){

                    Intent i = new Intent(MainActivity.this, Armado_Pedidos_Nuevo.class);
                    startActivityForResult(i, 2);


                }

            }
        });


        encajar_pedido = (Button) findViewById(R.id.encajar_pedido);
        encajar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String id_order_fin = id_pedido_edittext.getText().toString();
                String get_key_pedido = userFunctions.get_key_pedido(id_order_fin);
                Log.e("KEY_PEDIDO-----", get_key_pedido);


                get_cliente_name = userFunctions.get_name_cliente(id_order_fin);
                int add_id_order_int = userFunctions.add_Id_Order(id_order_fin, getApplicationContext());






                if(add_id_order_int == 1){

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Estimado:"+ nombre_user);
                    alert.setMessage("Administrarás el Cierre del pedido de:"+get_cliente_name);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            new AsyncTask<Object, Void, JSONObject>() {

                                @SuppressLint("StaticFieldLeak")
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();


                                }

                                @Override
                                protected JSONObject doInBackground(Object... params) {
                                    JSONObject resultados_json = new JSONObject();

                                    final JSONObject json;

                                    return userFunctions.cerrar_pedido_func(nombre_user, id_order_fin);

                                }


                                @SuppressLint("WrongConstant")
                                @Override
                                protected void onPostExecute(final JSONObject params) {
                                    super.onPostExecute(params);

                                    try {

                                        String success = params.getString("success");

                                        if(success.equals("1")){

                                            Toast.makeText(MainActivity.this, "Listo! Realizado", 6000).show();

                                        }else if(success.equals("2")){

                                            Toast.makeText(MainActivity.this, "Este Pedido ya ha sido administrado Anteriormente", 6000).show();


                                        }else if(success.equals("3")){

                                            Toast.makeText(MainActivity.this, "ERROR GENERAL REPORTAR SISTEMA", 6000).show();


                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }




                                } }.execute();





                            dialog.dismiss();

                        }
                    });

                    alert.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();





                }



            }
        });












    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            userFunctions.cerrar_session_login(getApplicationContext());
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if(id == R.id.add_bar_code){

            Intent i = new Intent(MainActivity.this, Add_Bar_Code.class);
            startActivityForResult(i, 2);

        }else if(id == R.id.add_bar_code_x_id){

            Intent i = new Intent(MainActivity.this, Add_Bar_Code_ID.class);
            startActivityForResult(i, 2);

        }else if(id == R.id.cerrar_session){


            userFunctions.cerrar_session_login(getApplicationContext());
            finish();

        }else if(id == R.id.comando_voz){

            Intent i = new Intent(MainActivity.this, Voz_texto.class);
            startActivity(i);

        }else if(id == R.id.search_pedidos){

            String URL_GLOBAL = "https://bestdream.store/Android/ver_pedidos_admin/?";

            Intent i = new Intent(MainActivity.this, Searchs.class);
            startActivity(i);

        }else if(id == R.id.autorizar_pedidos){



            Intent i = new Intent(MainActivity.this, Autorizar_Pedidos.class);
            startActivityForResult(i, REQUEST_CODE_QR_SCAN);

        }






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }





    private void escanear() {
        Intent i = new Intent(MainActivity.this, ActivityEscanear.class);
        startActivityForResult(i, CODIGO_CAMARA);
    }




    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(MainActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == CODIGO_CAMARA) {

            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    final String codigo = data.getStringExtra("codigo");

                    new AsyncTask<Object, Void, String>() {

                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();

                            id_encaje.setText("Buscando......");


                        }

                        @Override
                        protected String doInBackground(Object... params) {



                            //String cod_ = "1013486134341138917500396286302636";
                            return userFunctions.get_id_pedido_bar_code_fedex(codigo);



                        }


                        @SuppressLint("WrongConstant")
                        @Override
                        protected void onPostExecute(final String params) {
                            super.onPostExecute(params);

                            if(params.equals("0")){

                                id_encaje.setText("ERROR.. Ingresa Id Manual");
                            }else{

                                id_encaje.setText(params);

                            }




                        } }.execute();


                }
            }


        }else if(requestCode == CODIGO_VOZ  && resultCode == RESULT_OK && data != null){


            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String txt_fin = result.get(0).replace(" ","");
            id_pedido_edittext.setText(txt_fin);

           // textView.setText(result.get(0));

        }
    }





    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }





    private void capturarVoz(){
        Intent intent = new Intent(RecognizerIntent
                .ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, CODIGO_VOZ);
        } else
        {
            Log.e("ERROR","Su dispositivo no admite entrada de voz");
        }
    }





}
