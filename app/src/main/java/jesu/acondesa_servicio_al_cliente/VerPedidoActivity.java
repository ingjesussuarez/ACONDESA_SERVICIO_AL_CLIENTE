package jesu.acondesa_servicio_al_cliente;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerPedidoActivity extends AppCompatActivity {
    static String idpedido = "";
    static TextView nombreCliente;
    static TextView consecutivo;
    static TextView fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        consecutivo = (TextView) findViewById(R.id.consecutivotext);
        fecha = (TextView) findViewById(R.id.fechatext);
        nombreCliente = (TextView) findViewById(R.id.nomclientetext);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Bundle bundle = getIntent().getExtras();
        idpedido = bundle.getString("idpedido");
        cargarPedido(idpedido);
    }

    private void cargarPedido(String id){
        //showProgress(true);
        RequestQueue peticiones = Volley.newRequestQueue(getApplicationContext());
        String url = "http://movilwebacondesa.com/movilweb/app3/MuestraDetalles.php?pedido="+id;
        final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
        // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
        // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
        final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
        // se muestra error, hay que manejar este evento

        JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String resp) {

                    try {
                        JSONArray jsonArraypedidosList = new JSONArray(resp);

                        int length = jsonArraypedidosList.length();
                        String[] nombresproductos = new String[length];
                        String[] idproductos = new String[length];
                        String[] cantidades = new String[length];
                        String[] factores = new String[length];
                        String[] preciosunit = new String[length];
                        String[] precios = new String[length];
                        String[] precio = new String[length];
                        float totalPedido = 0;
                        JSONObject jsonObject;
                        for (int i = 0; i < length; i++) {
                            jsonObject = jsonArraypedidosList.getJSONObject(i);
                            nombresproductos[i] = jsonObject.getString("nomproducto");
                            idproductos[i] = jsonObject.getString("id");
                            cantidades[i] = jsonObject.getString("cantidadp");
                            factores[i] = jsonObject.getString("factorp");
                            Toast.makeText(getApplicationContext(), "codvendedor: " + codvendedor + " otradadata: " + otradata, Toast.LENGTH_LONG).show();
                           // String
                        }
                    }catch(JSONException e){

                    }
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

    });
}
