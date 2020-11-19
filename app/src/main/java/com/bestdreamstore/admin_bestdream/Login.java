package com.bestdreamstore.admin_bestdream;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import android.view.inputmethod.InputMethodManager;

public class Login extends AppCompatActivity {

    private static final String KEY_PERMISOS = "permisos";
    private static final String KEY_NAME = "name";

    Button login_button;
    EditText user, pass;
    TextView response_server;
    ImageView logo;
    String usuario, permisos;
    Functions userFunctions;
    DatabaseHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


       final LinearLayout content_general = (LinearLayout)findViewById(R.id.content_general);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        userFunctions = new Functions();

        logo = (ImageView)findViewById(R.id.logo);

        Picasso.get().load("https://bestdream.store/Views/Default/img/logo_bestdream_dark.png").into(logo);


        response_server = (TextView)findViewById(R.id.response_server);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);


        login_button = (Button) findViewById(R.id.login_button);





        if(userFunctions.isUserLoggedIn(getApplicationContext())){


            new AsyncTask<Object, Void, Boolean>() {

                @SuppressLint("StaticFieldLeak")
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();



                    user.setVisibility(View.INVISIBLE);
                    pass.setVisibility(View.INVISIBLE);
                    login_button.setVisibility(View.INVISIBLE);

                    response_server.setText("Espere Un Momento......");





                }

                @SuppressLint("WrongThread")
                @Override
                protected Boolean doInBackground(Object... params) {

                    final String nombre_user = userFunctions.get_name_user(getApplicationContext());
                    return userFunctions.check_autorizacion(nombre_user);

                }

                @Override
                protected void onPostExecute(final Boolean params) {
                    super.onPostExecute(params);

                    if(params){

                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivityForResult(i, 1);
                        finish();

                    }else{

                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.reset_login();

                        finish();

                    }





                } }.execute();





        }else{





            login_button.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View v) {



                    new AsyncTask<Object, Void, JSONObject>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();

                            response_server.setText("ESPERE UN MOMENTO.....");
                            hideKeyboard();

                        }


                        @SuppressLint("WrongThread")
                        @Override
                        protected JSONObject doInBackground(Object... params) {
                            JSONObject resultados_json = new JSONObject();

                            final JSONObject json;

                            String name_res = user.getText().toString();
                            String pass_res = pass.getText().toString();



                            return userFunctions.login_service(name_res, pass_res);

                        }


                        @Override
                        protected void onPostExecute(final JSONObject params) {
                            super.onPostExecute(params);

                            //Log.d("PARAMS_PEDIDO:", String.valueOf(params));

                            try {

                                String success = params.getString("success");
                                String permisos = params.getString("permisos");

                                if(success.equals("1")){

                                    String name_res = user.getText().toString();



                                    response_server.setText("NOMBRE  ::: "+name_res+ "---- PERMISOS:: "+permisos);
                                    userFunctions.addUser(name_res, permisos, getApplicationContext());

                                    Intent i = new Intent(Login.this, MainActivity.class);
                                    startActivityForResult(i, 1);
                                    finish();



                                }else{

                                    response_server.setText("ERROR  ::: "+success);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        } }.execute();









                }
            });







        }






    }



    public  void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }



}