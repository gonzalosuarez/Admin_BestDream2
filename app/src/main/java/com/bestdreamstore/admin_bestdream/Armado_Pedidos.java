package com.bestdreamstore.admin_bestdream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Cart_Adapter;
import com.bestdreamstore.admin_bestdream.ADAPTERS.SwipeToDeleteCallback;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Cart_Controller;
import com.bestdreamstore.admin_bestdream.CONTROLLER.RecyclerCartViewAdapter;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Web_View_Controller;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.bestdreamstore.admin_bestdream.LIBS.HTMLTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Armado_Pedidos extends AppCompatActivity{


    private static final String KEY_ID_PRODUCTO = "id_producto";
    private static final String KEY_NOMBRE = "nombre_producto";
    private static final String KEY_PRECIO_PREMIUM = "precio_premium";
    private static final String KEY_COSTO_PRODUCTO = "costo_producto";
    private static final String KEY_CANTIDAD = "cantidad";
    private static final String KEY_COSTO_ENVIO = "costo_envio";
    private static final String KEY_MARCA = "marca";
    private static final String KEY_PRODUCTO = "producto";
    private static final String KEY_BAR_CODE = "bar_code";
    private static final String KEY_IMAGEN= "imagen_comp";
    private static final String KEY_TIPO_ALTA_CART= "tipo_alta_cart";
    private static final String KEY_ERROR= "key_error";
    private static final String KEY_ID = "id";

     //GetCartAdapter1.remove(position);
    //recyclerViewadapter.notifyDataSetChanged();

    private static RecyclerView recyclerView_global;
    private static RecyclerView.Adapter recyclerViewadapter;
    JSONArray feed_todos;
    JSONObject json_base_2;
    private static LinearLayout content_window;
    public static View view;
    public static ArrayList GetCartAdapter1 = new ArrayList<>();
    public static TextView datos_pedido;
    MenuItem fav;
    public static Functions userFunctions;

    Cart_Controller cart;
    Web_View_Controller web_view_controller;

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;


    HTMLTextView datos_clienta;
    ImageView image_prod, image_confirmation;
    JSONObject json;
    String id_pedido;
    Button delivery, borrar, imprimir_guia, reporte;
    Get_Cart_Adapter GetCartAdapter2;
    ImageButton back;
    Parcelable recyclerViewState = null;
    DatabaseHandler db;
    String id_order_fin;
    JSONArray productos, productos_server;

    String barcode = "";


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.armado_pedidos);


        /*CAMBIO NUEVOOOOOOOOOO*/

        setFinishOnTouchOutside(false);

        db = new DatabaseHandler(getApplicationContext());



        cart = new Cart_Controller(getApplicationContext());
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





        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView_global = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView_global.setHasFixedSize(true);
        recyclerView_global.setLayoutManager(recyclerViewlayoutManager);



        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.DELETE_CART();
                //MOSTRAR_CARRITO();
                finish();


            }
        });







        delivery = (Button)findViewById(R.id.procesar_pedido);
        delivery.setText("Procesar: "+id_order_fin+ " >>");
        delivery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.DELETE_CART();
                PROCESAR_PETICION(id_order_fin);
                //MOSTRAR_CARRITO();


            }
        });



        datos_clienta = (HTMLTextView)findViewById(R.id.datos_clienta);






        reporte = (Button) findViewById(R.id.reporte);
        reporte.setVisibility(View.INVISIBLE);



        //MOSTRAR_CARRITO();
        enableSwipeToDeleteAndUndo();





    }





    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getAction()==KeyEvent.ACTION_DOWN
                && e.getKeyCode() != KeyEvent.KEYCODE_ENTER){ //Not Adding ENTER_KEY to barcode String
            char pressedKey = (char) e.getUnicodeChar();
            barcode += pressedKey;
        }
        if (e.getAction()==KeyEvent.ACTION_DOWN
                && e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            Log.e("Barcode Read-----", barcode);
            Log.e("PRODUCTOS-----", String.valueOf(productos));

            DELETE_ITEM(barcode);

            barcode="";
        }
        return false;
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


                    reporte.setVisibility(View.VISIBLE);
                    reporte.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String arr_fin = String.valueOf(GetCartAdapter1);
                            Log.i("CARRITO:::", "----" + GetCartAdapter1 + "---");

                            //Log.i("ids", Arrays.toString(GetCartAdapter1));






                         }
                    });



                    if(quien_armo.equals("")){



                        AlertDialog alertDialog = new AlertDialog.Builder(Armado_Pedidos.this).create();
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

                                           datos_clienta.setText("<strong>NUEVO ADMINISTRADOR</strong><br>"+
                                            "<strong>"+full_name+"</strong><br>"+
                                            calle+" "+ numero +" "+ colonia +"<br>"+
                                            municipio+" "+ estado +" "+ cp +"<br>"+
                                             "<strong>$"+total_pagado+"</strong><br>"+
                                             "<strong>Armo: "+nombre_user+"</strong><br>"
                                            );


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


                        AlertDialog alertDialog = new AlertDialog.Builder(Armado_Pedidos.this).create();
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
                                        upen_url(Armado_Pedidos.this, "https://bestdream.store/Index/details_order/"+key_pedido);
                                        finish();



                                    }
                                });

                        alertDialog.show();



                    }



                    datos_clienta.setText(
                            "<strong>"+full_name+"</strong><br>"+
                                    calle+" "+ numero +" "+ colonia +"<br>"+
                                    municipio+" "+ estado +" "+ cp +"<br>"+
                                    //"<strong>$"+total_pagado+"</strong><br>"+
                                    "<strong>Armo: "+quien_armo+"</strong><br>"
                    );




                } catch (JSONException e) {
                    e.printStackTrace();
                }



                //productos = userFunctions.ordenar_arr_bar_code(productos);
                // MOSTRAR_CARRITO(productos);


                if(userFunctions.insert_all_cart(productos_server, getApplicationContext())){

                    productos = userFunctions.get_jsonarray_cart(getApplicationContext());
                    productos = userFunctions.ordenar_arr_bar_code(productos);
                    MOSTRAR_CARRITO(productos);
                    Log.i("SUBIDA:", "CORRECTA");

                }else{

                    productos = null;
                    productos_server = null;
                    Log.i("SUBIDA:", "ERROR SUBIDA");

                }




            } }.execute();



    }




    public void DELETE_ITEM(String bar_code){

        json_base_2 = null;
        DatabaseHandler db2 = new DatabaseHandler(getApplicationContext());


        for(int i = 0; i<productos.length(); i++) {


            try {

                json_base_2 = productos.getJSONObject(i);



             //   int chek_if_exist = db2.check_if_product_inf_cart(json_base_2.getString(KEY_BAR_CODE));


                    if(bar_code.equals(json_base_2.getString(KEY_BAR_CODE))){
                        if(json_base_2.getInt(KEY_CANTIDAD) > 1){

                            //ELIMINAR 1 DE LA CANTIDAD
                            db2.cambiar_cantidad(json_base_2.getString(KEY_ID_PRODUCTO), json_base_2.getInt(KEY_CANTIDAD));
                            REFRESH_CART();

                        }else{

                            //DESAPARECE DEL LISTADO
                            db2.delete(json_base_2.getString(KEY_ID_PRODUCTO));
                            REFRESH_CART();


                        }


                    }


                    /*
                if(chek_if_exist > 0){


                }else{

                    //INSERT_ERROR
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"ERROR: NO EXISTE EN PEDIDO",Snackbar.LENGTH_SHORT);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

                }

                     */




            } catch (JSONException e) {

                e.printStackTrace();
            }



        }








    }







    public void MOSTRAR_CARRITO(JSONArray CART){

        GetCartAdapter1.clear();

        //JSONArray CART = userFunctions.get_jsonarray_cart(getApplicationContext());
        json_base_2 = null;



        for(int i = 0; i<CART.length(); i++) {

            GetCartAdapter2 = new Get_Cart_Adapter();

            try {

                json_base_2 = CART.getJSONObject(i);

                //GetCartAdapter2.setid(json_base_2.getInt(KEY_ID));
                GetCartAdapter2.setprecio_premium(Float.parseFloat(json_base_2.getString(KEY_PRECIO_PREMIUM)));
                GetCartAdapter2.setnombre_producto(json_base_2.getString(KEY_NOMBRE));
                GetCartAdapter2.setgetid_producto(json_base_2.getString(KEY_ID_PRODUCTO));
                //GetCartAdapter2.settipo_alta_cart(json_base_2.getString(KEY_TIPO_ALTA_CART));

                GetCartAdapter2.setqty(json_base_2.getInt(KEY_CANTIDAD));
                //GetCartAdapter2.setkey_error(json_base_2.getString(KEY_ERROR));
                GetCartAdapter2.setimagen_comp(json_base_2.getString(KEY_IMAGEN));
                GetCartAdapter2.setbar_code(json_base_2.getString(KEY_BAR_CODE));


            } catch (JSONException e) {

                e.printStackTrace();
            }


            GetCartAdapter1.add(GetCartAdapter2);

        }




        recyclerView_global.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        recyclerViewadapter = new RecyclerCartViewAdapter(GetCartAdapter1, getApplicationContext());
        recyclerView_global.setAdapter(recyclerViewadapter);




    }




    public void REFRESH_CART(){

        productos = userFunctions.get_jsonarray_cart(getApplicationContext());
        productos = userFunctions.ordenar_arr_bar_code(productos);
        MOSTRAR_CARRITO(productos);


    }



    @Override
    public void onBackPressed() {

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.DELETE_CART();
        //MOSTRAR_CARRITO();
        finish();
    }




    private void enableSwipeToDeleteAndUndo() {



        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                recyclerViewadapter.notifyItemRemoved(position);

                GetCartAdapter1.remove(position);
                recyclerViewadapter.notifyDataSetChanged();




                /*

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Borrar: "+res,Snackbar.LENGTH_SHORT);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                */


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView_global);



    }



    void upen_url(Activity activity, String url) {

        Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        if (i.resolveActivity(activity.getPackageManager()) == null) {
            i.setData(Uri.parse(url));
        }
        activity.startActivity(i);

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

