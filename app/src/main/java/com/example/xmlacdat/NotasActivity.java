package com.example.xmlacdat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xmlacdat.utils.CheckXML;

public class NotasActivity extends AppCompatActivity {

    TextView textoNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        textoNotas = (TextView) findViewById(R.id.txtV_notasXML);

        try {
            textoNotas.setText(CheckXML.analizarXmlNextText(this));
        } catch (Exception e)
        {
            Toast.makeText(this, "Error al leer XML", Toast.LENGTH_SHORT).show();
        }
    }


}
