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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private String email = "";
    private String password = "";
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
        rv=(RecyclerView)rootView.findViewById(R.id.rv);
//pedidos
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);

        sharedPreferences = this.getContext().getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "none");
        password = sharedPreferences.getString("password", "none");

        progress = (ProgressBar) rootView.findViewById(R.id.progressBar);

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
        Calendar hoy = Calendar.getInstance();
        String[] dias = new String[]{
                "Domingo",
                "Lunes",
                "Martes",
                "Miercoles",
                "Jueves",
                "Viernes",
                "Sabado"
        };
        //String diaHoy = dias[hoy.get(Calendar.DAY_OF_WEEK)-1];
        String diaHoy = "Martes";//para pruebas, borrar al entrar en produccion
        String url= "http://movilwebacondesa.com/movilweb/app3/Muestrapedidos.php?usuario="+email+"&dia="+diaHoy;
        Toast.makeText(getContext().getApplicationContext(),url,Toast.LENGTH_SHORT).show();


        progress.setVisibility(View.VISIBLE);

        StringRequest peticion = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String JSONresponse) {


                // Toast.makeText(getContext(), "Recibiendo...", Toast.LENGTH_LONG).show();

                JSONObject jsonObject=null;

                try {

                    JSONArray jsonArraypedidosList = new JSONArray(JSONresponse);

                    String[] nombres = new String[jsonArraypedidosList.length()];
                    String[] direcciones = new String[jsonArraypedidosList.length()];
                    String[] telefonos = new String[jsonArraypedidosList.length()];

                    for(int i=0;i< jsonArraypedidosList.length();i++){

                        jsonObject = jsonArraypedidosList.getJSONObject(i);

                        nombres[i] = jsonObject.getString("nombresucursal");
                        direcciones[i] = jsonObject.getString("direccion");
                        telefonos[i] = jsonObject.getString("telefono");

                        Person personObject =  new Person(nombres[i],direcciones[i],R.mipmap.carrito_compras,telefonos[i]);
                        pedidosList.add(personObject);
                    }
                    initializeAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                //          progress.setVisibility(View.GONE);

                //call initializeAdapter()
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext().getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        colaPeticiones.add(peticion);

    }

}
