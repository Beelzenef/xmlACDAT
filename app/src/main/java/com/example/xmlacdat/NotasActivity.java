package com.example.xmlacdat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.xmlacdat.utils.CheckXML;

public class NotasActivity extends AppCompatActivity {

    TextView textoNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        textoNotas = (TextView) findViewById(R.id.txtV_notasXML);

        try {
            textoNotas.setText(CheckXML.analizarNombres(this));
        } catch (Exception e)
        {
            e.getMessage();
        }
    }


}
