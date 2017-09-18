package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link info.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class resumen extends Fragment {
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        llenarResumen(container);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resumen, container, false);

    }

    public void llenarResumen(final View view){

        RequestQueue colaPeticiones = Volley.newRequestQueue(this.getContext().getApplicationContext());
        final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
        // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
        // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
        final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
        // se muestra error, hay que manejar este evento
        final SharedPreferences[] sharedPreferences = new SharedPreferences[1];
        sharedPreferences[0] = this.getContext().getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        final String[] usuario = {sharedPreferences[0].getString("usuario", "none")};
        final String[] idvendedor = {sharedPreferences[0].getString("idvendedor", "none")};

        String url= "http://movilwebacondesa.com/movilweb/app3/MuestraResumen.php?usuario="+usuario+"&idvendedor="+idvendedor;
        //Toast.makeText(this.getContext(), url, Toast.LENGTH_LONG).show();

//        progress.setVisibility(View.VISIBLE);

        StringRequest peticion = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String JSONresponse) {
                //Toast.makeText(getContext(), JSONresponse, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(JSONresponse);
                    TextView aux = (TextView)view.findViewById(R.id.visitasProgramadas);
                    aux.setText(jsonObject.getInt("visitasProgramadas"));
                    aux = (TextView)view.findViewById(R.id.visitasRealizadas);
                    aux.setText(jsonObject.getInt("visitasRealizadas"));
                    aux = (TextView)view.findViewById(R.id.visitasEfectivas);
                    aux.setText(jsonObject.getInt("visitasEfectivas"));
                    aux = (TextView)view.findViewById(R.id.visitasPendientes);
                    aux.setText(jsonObject.getInt("visitasPendientes"));
                    aux = (TextView)view.findViewById(R.id.kilos);
                    aux.setText(String.valueOf(jsonObject.getDouble("kilos")));
                    aux = (TextView)view.findViewById(R.id.efectividadProgramadas);
                    aux.setText(String.valueOf(jsonObject.getDouble("efectividadProgramadas")+" %"));
                    aux = (TextView)view.findViewById(R.id.eficienciaVisitas);
                    aux.setText(String.valueOf(jsonObject.getDouble("eficienciaVisitas")+" %"));
                    aux = (TextView)view.findViewById(R.id.efectividadVisitas);
                    aux.setText(String.valueOf(jsonObject.getDouble("efectividadVisitas")+" %"));

                }catch(JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(getContext().getApplicationContext(), "Error: "+volleyError, Toast.LENGTH_LONG).show();

            }
        });
        peticion.setShouldCache(true);
        peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        colaPeticiones.add(peticion);

    }

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static resumen newInstance(int sectionNumber) {
            resumen fragment = new resumen();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

}
