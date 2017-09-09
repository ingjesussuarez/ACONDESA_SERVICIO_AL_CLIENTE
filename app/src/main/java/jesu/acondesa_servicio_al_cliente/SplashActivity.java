package jesu.acondesa_servicio_al_cliente;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by JESUS SUAREZ on 08/19/2017.
 */
public class SplashActivity extends Activity {

    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 5000; // 3 segundos
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas
    String usuario = "";
    String password = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tenemos una plantilla llamada splash.xml donde mostraremos la información que queramos (logotipo, etc.)
        setContentView(R.layout.splash);

        sharedPreferences = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", "none");
        password = sharedPreferences.getString("password", "none");


        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
  //              if (password.equals("none") && usuario.equals("none")) {

                  startLogin();
      //          }else{
    //                startMain();
      //          }
                finish();
            }
        }, DURACION_SPLASH);
    }
    @Override
    public void onResume() {
        super.onResume();
        /*sharedPreferences = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", "none");
        password = sharedPreferences.getString("password", "none");
*/
        //al resumir la aplicacion, obtener los datos de login guardados en sharedpreferences,
        // para mantener la ultima sesion y asi el ultimo estado de la app

  //      if (password.equals("none") && usuario.equals("none")) {
            //enviar al login

    //        startLogin();
     //       finish();
       // }else{
       //     startMain();
         //   finish();
        //}

    }

    @Override
    public void onStop() {
        super.onStop();
        //al detenerse la app, guardar los datos de login en sharedpreferences
        Context context = this;
        sharedPreferences = context.getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("password", password);
        editor.commit();
    }

    public void startLogin(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void startMain(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
