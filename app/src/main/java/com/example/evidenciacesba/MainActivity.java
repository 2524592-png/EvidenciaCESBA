package com.example.evidenciacesba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imgFoto;
    private EditText edtTitulo;
    private EditText edtDescripcion;
    private Button btnTomarFoto;
    private Button btnGuardar;
    private TextView txtEstado;

    private Bitmap fotografiaBitmap = null;

    private final ActivityResultLauncher<Intent> launcherCamara =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    resultado -> {
                        if (resultado.getResultCode() == RESULT_OK) {
                            Intent datos = resultado.getData();

                            if (datos != null && datos.getExtras() != null) {
                                fotografiaBitmap = (Bitmap) datos.getExtras().get("data");
                                imgFoto.setImageBitmap(fotografiaBitmap);
                                txtEstado.setText("Fotografía tomada correctamente");

                                // Cambiar el icono del botón a una palomita (bien)
                                btnTomarFoto.setCompoundDrawablesWithIntrinsicBounds(
                                        android.R.drawable.checkbox_on_background, 0, 0, 0
                                );
                                btnTomarFoto.setText(""); // Oculta texto para dejar solo el icono

                                Toast.makeText(
                                        MainActivity.this,
                                        "Fotografía recibida",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        } else {
                            txtEstado.setText("La fotografía fue cancelada");

                            // Cambiar el icono del botón a una X (mal/cancelado)
                            btnTomarFoto.setCompoundDrawablesWithIntrinsicBounds(
                                    android.R.drawable.ic_delete, 0, 0, 0
                            );
                            btnTomarFoto.setText(""); // Oculta texto para dejar solo el icono

                            Toast.makeText(
                                    MainActivity.this,
                                    "Operación cancelada",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CONECTAR JAVA CON XML
        imgFoto = findViewById(R.id.imgFoto);
        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnGuardar = findViewById(R.id.btnGuardar);
        txtEstado = findViewById(R.id.txtEstado);

        btnTomarFoto.setOnClickListener(v -> abrirCamara());
        btnGuardar.setOnClickListener(v -> guardarEvidencia());
    }

    private void abrirCamara() {
        Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intentCamara.resolveActivity(getPackageManager()) != null) {
            launcherCamara.launch(intentCamara);
        } else {
            Toast.makeText(
                    this,
                    "No se encontró una aplicación de cámara",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void guardarEvidencia() {
        String titulo = edtTitulo.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();

        // VALIDAR TÍTULO
        if (titulo.isEmpty()) {
            edtTitulo.setError("Escribe un título");
            edtTitulo.requestFocus();
            return;
        }

        // VALIDAR DESCRIPCIÓN
        if (descripcion.isEmpty()) {
            edtDescripcion.setError("Escribe una descripción");
            edtDescripcion.requestFocus();
            return;
        }

        // VALIDAR FOTOGRAFÍA
        if (fotografiaBitmap == null) {
            Toast.makeText(this, "Por favor toma una fotografía antes de guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Evidencia registrada correctamente
        Toast.makeText(
                this,
                "Evidencia guardada",
                Toast.LENGTH_LONG
        ).show();
    }
}