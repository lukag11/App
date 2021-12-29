package com.example.animalapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2ActivityNivel6 extends AppCompatActivity {

    private TextView tvNombre, tvScoreJugador;
    private ImageView ivNumUno, ivNumDos, ivVidas, ivSigno;
    private EditText etRespuesta;
    private MediaPlayer mPlayer, mpGreat, mpBad;


    int score, numAleatorioUno, numAleatorioDos, resultado, vidas = 3;
    String nombreJugador, stringScore, stringVidas;

    String numero[] = {"ceroo", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_nivel6);
        Toast.makeText(this, "Nivel 6 - Sumas,Restas y Multiplicaciones ", Toast.LENGTH_SHORT).show();


        tvNombre = (TextView) findViewById(R.id.tvNombrejugador);
        tvScoreJugador = (TextView) findViewById(R.id.tviewScore);
        ivNumUno = (ImageView) findViewById(R.id.imageViewNumUno);
        ivNumDos = (ImageView) findViewById(R.id.imageViewNumDos);
        ivVidas = (ImageView) findViewById(R.id.imageViewVidas);
        ivSigno = (ImageView) findViewById(R.id.imageViewSigno);
        etRespuesta = (EditText) findViewById(R.id.txtResultado);

        nombreJugador = getIntent().getStringExtra("jugador");
        tvNombre.setText("Jugador: " + nombreJugador);

        stringScore = getIntent().getStringExtra("score");
        score = Integer.parseInt(stringScore);
        tvScoreJugador.setText("Score: " + score);

        stringVidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(stringVidas);
        if (vidas == 3){
            ivVidas.setImageResource(R.drawable.tresvidas);
        }
        if (vidas == 2){
            ivVidas.setImageResource(R.drawable.dosvidas);
        }
        if (vidas == 1){
            ivVidas.setImageResource(R.drawable.unavida);
        }


        getSupportActionBar().setDisplayShowHomeEnabled(true);// PONER ICONO EN EL ACTION BAR
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mPlayer = MediaPlayer.create(this, R.raw.goats);
        mPlayer.start();
        mPlayer.setLooping(true);

        mpGreat = MediaPlayer.create(this, R.raw.wonderful);
        mpBad = MediaPlayer.create(this, R.raw.bad);

        NumAleatorio();


    }

    public void Comparar(View view) {

        String respuesta = etRespuesta.getText().toString();

        if (!respuesta.equals("")) { // Si la respuesta es diferente a un espacio en blanco

            int respuestaJugador = Integer.parseInt(respuesta);

            if (resultado == respuestaJugador) {

                mpGreat.start();
                score++;
                tvScoreJugador.setText("Score: " + score);
                etRespuesta.setText("");
                BaseDeDatos();

            } else {

                mpBad.start();
                vidas--;
                BaseDeDatos();

                switch (vidas) {
                    case 3:
                        ivVidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, "Te quedan 2 manzanas", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, "Te quedan 1 manzanas", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, "No te quedan mas manzanas", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mPlayer.stop();
                        mPlayer.release();
                        break;
                }

                etRespuesta.setText("");


            }

            NumAleatorio();
        } else {
            Toast.makeText(this, "Escribe tu Respuesta", Toast.LENGTH_SHORT).show();
        }


    }


    public void NumAleatorio() {

        if (score <= 1000000) { // SIEMPRE QUE EL SCORE SEA MENOR O IGUAL QUE NUEVE SIGUE HACIENDO LAS OPERACIONES BASICAS

            numAleatorioUno = (int) (Math.random() * 10); // GENERAMOS NUMEROS ALEATORIOS
            numAleatorioDos = (int) (Math.random() * 10);

            if (numAleatorioUno >= 0 && numAleatorioUno <= 3){

                resultado = numAleatorioUno + numAleatorioDos;
                ivSigno.setImageResource(R.drawable.suma);

            }else if (numAleatorioUno >= 4 && numAleatorioUno <= 7){

                resultado = numAleatorioUno - numAleatorioDos;
                ivSigno.setImageResource(R.drawable.resta);

            } else{

                resultado = numAleatorioUno * numAleatorioDos;
                ivSigno.setImageResource(R.drawable.multiplicacion);
            }




            // CONDICION PARA QUE NO HAYA RESULTADOS CON NUMEROS NEGATIVOS
            if (resultado >= 0){

                for (int i = 0; i < numero.length; i++) { // RECORRO TODA LA VARIABLE Y GUARDO EN UNA VARIABLE ID // INTERCAMBIA UN VALOR DE TIPO STRING POR UNA IMAGEN
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if (numAleatorioUno == i) {
                        ivNumUno.setImageResource(id);
                    }
                    if (numAleatorioDos == i) {
                        ivNumDos.setImageResource(id);
                    }

                }

            }else{


                NumAleatorio();

            }



        } else {
            Intent intent = new Intent(this, MainActivity.class);

            Toast.makeText(this,"Eres un Genio !!!", Toast.LENGTH_LONG).show();

            startActivity(intent);
            finish();
            mPlayer.stop();
            mPlayer.release();
// 5-3-6-0-2-4
        }


    }

    public void BaseDeDatos() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase(); // APERTURA EN MODO LECTURA Y ESCRITURA DE MI BD

        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max (score) from puntaje)", null); // SELECIONA TODO DE LA TABLA PUNTAJE DONDE EL SCORE SEA IGUAL AL MAXICO SCORE DE LA TABLA PUNTAJE

        if (consulta.moveToFirst()) {
            String tempNombre = consulta.getString(0);
            String tempScore = consulta.getString(1);

            int bestScore = Integer.parseInt(tempScore);

            if (score > bestScore) {
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombreJugador); // LO GUARDAMOS DENTRO DEL OBJETO
                modificacion.put("score", score);

                BD.update("puntaje", modificacion, "score=" + bestScore, null); // EJECUTAMOS LA SENTENCIA

            }
            BD.close();

        } else {

            ContentValues insertar = new ContentValues();
            insertar.put("nombre", nombreJugador);
            insertar.put("score", score);

            BD.insert("puntaje", null, insertar);
            BD.close();

        }

    }

    @Override
    public void onBackPressed() {

    }


}


