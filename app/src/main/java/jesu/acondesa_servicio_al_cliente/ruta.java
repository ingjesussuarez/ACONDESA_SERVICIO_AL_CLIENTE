package jesu.acondesa_servicio_al_cliente;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.android.volley.RequestQueue;


import java.util.ArrayList;
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
    public static List<Person> persons;
    public RecyclerView rv;
    private RecyclerView.LayoutManager llm;
    RequestQueue colaPeticiones;
    private String nombre = "";
    private String direccion = "";
    private Person persona;
    private SharedPreferences sharedpreferences;
    private static final String MIS_PREFERENCIAS = "myPref"; // preferencias para el manejo de sesion nativa
    private String email = "";
    private String password = "";
    //Context context = getActivity().getApplicationContext();
    public ruta(){
        persons = new ArrayList<>();
    }

    public ruta(ArrayList<Person> per){

        persons = per;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ruta, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rv);
//ruta
       llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
/*
        sharedpreferences = context.getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString("email", "none");
        password = sharedpreferences.getString("password", "none");

*/

        //persons.add(new Person("Rafa","hhdhfh",R.mipmap.carrito_compras));

        //realizar una peticion http usando volley

        initializeAdapter();
        return rootView;


    }

    void setRutas(ArrayList<Person> pe){
        persons = pe;
        initializeAdapter();
        //redraw();
    }


    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }


}
