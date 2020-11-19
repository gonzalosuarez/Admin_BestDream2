package com.bestdreamstore.admin_bestdream;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Cart_Adapter;
import com.bestdreamstore.admin_bestdream.CONTROLLER.RecyclerCartViewAdapter;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Web_View_Controller;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;

import java.util.ArrayList;

import com.bestdreamstore.admin_bestdream.CONTROLLER.Cart_Controller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Comparar_Pedidos_2 extends Activity {


    private static final String KEY_ID_PRODUCTO = "id_producto";
    private static final String KEY_NOMBRE = "nombre_producto";
    private static final String KEY_PRECIO_PREMIUM = "precio_premium";
    private static final String KEY_COSTO_PRODUCTO = "costo_producto";
    private static final String KEY_CANTIDAD = "cantidad";
    private static final String KEY_COSTO_ENVIO = "costo_envio";
    private static final String KEY_MARCA = "marca";
    private static final String KEY_PRODUCTO = "producto";
    private static final String KEY_CATEGORIA = "categoria";
    private static final String KEY_IMAGEN= "imagen_comp";
    private static final String KEY_TIPO_ALTA_CART= "tipo_alta_cart";
    private static final String KEY_ERROR= "key_error";

    private static RecyclerView recyclerView_global;
    private static RecyclerView.Adapter recyclerViewadapter;
    JSONArray feed_todos;
    JSONObject json_fin, json_json;
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


    EditText id_pedido_edit_text;
    TextView response_server;
    ImageView image_prod, image_confirmation;
    JSONObject json;
    String id_pedido;
    Button delivery, borrar, imprimir_guia;
    Get_Cart_Adapter GetCartAdapter2;
    ImageButton back,ver_pedido_original, sacar_foto_pedido;
    Parcelable recyclerViewState = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparar_pedidos_2);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());



        cart = new Cart_Controller(getApplicationContext());
        web_view_controller = new Web_View_Controller(getApplicationContext());

        userFunctions = new Functions();
        GetCartAdapter1.clear();

        final String id_order_fin = userFunctions.get_id_order_admin(getApplicationContext());

        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView_global = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView_global.setHasFixedSize(true);
        recyclerView_global.setLayoutManager(recyclerViewlayoutManager);







        ver_pedido_original = (ImageButton)findViewById(R.id.ver_pedido_original);
        ver_pedido_original.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                String get_key_pedido = userFunctions.get_key_pedido(id_order_fin);
                Log.e("KEY_PEDIDO-----", get_key_pedido);
                Web_View_Controller.SHOW_POOP_UP_WEB_VIEW(Comparar_Pedidos_2.this, "https://bestdream.store/Index/details_order/"+get_key_pedido);

            }
        });






        sacar_foto_pedido = (ImageButton)findViewById(R.id.sacar_foto_pedido);
        sacar_foto_pedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                Intent i = new Intent(Comparar_Pedidos_2.this, Get_Image_Pedido.class);
                startActivityForResult(i, 1);


            }
        });






        imprimir_guia = (Button) findViewById(R.id.imprimir_guia);
        imprimir_guia.setText("Finalizar");
        imprimir_guia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.DELETE_CART();
                db.reset_id_pedido();

                Intent i = new Intent(Comparar_Pedidos_2.this, MainActivity.class);
                startActivityForResult(i, 1);

            }
        });







        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();

            }
        });



        delivery = (Button)findViewById(R.id.procesar_pedido);
        delivery.setText("Procesar: "+id_order_fin+ " >>");
        delivery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                PROCESAR_PETICION();
                MOSTRAR_CARRITO();


            }
        });






        borrar = (Button)findViewById(R.id.borrar);
        borrar.setText("Reiniciar");
        borrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.DELETE_CART();
                MOSTRAR_CARRITO();
                finish();




            }
        });










        MOSTRAR_CARRITO();







    }







    @SuppressLint("StaticFieldLeak")
    public void PROCESAR_PETICION(){

        new AsyncTask<Object, Void, JSONArray>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //response_server.setText("Espere Un Momento......");

            }


            @Override
            protected JSONArray doInBackground(Object... params) {
                JSONArray resultados_json = new JSONArray();


                id_pedido = userFunctions.get_id_order_admin(getApplicationContext());

                JSONObject pedido_json = new JSONObject();
                JSONObject pedido = new JSONObject();


                try {

                    json = userFunctions.ver_pedido_servidor(id_pedido);

                    pedido_json = new JSONObject(json.getString("pedido_json"));
                    pedido = new JSONObject(pedido_json.getString("pedido"));

                    resultados_json = pedido.getJSONArray("productos");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return resultados_json;

            }


            @Override
            protected void onPostExecute(final JSONArray params) {
                super.onPostExecute(params);

                JSONArray PRODUCTOS_ARR = userFunctions.get_jsonarray_cart(getApplicationContext());
                JSONArray CART_SERVER = params;




                MOSTRAR_CARRITO();




            } }.execute();



    }








    public void MOSTRAR_CARRITO(){

        GetCartAdapter1.clear();

        JSONArray CART = userFunctions.get_jsonarray_cart(getApplicationContext());
        JSONObject json_base_2 = null;

        for(int i = 0; i<CART.length(); i++) {

            GetCartAdapter2 = new Get_Cart_Adapter();

            try {

                json_base_2 = CART.getJSONObject(i);

                GetCartAdapter2.setprecio_premium(Float.parseFloat(json_base_2.getString(KEY_PRECIO_PREMIUM)));
                GetCartAdapter2.setnombre_producto(json_base_2.getString(KEY_NOMBRE));
                GetCartAdapter2.setgetid_producto(json_base_2.getString(KEY_ID_PRODUCTO));
                GetCartAdapter2.settipo_alta_cart(json_base_2.getString(KEY_TIPO_ALTA_CART));
                GetCartAdapter2.setqty(json_base_2.getInt(KEY_CANTIDAD));
                GetCartAdapter2.setkey_error(json_base_2.getString(KEY_ERROR));
                GetCartAdapter2.setimagen_comp(json_base_2.getString(KEY_IMAGEN));


            } catch (JSONException e) {

                e.printStackTrace();
            }


            GetCartAdapter1.add(GetCartAdapter2);

        }




        recyclerView_global.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        recyclerViewadapter = new RecyclerCartViewAdapter(GetCartAdapter1, getApplicationContext());
        recyclerView_global.setAdapter(recyclerViewadapter);




    }














}