package com.bestdreamstore.admin_bestdream.CONTROLLER;


import android.content.Context;
        import android.content.Intent;
        import android.graphics.Typeface;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.android.volley.toolbox.ImageLoader;
        import com.bestdreamstore.admin_bestdream.ADAPTERS.GetDataAdapter;
import com.bestdreamstore.admin_bestdream.ADAPTERS.Get_List_Pedidos_Adapter;
import com.bestdreamstore.admin_bestdream.Add_Bar_Code_ID;
import com.bestdreamstore.admin_bestdream.Armado_Pedidos;
import com.bestdreamstore.admin_bestdream.DATA_BASE.DatabaseHandler;
        import com.bestdreamstore.admin_bestdream.Funciones.Functions;
import com.bestdreamstore.admin_bestdream.MainActivity;
import com.bestdreamstore.admin_bestdream.R;
        import com.squareup.picasso.Picasso;

        import java.util.List;


/**
 * Created by JUNED on 6/16/2016.
 */

public class RecyclerView_List_Pedidos_Adapter extends RecyclerView.Adapter<RecyclerView_List_Pedidos_Adapter.ViewHolder> {


    Get_List_Pedidos_Adapter getDataAdapter1;

    Context context;
    List<Get_List_Pedidos_Adapter> getDataAdapter;
    ImageLoader imageLoader1;
    Typeface typeface;
    Functions userFunctions;

    String MARKETING_FEED;


    public String ID_PEDIDO_GLOBAL;





    public DatabaseHandler db;
    public Cart_Controller cart;




    public RecyclerView_List_Pedidos_Adapter(List<Get_List_Pedidos_Adapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;


    }









    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitemspedidos, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }






    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {


        final StaggeredGridLayoutManager.LayoutParams layoutParams =  new StaggeredGridLayoutManager.LayoutParams(
                Viewholder.itemView.getLayoutParams());


        getDataAdapter1 =  getDataAdapter.get(position);





        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/
        /*FEED NORMAL*/



        layoutParams.setFullSpan(false);
        ID_PEDIDO_GLOBAL = getDataAdapter1.getid_pedido();


        Viewholder.id_text_global_pedidos.setText(getDataAdapter1.getid_pedido());



        Viewholder.button_click.setText(getDataAdapter1.getid_pedido());





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

        public TextView id_text_global_pedidos;
        public Button button_click;



        public ViewHolder(View itemView) {

            super(itemView);


            id_text_global_pedidos = (TextView)itemView.findViewById(R.id.id_text_global_pedidos);
            id_text_global_pedidos.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                  String id =   id_text_global_pedidos.getText().toString();

                    Log.e("ID_CLICK::::", id);


                }

            });



            button_click = (Button) itemView.findViewById(R.id.button_click);
            button_click.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    String id =   id_text_global_pedidos.getText().toString();

                    Log.e("ID_CLICK::::", id);

                    //String id_order_fin = id;
                    //id_order_result.setText(id_order_fin);



                        Intent intent = new Intent(context.getApplicationContext(), Armado_Pedidos.class);
                        intent.putExtra("id_pedido", id);
                        context.startActivity(intent);








                }

            });





        }



    }










}

