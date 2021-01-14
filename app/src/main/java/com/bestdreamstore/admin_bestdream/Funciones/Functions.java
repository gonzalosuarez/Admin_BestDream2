package com.bestdreamstore.admin_bestdream.Funciones;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;



public class Functions {


    private JSONParser jsonParser;

    JSONObject json_fin, json_json, images_extras;
    JSONArray feed_todos, feed_images_extras;

    String GLOBAL_STRING_VOLLEY_RETURN;

    private static final String KEY_PERMISOS = "permisos";
    private static final String KEY_NAME = "name";

    private static final String KEY_ID_PEDIDO = "id_pedido";

    // TESTEO EN SERVIDOR
    private static String loginURL = "https://bestdream.store/Admin/serach_login";
    private static String subir_pedido = "https://bestdream.store/Android/alta_pedido_android";
    public Functions(){
        jsonParser = new JSONParser();
    }



    String nombre, permisos, id_order;



    public int addUser(String nombre, String permisos, Context context) {

        DatabaseHandler db = new DatabaseHandler(context);
        int res = db.addUser(nombre, permisos);

        return res;

    }










    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);

        int count = db.getRowCount();
        if(count > 0){

            return true;

        }
        return false;

    }




    public String get_name_user(Context context){

        DatabaseHandler db = new DatabaseHandler(context);


        JSONArray USER = null;

        try {

            USER = db.getUserDetails();

            for(int j = 0; j<USER.length(); j++) {

                JSONObject json_base = null;
                try {

                    json_base = USER.getJSONObject(j);
                    JSONArray user_login = json_base.getJSONArray("user_login");
                    JSONObject JSON_NAME = user_login.getJSONObject(0);
                    nombre = JSON_NAME.getString(KEY_NAME);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return nombre;

    }







    public String get_permisos_user(Context context){

        DatabaseHandler db = new DatabaseHandler(context);


        JSONArray USER = null;

        try {

            USER = db.getUserDetails();

            for(int j = 0; j<USER.length(); j++) {

                JSONObject json_base = null;
                try {

                    json_base = USER.getJSONObject(j);
                    JSONArray user_login = json_base.getJSONArray("user_login");
                    JSONObject JSON_NAME = user_login.getJSONObject(0);
                    permisos = JSON_NAME.getString(KEY_PERMISOS);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return permisos;

    }







    public boolean cerrar_session_login(Context context){

        DatabaseHandler db = new DatabaseHandler(context);

        int count = db.reset_login();
        if(count > 0){

            return true;

        }else{

            return false;

        }



    }











    public JSONObject login_service(String email, String pass){

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("pass", pass);

        JSONObject json = jsonParser.makeHttpRequest(loginURL, "POST",params);
        // RETORNO JSON
        //Log.e("JSON----", "Es un Objeto");
        Log.e("TEST::LOGIN", String.valueOf(json));

        return json;


    }






    public boolean check_autorizacion(String user){

        boolean res_fin = false;

        String urlfin = "https://bestdream.store/Admin/check_autorizacion_empleados/?user="+user;
        HashMap<String, String> params = new HashMap<>();
        params.put("user", user);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        try {

            if(json.getInt("success") == 1){
                res_fin = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return res_fin;


    }









    public JSONObject get_details_bar_code(String bar_code){


        String urlfin = "https://bestdream.store/Admin/get_details_bar_code/?bar_code="+bar_code;
        HashMap<String, String> params = new HashMap<>();
        params.put("bar_code", bar_code);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);



        Log.e("DETALils: ", String.valueOf(json));
        return json;


    }





    public JSONObject ver_pedido_servidor(String id_pedido){

        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);
        params.put("tag", "ANDROID_NORMAL");

        JSONObject json = jsonParser.makeHttpRequest("https://bestdream.store/Android/details_pedido/"+id_pedido, "POST",params);
        // RETORNO JSON
        //Log.e("JSON----", "Es un Objeto");
        Log.e("PEDIDO::::", String.valueOf(json));

        return json;


    }




    public JSONObject ver_faltantes_productos(String id_pedido){

        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);
        params.put("tag", "ANDROID_NORMAL");

        JSONObject json = jsonParser.makeHttpRequest("https://bestdream.store/Android/productos_faltantes/"+id_pedido, "POST",params);
        // RETORNO JSON
        //Log.e("JSON----", "Es un Objeto");
        Log.e("PRODUCTOS::::", String.valueOf(json));

        return json;


    }







    public int armar_pedido_func(String nombre_user, String id_pedido){

        int res = 0;

        String urlfin = "https://bestdream.store/Android/armar_pedido/?nombre_user="+nombre_user+"&id_pedido="+id_pedido;
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);
        params.put("nombre_user", nombre_user);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        try {

            res = json.getInt("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return res;


    }








    public String get_id_pedido_bar_code_fedex(String bar_code_fedex){

        String res = "";

        String urlfin = "https://bestdream.store/Android/get_id_pedido_bar_code_fedex/?bar_code_fedex="+bar_code_fedex;

        Log.e("URL_DETAILS-----", urlfin);

        HashMap<String, String> params = new HashMap<>();
        params.put("bar_code_fedex", bar_code_fedex);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        try {

            res = json.getString("id_pedido");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return res;


    }




    public String autorizar_pedido(String codigo_id_pedido){

        String res = "null";

        String urlfin = "https://bestdream.store/Android/autorizar_pedido/?id_pedido="+codigo_id_pedido;

        Log.e("URL_DETAILS-----", urlfin);

        HashMap<String, String> params = new HashMap<>();
        params.put("codigo_id_pedido", codigo_id_pedido);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        try {

            res = json.getString("msg");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return res;


    }






    public String get_name_cliente(String id_pedido){
        String res1 = "";
        String res2 = "";


        String urlfin = "https://bestdream.store/Android/get_nombre_cliente_id_pedido/?id_pedido="+id_pedido;
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        try {


            res1 = json.getString("nombre_cliente");
            if (res1 != null) {
                res2 = json.getString("nombre_cliente");
            }else{
                res2 = "error";
            }


        } catch (JSONException e) {

            e.printStackTrace();

        }



        return res2;


    }







    public JSONObject ver_reportes_pedido(String id_pedido){

        String urlfin = "https://bestdream.store/Android/ver_reportes_pedido/?id_pedido="+id_pedido;
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        Log.e("REPORTES_PEDIDO: ", String.valueOf(json));

        return json;


    }







    public JSONObject enviar_peticion_monedero(String user_admin, String id_pedido, String productos){

        String urlfin = "https://bestdream.store/Android/enviar_peticion_monedero/";
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);
        params.put("user_admin", user_admin);
        params.put("productos", productos);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        return json;


    }








    public int encajar_pedido_func(String nombre_user, String id_pedido){

        int res = 0;

        String urlfin = "https://bestdream.store/Android/encajar_pedido_user/?nombre_user="+nombre_user+"&id_pedido="+id_pedido;
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);
        params.put("nombre_user", nombre_user);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        try {

            res = json.getInt("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return res;


    }




    public JSONObject cerrar_pedido_func(String nombre_user, String id_pedido){

        String urlfin = "https://bestdream.store/Android/cerrar_pedido/?nombre_user="+nombre_user+"&id_pedido="+id_pedido;
        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);
        params.put("nombre_user", nombre_user);

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);

        return json;


    }




    public JSONArray get_all_users_admin(){

        String urlfin = "https://bestdream.store/Android/get_all_users_admin/";
        HashMap<String, String> params = new HashMap<>();
        JSONArray arr = null;

        JSONObject json = jsonParser.makeHttpRequest(urlfin, "POST",params);
        try {

             arr = json.getJSONArray("feed");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arr;


    }






    public String get_key_pedido(String id_pedido){

        String request_url = "https://bestdream.store/Admin/get_key_pedido/"+id_pedido;

        String key_pedido = "";


        HashMap<String, String> params = new HashMap<>();
        params.put("id_pedido", id_pedido);
        JSONObject json = jsonParser.makeHttpRequest(request_url, "POST",params);

        try {

            key_pedido = json.getString("key_pedido");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return key_pedido;

    }








    public int add_Id_Order(String id_pedido, Context context) {

        DatabaseHandler db = new DatabaseHandler(context);
        int res = db.ADD_ID_PEDIDO(id_pedido);

        return res;

    }







    public String get_id_order_admin(Context context){

        DatabaseHandler db = new DatabaseHandler(context);


        JSONArray USER = null;

        try {

            USER = db.getID_Order();

            for(int j = 0; j<USER.length(); j++) {

                JSONObject json_base = null;
                try {

                    json_base = USER.getJSONObject(j);
                    JSONArray user_login = json_base.getJSONArray("id_order");
                    JSONObject JSON_NAME = user_login.getJSONObject(0);
                    id_order = JSON_NAME.getString(KEY_ID_PEDIDO);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return id_order;

    }








    public JSONArray get_jsonarray_cart(Context ctx){

        JSONArray CART = null;
        JSONObject PRODUCTOS = null;
        JSONArray PRODUCTOS_ARR = null;


        DatabaseHandler db = new DatabaseHandler(ctx);

        try {

            CART = db.get_Cart_Json();


            PRODUCTOS = CART.getJSONObject(0);
            PRODUCTOS_ARR = ordenar_arr_bar_code(PRODUCTOS.getJSONArray("productos"));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        /*2223668454*/
        return PRODUCTOS_ARR;




    }




public static JSONArray ordenar_arr_bar_code(JSONArray array) {

        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {

            try {

                jsons.add(array.getJSONObject(i));
                Log.i("JSON_COMPARE:::", "----" + jsons + "---");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;

                try {

                    if (!lhs.has("bar_code")){

                        Log.i("NO_TIENE_BAR_CODE", "-------");
                        lid = "0";
                        rid = "0";


                    }else{

                        Log.i("SI_TIENE_BAR_CODE", "-------");
                        lid = lhs.getString("bar_code");
                        rid = rhs.getString("bar_code");


                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }







    public static JSONArray ordenar_arr_marca(JSONArray array) {

        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {

            try {

                jsons.add(array.getJSONObject(i));
                Log.i("JSON_COMPARE:::MARCA", "----" + jsons + "---");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;

                try {

                        lid = lhs.getString("marca");
                        rid = rhs.getString("marca");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }






    public static JSONArray ordenar_arr_categoria(JSONArray array) {

        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {

            try {

                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;

                try {

                    lid = lhs.getString("categoria");
                    rid = rhs.getString("categoria");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }







    public boolean insert_all_cart(JSONArray productos, Context ctx){

        DatabaseHandler db = new DatabaseHandler(ctx);
        return db.insert_all_cart(productos);

    }








    public JSONObject add_bar_code_id_server(String id_producto, String bar_code, String permisos){

        String request_url = "https://bestdream.store/Admin/add_bar_code_server/";

        Log.e("PERMISOS-----", permisos);


        HashMap<String, String> params = new HashMap<>();
        params.put("bar_code", bar_code);
        params.put("id_producto", id_producto);
        params.put("permisos", permisos);


        JSONObject json = jsonParser.makeHttpRequest(request_url, "POST",params);
        // RETORNO JSON
        //Log.e("JSON----", "Es un Objeto");
        //Log.e("TEST::CREATE_NEW_CUEMTA", String.valueOf(json));

        return json;

    }






    public JSONObject check_if_bar_code_exist(String id_prod){


        String request_url = "https://bestdream.store/Admin/check_if_bar_code_exist/?id_producto="+id_prod;
        String res ="";

        Log.e("URL_DETAILS-----", request_url);


        HashMap<String, String> params = new HashMap<>();
        params.put("id_prod", id_prod);

        JSONObject json = jsonParser.makeHttpRequest(request_url, "GET",params);



        return json;

    }






}
