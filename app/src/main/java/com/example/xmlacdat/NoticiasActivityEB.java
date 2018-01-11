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

import com.example.xmlacdat.network.DownloadTask;
import com.example.xmlacdat.pojo.FailureEvent;
import com.example.xmlacdat.pojo.Noticia;
import com.example.xmlacdat.pojo.SuccessEvent;
import com.example.xmlacdat.utils.CheckXML;
import com.example.xmlacdat.network.RestClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NoticiasActivityEB extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //public static final String CANAL = "https://geekstorming.wordpress.com/feed/";
    //public static final String TEMPORAL = "geekst.xml";

    public static final String CANAL = "http://www.europapress.es/rss/rss.aspx?ch=279";
    public static final String TEMPORAL = "europapress.xml";
    ListView lista;
    ArrayList<Noticia> listaNoticias;
    ArrayAdapter<Noticia> adapter;
    FloatingActionButton fab_updateNews;

    ProgressDialog progreso;

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

    // Suscripción con EventBus

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SuccessEvent event) {
        try {
            progreso.dismiss();
            listaNoticias = CheckXML.analizarNoticias(event.file);
            mostrar();
        } catch (Exception e) {
            Toast.makeText(NoticiasActivityEB.this, "¡Error! :(",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void handleFailure(FailureEvent event) {
        Toast.makeText(NoticiasActivityEB.this, event.msg,
                Toast.LENGTH_SHORT).show();
    }

    private void descarga(String canal, String temporal) {
        progreso = new ProgressDialog(this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Descargando");
        progreso.setCancelable(false);
        progreso.show();

        DownloadTask.executeDownload(canal, temporal);
    }

    private void mostrar() {
        if (listaNoticias != null)
            if (adapter == null) {
                adapter = new ArrayAdapter<Noticia>(this, android.R.layout.simple_list_item_1, listaNoticias);
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
