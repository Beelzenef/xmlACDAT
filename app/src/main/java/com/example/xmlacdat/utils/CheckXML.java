package com.example.xmlacdat.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.util.Xml;

import com.example.xmlacdat.R;
import com.example.xmlacdat.pojo.Noticia;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Analizador de XML
 */

public class CheckXML {

    public static String analizar(String texto) throws XmlPullParserException, IOException {
        StringBuilder cadena = new StringBuilder();
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new StringReader(texto));
        int eventType = xpp.getEventType();
        cadena.append("Leyendo XML:\n ");
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT)
                cadena.append("START DOCUMENT \n");
            if (eventType == XmlPullParser.START_TAG)
                cadena.append("START TAG: " + xpp.getName() + "\n");
            if (eventType == XmlPullParser.TEXT)
                cadena.append("CONTENT: " + xpp.getText() + "\n");
            if (eventType == XmlPullParser.END_TAG)
                cadena.append("END TAG: " + xpp.getName() + "\n");
            eventType = xpp.next();
        }

        cadena.append("END DOCUMENT");
        return cadena.toString();
    }

    public static String analizarXmlGet(Context c) throws XmlPullParserException, IOException {
        boolean esNombre = false;
        boolean esNota = false;
        StringBuilder stringResultante = new StringBuilder();
        Double suma = 0.0;
        int contador = 0;

        XmlResourceParser xrp = c.getResources().getXml(R.xml.alumnos);
        int eventType = xrp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (xrp.getName().equals("nombre")) {
                        esNombre = true;
                    }

                    if (xrp.getName().equals("nota")) {
                        esNota = true;

                        for (int i = 0; i < xrp.getAttributeCount(); i++) {
                            stringResultante.append(xrp.getAttributeName(i) + ": " + xrp.getAttributeValue(i) + "\n");
                            contador++;
                        }
                    }

                    if (xrp.getName().equals("observaciones")) {

                    }
                    break;
                case XmlPullParser.TEXT:

                    if (esNombre) {
                        stringResultante.append("Nombre: " + xrp.getText() + "\n");
                    }
                    else if (esNota) {
                        suma += Double.parseDouble(xrp.getText());
                        stringResultante.append("Nota: " + xrp.getText() + "\n");
                    }
                    else {
                        stringResultante.append("Observaciones: " + xrp.getText() + "\n");
                    }

                    break;
                case XmlPullParser.END_TAG:
                    if (xrp.getName().equals("nombre"))
                        esNombre = false;
                    if (xrp.getName().equals("nota"))
                        esNota = false;
                    if (xrp.getName().equals("alumno"))
                        stringResultante.append("\n");
                    break;
            }
            eventType = xrp.next();
        }

        stringResultante.append("Media de todas las notas : " + String.format("%.2f", suma / contador));
        xrp.close();
        return stringResultante.toString();
    }

    public static String analizarXmlNextText(Context c) throws XmlPullParserException, IOException {

        StringBuilder stringResultante = new StringBuilder();

        double suma = 0.0;
        double getNota = 0;
        int contador = 0;


        XmlResourceParser xrp = c.getResources().getXml(R.xml.alumnos);
        int eventType = xrp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (xrp.getName().equals("nombre")) {
                        stringResultante.append("Nombre: " + xrp.nextText() + "\n");
                    }

                    if (xrp.getName().equals("nota")) {

                        for (int i = 0; i < xrp.getAttributeCount(); i++) {
                            stringResultante.append(xrp.getAttributeName(i) + ": " + xrp.getAttributeValue(i) + "\n");
                            contador++;
                        }

                        getNota = Double.parseDouble(xrp.nextText());
                        stringResultante.append("Nota: " + Double.toString(getNota) + "\n");
                        suma += getNota;
                    }
                    if (xrp.getName().equals("observaciones")) {
                        stringResultante.append("Observaciones: " + xrp.nextText() + "\n\n");
                    }
                    break;
            }
            eventType = xrp.next();
        }

        stringResultante.append("Media de todas las notas : " + String.format("%.2f", suma / contador));
        xrp.close();
        return stringResultante.toString();
    }

    public static String analizarRSS(File file) throws NullPointerException, XmlPullParserException, IOException {

        boolean dentroItem = false;

        StringBuilder builder = new StringBuilder();
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new FileReader(file));

        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("item"))
                        dentroItem = true;

                    if (dentroItem && xpp.getName().equals("title")) {
                        builder.append("Post: " + xpp.nextText() + "\n");
                        dentroItem = false;
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return builder.toString();
    }

    public static ArrayList<Noticia> analizarNoticias(File file) throws XmlPullParserException, IOException {
        int eventType;

        ArrayList<Noticia> noticias = null;
        Noticia actual = null;
        boolean dentroItem = false;

        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(new FileReader(file));
        eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    noticias = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("item")) {
                        dentroItem = true;
                        actual = new Noticia();
                    }

                    if (dentroItem && xpp.getName().equals("title")) {
                        actual.setTitle(xpp.nextText());
                    }
                    if (dentroItem && xpp.getName().equals("link")) {
                        actual.setLink(xpp.nextText());
                    }
                    if (dentroItem && xpp.getName().equals("description")) {
                        actual.setDescription(xpp.nextText());
                    }
                    if (dentroItem && xpp.getName().equals("pubDate")) {
                        actual.setPubDate(xpp.nextText());
                    }

                    break;
                case XmlPullParser.END_TAG:
                    if (xpp.getName().equals("item")) {
                        dentroItem = false;
                        noticias.add(actual);
                    }
                    break;
            }
            eventType = xpp.next();
        }
        //devolver el array de noticias
        return noticias;
    }

    public static void crearXML(ArrayList<Noticia> listaNoticias, String fichero) throws IOException {
        FileOutputStream fout;
        fout = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fichero));
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fout, "UTF-8");
        serializer.startDocument(null, true);

        // Settear tabulación
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        serializer.startTag(null, "titulares");
        for (Noticia noticia : listaNoticias) {

            serializer.startTag(null, "item");

            // Elemento titulo
            serializer.startTag(null, "titulo");
            serializer.attribute(null, "fecha", noticia.getPubDate());
            serializer.text(noticia.getTitle());
            serializer.endTag(null, "titulo");
            // Elemento link
            serializer.startTag(null, "enlace");
            serializer.text(noticia.getLink());
            serializer.endTag(null, "enlace");
            // Elemento descripcion
            serializer.startTag(null, "descripcion");
            serializer.text(noticia.getDescription());
            serializer.endTag(null, "descripcion");

            serializer.endTag(null, "item");
        }

        serializer.endTag(null, "titulares");
        serializer.endDocument();
        serializer.flush();
        fout.close();
    }
}
