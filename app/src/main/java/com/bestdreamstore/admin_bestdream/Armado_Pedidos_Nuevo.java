package com.bestdreamstore.admin_bestdream;




import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Cart_Adapter;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Cart_Controller;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Controlador_Carrito;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Web_View_Controller;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.bestdreamstore.admin_bestdream.LIBS.HTMLTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class Armado_Pedidos_Nuevo extends AppCompatActivity {

    private static final String KEY_PERMISOS = "permisos";
    private static final String KEY_NAME = "name";


    Functions userFunctions;
    JSONObject json;
    JSONArray productos, productos_server;

    private static RecyclerView recyclerView_global;
    private static RecyclerView.Adapter recyclerViewadapter;
    JSONArray ARRAY_ERRORES;
    public static View view;
    public static ArrayList GetCartAdapter1 = new ArrayList<>();
    MenuItem fav;
    public static Functions functions;
    Controlador_Carrito cart;
    Web_View_Controller web_view_controller;



    HTMLTextView datos_clienta;
    ImageView image_prod, image_confirmation;
    String id_pedido;
    Button procesar_pedido;
    Get_Cart_Adapter GetCartAdapter2;
    ImageButton back, add_monedero;
    Parcelable recyclerViewState = null;
    DatabaseHandler db;
    String id_order_fin;
    View popupView;
    String barcode = "";
    ListView myListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.armado_pedidos_nuevo);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        userFunctions = new Functions();

        /*NUEVO DETECTOR SIN EDIT TEXT - 2*/

        setFinishOnTouchOutside(false);
        ARRAY_ERRORES = new JSONArray();

        db = new DatabaseHandler(getApplicationContext());



        cart = new Controlador_Carrito(getApplicationContext());
        web_view_controller = new Web_View_Controller(getApplicationContext());

        userFunctions = new Functions();
        GetCartAdapter1.clear();



        String get_extras = get_extras_and_search();
        if(!get_extras.equals("null")){

            Log.i("EXTRAS:::", "----" + get_extras + "---");
            id_order_fin = get_extras;



        }else{


            Log.i("EXTRAS:::", "----NO HAY---");
            id_order_fin = userFunctions.get_id_order_admin(getApplicationContext());



        }



        procesar_pedido = (Button)findViewById(R.id.procesar_pedido);
        procesar_pedido.setText("Procesar:"+ id_order_fin);
        procesar_pedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.DELETE_CART();

                PROCESAR_PETICION(id_order_fin);



            }
        });


        //Controlador_Carrito.SHOW_POOP_UP_CART(Armado_Pedidos_Nuevo.this);



    }




    @SuppressLint("StaticFieldLeak")
    public void PROCESAR_PETICION(final String id_order){

        new AsyncTask<Object, Void, JSONObject>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //response_server.setText("Espere Un Momento......");

            }


            @Override
            protected JSONObject doInBackground(Object... params) {
                JSONObject resultados_json = new JSONObject();



                json = userFunctions.ver_pedido_servidor(id_order);

                resultados_json = json;


                return resultados_json;

            }








            @Override
            protected void onPostExecute(final JSONObject params) {
                super.onPostExecute(params);



                JSONObject pedido_json = new JSONObject();
                JSONObject pedido = new JSONObject();


                productos = new JSONArray();


                try {

                    final String quien_armo = params.getString("quien_armo");
                    final String key_pedido = params.getString("key_pedido");
                    final String QUIEN_ARMO_PAS = params.getString("quien_armo");
                    pedido_json = new JSONObject(params.getString("pedido_json"));
                    pedido = new JSONObject(pedido_json.getString("pedido"));


                    productos_server = pedido.getJSONArray("productos");



                    JSONObject datos_clientes = pedido.getJSONObject("datos_cliente");
                    JSONObject totales = pedido.getJSONObject("totales");

                    final String total_pagado = totales.getString("total_pagado");

                    final String full_name = datos_clientes.getString("full_name");
                    String email = datos_clientes.getString("email");
                    final String calle = datos_clientes.getString("calle");
                    final String numero = datos_clientes.getString("numero");
                    final String colonia = datos_clientes.getString("colonia");
                    final String municipio = datos_clientes.getString("municipio");
                    final String estado = datos_clientes.getString("estado");
                    final String cp = datos_clientes.getString("cp");
                    String referencias = datos_clientes.getString("referencias");

                    //String datos_gral = full_name





                    if(quien_armo.equals("")){



                        AlertDialog alertDialog = new AlertDialog.Builder(Armado_Pedidos_Nuevo.this).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setTitle("Bienvenido.");
                        alertDialog.setMessage("Deseas Ser el Administrador del Pedido:"+id_order_fin+"??");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ACEPTAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        String nombre_user = userFunctions.get_name_user(getApplicationContext());
                                        //String id_pedido  = userFunctions.get_id_order_admin(getApplicationContext());
                                        int res =  userFunctions.armar_pedido_func(nombre_user, id_order_fin);

                                        if(res == 1){


                                            /*
                                            datos_clienta.setText("<strong>NUEVO ADMINISTRADOR</strong><br>"+
                                                    "<strong>"+full_name+"</strong><br>"+
                                                    //calle+" "+ numero +" "+ colonia +"<br>"+
                                                    //municipio+" "+ estado +" "+ cp +"<br>"+
                                                    //"<strong>$"+total_pagado+"</strong><br>"+
                                                    "<strong>Armo: "+nombre_user+"</strong><br>"
                                            );

                                             */


                                        }else{

                                            finish();

                                        }


                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        //key_pedido
                                        //upen_url(Armado_Pedidos.this, "https://bestdream.store/Index/details_order/"+key_pedido);
                                        finish();




                                    }
                                });
                        alertDialog.show();





                    }else{


                        AlertDialog alertDialog = new AlertDialog.Builder(Armado_Pedidos_Nuevo.this).create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setTitle("ALERTA! ALERTA!");
                        alertDialog.setMessage("Este pedido ya ha sido armado con aterioridad por: "+QUIEN_ARMO_PAS);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Armar Sin Ser Administrador",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ver En Web",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        //key_pedido
                                        //upen_url(Armado_Pedidos.this, "https://bestdream.store/Index/details_order/"+key_pedido);
                                        finish();



                                    }
                                });

                        alertDialog.show();



                    }


                    /*

                    datos_clienta.setText(
                            "Nombre Cliente: <strong>"+full_name+"</strong><br>"+
                                    //calle+" "+ numero +" "+ colonia +"<br>"+
                                    // municipio+" "+ estado +" "+ cp +"<br>"+
                                    //"<strong>$"+total_pagado+"</strong><br>"+
                                    "<strong>Usuario Interno: "+quien_armo+"</strong><br>"
                    );
                    */





                } catch (JSONException e) {
                    e.printStackTrace();
                }



                //productos = userFunctions.ordenar_arr_bar_code(productos);
                // MOSTRAR_CARRITO(productos);


                if(userFunctions.insert_all_cart(productos_server, getApplicationContext())){

                    productos = userFunctions.get_jsonarray_cart(getApplicationContext());
                    productos = userFunctions.ordenar_arr_bar_code(productos);
                    //MOSTRAR_CARRITO(productos);
                    Controlador_Carrito.SHOW_POOP_UP_CART(Armado_Pedidos_Nuevo.this);
                    Log.i("SUBIDA:", "CORRECTA");

                }else{

                    productos = null;
                    productos_server = null;
                    Log.i("SUBIDA:", "ERROR SUBIDA");

                }




            } }.execute();



    }





    public String get_extras_and_search(){

        String categoria_entrante = "null";
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("id_pedido")) {

                categoria_entrante = extras.getString("id_pedido");

            }
        }else{

            categoria_entrante = "null";

        }


        return categoria_entrante;

    }



}