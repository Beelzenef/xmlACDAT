package com.example.xmlacdat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xmlacdat.utils.CheckXML;
import com.example.xmlacdat.network.RestClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class TitularesActivity extends AppCompatActivity {

    public static final String RSS = "https://geekstorming.wordpress.com/feed/";
    public static final String TEMPORAL = "geekstorming.xml";
    TextView txtV_Titulares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulares);
        txtV_Titulares = (TextView) findViewById(R.id.txtV_Titulares);
    }

    public void onClick_descargarTitulares(View v) {
        switch (v.getId()) {
            case R.id.btn_ObtenerTitulares:
                descargarTitulares(RSS, TEMPORAL);
                break;
        }
    }

    private void descargarTitulares(String rss, String tmp) {
        final ProgressDialog progreso = new ProgressDialog(this);
        RestClient.get(rss, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progreso.dismiss();
                Toast.makeText(TitularesActivity.this, "Algo ha salido mal... :(",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progreso.dismiss();
                Toast.makeText(TitularesActivity.this, "Fichero descargado correctamente",
                        Toast.LENGTH_SHORT).show();
                try {
                    txtV_Titulares.setText(CheckXML.analizarRSS(file));
                } catch (Exception e) {
                    Toast.makeText(TitularesActivity.this, "¡Error! :(",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Descargando");
                progreso.setCancelable(false);
                progreso.show();
            }
        });
    }
}
