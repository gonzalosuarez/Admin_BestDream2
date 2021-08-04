package com.bestdreamstore.admin_bestdream;


import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;



public class Voz_texto extends AppCompatActivity {

    private TextView textView;
    private ImageButton imageButton;
    private final int RCODE = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voz_texto);

        textView = (TextView)findViewById(R.id.textView);
        imageButton = (ImageButton) findViewById(R.id.telefono_nine);
        imageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                capturarVoz();
            }
        });
    }




    private void capturarVoz(){
        Intent intent = new Intent(RecognizerIntent
                .ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, RCODE);
        } else
        {
            Log.e("ERROR","Su dispositivo no admite entrada de voz");
        }
    }

    @Override
    protected  void onActivityResult(int requestCode,
                                     int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RCODE && resultCode == RESULT_OK && data != null)
        {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            textView.setText(result.get(0));
        }

    }


}
