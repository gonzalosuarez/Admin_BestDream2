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
import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Cart_Adapter;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.R;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;
import java.util.List;


/**
 * Created by JUNED on 6/16/2016.
 */

public class RecyclerCartViewAdapter extends RecyclerView.Adapter<RecyclerCartViewAdapter.ViewHolder> {

    Context context;
    List<Get_Cart_Adapter> getDataAdapter;
    ImageView iv_cart, delete_item;
    private static RecyclerView recyclerView_global;
    Cart_Controller cart = new Cart_Controller(context);
    int qty, multi_fin, id;
    float precio_premium, SUBTOTAL_PROD;
    String precio_p;



    boolean check_ini = false;







    public RecyclerCartViewAdapter(List<Get_Cart_Adapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;


    }







    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclercartview_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }






    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        Get_Cart_Adapter getDataAdapter1 =  getDataAdapter.get(position);

        //Ion.with(image).load("http://mygifimage.gif");



        Picasso.get().load(getDataAdapter1.getimagen_comp()).into(Viewholder.iv_cart);





        qty = getDataAdapter1.getqty();
        int position_real = getDataAdapter1.getid()-1;

 //       Viewholder.cantidad_txt.setText(String.valueOf(qty)+" -- "+position_real);
       // Viewholder.cantidad_txt.setText(String.valueOf(qty));


        //Viewholder.spinner.setSelection(qty);




        //multi_fin = qty+1;

        SUBTOTAL_PROD = getDataAdapter1.getprecio_premium()*qty;


        //SUBTOTAL_PROD += getDataAdapter1.getSub_Total();
        precio_p = String.valueOf(SUBTOTAL_PROD);




        //Viewholder.totales.setText("$"+precio_p);
        //Log.e("PRODUCTO PRECIO:", String.valueOf(precio_premium));
        Viewholder.nombre_producto.setText(getDataAdapter1.getnombre_producto());
        Viewholder.ID_PRODUCTO.setText(getDataAdapter1.getid_producto());
        Viewholder.QTY.setText("QTY --  "+String.valueOf(qty));









    }



    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }






    class ViewHolder extends RecyclerView.ViewHolder{


        DatabaseHandler db;

        public TextView cantidad_txt;
        public TextView ID_PRODUCTO, QTY, nombre_producto;
        public ImageView iv_cart, delete_item, details_img;


        public NetworkImageView networkImageView;
        public Button button_cantidad;
        public PopupWindow popupWindow;
        LinearLayout content_window;


        public ViewHolder(final View itemView) {

            super(itemView);



            ID_PRODUCTO = (TextView) itemView.findViewById(R.id.textView_item);
            nombre_producto = (TextView) itemView.findViewById(R.id.nombre_producto);
            QTY = (TextView) itemView.findViewById(R.id.QTY);
            cantidad_txt = (TextView) itemView.findViewById(R.id.cantidad_txt);
            iv_cart = (ImageView) itemView.findViewById(R.id.iv_cart);
            delete_item = (ImageView) itemView.findViewById(R.id.delete_item);
            delete_item.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    Controlador_Carrito db3 = new Controlador_Carrito(context);

                    db3.DELETE_ITEM(ID_PRODUCTO.getText().toString(), context);

                    notifyDataSetChanged();

                    /*
                    Cart_Controller db3 = new Cart_Controller(context);


                    if(db3.DELETE_ITEM(ID_PRODUCTO.getText().toString(), context)){
                        db3.ACTUALIZAR_CARRITO(context);

                    }
                    */





                }

            });


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

