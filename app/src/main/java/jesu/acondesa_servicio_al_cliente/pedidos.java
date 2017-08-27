package jesu.acondesa_servicio_al_cliente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link pagos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link pagos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pedidos extends Fragment {

    public List<Person> persons;
    public RecyclerView rv;
    private RecyclerView.LayoutManager llm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pedidos, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rv);

        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
        return rootView;


    }

    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new Person("Jesus Suarez", "Consecutivo: 50412122017001", R.mipmap.erdabn));
        persons.add(new Person("Jorbel Perez", "Consecutivo: 50412122017001", R.mipmap.erdabn));
        persons.add(new Person("Adelita Casalins", "Consecutivo: 50412122017001", R.mipmap.erdabn));
    }

    private void initializeAdapter(){
        RVAdapterRojo adapter = new RVAdapterRojo(persons);
        rv.setAdapter(adapter);
    }
}
