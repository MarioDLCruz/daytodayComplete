package mx.ita.daytoday;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {


    EditText nombresEt, correoElectEt, contraseñaEt, confirmContraseñaEt;
    Button registrarUsuario;
    TextView tengoCuenta;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String nombre = " ", correo = " ", password = " ", confirmarpassword = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Registrar");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        nombresEt = findViewById(R.id.nombresEt);
        correoElectEt = findViewById(R.id.correoElectEt);
        contraseñaEt = findViewById(R.id.contraseñaEt);
        confirmContraseñaEt = findViewById(R.id.confirmContraseñaEt);
        registrarUsuario = findViewById(R.id.registrarUsuario);
        tengoCuenta = findViewById(R.id.tengoCuenta);

        firebaseAuth = firebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        registrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaraDatos();
            }
        });

        tengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class));
            }
        });

    }

    private void validaraDatos(){
        nombre = nombresEt.getText().toString();
        correo = correoElectEt.getText().toString();
        password = contraseñaEt.getText().toString();
        confirmarpassword = confirmContraseñaEt.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();

        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese contraseñá", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(confirmarpassword)){
            Toast.makeText(this, "Confirme contraseña", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(confirmarpassword)){
            Toast.makeText(this, "Las  contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }else{
            crearCuenta();
        }
    }

    private void crearCuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        //crear usuario en firebase

        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        guardarInformacion();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        }

    private void guardarInformacion() {
        progressDialog.setMessage("Guardando su información");
        progressDialog.dismiss();

        //obtener la identificacion de usuario actual
        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombres", nombre);
        Datos.put("password", password);

        Datos.put("apellidos", "");
        Datos.put("edad", "");
        Datos.put("telefono", "");
        Datos.put("domicilio", "");
        Datos.put("padecimientos", "");
        Datos.put("medicamentos", "");
        Datos.put("fecha_de_nacimiento", "");
        Datos.put("imagen_perfil","");


        DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databasereference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registro.this, menuPrincipal.class ));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}