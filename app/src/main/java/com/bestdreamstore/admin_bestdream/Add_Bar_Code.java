package com.bestdreamstore.admin_bestdream;


import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import com.bestdreamstore.admin_bestdream.CONTROLLER.RecyclerViewAdapter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bestdreamstore.admin_bestdream.ADAPTERS.GetDataAdapter;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Add_Bar_Code extends AppCompatActivity {


    List<GetDataAdapter> GetDataAdapter1;
    GetDataAdapter GetDataAdapter2;


    RecyclerView recyclerView;



    private Parcelable recyclerViewState;






    int offset_global = 0;
    Toolbar mToolbar;
    boolean paginador = false;
    int FEED_SIZE_GLOBAL;
    int contador = 0;
    String URL_GLOBAL = "";




    RecyclerView.Adapter recyclerViewadapter;



    private static final String KEY_PERMISOS = "permisos";
    private static final String KEY_NAME = "name";

    TextView response_server;
    ImageView logo;
    String usuario, permisos, GLOBAL_SEARCH;
    Functions userFunctions;
    DatabaseHandler db;





    public static View view;

    EditText like;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bar_code);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bar_code);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        userFunctions = new Functions();

        like = (EditText) findViewById(R.id.like);
        like.addTextChangedListener(new TextWatcher() {

            private Timer timer=new Timer();
            private final long DELAY = 1000; // milliseconds

            @Override
            public void afterTextChanged(final Editable s) {


                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {


                                limpiar_busqueda();
                                URL_GLOBAL = "https://bestdream.store/Android/like/?like="+s.toString()+"&";
                                JSON_DATA_WEB_CALL(URL_GLOBAL, 20, offset_global);


                            }
                        },
                        DELAY
                );





            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {




            }
        });









        GetDataAdapter1 = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_like);

        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if (!recyclerView.canScrollVertically(1)) {

                    /*MANTENEMOS LA POSITION ORIGINAL DEL SCROLL*/
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                    /*MANTENEMOS LA POSITION ORIGINAL DEL SCROLL*/

                    Toast.makeText(Add_Bar_Code.this, "Cargando.....", Toast.LENGTH_LONG).show();

                    if(FEED_SIZE_GLOBAL == 0){


                        URL_GLOBAL = "https://bestdream.store/Android/like/?like="+GLOBAL_SEARCH+"&";
                        JSON_DATA_WEB_CALL(URL_GLOBAL , 20, offset_global);


                    }else{

                        JSON_DATA_WEB_CALL(URL_GLOBAL, 20, offset_global);

                    }




                }
            }
        });









    }









    @Override
    public void onBackPressed() {
        finish();

    }






    public void JSON_DATA_WEB_CALL(String PETICION_URL, int limit_, int offset_){


        String URL_FINAL = "";
        
        if(paginador){

            offset_global = limit_ + offset_;
            URL_FINAL = PETICION_URL+"limit="+limit_+"&offset="+offset_global;

            //https://bestdream.store/Android/paginador_search/?limit=7&offset=0

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



                            JSONArray array = response.getJSONArray("feed_todos");
                            int cout_busqueda_total = response.getInt("num_rows");


                            JSON_PARSE_DATA_AFTER_WEBCALL(array, cout_busqueda_total);


                            //Log.i("ARRAY_FINAL", "JSON" + array + "---");
                            //Log.i("ARRAY_COUNT", "count" + cout_busqueda_total + "---");


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



        Log.i("COUNTERS_COUNT_TOTAL", "count" + count_busqueda_total + "---");
        Log.i("COUNTERS_OFFSET_GLOBAL", "OFFSET" + offset_global + "---");






        FEED_SIZE_GLOBAL = array.length();

        incrementar_contador();

        for(int i=0;i<array.length();i++){
            // Get current json object


            GetDataAdapter2 = new GetDataAdapter();

            try {


                JSONObject json_base_2 = array.getJSONObject(i);

                String images_extras_txt = json_base_2.getString("images_extras");
                JSONObject images_extras = new JSONObject(images_extras_txt);
                JSONArray feed_img = images_extras.getJSONArray("feed");

                Log.i("ID", json_base_2.getString("id")+" ::FEED"+feed_img);

                ArrayList<String> arr_fin_extras = new ArrayList<String>();

                /*AGREGAMNOS LA PRINCIPAL*/
                arr_fin_extras.add(json_base_2.getString("imagen"));
                /*AGREGAMNOS LA PRINCIPAL*/



                if(feed_img.length() > 0){

                    for(int x=0;x<feed_img.length();x++){

                        JSONObject json_base_3 = feed_img.getJSONObject(x);
                        //Log.i("LLEVA_IMAGEN_EXTRA::", json_base_3.getString("imagen"));
                        arr_fin_extras.add(json_base_3.getString("imagen"));

                    }

                }


                Random rand = new Random();
                String random = arr_fin_extras.get(rand.nextInt(arr_fin_extras.size()));





                GetDataAdapter2.setnombre(json_base_2.getString("nombre"));
                GetDataAdapter2.setImageServerUrl(random);
                GetDataAdapter2.setid(json_base_2.getString("id"));
                GetDataAdapter2.setprecio_mayoreo(json_base_2.getString("precio_mayoreo"));
                GetDataAdapter2.setdecimales_mayoreo(json_base_2.getString("decimales_mayoreo"));
                GetDataAdapter2.setcosto_producto(json_base_2.getString("costo_producto"));
                GetDataAdapter2.setdecimales_costo(json_base_2.getString("decimales_costo"));
                GetDataAdapter2.setpeso(json_base_2.getString("peso"));
                GetDataAdapter2.setmarca(json_base_2.getString("marca"));
                GetDataAdapter2.setproducto(json_base_2.getString("producto"));
                GetDataAdapter2.setcategoria(json_base_2.getString("categoria"));



            } catch (JSONException e) {

                e.printStackTrace();
            }



            GetDataAdapter1.add(GetDataAdapter2);


            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1,  this);

            recyclerView.setAdapter(recyclerViewadapter);



        }





    }









    public void incrementar_contador(){
        Log.i("----CONTADOR:--", String.valueOf(contador));
        contador++;
    }




    public String reemplazar_espacios_blanco_url(String url){

        return url.replace(" ", "%20");


    }




    public void limpiar_busqueda(){


        offset_global = 0;
        GetDataAdapter1.clear();
        paginador = false;


    }






}