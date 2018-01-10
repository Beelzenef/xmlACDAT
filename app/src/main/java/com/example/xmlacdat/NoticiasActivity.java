package com.example.xmlacdat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xmlacdat.pojo.Noticia;
import com.example.xmlacdat.utils.CheckXML;
import com.example.xmlacdat.utils.RestClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NoticiasActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //public static final String CANAL = "https://geekstorming.wordpress.com/feed/";
    //public static final String TEMPORAL = "geekst.xml";

    public static final String CANAL = "http://www.europapress.es/rss/rss.aspx?ch=279";
    public static final String TEMPORAL = "europapress.xml";
    ListView lista;
    ArrayList<Noticia> listaNoticias;
    ArrayAdapter<Noticia> adapter;
    FloatingActionButton fab_updateNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        lista = (ListView) findViewById(R.id.listView);
        lista.setOnItemClickListener(this);
        fab_updateNews = (FloatingActionButton) findViewById(R.id.fab_updateNews);
        fab_updateNews.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == fab_updateNews)
            descarga(CANAL, TEMPORAL);
    }

    private void descarga(String canal, String temporal) {
        final ProgressDialog progreso = new ProgressDialog(this);
        RestClient.get(canal, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progreso.dismiss();
                Toast.makeText(NoticiasActivity.this, "Algo ha salido mal... :(",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                try {
                    progreso.dismiss();
                    listaNoticias = CheckXML.analizarNoticias(file);
                    mostrar();
                } catch (Exception e) {
                    Toast.makeText(NoticiasActivity.this, "¡Error! :(",
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

    private void mostrar() {
        if (listaNoticias != null)
            if (adapter == null) {
                adapter = new ArrayAdapter<Noticia>(this, android.R.layout.simple_list_item_1);
                lista.setAdapter(adapter);
            }
            else {
                adapter.clear();
                adapter.addAll(listaNoticias);
            }
        else
            Toast.makeText(getApplicationContext(), "Error al crear la lista", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Uri uri = Uri.parse((String) listaNoticias.get(position).getLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else
            Toast.makeText(getApplicationContext(), "No hay un navegador", Toast.LENGTH_SHORT).show();
    }

}
