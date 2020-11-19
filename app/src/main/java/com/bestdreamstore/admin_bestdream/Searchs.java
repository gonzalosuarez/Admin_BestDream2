package com.bestdreamstore.admin_bestdream;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_List_Pedidos_Adapter;
import com.bestdreamstore.admin_bestdream.CONTROLLER.RecyclerView_List_Pedidos_Adapter;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.StrictMode;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;



public class Searchs extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Get_List_Pedidos_Adapter> Get_List_Pedidos_Adapter_1;
    Get_List_Pedidos_Adapter Get_List_Pedidos_Adapter_2;



    RecyclerView recyclerView;




    private Parcelable recyclerViewState;






    int offset_global = 0;
    Toolbar mToolbar;
    boolean paginador = false;
    int FEED_SIZE_GLOBAL;
    int contador = 0;
    String URL_GLOBAL = "";
    boolean scrool_action = true;

    Button  ver_mas;
    LinearLayout layout_button_espere;



    RecyclerView.Adapter recyclerViewadapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchs);








        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        mToolbar = (Toolbar) findViewById(R.id.toolbar_search);
        mToolbar.setTitle("Buscando....");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ver_mas = (Button) findViewById(R.id.ver_mas);
        ver_mas.setVisibility(View.INVISIBLE);


        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/


        String get_extras = get_extras_and_search();

        if(!get_extras.equals("null")){

            Log.i("EXTRAS:::", "----" + get_extras + "---");
            URL_GLOBAL = get_extras;
           JSON_DATA_WEB_CALL(URL_GLOBAL, 20, 0);


        }else{


            Log.i("EXTRAS:::", "----NO HAY---");
            /*INICIAMOS SI NO HAY EXTRAS*/
            URL_GLOBAL = "https://bestdream.store/Android/ver_pedidos_admin/?";
            JSON_DATA_WEB_CALL(URL_GLOBAL, 20, offset_global);
            /*INICIAMOS SI NO HAY EXTRAS*/


        }


        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/
        /*RECIBIMOS CATEGORIAS EN INTENT O INICIAMOS*/







        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);



        mSwipeRefreshLayout.setRefreshing(false);






        Get_List_Pedidos_Adapter_1 = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_searchs);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


               // if(scrool_action){ }

                    /*MANTENEMOS LA POSITION ORIGINAL DEL SCROLL*/
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                    /*MANTENEMOS LA POSITION ORIGINAL DEL SCROLL*/


                if (!recyclerView.canScrollVertically(1)) {

                    ver_mas.setVisibility(View.VISIBLE);
                    ver_mas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if(FEED_SIZE_GLOBAL == 0){

                                URL_GLOBAL = "https://bestdream.store/Android/ver_pedidos_admin/?";
                                JSON_DATA_WEB_CALL(URL_GLOBAL , 20, 0);
                                scrool_action = true;


                            }else{


                                JSON_DATA_WEB_CALL(URL_GLOBAL, 20, offset_global);
                                scrool_action = true;

                            }


                        }
                    });







                }



            }
        });












    }





    @Override
    public void onRefresh() {

        REFRESH_SEARCH_DOWN();

    }




    @Override
    public void onBackPressed() {
        finish();

    }









    public void REFRESH_SEARCH_DOWN(){

        mSwipeRefreshLayout.setRefreshing(true);
        recyclerView.scrollToPosition(1);
        ver_mas.setVisibility(View.INVISIBLE);


        Get_List_Pedidos_Adapter_1.clear(); // clear list

        offset_global = 0;
        paginador = false;
        FEED_SIZE_GLOBAL = 0;
        contador = 0;
        scrool_action = false;

        URL_GLOBAL = "https://bestdream.store/Android/ver_pedidos_admin/?";
        JSON_DATA_WEB_CALL(URL_GLOBAL , 20, 0);

        recyclerViewadapter.notifyDataSetChanged();

    }






    public void JSON_DATA_WEB_CALL(String PETICION_URL, int limit_, int offset_){


        String URL_FINAL = "";
        ver_mas.setVisibility(View.INVISIBLE);


        //Toast.makeText(Searchs.this, "ARRAY_SIZE:::....."+FEED_SIZE_GLOBAL, Toast.LENGTH_LONG).show();


        if(paginador){

            offset_global = limit_ + offset_;

            URL_FINAL = PETICION_URL+"limit="+limit_+"&offset="+offset_global;


        }else{

            URL_FINAL = PETICION_URL+"limit="+limit_+"&offset="+offset_;
            paginador = true;

        }






        String URL_ENCODE = reemplazar_espacios_blanco_url(URL_FINAL);
        Log.i("URL_ENCODE", URL_FINAL);


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



                            JSONArray array = response.getJSONArray("feed");
                            int cout_busqueda = response.getInt("num_rows");
                            int array_busqueda_parcial = response.getInt("num_rows_busqueda_parcial");


                            mToolbar.setTitle("Total Pedidos:"+cout_busqueda);


                            Log.i("ARRAY_BUSQUEDA_PARCIAL:",  array_busqueda_parcial + "---");
                            Log.i("TOTAL_BUSQUEDA:",  cout_busqueda + "---");
                            Log.i("COUNTERS_OFFSET_GLOBAL:",  offset_global + "---");




                            if(offset_global >= cout_busqueda){

                                    Toast.makeText(Searchs.this, "No Hay Mas Resultados", Toast.LENGTH_LONG).show();
                                    scrool_action = false;

                            }else{

                                    JSON_PARSE_DATA_AFTER_WEBCALL(array, cout_busqueda);
                                    //mSwipeRefreshLayout.setRefreshing(true);
                                    //Toast.makeText(Searchs.this, "Buscando", Toast.LENGTH_LONG).show();


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








    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array, int count_busqueda_total) {


        FEED_SIZE_GLOBAL = array.length();



        Log.i("ARRAY_SIZE:", "----" + FEED_SIZE_GLOBAL + "---");
        Log.i("OFFSET_GLOBAL:", "----" + offset_global + "---");

        incrementar_contador();

        for(int i=0;i<array.length();i++){
            // Get current json object


            Get_List_Pedidos_Adapter_2 = new Get_List_Pedidos_Adapter();

            try {


                JSONObject json_base_2 = array.getJSONObject(i);


                Get_List_Pedidos_Adapter_2.setid_pedido(json_base_2.getString("id_pedido"));



            } catch (JSONException e) {

                e.printStackTrace();
            }



            Get_List_Pedidos_Adapter_1.add(Get_List_Pedidos_Adapter_2);


            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewadapter = new RecyclerView_List_Pedidos_Adapter(Get_List_Pedidos_Adapter_1,  this);

            recyclerView.setAdapter(recyclerViewadapter);

            mSwipeRefreshLayout.setRefreshing(false);

            scrool_action = true;



        }





    }









    public void incrementar_contador(){
        Log.i("----CONTADOR:--", String.valueOf(contador));
        contador++;
    }




    public String reemplazar_espacios_blanco_url(String url){

        return url.replace(" ", "%20");


    }




    public String get_extras_and_search(){

        String categoria_entrante = "null";
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("URL_GLOBAL")) {

                categoria_entrante = extras.getString("URL_GLOBAL");

            }
        }else{

            categoria_entrante = "null";

        }


        return categoria_entrante;

    }











}
