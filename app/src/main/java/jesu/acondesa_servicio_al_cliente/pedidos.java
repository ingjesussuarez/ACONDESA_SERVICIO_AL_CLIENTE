package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link pedidos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link pedidos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pedidos extends Fragment {
    public static ArrayList<Person> pedidosList;
    public RecyclerView rv;
    private RecyclerView.LayoutManager llm;
    private RVAdapterRojo adapter;
    private String usuario = "";
    private String password = "";
    private TextView numPedidos;
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas
    ProgressBar progress;

    public pedidos(){
        pedidosList = new ArrayList<>();
        adapter = new RVAdapterRojo(pedidosList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pedidos, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rv2);
        numPedidos = (TextView) rootView.findViewById(R.id.num_pedidos);
//pedidos
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);

        sharedPreferences = this.getContext().getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", "none");
        password = sharedPreferences.getString("password", "none");

        progress = (ProgressBar) rootView.findViewById(R.id.progressBar2);

        if(pedidosList.isEmpty())
            llenarpedidos();
        else
            initializeAdapter();

        return rootView;


    }


    private void initializeAdapter(){
        adapter = new RVAdapterRojo(pedidosList);
        rv.setAdapter(adapter);
    }

    public void llenarpedidos(){
        pedidosList = new ArrayList<>();
        RequestQueue colaPeticiones = Volley.newRequestQueue(this.getContext().getApplicationContext());

        final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
        // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
        // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
        final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
        // se muestra error, hay que manejar este evento


        String url= "http://movilwebacondesa.com/movilweb/app3/MuestraPedidos.php?usuario="+usuario;

        progress.setVisibility(View.VISIBLE);

        StringRequest peticion = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

            @Override
            public void onResponse(String JSONresponse) {
                int numeroPedidos = 0;

                // Toast.makeText(getContext(), "Recibiendo...", Toast.LENGTH_LONG).show();

                JSONObject jsonObject=null;

                try {

                    JSONArray jsonArraypedidosList = new JSONArray(JSONresponse);

                    String[] nombres = new String[jsonArraypedidosList.length()];
                    String[] direcciones = new String[jsonArraypedidosList.length()];
                    String[] telefonos = new String[jsonArraypedidosList.length()];
                    numeroPedidos = jsonArraypedidosList.length();
                    numPedidos.setText(String.valueOf(numeroPedidos));

                    for(int i=0;i< numeroPedidos;i++){

                        jsonObject = jsonArraypedidosList.getJSONObject(i);

                        nombres[i] = jsonObject.getString("nombresucursal");
                        direcciones[i] = jsonObject.getString("consecutivo");
                        telefonos[i] = jsonObject.getString("fecha");

                        Person personObject =  new Person(nombres[i],direcciones[i],R.mipmap.carrito_compras,telefonos[i],"","");
                        pedidosList.add(personObject);
                    }
                    initializeAdapter();
                } catch (JSONException e) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error al conectar...", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }



                 progress.setVisibility(View.GONE);

                //call initializeAdapter()
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                initializeAdapter();
                Toast.makeText(getContext().getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        peticion.setShouldCache(true);
        peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        colaPeticiones.add(peticion);

    }

}
