package mx.ita.daytoday;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mx.ita.daytoday.AgregarNota.Agregar_Nota;
import mx.ita.daytoday.Contactos.Listar_Contactos;
import mx.ita.daytoday.ListarNotas.Listar_Notas;
import mx.ita.daytoday.Perfil.Perfil_Usuario;

public class menuPrincipal extends AppCompatActivity {

    Button cerrarSesion, agregarNotas, listarNotas, Contactos, emerg, chats, acercaDe;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView uidPrincipal, nombresPrincipal, correoPrincipal;
    ProgressBar progressBarDatos;

    LinearLayoutCompat Linear_Nombres , Linear_Correo ,Linear_Verificacion;
    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Agenda Personal");

        uidPrincipal = findViewById(R.id.uidPrincipal);

        cerrarSesion = findViewById(R.id.cerrarSesion);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        nombresPrincipal = findViewById(R.id.nombresPrincipal);
        correoPrincipal = findViewById(R.id.correoPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);

        Linear_Nombres = findViewById(R.id.Linear_Nombres);
        Linear_Correo = findViewById(R.id.Linear_Correo);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        agregarNotas = findViewById(R.id.agregarNotas);
        listarNotas = findViewById(R.id.listarNotas);

        Contactos = findViewById(R.id.Contactos);
        emerg = findViewById(R.id.emerg);

        acercaDe = findViewById(R.id.acercaDe);

        cerrarSesion = findViewById(R.id.cerrarSesion);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        agregarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid_usuario = uidPrincipal.getText().toString();
                String correo_usuario = correoPrincipal.getText().toString();

                Intent intent =  new Intent(menuPrincipal.this, Agregar_Nota.class);
                intent.putExtra("uid", uid_usuario);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);

            }
        });
        listarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menuPrincipal.this, Listar_Notas.class));
                Toast.makeText(menuPrincipal.this, "Listar Notas", Toast.LENGTH_SHORT).show();
            }
        });
        /*importantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menuPrincipal.this, Notas_Importantes.class));
                Toast.makeText(menuPrincipal.this, "Archivar Notas", Toast.LENGTH_SHORT).show();
            }
        });*/
        Contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MenuPrincipal.this, "Contactos", Toast.LENGTH_SHORT).show();
                /*Obteniendo el dato uid del usuario*/
                String uid_usuario = uidPrincipal.getText().toString();
                Intent intent = new Intent(menuPrincipal.this, Listar_Contactos.class);
                /*Enviamos el dato a la siguiente actividad*/
                intent.putExtra("Uid", uid_usuario);
                startActivity(intent);
            }
        });
        emerg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:911");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                Toast.makeText(menuPrincipal.this, "Llamada de emergencia", Toast.LENGTH_SHORT).show();
            }
            /*public void onClick(View v) {
                Toast.makeText(menuPrincipal.this, "Agregar Nota", Toast.LENGTH_SHORT).show();
            }*/
        });
        /*chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, "Hola ");
                try{
                    startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    Toast.makeText(menuPrincipal.this, "La aplicación Whastapp no se encuentra instalada en el dispositivo.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(menuPrincipal.this, "Información", Toast.LENGTH_SHORT).show();
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirApplicacion();
            }
        });
    }

    @Override
    protected void onStart() {
        comprobarInicioSesion();
        super.onStart();
    }

    private void comprobarInicioSesion(){
        if(user!=null){
            //usuario ha iniciado sesion
            cargaDatos();
        }else{
            //lo dirigira al MainActivity
            startActivity(new Intent(menuPrincipal.this, MainActivity.class));
        }
    }
    private void cargaDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //si el usuario existe
                if(snapshot.exists()){
                    //progressbar se oculta
                    progressBarDatos.setVisibility(View.GONE);
                    //textviews se muestran
                    //uidPrincipal.setVisibility(View.VISIBLE);
                    //nombresPrincipal.setVisibility(View.VISIBLE);
                    //correoPrincipal.setVisibility(View.VISIBLE);
                    Linear_Nombres.setVisibility(View.VISIBLE);
                    Linear_Correo.setVisibility(View.VISIBLE);
                    //Linear_Verificacion.setVisibility(View.VISIBLE);

                    //obtener datos
                    String uid = " " +snapshot.child("uid").getValue();
                    String nombres = " " +snapshot.child("nombres").getValue();
                    String correo = " " +snapshot.child("correo").getValue();

                    //setear en los respectivos text view
                    uidPrincipal.setText(uid);
                    nombresPrincipal.setText(nombres);
                    correoPrincipal.setText(correo);

                    //habilitar botones menú
                    agregarNotas.setEnabled(true);
                    listarNotas.setEnabled(true);
                    /*importantes.setEnabled(true);*/
                    Contactos.setEnabled(true);
                    emerg.setEnabled(true);
                    /*chats.setEnabled(true);*/
                    acercaDe.setEnabled(true);
                    cerrarSesion.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Perfil_usuario){
            startActivity(new Intent(menuPrincipal.this, Perfil_Usuario.class));
        }
        /*if (item.getItemId() == R.id.Configuracion){
            String uid_usuario = uidPrincipal.getText().toString();

            Intent intent = new Intent(menuPrincipal.this, Configuracion.class);
            intent.putExtra("Uid", uid_usuario);
            startActivity(intent);
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void salirApplicacion(){
        firebaseAuth.signOut();
        startActivity(new Intent(menuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste Sesión cexitosamente", Toast.LENGTH_SHORT).show();
    }
}