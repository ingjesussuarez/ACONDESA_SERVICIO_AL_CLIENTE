package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerPedidoActivity extends AppCompatActivity {
    static String datapedido = "";
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
        datapedido = bundle.getString("datapedido");
        //Toast.makeText(this, datapedido, Toast.LENGTH_SHORT).show();
        cargarPedido();
    }

    private void cargarPedido(){
        //showProgress(true);
        RequestQueue peticiones = Volley.newRequestQueue(getApplicationContext());
        JSONObject pedido = null;
        String url = "";

        try {
            pedido = new JSONObject(datapedido);
            consecutivo.setText(pedido.getString("consecutivo"));
            nombreCliente.setText(pedido.getString("nombresucursal"));
            fecha.setText(pedido.getString("fecha"));

            url = "http://movilwebacondesa.com/movilweb/app3/MuestraDetalle.php?consecutivo="+pedido.getString("consecutivo");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(this, datapedido, Toast.LENGTH_SHORT).show();
        //showDialog("datapedido",datapedido,true,"ok");
        final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
        // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
        // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
        final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
        // se muestra error, hay que manejar este evento
        final Context context = this;

        StringRequest peticion = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String resp) {
                        Toast.makeText(context, "resp:"+resp, Toast.LENGTH_SHORT).show();
                        // Every UI object must have a layout parameter
                        int widthContent = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        int heightContent = RelativeLayout.LayoutParams.WRAP_CONTENT;

                        // Create relative layout
                        RelativeLayout.LayoutParams rlParamsName = new RelativeLayout.LayoutParams(widthContent,heightContent);

                        // Create table row layout
                        int trWidthContent = TableRow.LayoutParams.WRAP_CONTENT;
                        int trHeightContent = TableRow.LayoutParams.WRAP_CONTENT;

                        TableRow.LayoutParams trLayout = new TableRow.LayoutParams(trWidthContent, trHeightContent);

                        // Create a linear layout to add new object as vertical
                        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablaproductos);


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
                            precios[i] = jsonObject.getString("precio2");


                                    final TableRow tr = new TableRow(context);
                                    tr.setLayoutParams(trLayout);
                                    tr.setId(i);
                                    //tr.setBackgroundColor(0xFFBEBEBE);

                                    // What is the text view for category name
                                    TextView nombre = new TextView(context);
                                    nombre.setPadding(8, 8, 8, 8);
                            nombre.setTextSize(10);
                            nombre.setLayoutParams(trLayout);
                            nombre.setText(nombresproductos[i]);
                            nombre.setId(i);
                            nombre.setTypeface(null, Typeface.BOLD);
                            nombre.setTextColor(0xFF000000);
//push
                                    // What is count for category
                                    TextView cantidad = new TextView(context);
                            cantidad.setPadding(7, 8, 8, 8);
                            cantidad.setLayoutParams(trLayout);
                            //cantidad.setBackgroundColor(Color.WHITE);
                            cantidad.setText(cantidades[i]);

                            TextView preciostext = new TextView(context);
                            preciostext.setPadding(8, 8, 8, 8);
                            preciostext.setTextSize(10);
                            preciostext.setLayoutParams(trLayout);
                            preciostext.setText(precios[i]);
                            preciostext.setId(i);
                            preciostext.setTypeface(null, Typeface.BOLD);
                            preciostext.setTextColor(0xFF000000);

                                    // Add name of category
                                    tr.addView(nombre);

                                    // Add count for category
                                    tr.addView(cantidad);
                            tr.addView(preciostext);



                                    tableLayout.addView(tr);


                                    // What happen when user click on table row
                                    /*tr.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {

                                            Toast.makeText(Main.this, ""+tr.getId(),Toast.LENGTH_SHORT).show();

                                            Log.e("TAG", " clicked ID: "+tr.getId());
                                        }
                                    });
                                    */
                            }// end for SpecialCategories.length()

                            //Add button number two to the activity.
                          //  RelativeLayout objRelativeLayout = (RelativeLayout)findViewById(R.id.row_special);
                            //objRelativeLayout.addView(objLinearLayout);

                    }catch(JSONException e){

                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //showProgress(false);

            }
        });
        peticion.setShouldCache(true);
        peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        peticiones.add(peticion);

    }

    public void showDialog(String titulo, String msg, boolean cancelable, String okText) {
        //funcion que se invoca desde el webview y recibe un string de mensaje y otro para el tipo de dialogo
        // para mostrarlo un dialogo en la app
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
