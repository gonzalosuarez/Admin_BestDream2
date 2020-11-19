package com.bestdreamstore.admin_bestdream.CONTROLLER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bestdreamstore.admin_bestdream.ADAPTERS.GetDataAdapter;
import com.bestdreamstore.admin_bestdream.Add_Bar_Code_ID;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.bestdreamstore.admin_bestdream.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by JUNED on 6/16/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    GetDataAdapter getDataAdapter1;

    Context context;
    List<GetDataAdapter> getDataAdapter;
    ImageLoader imageLoader1;
    Typeface typeface;
    Functions userFunctions;

    String MARKETING_FEED;


    public String ID_PRODUCTO_GLOBAL, NOMBRE_PRODUCTO_GLOBAL, IMAGEN_PRODUCTO_GLOBAL, CATEGORIA_PRODUCTO_GLOBAL, MARCA_PRODUCTO_GLOBAL;

    public String PRODUCTO_GLOBAL, PRECIO_PREMIUM_GLOBAL, COSTO_PRODUCTO_GLOBAL, COSTO_ENVIO_GLOBAL;



    public DatabaseHandler db;
    public Cart_Controller cart;




    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;


    }









    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitems, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }






    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {


        final StaggeredGridLayoutManager.LayoutParams layoutParams =  new StaggeredGridLayoutManager.LayoutParams(
                Viewholder.itemView.getLayoutParams());



        getDataAdapter1 =  getDataAdapter.get(position);
        cart = new Cart_Controller(context);








        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/



        layoutParams.setFullSpan(false);
        ID_PRODUCTO_GLOBAL = getDataAdapter1.getid();
        NOMBRE_PRODUCTO_GLOBAL = getDataAdapter1.getnombre();
        IMAGEN_PRODUCTO_GLOBAL = getDataAdapter1.getImageServerUrl();
        CATEGORIA_PRODUCTO_GLOBAL = getDataAdapter1.getcategoria();
        MARCA_PRODUCTO_GLOBAL = getDataAdapter1.getmarca();
        PRODUCTO_GLOBAL = getDataAdapter1.getproducto();
        COSTO_ENVIO_GLOBAL = getDataAdapter1.getpeso();


        int precio_mayoreo_url = Integer.parseInt(getDataAdapter1.getprecio_mayoreo());
        int decimales_mayoreo_url = Integer.parseInt(getDataAdapter1.getdecimales_mayoreo());
        PRECIO_PREMIUM_GLOBAL = String.valueOf(precio_mayoreo_url)+"."+String.valueOf(decimales_mayoreo_url);


        int costo_producto = Integer.parseInt(getDataAdapter1.getcosto_producto());
        int decimales_costo = Integer.parseInt(getDataAdapter1.getdecimales_costo());
        COSTO_PRODUCTO_GLOBAL = String.valueOf(costo_producto)+"."+String.valueOf(decimales_costo);






        Viewholder.id_text_global.setText(getDataAdapter1.getid());
        Viewholder.nombre.setText(
                "<strong>$"+getDataAdapter1.getprecio_mayoreo()+"."+
                        getDataAdapter1.getdecimales_mayoreo()+"</strong><br>"+
                        getDataAdapter1.getnombre()+" -- ID:"+ID_PRODUCTO_GLOBAL
        );



        Picasso.get().load(getDataAdapter1.getImageServerUrl()).into(Viewholder.iv);



        Viewholder.id_text_global.setText(getDataAdapter1.getid());

        Viewholder.nombre.setText(
                "<strong>$"+getDataAdapter1.getprecio_mayoreo()+"."+
                        getDataAdapter1.getdecimales_mayoreo()+"</strong><br>"+
                        getDataAdapter1.getnombre()+" -- ID:"+ID_PRODUCTO_GLOBAL
        );



        Viewholder.image_txt.setText(getDataAdapter1.getImageServerUrl());
        Viewholder.select.setText(ID_PRODUCTO_GLOBAL+" >> ");







        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/









        Viewholder.itemView.setLayoutParams(layoutParams);






    }



    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }






    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView id_text_global;
        public TextView nombre, image_txt;
        public ImageView iv;
        public Button select;


        public ViewHolder(View itemView) {

            super(itemView);
            cart = new Cart_Controller(context);




            image_txt = (TextView)itemView.findViewById(R.id.image_txt);

            nombre = (TextView) itemView.findViewById(R.id.nombre_txt);
            nombre.setTypeface(typeface);


            iv = (ImageView) itemView.findViewById(R.id.iv);


            select = (Button) itemView.findViewById(R.id.select);


            id_text_global = (TextView)itemView.findViewById(R.id.id_text_global);



            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String ID_ = id_text_global.getText().toString();
                    String IMAGE = image_txt.getText().toString();
                    Intent intent = new Intent(context.getApplicationContext(), Add_Bar_Code_ID.class);
                    intent.putExtra("ID_PRODUCTO", ID_);
                    intent.putExtra("IMAGEN", IMAGE);
                    context.startActivity(intent);


                }
            });











        }



    }




    public String reemplazar_espacios_blanco_url(String url){

        return url.replace(" ", "%20");


    }







}
