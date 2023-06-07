package mx.ita.daytoday.Contactos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import mx.ita.daytoday.R;

public class Actualizar_Contacto extends AppCompatActivity {
    TextView Id_C_A, Uid_C_A, Telefono_C_A;
    EditText Nombres_C_A, Apellidos_C_A, Correo_C_A, Edad_C_A, Direccion_C_A;
    ImageView Imagen_C_A, Actualizar_imagen_C_A, Actualizar_Telefono_C_A;
    Button Btn_Actualizar_C_A;

    String id_c , uid_usuario, nombres_c, apellidos_c, correo_c, telefono_c, edad_c, direccion_c;

    Dialog dialog_establecer_telefono;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Uri imagenUri = null;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Actualizar contacto");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        InicializarVistas();
        RecuperarDatos();
        SetearDatosRecuperados();

        Actualizar_Telefono_C_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Establecer_telefono_usuario();
            }
        });
        Btn_Actualizar_C_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarInformacionContacto();
            }
        });


    }

    private void InicializarVistas(){
        Id_C_A = findViewById(R.id.Id_C_A);
        Uid_C_A = findViewById(R.id.Uid_C_A);
        Telefono_C_A = findViewById(R.id.Telefono_C_A);
        Nombres_C_A = findViewById(R.id.Nombres_C_A);
        Apellidos_C_A = findViewById(R.id.Apellidos_C_A);
        Correo_C_A = findViewById(R.id.Correo_C_A);
        Edad_C_A = findViewById(R.id.Edad_C_A);
        Direccion_C_A = findViewById(R.id.Direccion_C_A);
        Imagen_C_A = findViewById(R.id.Imagen_C_A);
        Actualizar_imagen_C_A = findViewById(R.id.Actualizar_imagen_C_A);
        Actualizar_Telefono_C_A = findViewById(R.id.Actualizar_Telefono_C_A);
        Btn_Actualizar_C_A = findViewById(R.id.Btn_Actualizar_C_A);

        dialog_establecer_telefono = new Dialog(Actualizar_Contacto.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

    }

    private void RecuperarDatos(){
        Bundle bundle = getIntent().getExtras();
        id_c = bundle.getString("id_c");
        uid_usuario = bundle.getString("uid_usuario");
        nombres_c = bundle.getString("nombres_c");
        apellidos_c = bundle.getString("apellidos_c");
        correo_c = bundle.getString("correo_c");
        telefono_c = bundle.getString("telefono_c");
        edad_c = bundle.getString("edad_c");
        direccion_c = bundle.getString("direccion_c");
    }

    private void SetearDatosRecuperados(){
        Id_C_A.setText(id_c);
        Uid_C_A.setText(uid_usuario);
        Nombres_C_A.setText(nombres_c);
        Apellidos_C_A.setText(apellidos_c);
        Correo_C_A.setText(correo_c);
        Telefono_C_A.setText(telefono_c);
        Edad_C_A.setText(edad_c);
        Direccion_C_A.setText(direccion_c);
    }

    private void Establecer_telefono_usuario(){
        CountryCodePicker ccp;
        EditText Establecer_Telefono;
        Button Btn_Aceptar_Telefono;

        dialog_establecer_telefono.setContentView(R.layout.cuadro_dialogo_establecer_telefono);

        ccp = dialog_establecer_telefono.findViewById(R.id.ccp);
        Establecer_Telefono = dialog_establecer_telefono.findViewById(R.id.Establecer_Telefono);
        Btn_Aceptar_Telefono = dialog_establecer_telefono.findViewById(R.id.Btn_Aceptar_Telefono);

        Btn_Aceptar_Telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo_pais = ccp.getSelectedCountryCodeWithPlus();
                String telefono = Establecer_Telefono.getText().toString();
                String codigo_pais_telefono = codigo_pais+telefono; //+51956605043

                if (!telefono.equals("")){
                    Telefono_C_A.setText(codigo_pais_telefono);
                    dialog_establecer_telefono.dismiss();
                }else {
                    Toast.makeText(Actualizar_Contacto.this, "Ingrese un número telefónico", Toast.LENGTH_SHORT).show();
                    dialog_establecer_telefono.dismiss();
                }
            }
        });

        dialog_establecer_telefono.show();
        dialog_establecer_telefono.setCanceledOnTouchOutside(true);
    }

    private void ActualizarInformacionContacto(){
        String NombresActualizar = Nombres_C_A.getText().toString().trim();
        String ApellidosActualizar = Apellidos_C_A.getText().toString().trim();
        String CorreoActualizar = Correo_C_A.getText().toString().trim();
        String TelefonoActualizar = Telefono_C_A.getText().toString().trim();
        String EdadActualizar = Edad_C_A.getText().toString().trim();
        String DireccionActualizar = Direccion_C_A.getText().toString().trim();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");

        Query query = databaseReference.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("nombres").setValue(NombresActualizar);
                    ds.getRef().child("apellidos").setValue(ApellidosActualizar);
                    ds.getRef().child("correo").setValue(CorreoActualizar);
                    ds.getRef().child("telefono").setValue(TelefonoActualizar);
                    ds.getRef().child("edad").setValue(EdadActualizar);
                    ds.getRef().child("direccion").setValue(DireccionActualizar);
                }

                Toast.makeText(Actualizar_Contacto.this, "Información actualizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}