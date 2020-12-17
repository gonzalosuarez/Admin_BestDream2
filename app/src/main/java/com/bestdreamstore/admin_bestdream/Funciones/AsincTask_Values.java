package com.bestdreamstore.admin_bestdream.Funciones;

import android.content.Context;
import android.os.AsyncTask;



public  class AsincTask_Values extends AsyncTask<String,String,String> {



    Context context;
    public AsincTask_Values(Context ctx){
        context =ctx;
    }


    @Override
    public String doInBackground(String... params) {

        String type = params[0];
        return type;

    }


    @Override
    protected void onPreExecute() {

        //AD = new AlertDialog.Builder(context).create();
        //AD.setTitle("Login Status");

    }

    @Override
    public void onPostExecute(String result) {
        //AD.setMessage(result);
        //AD.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
    }


}
