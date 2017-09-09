package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class cliente extends AppCompatActivity {
    public static ArrayList<Person> persons;
    public RecyclerView rv;
    private RecyclerView.LayoutManager llm;
    private ProgressBar progress;
    private ViewPager container;
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = (ProgressBar) findViewById(R.id.progress_clientes);
        container = (ViewPager) findViewById(R.id.container);

        rv=(RecyclerView)this.findViewById(R.id.rv);

        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        //new RVAdapter(this);
        initializeData();
        initializeAdapter();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent MainActivity3 = new Intent(getApplicationContext(), jesu.acondesa_servicio_al_cliente.cliente.class);
            //startActivity(MainActivity3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeData(){
        persons = new ArrayList<>();
        llenarClientes();
       /* persons.add(new Person("Jesus Suarez", "Calle 115 17 10", R.mipmap.carrito_compras,"76872232"));
        persons.add(new Person("Jorbel Perez", "Cra 45 70 55", R.mipmap.carrito_compras,"8769768"));
        persons.add(new Person("Adelita Casalins", "Cra 50 74 208", R.mipmap.carrito_compras,"879687"));*/
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }

    private void llenarClientes(){

            persons = new ArrayList<>();
            RequestQueue colaPeticiones = Volley.newRequestQueue(this.getApplicationContext());
            final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
            // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
            // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
            final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
            // se muestra error, hay que manejar este evento

            String url= "http://movilwebacondesa.com/movilweb/app3/MuestraClientes.php";
            //Toast.makeText(this.getContext(), url, Toast.LENGTH_LONG).show();

            progress.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);

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
                        String[] idclientes = new String[length];
                        //para obtener los datos del vendeor accedemos a las variables compartidas de la sesion y armamos una String en formato JSON

                        SharedPreferences sharedPreferences = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
                        String usuario = sharedPreferences.getString("usuario", "none");
                        String idvendedor = sharedPreferences.getString("idvendedor", "none");
                        String idzona = sharedPreferences.getString("idzona", "none");
                        String nomvendedor = sharedPreferences.getString("nomvendedor", "none");
                        String codvendedor = sharedPreferences.getString("codvendedor", "none");
                        String datavendedor = "";

                        datavendedor =  "{\"usuario\":\""+usuario+"\"," +
                                "\"idvendedor\":\"" + idvendedor + "\"," +
                                "\"idzona\":\"" + idzona + "\"," +
                                "\"nomvendedor\":\"" + nomvendedor + "\"," +
                                "\"codvendedor\":\"" + codvendedor+ "\"}";


                        //a parte se necesitan idvendedor, numero consecutivo, codigovendedor y nombrevendedor

                        for(int i=0;i< jsonArrayPersons.length();i++){

                            jsonObject = jsonArrayPersons.getJSONObject(i);

                            nombres[i] = jsonObject.getString("nombresucursal");
                            direcciones[i] = jsonObject.getString("direccion");
                            telefonos[i] = jsonObject.getString("telefono");
                            idclientes[i] = jsonObject.getString("id");



                            //armamos un String JSON para ser pasado a la activity registrarpedido y posteriormente
                            // ser parseado a un Objeto JavaScript
                            datas[i] = jsonObject.toString();

                            Person personObject =  new Person(nombres[i],direcciones[i],R.mipmap.carrito_compras,telefonos[i],
                                    idclientes[i],datas[i],datavendedor,false,false);
                            persons.add(personObject);
                        }
                        initializeAdapter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progress.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progress.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Error: "+volleyError, Toast.LENGTH_LONG).show();

                }
            });
            peticion.setShouldCache(true);
            peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            colaPeticiones.add(peticion);

        }


}
