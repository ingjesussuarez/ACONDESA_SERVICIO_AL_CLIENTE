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
    private String email = "";
    private String password = "";
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas
    ProgressBar progress;

    public ruta(){
        persons = new ArrayList<>();
        adapter = new RVAdapter(persons);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ruta, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rv);
//ruta
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);

        sharedPreferences = this.getContext().getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "none");
        password = sharedPreferences.getString("password", "none");

        progress = (ProgressBar) rootView.findViewById(R.id.progressBar);

        if(persons.isEmpty())
            llenarRuta();
        else
            initializeAdapter();

        return rootView;


    }


    private void initializeAdapter(){
        adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }

    public void llenarRuta(){
        persons = new ArrayList<>();
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
        String url= "http://movilwebacondesa.com/movilweb/app3/MuestraRuta.php?usuario="+email+"&dia="+diaHoy;
        Toast.makeText(getContext().getApplicationContext(),url,Toast.LENGTH_SHORT).show();


        progress.setVisibility(View.VISIBLE);

        StringRequest peticion = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String JSONresponse) {


                // Toast.makeText(getContext(), "Recibiendo...", Toast.LENGTH_LONG).show();

                JSONObject jsonObject=null;

                try {

                    JSONArray jsonArrayPersons = new JSONArray(JSONresponse);

                    String[] nombres = new String[jsonArrayPersons.length()];
                    String[] direcciones = new String[jsonArrayPersons.length()];
                    String[] telefonos = new String[jsonArrayPersons.length()];

                    for(int i=0;i< jsonArrayPersons.length();i++){

                        jsonObject = jsonArrayPersons.getJSONObject(i);

                        nombres[i] = jsonObject.getString("nombresucursal");
                        direcciones[i] = jsonObject.getString("direccion");
                        telefonos[i] = jsonObject.getString("telefono");

                        Person personObject =  new Person(nombres[i],direcciones[i],R.mipmap.carrito_compras,telefonos[i]);
                        persons.add(personObject);
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
