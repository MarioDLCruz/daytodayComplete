package mx.ita.daytoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class pantallaCarga extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        firebaseAuth = FirebaseAuth.getInstance();

        int tiempo = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*startActivity(new Intent(pantallaCarga.this, MainActivity.class));
                finish();*/
                verificarUsuario();
            }
        },tiempo);
    }

    private void verificarUsuario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            startActivity(new Intent(pantallaCarga.this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(pantallaCarga.this, menuPrincipal.class));
            finish();
        }
    }
}