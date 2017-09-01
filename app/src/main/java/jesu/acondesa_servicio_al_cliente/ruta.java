package jesu.acondesa_servicio_al_cliente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    public List<Person> persons;
    public RecyclerView rv;
    private RecyclerView.LayoutManager llm;
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

        initializeData();
        initializeAdapter();
        return rootView;


    }

    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new Person("Jesus Suarez", "Calle 115 17 10", R.mipmap.carrito_compras));
        persons.add(new Person("Jorbel Perez", "Cra 45 70 55", R.mipmap.carrito_compras));
        persons.add(new Person("Adelita Casalins", "Cra 50 74 208", R.mipmap.carrito_compras));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }
}
