package com.bestdreamstore.admin_bestdream.CONTROLLER;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_Cart_Adapter;
import com.bestdreamstore.admin_bestdream.ADAPTERS.SwipeToDeleteCallback;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
import com.bestdreamstore.admin_bestdream.R;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import static com.bestdreamstore.admin_bestdream.CONTROLLER.Cart_Controller.GetCartAdapter1;


/**
 * Created by JUNED on 6/16/2016.
 */

public class RecyclerCartViewAdapter extends RecyclerView.Adapter<RecyclerCartViewAdapter.ViewHolder> {

    Context context;
    List<Get_Cart_Adapter> getDataAdapter;
    ImageView delete_item;
    RecyclerView recyclerView_global;
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


       // Picasso.get().load("https://bestdream.store/Views/Default/img/flecha_amarilla_derecha.jpg").into(Viewholder.tipo_alta_cart);









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
        Viewholder.textView_item.setText(getDataAdapter1.getnombre_producto()+" -- ID: "+getDataAdapter1.getid_producto()+" -- "+getDataAdapter1.getbar_code());
        Viewholder.QTY.setText("QTY --  "+String.valueOf(qty));
        Viewholder.id_producto.setText(getDataAdapter1.getid_producto());









    }



    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }






    class ViewHolder extends RecyclerView.ViewHolder{


        DatabaseHandler db;

        public TextView cantidad_txt;
        public TextView textView_item, QTY, id_producto;
        public ImageView iv_cart, delete_item;



        public ViewHolder(final View itemView) {

            super(itemView);





            textView_item = (TextView) itemView.findViewById(R.id.textView_item);
            id_producto = (TextView) itemView.findViewById(R.id.id_producto);
            QTY = (TextView) itemView.findViewById(R.id.QTY);
            cantidad_txt = (TextView) itemView.findViewById(R.id.cantidad_txt);
            iv_cart = (ImageView) itemView.findViewById(R.id.iv_cart);

            delete_item = (ImageView) itemView.findViewById(R.id.delete_item);
            delete_item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {

                    String id_prod_fin = id_producto.getText().toString();
                    Toast.makeText(context, id_prod_fin, Toast.LENGTH_SHORT).show(); //Correcto

                    DatabaseHandler db = new DatabaseHandler(context);
                    db.delete(id_prod_fin);


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

