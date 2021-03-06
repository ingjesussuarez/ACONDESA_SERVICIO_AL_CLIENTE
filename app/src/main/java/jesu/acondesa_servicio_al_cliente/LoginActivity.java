package jesu.acondesa_servicio_al_cliente;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via usuario/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserView = (AutoCompleteTextView) findViewById(R.id.usuario);

        mPasswordView = (EditText) findViewById(R.id.password);
        
        sharedPreferences = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUserSignInButton = (Button) findViewById(R.id.usuario_sign_in_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button salirButton = (Button) findViewById(R.id.button_salir);
        salirButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid usuario, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String usuario = mUserView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the usuario entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        // Check for a valid usuario address.
        if (TextUtils.isEmpty(usuario)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (isUserValid(usuario)) {
            mUserView.setError("Ingrese un codigo de usuario valido");
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the usuario login attempt.
            showProgress(true);

            RequestQueue peticiones = Volley.newRequestQueue(getApplicationContext());
            String url = "http://movilwebacondesa.com/movilweb/app3/ValidaUser.php?usuario="+usuario+"&password="+password;
            Toast.makeText(LoginActivity.this, url, Toast.LENGTH_SHORT).show();
            final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
            // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
            // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
            final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
            // se muestra error, hay que manejar este evento
            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject resp) {
                            showProgress(false);
                            try {

                                //Toast.makeText(LoginActivity.this, validation, Toast.LENGTH_LONG).show();
                                if(resp.getString("validacion").equals("ok")){
                                    //abrimos el editor de sesiones y guardamos las credenciales en la sesion
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("usuario",usuario); // insertando el usuario
                                    editor.putString("password",password); // insertando la clave
                                    editor.putString("nomvendedor",resp.getString("nomvendedor")); // insertando el nombre del vendedor
                                    editor.putString("idvendedor",resp.getString("idvendedor")); // insertando el id del vendedor
                                    editor.putString("codvendedor",resp.getString("codigo")); // insertando el codigo del vendedor
                                    editor.putString("idzona",resp.getString("idzona")); // insertando el codigo del vendedor
                                    editor.commit(); // guardar datos
                                    mPasswordView.setText("");
                                    //mUserView.setText("");
                                    Intent MainActivity = new Intent(getApplicationContext(), jesu.acondesa_servicio_al_cliente.Usuario.class);
                                    startActivity(MainActivity);

                                    Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_LONG).show();

                                }else {

                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "Error en respuesta del servidor", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "Error al conectar: "+volleyError, Toast.LENGTH_SHORT).show();
                }
            });

            peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            peticiones.add(peticion);
        }
    }

    private boolean isUserValid(String usuario) {
        //TODO: Replace this with your own logic
        return usuario.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void cerrar(){
        finish();
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

