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
import com.bestdreamstore.admin_bestdream.Armado_Pedidos_Nuevo;
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


public class Controlador_Carrito {


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


    public Controlador_Carrito(Context context){

        super();
        this.context = context;





    }













    public void DELETE_ITEM(String ID_PRODUCTO, Context ctx){

        DatabaseHandler db3 = new DatabaseHandler(context);

        boolean delete = db3.delete(ID_PRODUCTO);

        if(delete) {

            GetCartAdapter1.clear();
            recyclerView_global = GET_CART(ctx);

        }else{
            Toast.makeText(context, "ERROR:  " +ID_PRODUCTO, Toast.LENGTH_SHORT).show();

        }


    }





    public boolean DELETE_ITEM_BAR_CODE(String BAR_CODE, Context ctx){

        DatabaseHandler db3 = new DatabaseHandler(context);

        boolean delete = db3.delete_bar_code(BAR_CODE);


        return delete;


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

    }






    public int UPDATE_QTY_BAR_CODE(String bar_code, Context ctx){

        DatabaseHandler db3 = new DatabaseHandler(context);
        return db3.restar_uno_item(bar_code);

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






    public int check_if_prod_in_cart_bar_code(String bar_code){

        DatabaseHandler db3 = new DatabaseHandler(context);
        int res = db3.check_if_product_inf_cart_bar_code(bar_code);
        return res;


    }











    public static void SHOW_POOP_UP_CART(final Context ctx) {


        DatabaseHandler db = new DatabaseHandler(ctx);

        GetCartAdapter1.clear();

        Get_Cart_Adapter GetCartAdapter2;

        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(ctx, 1);

        Parcelable recyclerViewState = null;

        final RecyclerView recyclerView_global = (RecyclerView) ((Armado_Pedidos_Nuevo) ctx).findViewById(R.id.content_window);

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

       // final RecyclerView recyclerView_global = (RecyclerView) ((Activity) ctx).findViewById(R.id.recycler_view_nuevo);

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
