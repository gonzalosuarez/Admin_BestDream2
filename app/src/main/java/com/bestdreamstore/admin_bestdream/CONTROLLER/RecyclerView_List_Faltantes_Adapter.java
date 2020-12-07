package com.bestdreamstore.admin_bestdream.CONTROLLER;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Faltantes_Adapter;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.R;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;
import java.util.List;


/**
 * Created by JUNED on 6/16/2016.
 */

public class RecyclerView_List_Faltantes_Adapter extends RecyclerView.Adapter<RecyclerView_List_Faltantes_Adapter.ViewHolder> {

    Context context;
    List<Get_Faltantes_Adapter> getDataAdapter;
    int qty;
    float precio_premium, SUBTOTAL_PROD;
    String precio_p;



    boolean check_ini = false;







    public RecyclerView_List_Faltantes_Adapter(List<Get_Faltantes_Adapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;


    }







    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewfaltantes, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }






    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        Get_Faltantes_Adapter getDataAdapter1 =  getDataAdapter.get(position);

        //Ion.with(image).load("http://mygifimage.gif");



        Picasso.get().load(getDataAdapter1.getimagen_producto()).into(Viewholder.iv_cart);


        Picasso.get().load("https://bestdream.store/Views/Default/img/flecha_amarilla_derecha.jpg").into(Viewholder.tipo_alta_cart);


        qty = getDataAdapter1.getqty();


        Viewholder.textView_item.setText(getDataAdapter1.getnombre_producto()+" -- ID: "+getDataAdapter1.getid_producto());
        Viewholder.QTY.setText("QTY --  "+String.valueOf(qty));









    }



    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }






    class ViewHolder extends RecyclerView.ViewHolder{


        DatabaseHandler db;

        public TextView cantidad_txt;
        public TextView textView_item, QTY;
        public ImageView iv_cart, tipo_alta_cart, details_img;


        public NetworkImageView networkImageView;
        public Button button_cantidad;
        public PopupWindow popupWindow;
        LinearLayout content_window;


        public ViewHolder(final View itemView) {

            super(itemView);



            textView_item = (TextView) itemView.findViewById(R.id.textView_item);
            QTY = (TextView) itemView.findViewById(R.id.QTY);
            cantidad_txt = (TextView) itemView.findViewById(R.id.cantidad_txt);
            iv_cart = (ImageView) itemView.findViewById(R.id.iv_cart);
            tipo_alta_cart = (ImageView) itemView.findViewById(R.id.tipo_alta_cart);


            iv_cart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {



                    //AlertDialog builder1 = new AlertDialog.Builder(context).create();
                    final Dialog dialog = new Dialog(view.getRootView().getContext()); // Context, this, etc.
                    dialog.setContentView(R.layout.alert_dialog_details_img);
                    ImageView details_img = (ImageView) dialog.findViewById(R.id.details_img);
                    Button dialog_cerrar = (Button)dialog.findViewById(R.id.dialog_cerrar);
                    dialog_cerrar.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(final View view) {

                            dialog.cancel();

                        }
                    });



                    Bitmap bm=((BitmapDrawable)iv_cart.getDrawable()).getBitmap();
                    details_img.setImageBitmap(bm);



                    dialog.setTitle("Detalle");
                    dialog.show();





                }
            });






        }






    }











}

