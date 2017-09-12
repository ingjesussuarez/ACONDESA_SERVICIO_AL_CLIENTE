package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ruta.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ruta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ruta extends Fragment {
    public static ArrayList<Person> persons;
    public RecyclerView rv;
    private RecyclerView.LayoutManager llm;
    private RVAdapter adapter;
    private String usuario = "";
    private String password = "";
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas
    ProgressBar progress;

    public ruta(){
        persons = new ArrayList<>();
        adapter = new RVAdapter(persons,1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ruta, container, false);
        rv= rootView.findViewById(R.id.rv);
//ruta
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);

        sharedPreferences = this.getContext().getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", "none");
        password = sharedPreferences.getString("password", "none");

        progress = rootView.findViewById(R.id.progressBar);

        if(persons.isEmpty())
            llenarRuta();
        else
            initializeAdapter();


        return rootView;


    }


    private void initializeAdapter(){
        adapter = new RVAdapter(persons,1);
        rv.setAdapter(adapter);
    }

    public void llenarRuta(){
        persons = new ArrayList<>();
        RequestQueue colaPeticiones = Volley.newRequestQueue(this.getContext().getApplicationContext());
        final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
        // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
        // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
        final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
        // se muestra error, hay que manejar este evento

        String url= "http://movilwebacondesa.com/movilweb/app3/MuestraRuta.php?usuario="+usuario;
        //Toast.makeText(this.getContext(), url, Toast.LENGTH_LONG).show();

        progress.setVisibility(View.VISIBLE);

        StringRequest peticion = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String JSONresponse) {

                JSONObject jsonObject=null;

                try {

                    JSONArray jsonArrayPersons = new JSONArray(JSONresponse);

                    int length = jsonArrayPersons.length();
                    String[] nombres = new String[length];
                    String[] direcciones = new String[length];
                    String[] telefonos = new String[length];
                    String[] datas = new String[length];
                    String[] idsucursales = new String[length];
                    int[] visitados = new int[length];
                    int[] hizopedidos = new int[length];
                    //a parte se necesitan idvendedor, numero consecutivo, codigovendedor y nombrevendedor

                    for(int i=0;i< jsonArrayPersons.length();i++){

                        jsonObject = jsonArrayPersons.getJSONObject(i);

                        nombres[i] = jsonObject.getString("nombresucursal");
                        direcciones[i] = jsonObject.getString("direccion");
                        telefonos[i] = jsonObject.getString("telefono");
                        idsucursales[i] = jsonObject.getString("idsucursal");
                        visitados[i] = jsonObject.getInt("visitado");
                        hizopedidos[i] = jsonObject.getInt("hizopedido");

                        sharedPreferences = getContext().getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
                        String codvendedor = sharedPreferences.getString("codvendedor", "none");
                        String idvendedor = sharedPreferences.getString("idvendedor", "none");

                        String datavendedor = "{\"codvendedor\":\""+codvendedor+"\",\"idvendedor\":\""+idvendedor+"\"}";

                        //armamos un String JSON para ser pasado a la activity registrarpedido y posteriormente
                        // ser parseado a un Objeto JavaScript
                        datas[i] = jsonObject.toString();

                        Person personObject =  new Person(nombres[i],direcciones[i],R.mipmap.carrito_compras,telefonos[i],
                                idsucursales[i],datas[i],datavendedor,hizopedidos[i],visitados[i]);
                        persons.add(personObject);
                    }
                    initializeAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getContext().getApplicationContext(), "Error: "+volleyError, Toast.LENGTH_LONG).show();

            }
        });
        peticion.setShouldCache(true);
        peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        colaPeticiones.add(peticion);

    }

}
