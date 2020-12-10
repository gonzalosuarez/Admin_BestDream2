package com.bestdreamstore.admin_bestdream.CONTROLLER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Cart_Adapter;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.LIBS.DownloadImageTask;
import com.bestdreamstore.admin_bestdream.LIBS.HTMLTextView;
import com.bestdreamstore.admin_bestdream.LIBS.SliderVolleyRequest;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.bestdreamstore.admin_bestdream.MainActivity;
import com.bestdreamstore.admin_bestdream.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;


public class Cart_Controller_Nuevo {


    private static RecyclerView recyclerView_global;
    private static RecyclerView.Adapter recyclerViewadapter;
    JSONArray feed_todos;
    JSONObject json_fin, json_json;
    private static LinearLayout content_window;
    public static View view;
    public static ArrayList GetCartAdapter1 = new ArrayList<>();
    public static TextView datos_pedido;
    MenuItem fav;
    public static Functions userfunctions;



    Context context;






    private static final String KEY_ID_PRODUCTO = "id_producto";
    private static final String KEY_NOMBRE = "nombre_producto";
    private static final String KEY_PRECIO_PREMIUM = "precio_premium";
    private static final String KEY_COSTO_PRODUCTO = "costo_producto";
    private static final String KEY_CANTIDAD = "cantidad";
    private static final String KEY_COSTO_ENVIO = "costo_envio";
    private static final String KEY_MARCA = "marca";
    private static final String KEY_BAR_CODE = "bar_code";
    private static final String KEY_PRODUCTO = "producto";
    private static final String KEY_CATEGORIA = "categoria";
    private static final String KEY_IMAGEN= "imagen_comp";

    ProgressDialog dialog;




    private static float total_prod;


    public Cart_Controller_Nuevo(Context context){

        super();
        this.context = context;





    }













    public boolean ADD_CART_SKU(String id_producto){
        //update_icon_cart(context);
        return ADD_CART_DB_PRINCIPAL(id_producto);
    }



    public boolean ADD_CART_DETAILS_SKU(String id_producto){
        return ADD_CART_DB_PRINCIPAL(id_producto);
    }




    public boolean ADD_CART_DB_PRINCIPAL(String id_producto){


        String request_url = "https://bestdream.store/Android/details_id/"+id_producto;

        final JsonArrayRequest searchMsg = new JsonArrayRequest(request_url, new Response.Listener<JSONArray>() {

            // ProgressDialog dialog = ProgressDialog.show(context, "", "Espere un Momento....", true);

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject obj = response.getJSONObject(i);


                        feed_todos = obj.getJSONArray("feed_todos");
                        json_fin = feed_todos.getJSONObject(0);
                        json_json = json_fin.getJSONObject("json");

                        /*IMAGEN PINCIPAL*/

                        int precio_mayoreo_url = json_json.getInt("precio_mayoreo");
                        int decimales_mayoreo_url = json_json.getInt("decimales_mayoreo");
                        String PRECIO_PREMIUM_GLOBAL = String.valueOf(precio_mayoreo_url)+"."+String.valueOf(decimales_mayoreo_url);


                        Log.i("----PRECIO PREMIUM---", PRECIO_PREMIUM_GLOBAL);


                        int costo_producto = json_json.getInt("costo_producto");
                        int decimales_costo = json_json.getInt("decimales_costo");
                        String COSTO_PRODUCTO_GLOBAL = String.valueOf(costo_producto)+"."+String.valueOf(decimales_costo);

                        Log.i("----COSTO PRODCUTO---", COSTO_PRODUCTO_GLOBAL);


                        /*
                        DatabaseHandler db3 = new DatabaseHandler(context);
                        db3.addCart(
                                json_json.getString("id"),
                                json_json.getString("nombre"),
                                json_json.getString("imagen"),
                                json_json.getString("categoria"),
                                json_json.getString("bar_code"),
                                json_json.getString("marca"),
                                PRECIO_PREMIUM_GLOBAL,
                                COSTO_PRODUCTO_GLOBAL,
                                json_json.getInt("peso"),
                                json_json.getString("producto"),
                                1
                        );
                        */




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error volley", "Error: " + error.getMessage());
                // dialog.dismiss();

            }
        });
        // Adding request to request queue

        SliderVolleyRequest.getInstance(context).addToRequestQueue(searchMsg);



        return true;

    }







    public void DELETE_ITEM(String ID_PRODUCTO, Context ctx){


        DatabaseHandler db3 = new DatabaseHandler(context);

        boolean delete = db3.delete(ID_PRODUCTO);

        if(delete) {

            //Toast.makeText(context, "Borrado ID:  " + ID_PRODUCTO, Toast.LENGTH_SHORT).show();


            GetCartAdapter1.clear();
            recyclerView_global = GET_CART(ctx);

            //datos_pedido = (TextView)view.findViewById(R.id.datos_pedido);
            //datos_pedido.setText("TOTAL: "+GET_SUBTOTAL(ctx));


        }else{
            Toast.makeText(context, "ERROR:  " +ID_PRODUCTO, Toast.LENGTH_SHORT).show();

        }


        //update_icon_cart(ctx);

    }








    public void UPDATE_QTY(String id_producto, int cantidad, Context ctx){

        int qty_fin;
        DatabaseHandler db3 = new DatabaseHandler(context);

        if(cantidad == 0){
            qty_fin = 1;
        }else{
            qty_fin = cantidad;
        }

        Log.e("SPINNER VALOR SPINNER:", String.valueOf(qty_fin));
        Log.e("SPINNER ID PRODUCTO:", id_producto);

        db3.cambiar_cantidad(id_producto, qty_fin);

        GetCartAdapter1.clear();
        recyclerView_global = GET_CART(ctx);

       // datos_pedido = (TextView)view.findViewById(R.id.datos_pedido);
       // datos_pedido.setText("TOTAL: "+GET_SUBTOTAL(ctx));


        //update_icon_cart(ctx);


    }











    public boolean check_if_prod_in_cart(String id_producto){

        boolean res_boll = false;
        DatabaseHandler db3 = new DatabaseHandler(context);
        int res = db3.check_if_product_inf_cart(id_producto);
        if(res > 0){
            res_boll = true;
        }


        return res_boll;
    }














    public static void SHOW_POOP_UP_CART(final Context ctx) {

        final Functions userFunction = new Functions();

        GetCartAdapter1.clear();

        /*INICIA POPUP*/
        final PopupWindow mRecordWindow;
        view = View.inflate(ctx, R.layout.poopup_cart, null);

        mRecordWindow = new PopupWindow(view, -1, -1);
        mRecordWindow.showAtLocation(view, 17, 0, 0);
        mRecordWindow.setFocusable(true);
        mRecordWindow.setOutsideTouchable(false);
        mRecordWindow.setTouchable(false);

        final ImageView logo_cart = (ImageView)view.findViewById(R.id.logo_cart);
        final ImageButton close = (ImageButton)view.findViewById(R.id.closePopupBtn);
        //final Button delivery = (Button)view.findViewById(R.id.delivery);
        //final HTMLTextView datos_pedido_html = (HTMLTextView)view.findViewById(R.id.datos_pedido_html);
        final LinearLayout content_window = (LinearLayout)view.findViewById(R.id.content_window);
        //datos_pedido = (TextView)view.findViewById(R.id.datos_pedido);



        new AsyncTask<Object, Void, RecyclerView>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                datos_pedido.setText("Espere. Abriendo Carrito......");
                GetCartAdapter1.clear();

            }

            @Override
            protected RecyclerView doInBackground(Object... params) {

                recyclerView_global = GET_CART(ctx);
                return recyclerView_global;

            }


            @Override
            protected void onPostExecute(final RecyclerView params) {
                super.onPostExecute(params);


                new DownloadImageTask(ctx, logo_cart, "https://bestdream.store/Views/Default/img/logo_bestdream_dark.png").execute();


                /*
                delivery.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        //mRecordWindow.dismiss();
                        Intent i = new Intent(ctx, Delivery.class);
                        ctx.startActivity(i);
                        mRecordWindow.dismiss();



                    }
                });

                 */


                close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        mRecordWindow.dismiss();

                    }
                });



                datos_pedido.setText("TOTAL: "+GET_SUBTOTAL(ctx));


                //int precio_envio = userFunction.get_envio_costo();
                //datos_pedido_html.setText("Envío: <span style='color:red'> $"+precio_envio+".00 </span>");

                recyclerView_global = GET_CART(ctx);


                content_window.addView(params);



            } }.execute();





    }











    public static float GET_SUBTOTAL(Context ctx){

        float result = 0;
        float subtotal_prod = 0;

        DatabaseHandler db = new DatabaseHandler(ctx);
        JSONArray CART = null;
        try {
            CART = db.get_Cart_Json();

            for(int j = 0; j<CART.length(); j++) {

                JSONObject json_base = null;
                try {
                    json_base = CART.getJSONObject(j);

                    JSONArray feed = new JSONArray(json_base.getString("productos"));

                    JSONObject json_base_2 = null;

                    for(int i = 0; i<feed.length(); i++) {
                        try {
                            json_base_2 = feed.getJSONObject(i);

                            float sub_t_precio = Float.parseFloat(json_base_2.getString(KEY_PRECIO_PREMIUM));
                            int cantidad = json_base_2.getInt(KEY_CANTIDAD);

                            /*
                            int multi_fin = cantidad+1;
                            subtotal_prod = sub_t_precio*multi_fin;
                            */

                            subtotal_prod = sub_t_precio*cantidad;


                            result += subtotal_prod;
                            Log.i("-SUBTOTAL_PRODUCTO-", String.valueOf(subtotal_prod)+" -- ID: "+json_base_2.getString(KEY_ID_PRODUCTO));


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return result;
    }













    public static RecyclerView GET_CART(Context ctx){


        DatabaseHandler db = new DatabaseHandler(ctx);

        GetCartAdapter1.clear();

        Get_Cart_Adapter GetCartAdapter2;

        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(ctx, 1);

        Parcelable recyclerViewState = null;

        recyclerView_global = new RecyclerView(ctx);
        recyclerView_global.setHasFixedSize(true);
        recyclerView_global.setLayoutManager(recyclerViewlayoutManager);

        JSONArray CART = null;

        try {

            CART = db.get_Cart_Json();

            //Log.i("----CARRITO TOTAL---", String.valueOf(CART));


            for(int j = 0; j<CART.length(); j++) {

                JSONObject json_base = null;
                try {

                    json_base = CART.getJSONObject(j);

                    JSONArray feed = new JSONArray(json_base.getString("productos"));
                    //JSONArray feed =  json_base.getJSONArray("feed");

                    JSONObject json_base_2 = null;

                    for(int i = 0; i<feed.length(); i++) {

                        GetCartAdapter2 = new Get_Cart_Adapter();

                        try {

                            json_base_2 = feed.getJSONObject(i);

                            GetCartAdapter2.setprecio_premium(Float.parseFloat(json_base_2.getString(KEY_PRECIO_PREMIUM)));
                            GetCartAdapter2.setnombre_producto(json_base_2.getString(KEY_NOMBRE));
                            GetCartAdapter2.setgetid_producto(json_base_2.getString(KEY_ID_PRODUCTO));
                            GetCartAdapter2.setbar_code(json_base_2.getString(KEY_BAR_CODE));
                            GetCartAdapter2.setqty(json_base_2.getInt(KEY_CANTIDAD));
                            GetCartAdapter2.setimagen_comp(json_base_2.getString(KEY_IMAGEN));


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                        GetCartAdapter1.add(GetCartAdapter2);

                    }


                    recyclerView_global.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    recyclerViewadapter = new RecyclerCartViewAdapter(GetCartAdapter1, ctx);
                    recyclerView_global.setAdapter(recyclerViewadapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return recyclerView_global;




    }












    public static JSONArray GET_CART_JSON(Context ctx){

        DatabaseHandler db = new DatabaseHandler(ctx);


        JSONArray CART = null;
        JSONArray feed = null;
        try {
            CART = db.get_Cart_Json();
            for(int j = 0; j<CART.length(); j++) {

                JSONObject json_base = null;
                try {

                    json_base = CART.getJSONObject(j);

                    feed = new JSONArray(json_base.getString("productos"));
                    //JSONArray feed =  json_base.getJSONArray("feed");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Log.i("----CARRITO TOTAL---", String.valueOf(CART));
        return feed;


    }




    public static int total_products_in_cart(Context ctx){

        DatabaseHandler db = new DatabaseHandler(ctx);
        int res = db.Count_Cart();


        return res;

    }













}
