package com.bestdreamstore.admin_bestdream;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.Spanned;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import com.bestdreamstore.admin_bestdream.Funciones.Functions;


public class Get_Image_Pedido extends Activity{
    public static final String KEY_User_Document1 = "doc1";
    ImageView IDProf;
    Button Upload_Btn;

    public static final String UPLOAD_URL = "http://bestdream.store/Admin/insert_image_pedido_testigo/";
    public static final String UPLOAD_IMAGE = "image";
    public static final String UPLOAD_ID_PEDIDO = "id_pedido";

    private String Document_img1="";

    private static final int CAMERA_REQUEST = 2;
    private int PICK_IMAGE_REQUEST = 1;


    Dialog dialog;
    private Button buttonChoose;
    private Bitmap bitmap;
    ProgressDialog pDialog;
    private Uri filePath;
    String checkbox_clientes,checkbox_compras,checkbox_pago_banco_string;
    ImageView ver_mas,add_listas;

    private String id_pedido, estatus_pedido;
    Spanned result;
    public static Functions userFunctions;


    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_image_pedido);


        /*PERMISO DE USO DE CAMARA Y ESTORAGE*/
        /*PERMISO DE USO DE CAMARA Y ESTORAGE*/
        /*PERMISO DE USO DE CAMARA Y ESTORAGE*/


        checkPermissions();


        /*PERMISO DE USO DE CAMARA Y ESTORAGE*/
        /*PERMISO DE USO DE CAMARA Y ESTORAGE*/
        /*PERMISO DE USO DE CAMARA Y ESTORAGE*/



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        IDProf=(ImageView)findViewById(R.id.IdProf);
        Upload_Btn=(Button)findViewById(R.id.UploadBtn);

        userFunctions = new Functions();

        IDProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });


    }


    private void selectImage() {
        final CharSequence[] options = { "Camara", "Galeria","Cerrar" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Get_Image_Pedido.this);
        builder.setTitle("SELECCIONA OPCION");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Camara")){





                    /*CHECAMOS LA VERSION DE ANDROID*/
                    /*CHECAMOS LA VERSION DE ANDROID*/
                    if (android.os.Build.VERSION.SDK_INT >=24){

                        File imagePath = new File(getFilesDir(), "images_rastreo");
                        File newFile = new File(imagePath, "image.jpg");
                        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),"com.bestdreamstore.admin_bestdream.FileProvider", newFile);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        startActivityForResult(intent, CAMERA_REQUEST);


                    }else {


                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, CAMERA_REQUEST);

                    }

                    /*CHECAMOS LA VERSION DE ANDROID*/
                    /*CHECAMOS LA VERSION DE ANDROID*/

                }else if (options[item].equals("Galeria")){

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Selecciona Una Imagen"), PICK_IMAGE_REQUEST);

                }


                else if (options[item].equals("Cerrar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /*DETERMINADMOS SI LA IMAGEN VIENE DE ARCHIVO O FUE TOMADA POR CAMARA*/
        /*DETERMINADMOS SI LA IMAGEN VIENE DE ARCHIVO O FUE TOMADA POR CAMARA*/
        /*DETERMINADMOS SI LA IMAGEN VIENE DE ARCHIVO O FUE TOMADA POR CAMARA*/
        /*DETERMINADMOS SI LA IMAGEN VIENE DE ARCHIVO O FUE TOMADA POR CAMARA*/

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }



        }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {


            /*CHECAMOS LA VERSION DE ANDROID*/
            /*CHECAMOS LA VERSION DE ANDROID*/

            if (android.os.Build.VERSION.SDK_INT >=24) {

                File path = new File(getFilesDir(), "images_rastreo/");
                if (!path.exists()) path.mkdirs();
                File imageFile = new File(path, "image.jpg");

                bitmap = decodeSampledBitmapFromFile(imageFile.getAbsolutePath(), 1500, 1000);

            }else {

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1500, 1000);

            }


        }



        confirmar_guia_envio_poop_up(bitmap);
    }





    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/
    /*OBTENEMOS NUESTRA IMAGEN CON ALTA CALIDAD DE CAMARA*/










    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }


    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/
    /*PERMISOS DE USO DE CAMARA Y STORAGE*/





    private void uploadImage(final String id_pedido){


        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestImg_Server rh = new RequestImg_Server();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Get_Image_Pedido.this, "Enviando Confirmación", "Por Favor Espere un Momento...",true,true);
                loading.setCancelable(false);

            }


            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];


                /*ESCALAMOS NUESTRO BITMAP U ENVIAMOS*/
                /*ESCALAMOS NUESTRO BITMAP U ENVIAMOS*/

                Bitmap sacale = resizeBitmap(bitmap, 1000);
                String uploadImage = getStringImage(sacale);

                /*ESCALAMOS NUESTRO BITMAP U ENVIAMOS*/
                /*ESCALAMOS NUESTRO BITMAP U ENVIAMOS*/



                HashMap<String,String> data = new HashMap<>();
                data.put("image", uploadImage);
                data.put("id_pedido", id_pedido);


                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }



            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                imagen_upload(s);



            }



        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }




    public void imagen_upload(String msg){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("BestDream Admin Admin");
        builder1.setMessage(msg);
        builder1.setPositiveButton("Listo!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Get_Image_Pedido.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

        builder1.create().show();

    }





    public static Bitmap resizeBitmap(final Bitmap temp, final int size) {
        if (size > 0) {
            int width = temp.getWidth();
            int height = temp.getHeight();
            float ratioBitmap = (float) width / (float) height;
            int finalWidth = size;
            int finalHeight = size;
            if (ratioBitmap < 1) {
                finalWidth = (int) ((float) size * ratioBitmap);
            } else {
                finalHeight = (int) ((float) size / ratioBitmap);
            }
            return Bitmap.createScaledBitmap(temp, finalWidth, finalHeight, true);
        } else {
            return temp;
        }
    }





    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }










    public void confirmar_guia_envio_poop_up(Bitmap img){

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_evidencia_pedido);



        ImageView img_producto = (ImageView) dialog.findViewById(R.id.img_alert);
        img_producto.setImageBitmap(img);




        Button dialogButton = (Button) dialog.findViewById(R.id.button1);
        dialogButton.setText("Aceptar");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String id_order_fin = userFunctions.get_id_order_admin(getApplicationContext());

                dialog.dismiss();
                dialog.cancel();
                uploadImage(id_order_fin);

            }
        });



        Button dialogButton2 = (Button) dialog.findViewById(R.id.button2);
        dialogButton2.setText(" X ");
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();

            }
        });

        dialog.show();
    }





    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/
    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/
    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/
    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/
    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/
    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/
    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/
    /*CONFIRMAR GUIA DE ENVÍO POOP UP*/






}




