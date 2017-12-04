package com.example.xmlacdat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_lanzarExs(View v)
    {
        Intent unIntent = null;

        switch (v.getId())
        {
            case R.id.btn_Partes:
                unIntent = new Intent(MainActivity.this, PartesActivity.class);
                break;
            case R.id.btn_Notas:
                unIntent = new Intent(MainActivity.this, NotasActivity.class);
                break;
            case R.id.btn_Titulares:
                unIntent = new Intent(MainActivity.this, TitularesActivity.class);
                break;
            case R.id.btn_Noticas:
                unIntent = new Intent(MainActivity.this, NoticiasActivity.class);
                break;
            case R.id.btn_Creacion:
                unIntent = new Intent(MainActivity.this, CreacionActivity.class);
                break;
            case R.id.btn_Subida:
                unIntent = new Intent(MainActivity.this, SubidaActivity.class);
                break;
        }

        startActivity(unIntent);
    }
}
