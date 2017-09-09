package jesu.acondesa_servicio_al_cliente;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegistrarPedido extends AppCompatActivity {

    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas
    static WebView webview;
    String otradata = new String();
    String datacliente = "";
    String datavendedor = "";
    private View mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        //datos del cliente en formato JSON; traidos desde el RVAdapter
        datacliente = bundle.getString("datacliente");
        //datos del vendedor en formato JSON; traidos desde el RVAdapter
        datavendedor = bundle.getString("datavendedor");

        mProgressView = findViewById(R.id.progress_pedido);
        sharedPreferences = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        webview = new WebView(this);
        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setAppCacheEnabled(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        final Context c = this;
        webview.addJavascriptInterface(new MyJavaScriptInterface(c), "Android");

        //obtener los datos que faltan: fecha y numconsecutivo
        SharedPreferences sharedPreferences = this.getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        String codvendedor = sharedPreferences.getString("codvendedor", "none");
        getOtraData(codvendedor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cerrarActivity(){

         finish();

    }

    @Override
    public void onBackPressed(){

    }

    public void getOtraData(final String codvendedor){

        showProgress(true);
        RequestQueue peticiones = Volley.newRequestQueue(getApplicationContext());
        String url = "http://movilwebacondesa.com/movilweb/app3/DatosFormPedidos.php?vendedor="+codvendedor;
        final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
        // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
        // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
        final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
        // se muestra error, hay que manejar este evento

        JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject resp) {
                                otradata = resp.toString();
                        showProgress(false);
                        Toast.makeText(getApplicationContext(), "codvendedor: "+codvendedor+" otradadata: "+ otradata, Toast.LENGTH_LONG).show();
                        webview.loadUrl("file:///android_asset/registrarProducto.html");
                        //podemos cargar los datos del cliente usando la variable data, recibida desde el cardview seleccionado
                        // IMPORTANTE: se esto se hace solo despues que cargue la pagina completa (hasta el javascript),
                        // por eso se carga dentro de un WebViewClient para capturar el envento de fin de carga de la web,
                        // y solo entonces podemos invocar cualquier metodo JS que este dentro de la pagina
                        webview.setWebViewClient(new WebViewClient() {
                            public void onPageFinished(WebView web, String url) {

                                web.loadUrl("javascript:set_datos_pedido('" + datacliente + "','" + datavendedor + "','" + otradata + "');");
                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showProgress(false);

            }
        });
        peticion.setShouldCache(true);
        peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        peticiones.add(peticion);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedidos2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_actualizar_productos) {
            //llamar a la funcion javascript para actualizar productos
            webview.loadUrl("javascript:actualizar_productos();");
            return true;
        }
        if (id == R.id.action_cancelar) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            webview.setVisibility(show ? View.GONE : View.VISIBLE);
            webview.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    webview.setVisibility(show ? View.GONE : View.VISIBLE);
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
            webview.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    class MyJavaScriptInterface {
        Context mContext;


        MyJavaScriptInterface(Context c) {
            mContext = c; // contexto
        }

        public MyJavaScriptInterface() {

        }


        @JavascriptInterface
        public void showToast(String msg, int duracion) {
            //funcion que se invoca desde el webview y recibe un string de mensaje y un entero para la duracion en ms,
            // para mostrar un toast en la app
            Toast.makeText(mContext, msg, duracion).show();
        }

        @JavascriptInterface
        public void cerrar() {
            cerrarActivity();

        }
        @JavascriptInterface
        public void showDialog(String titulo, String msg, boolean cancelable, String okText, String noText, final String action) {
            //funcion que se invoca desde el webview y recibe un string de mensaje y otro para el tipo de dialogo
            // para mostrarlo un dialogo en la app
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle(titulo);
            dialog.setMessage(msg);
            dialog.setCancelable(cancelable);
            if (!okText.isEmpty()) {
                dialog.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i) {
                        // accion al presionar el boton OK haciendo switch a la variable action
                        switch (action) {
                            case "enviar_pedido":
                                return;
                            case "accion2":
                                return;
                            default:
                        }
                    }
                });
            }
            if (!noText.isEmpty()) {
                dialog.setNegativeButton(noText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i) {
                        // accion al presionar el boton cancelar
                        switch (action) {
                            case "accion1":
                                return;
                            case "accion2":
                                return;
                            default:
                        }
                    }
                });
            }
            dialog.show();
        }

        @JavascriptInterface
        public void showDialog(String titulo, String msg, boolean cancelable, String okText) {
            //funcion que se invoca desde el webview y recibe un string de mensaje y otro para el tipo de dialogo
            // para mostrarlo un dialogo en la app
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle(titulo);
            dialog.setMessage(msg);
            dialog.setCancelable(cancelable);
            if (!okText.isEmpty()) {
                dialog.setPositiveButton(okText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i) {
                        // accion al presionar el boton OK haciendo switch a la variable action

                    }
                });
            }

            dialog.show();
        }


    }


}