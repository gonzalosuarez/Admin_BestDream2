package com.bestdreamstore.admin_bestdream.DATA_BASE;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileNotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DatabaseHandler extends SQLiteOpenHelper {

    //private SQLiteHelper sqLiteHelper;
    // VARIABLES ESTATICAS
    private static final int DATABASE_VERSION = 14;



    // NOMBRE DE TABLA LOGIN
    private static final String TABLE_LOGIN = "login_admin_fin_2";
    private static final String TABLE_CART = "cart_administracion_5";
    private static final String TABLE_ID_PEDIDO = "id_pedido_admin_2";
    private static final String TABLE_ERRORES = "errores_2";






    // LOGIN NOMBRE DE COLUMNAS

    private static final String KEY_ID = "id";

    private static final String KEY_PERMISOS = "permisos";

    private static final String KEY_FECHA = "fecha";


    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "image_url_user";
    private static final String KEY_ID_PEDIDO = "id_pedido";





    private static final String KEY_ID_PRODUCTO = "id_producto";
    private static final String KEY_NOMBRE = "nombre_producto";
    private static final String KEY_PRECIO_PREMIUM = "precio_premium";
    private static final String KEY_COSTO_PRODUCTO = "costo_producto";
    private static final String KEY_BAR_CODE = "bar_code";
    private static final String KEY_CANTIDAD = "cantidad";
    private static final String KEY_COSTO_ENVIO = "costo_envio";
    private static final String KEY_MARCA = "marca";
    private static final String KEY_PRODUCTO = "producto";
    private static final String KEY_CATEGORIA = "categoria";
    private static final String KEY_IMAGEN= "imagen_comp";
    private static final String KEY_TIPO_ALTA_CART= "tipo_alta_cart";
    private static final String KEY_ERROR= "key_error";





    public DatabaseHandler(Context context) {

        super(context, TABLE_CART,null, DATABASE_VERSION);

    }




    String CREATE_TABLE_ID_PEDIDO = "CREATE TABLE IF NOT EXISTS " + TABLE_ID_PEDIDO + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_PEDIDO + " TEXT" + ")";



    String CREATE_TABLE_LIST_ERRORS = "CREATE TABLE IF NOT EXISTS " + TABLE_ERRORES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_PRODUCTO + " TEXT" + ")";


    String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_PERMISOS + " TEXT" + ")";



    String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS " + TABLE_CART + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_PRODUCTO + " TEXT,"
            + KEY_NOMBRE + " TEXT,"
            + KEY_IMAGEN + " TEXT,"
            + KEY_CATEGORIA + " TEXT,"
            + KEY_MARCA + " TEXT,"
            + KEY_PRECIO_PREMIUM + " TEXT,"
            + KEY_COSTO_PRODUCTO + " TEXT,"
            + KEY_BAR_CODE + " TEXT,"
            + KEY_COSTO_ENVIO + " INTEGER,"
            + KEY_PRODUCTO + " TEXT,"
            + KEY_TIPO_ALTA_CART + " TEXT,"
            + KEY_ERROR + " TEXT,"
            + KEY_CANTIDAD + " INTEGER" + ")";





    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creaci�n de la tabla
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_ID_PEDIDO);
        db.execSQL(CREATE_TABLE_LIST_ERRORS);


    }





    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ERRORES**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ERRORES**************************************************/
    /****************************************************************************************/










    public int add_Error() {

        int res_fin = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_PRODUCTO, "ERR");


        long result = db.insert(TABLE_ERRORES, null, values);
        if(result == -1){
            res_fin = 0;
        }else{
            res_fin = 1;
        }

        Log.e("error_nuevo:", String.valueOf(result));

        db.close(); // CERRAR CONEXION

        return res_fin;
    }




    public int reset_errores(){

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_ERRORES, null, null);
        db.close();

        return result;


    }



    public int get_num_errors() {

        String countQuery = "SELECT  * FROM " + TABLE_ERRORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }


    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ERRORES**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ERRORES**************************************************/
    /****************************************************************************************/






    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ID_PEDIDO**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ID_PEDIDO**************************************************/
    /****************************************************************************************/




    public int ADD_ID_PEDIDO(String id_pedido) {

        int res_fin = 0;

        if(if_exist_id_pedido()){


            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues args = new ContentValues();
            args.put(KEY_ID_PEDIDO, id_pedido);

            db.update(TABLE_ID_PEDIDO, args, null, null);

            res_fin = 1;


        }else{


            ContentValues values = new ContentValues();
            values.put(KEY_ID_PEDIDO, id_pedido);

            SQLiteDatabase db = this.getWritableDatabase();
            long result = db.insert(TABLE_ID_PEDIDO, null, values);
            if(result == -1){
                res_fin = 0;
            }else{
                res_fin = 1;
            }

            db.close(); // CERRAR CONEXION

        }



        return res_fin;
    }





    public boolean if_exist_id_pedido() {
        String countQuery = "SELECT  * FROM " + TABLE_ID_PEDIDO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        if(rowCount > 0){

            return true;

        }
        return false;


    }





    public JSONArray getID_Order() throws JSONException, FileNotFoundException {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_ID_PEDIDO, null);
        cursor.moveToFirst();

        JSONObject User_Login = new JSONObject();

        JSONArray UserArray = new JSONArray();
        JSONArray UserArray_Fin = new JSONArray();

        int i = 0;
        while (!cursor.isAfterLast()) {

            JSONObject user = new JSONObject();
            try {

                user.put(KEY_ID_PEDIDO, cursor.getString(cursor.getColumnIndex(KEY_ID_PEDIDO)));
                cursor.moveToNext();

                UserArray.put(i, user);
                i++;

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

        User_Login.put("id_order", UserArray);
        UserArray_Fin.put(User_Login);

        db.close(); // CERRAR CONEXION


        return UserArray_Fin;
    }




    public int reset_id_pedido(){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_ID_PEDIDO, null, null);
        db.close();

        return result;


    }





    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ID_PEDIDO**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************ID_PEDIDO**************************************************/
    /****************************************************************************************/






    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************LOGIN**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************LOGIN**************************************************/
    /****************************************************************************************/



    /**
     *INSERTAR A DE BASE DE DATOS
     * */
    public int addUser(String nombre, String permisos) {

        int res_fin = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, nombre); // Name
        values.put(KEY_PERMISOS, permisos);


        long result = db.insert(TABLE_LOGIN, null, values);
        if(result == -1){
            res_fin = 0;
        }else{
            res_fin = 1;
        }

        db.close(); // CERRAR CONEXION

        return res_fin;
    }


    /**
     * OBTENER DATOS DE USUSARIO DE BASE DE DATOS
     * */



    public JSONArray getUserDetails() throws JSONException, FileNotFoundException {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_LOGIN, null);
        cursor.moveToFirst();

        JSONObject User_Login = new JSONObject();

        JSONArray UserArray = new JSONArray();
        JSONArray UserArray_Fin = new JSONArray();

        int i = 0;
        while (!cursor.isAfterLast()) {


            JSONObject user = new JSONObject();
            try {

                user.put(KEY_NAME, cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.put(KEY_PERMISOS, cursor.getString(cursor.getColumnIndex(KEY_PERMISOS)));

                cursor.moveToNext();

                UserArray.put(i, user);
                i++;

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

        User_Login.put("user_login", UserArray);
        UserArray_Fin.put(User_Login);

        db.close(); // CERRAR CONEXION


        return UserArray_Fin;
    }



    /////////////////////////////////////////ESTATUS DE LOGEO///////////////////////////////////////////////////////////////



    /**
     * OBTENER ESTATUS DE LOGIN
     * REGRESAR TRUE SI LOS ROWS ESTAN EN LA TABLA
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // REGRESAR CONTADOR
        return rowCount;
    }



    /**
     * RECREAR BASE DE DATOS
     * BORRAR TODAS LAS TABLAS Y CREAR NUEVAMENTE
     * */
    public int reset_login(){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_LOGIN, null, null);
        db.close();

        return result;


    }





    /////////////////////////////////////////


    // BORRAR TABLAS DE SESSION LOGIN | LOGOUT
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // BORRAR TABLA VIEJA SI EXISTE
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // CREAR NUEVAMENTE LA TABLA
        onCreate(db);
    }





    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************LOGIN**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************LOGIN**************************************************/
    /****************************************************************************************/




    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************CART**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************CART**************************************************/
    /****************************************************************************************/


public boolean insert_all_cart(JSONArray CART){

    boolean RES_FINAL = false;
    SQLiteDatabase db = this.getWritableDatabase();
    JSONObject json_base_2 = null;

    for(int i = 0; i<CART.length(); i++) {

        try {

            json_base_2 = CART.getJSONObject(i);

            ContentValues values = new ContentValues();
            values.put(KEY_ID_PRODUCTO, json_base_2.getString(KEY_ID_PRODUCTO)); // Name
            values.put(KEY_NOMBRE, json_base_2.getString(KEY_NOMBRE));
            values.put(KEY_IMAGEN, json_base_2.getString(KEY_IMAGEN));
            values.put(KEY_CATEGORIA, "categoria");
            values.put(KEY_MARCA, "marca");
            values.put(KEY_PRECIO_PREMIUM, json_base_2.getString(KEY_PRECIO_PREMIUM));
            values.put(KEY_COSTO_PRODUCTO, "costo_producoto");
            values.put(KEY_BAR_CODE, json_base_2.getString(KEY_BAR_CODE));
            values.put(KEY_COSTO_ENVIO, "costo_envio");
            values.put(KEY_PRODUCTO, "producto");
            values.put(KEY_TIPO_ALTA_CART, "tipo_alta_cart");
            values.put(KEY_ERROR, "key_error");
            values.put(KEY_CANTIDAD, json_base_2.getInt(KEY_CANTIDAD));


            // INSERTAR///////////////////
            db.insert(TABLE_CART, null, values);



            if (i == CART.length()-1) {
                RES_FINAL = true;
                db.close();
            }


        } catch (JSONException e) {

            e.printStackTrace();
        }



    }

    return RES_FINAL;

}






    public int check_if_product_inf_cart(String id_producto){



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + KEY_ID_PRODUCTO + " = ?", new String[] {id_producto});
        Log.i("Number of Records"," :: "+cursor.getCount());


        db.close();

        return cursor.getCount();




    }



    public int check_if_product_inf_cart_bar_code(String bar_code){

      int num_rows = 0;

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + KEY_BAR_CODE + " = ?",new String[]{bar_code});
        if (cursor.moveToFirst()) {
            num_rows = cursor.getInt(cursor.getColumnIndex(KEY_CANTIDAD)); //<< get the data from the column
        }

        return num_rows;


    }



    public JSONArray get_Cart_Json() throws JSONException, FileNotFoundException {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_CART, null);
        cursor.moveToFirst();

        JSONObject Cart_Total = new JSONObject();


        JSONArray ContactArray = new JSONArray();
        JSONArray ContactArray_Fin = new JSONArray();

        int i = 0;
        while (!cursor.isAfterLast()) {


            JSONObject cart = new JSONObject();


            try {

                cart.put(KEY_ID, cursor.getString(cursor.getColumnIndex(KEY_ID)));
                cart.put(KEY_ID_PRODUCTO, cursor.getString(cursor.getColumnIndex(KEY_ID_PRODUCTO)));
                cart.put(KEY_NOMBRE, cursor.getString(cursor.getColumnIndex(KEY_NOMBRE)));
                cart.put(KEY_IMAGEN, cursor.getString(cursor.getColumnIndex(KEY_IMAGEN)));
                cart.put(KEY_CATEGORIA, cursor.getString(cursor.getColumnIndex(KEY_CATEGORIA)));
                cart.put(KEY_MARCA, cursor.getString(cursor.getColumnIndex(KEY_MARCA)));
                cart.put(KEY_PRECIO_PREMIUM, cursor.getString(cursor.getColumnIndex(KEY_PRECIO_PREMIUM)));
                cart.put(KEY_COSTO_PRODUCTO, cursor.getString(cursor.getColumnIndex(KEY_COSTO_PRODUCTO)));
                cart.put(KEY_BAR_CODE, cursor.getString(cursor.getColumnIndex(KEY_BAR_CODE)));
                cart.put(KEY_COSTO_ENVIO, cursor.getString(cursor.getColumnIndex(KEY_COSTO_ENVIO)));
                cart.put(KEY_PRODUCTO, cursor.getString(cursor.getColumnIndex(KEY_PRODUCTO)));
                cart.put(KEY_TIPO_ALTA_CART, cursor.getString(cursor.getColumnIndex(KEY_TIPO_ALTA_CART)));
                cart.put(KEY_ERROR, cursor.getString(cursor.getColumnIndex(KEY_ERROR)));
                cart.put(KEY_CANTIDAD, cursor.getString(cursor.getColumnIndex(KEY_CANTIDAD)));

                cursor.moveToNext();

                ContactArray.put(i, cart);
                i++;

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }






        Cart_Total.put("productos", ContactArray);
        ContactArray_Fin.put(Cart_Total);

        db.close(); // CERRAR CONEXION


        return ContactArray_Fin;
    }








    public int cambiar_cantidad(String id_producto, Integer cantidad){


        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues args = new ContentValues();
        args.put(KEY_CANTIDAD, cantidad-1);

        return db.update(TABLE_CART, args,  KEY_ID_PRODUCTO +"=?", new String[]{id_producto});



    }




    public int get_cantidad_item_bar_code(String bar_code){

        int num_rows = 0;

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + KEY_BAR_CODE + " = ?",new String[]{bar_code});
        if (cursor.moveToFirst()) {
            num_rows = cursor.getInt(cursor.getColumnIndex(KEY_CANTIDAD)); //<< get the data from the column
        }

        return num_rows;


    }




    public int restar_uno_item(String bar_code){


        SQLiteDatabase db = this.getReadableDatabase();

        int num_total = get_cantidad_item_bar_code(bar_code)-1;

        ContentValues args = new ContentValues();
        args.put(KEY_CANTIDAD, num_total);

        return db.update(TABLE_CART, args,  KEY_BAR_CODE +"=?", new String[]{bar_code});



    }






    public boolean delete(String id_producto){

        SQLiteDatabase db = this.getReadableDatabase();

        return db.delete(TABLE_CART, KEY_ID_PRODUCTO + "=?", new String[]{id_producto}) > 0;


    }




    public boolean delete_bar_code(String bar_code){

        SQLiteDatabase db = this.getReadableDatabase();

        return db.delete(TABLE_CART, KEY_BAR_CODE + "=?", new String[]{bar_code}) > 0;


    }




    public int Count_Cart() {

        int total_productos = 0;
        int i = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_CART, null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {

            total_productos += cursor.getInt(cursor.getColumnIndex(KEY_CANTIDAD));
            cursor.moveToNext();
            i++;

        }


        db.close();

        return total_productos;




    }




    public int DELETE_CART(){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_CART, null, null);
        db.close();

        return result;


    }

    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************CART**************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /****************************************************************************************/
    /**************************************CART**************************************************/
    /****************************************************************************************/





}