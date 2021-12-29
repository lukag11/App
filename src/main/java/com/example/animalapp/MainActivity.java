package com.example.animalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etNombre;
    private ImageView ivPersonajes;
    private TextView tvBestScore;
    private MediaPlayer mPlayer ;

    int numAleatorio = (int)(Math.random() * 10); // GENERAR NUMEROS ALEATORIOS PARA PODER CAMBIAR EL ICONO. HAY QUE HACAER UN CASTING XQ MATH.RANDOM DEVUELVE VALORES DOUBLE



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etNombre = (EditText)findViewById(R.id.txtnombre);
        ivPersonajes = (ImageView)findViewById(R.id.imageViewPersonaje);
        tvBestScore = (TextView)findViewById(R.id.textViewBestScore);


        getSupportActionBar().setDisplayShowHomeEnabled(true);// PONER ICONO EN EL ACTION BAR
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        int id; //
        if (numAleatorio == 0 || numAleatorio == 10){ // SI NUMERO A
        id = getResources().getIdentifier("elefante", "drawable", getPackageName()); // CON ESTO BUSCAMOS LA IMAGEN
            ivPersonajes.setImageResource(id); // LO MOSTRAMOS

        } else if (numAleatorio == 1 || numAleatorio == 9){ // {
            id = getResources().getIdentifier("leon", "drawable", getPackageName()); // CON ESTO BUSCAMOS LA IMAGEN
            ivPersonajes.setImageResource(id); // LO MOSTRAMOS


        } else if (numAleatorio == 2 || numAleatorio == 8){ // {
            id = getResources().getIdentifier("llama", "drawable", getPackageName()); // CON ESTO BUSCAMOS LA IMAGEN
            ivPersonajes.setImageResource(id); // LO MOSTRAMOS

        }

        else if (numAleatorio == 3 || numAleatorio == 7){ // {
            id = getResources().getIdentifier("vaca", "drawable", getPackageName()); // CON ESTO BUSCAMOS LA IMAGEN
            ivPersonajes.setImageResource(id); // LO MOSTRAMOS

        }
        else if (numAleatorio == 4 || numAleatorio == 6){ // {
            id = getResources().getIdentifier("pato", "drawable", getPackageName()); // CON ESTO BUSCAMOS LA IMAGEN
            ivPersonajes.setImageResource(id); // LO MOSTRAMOS

        }

        // BASE DE DATOS

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();// ABRIR LA BD modo lectura

        Cursor consulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null // SELECIONA TODO DE LA TABLA PUNTAJE DONDE EL SSCORE SEA IGUAL A SCORE
        );
        if (consulta.moveToFirst()) { // SI ENCONTRASTE ALGO EN LA INSTRUCCION ANTERIOR
            String tempNombre = consulta.getString(0);
            String tempScore = consulta.getString(1);
            tvBestScore.setText("Record: " + tempScore + "  de  " + tempNombre);
            BD.close();


        } else {
            BD.close();
        }







        // SONIDO

        mPlayer = MediaPlayer.create(this, R.raw.alphabet_song);
        mPlayer.start();
        mPlayer.setLooping(true); // PISTA EN REPRODUCION



    }


    public void Jugar (View view){
        String nombre = etNombre.getText().toString();

        if (!nombre.equals("")){ // SI ES DIFERENTE A ALGO ESCRITO QUE HAGA LA ACCION
            mPlayer.stop();
            mPlayer.release(); // DESTRUIR EL OBJETO DE MEDIA PLAER ASI LIBERA RECURSOS

            Intent intent = new Intent(this, Main2ActivityNivel1.class);
            intent.putExtra("jugador", nombre); // ENVIAR EL NOMBRE DEL JUGADOR AL SIGUIDENTE ACTIVITY
            startActivity(intent); // ABRIMOS EL ACTIVITY
            finish(); // CERRAMOS EL ACTUAL


        } else {
            Toast.makeText(this, "Primero debes escribir tu Nombre", Toast.LENGTH_SHORT).show();

            etNombre.requestFocus(); // Abrir el teclado en la caja de texto
            InputMethodManager inMet = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            inMet.showSoftInput(etNombre, InputMethodManager.SHOW_IMPLICIT);
            // SE TIENE QUE ABRIR EL TECLADO
        }

    }


    @Override // SOBRESCRBIR UN METODO

    public void onBackPressed (){ // BOTON PARA VOLVER

    }


}
