package com.example.xmlacdat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.xmlacdat.utils.CheckXML;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class PartesActivity extends AppCompatActivity {

    private TextView txtV_partesXML;

    public  static final String TEXTO = "<texto><uno>Hello World! </uno><dos>Goodbye</dos></texto>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partes);

        txtV_partesXML = (TextView) findViewById(R.id.txtV_PartesXML);

        leerXML();
    }

    private void leerXML() {
        try {
            txtV_partesXML.setText(CheckXML.analizar(TEXTO));
        } catch (IOException | XmlPullParserException e)
        {
            txtV_partesXML.setText(e.getMessage());
        }
    }
}
