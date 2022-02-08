package com.bestdreamstore.admin_bestdream;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Faltantes_Adapter;
import com.bestdreamstore.admin_bestdream.ADAPTERS.SwipeToDeleteCallback;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Cart_Controller;
import com.bestdreamstore.admin_bestdream.CONTROLLER.RecyclerView_List_Faltantes_Adapter;
import com.bestdreamstore.admin_bestdream.CONTROLLER.Web_View_Controller;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.bestdreamstore.admin_bestdream.LIBS.HTMLTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Faltantes_Productos extends AppCompatActivity{


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


    EditText id_pedido_edit_text;
    HTMLTextView datos_clienta;
    ImageView image_prod, image_confirmation;
    JSONObject json;
    String id_pedido;
    Button delivery, borrar, imprimir_guia, reporte;
    Get_Faltantes_Adapter GetCartAdapter2;
    ImageButton back;
    Parcelable recyclerViewState = null;
    DatabaseHandler db;
    String id_order_fin, nombre_user;
    JSONArray productos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faltantes_productos);

        setFinishOnTouchOutside(false);

        db = new DatabaseHandler(getApplicationContext());



        cart = new Cart_Controller(getApplicationContext());
        web_view_controller = new Web_View_Controller(getApplicationContext());

        userFunctions = new Functions();
        GetCartAdapter1.clear();



         //nombre_user = userFunctions.get_name_user(getApplicationContext());




        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                //db.DELETE_CART();
                //MOSTRAR_CARRITO();
                finish();


            }
        });



        Log.i("EXTRAS:::", "----NO HAY---");
        id_order_fin = userFunctions.get_id_order_admin(getApplicationContext());




        RecyclerView.LayoutManager recyclerViewlayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView_global = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView_global.setHasFixedSize(true);
        recyclerView_global.setLayoutManager(recyclerViewlayoutManager);

        recyclerView_global.getRecycledViewPool().clear();









        PROCESAR_PETICION(id_order_fin);




        //MOSTRAR_CARRITO();
        enableSwipeToDeleteAndUndo();





    }








    @SuppressLint("StaticFieldLeak")
    public void PROCESAR_PETICION(final String id_order){

        new AsyncTask<Object, Void, JSONArray>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //response_server.setText("Espere Un Momento......");

            }


            @Override
            protected JSONArray doInBackground(Object... params) {
                JSONArray resultados_json = new JSONArray();




                json = userFunctions.ver_faltantes_productos(id_order);


                try {

                    resultados_json = json.getJSONArray("feed");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return resultados_json;

            }








            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(final JSONArray params) {
                super.onPostExecute(params);



                    for(int i = 0; i<params.length(); i++) {

                        GetCartAdapter2 = new Get_Faltantes_Adapter();


                        try {

                            JSONObject jObj = params.getJSONObject(i);

                            GetCartAdapter2.setid_pedido(jObj.getString("id_pedido"));
                            GetCartAdapter2.setnombre_producto(jObj.getString("nombre_producto"));
                            GetCartAdapter2.setid_producto(jObj.getInt("id_producto"));
                            GetCartAdapter2.setqty(jObj.getInt("qty"));
                            GetCartAdapter2.setimagen_producto(jObj.getString("imagen_producto"));


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                        GetCartAdapter1.add(GetCartAdapter2);


                    }






                recyclerView_global.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                recyclerViewadapter = new RecyclerView_List_Faltantes_Adapter(GetCartAdapter1, getApplicationContext());
                recyclerView_global.setAdapter(recyclerViewadapter);






            } }.execute();



    }











    @Override
    public void onBackPressed() {

        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        //db.DELETE_CART();
        //MOSTRAR_CARRITO();
        //finish();
    }




    private void enableSwipeToDeleteAndUndo() {



        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                recyclerViewadapter.notifyItemRemoved(position);

                GetCartAdapter1.remove(position);
                recyclerViewadapter.notifyDataSetChanged();




            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView_global);



    }








}