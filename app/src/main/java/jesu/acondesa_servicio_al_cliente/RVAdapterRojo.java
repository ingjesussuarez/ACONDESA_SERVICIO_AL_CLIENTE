package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RVAdapterRojo extends RecyclerView.Adapter<RVAdapterRojo.PersonViewHolder> {
    // private static Context context;
    //public RVAdapter(Context context) {
    //    this.context = context;
    //}



    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public MenuView.ItemView currentItem;
        Context context;
        CardView cv;
        TextView personName;
        TextView personAge;
        TextView personTel;
        TextView extradata;
        TextView data;
        ImageView personPhoto;
        ImageButton menuButton;

        private ItemClickListener clickListener;


        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = itemView.findViewById(R.id.cv);
            view = itemView;
            personName = itemView.findViewById(R.id.person_name);
            personAge = itemView.findViewById(R.id.person_age);
            personTel = itemView.findViewById(R.id.person_tel);
            data = itemView.findViewById(R.id.person_data);
            extradata = itemView.findViewById(R.id.extra_data);
            personPhoto = itemView.findViewById(R.id.person_photo);
            menuButton = new ImageButton(itemView.getContext());
            menuButton= view.findViewById(R.id.imageButton);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(),"data:"+ data.getText().toString(), Toast.LENGTH_SHORT).show();
                    showPopupMenu(view,data.getText().toString(),extradata.getText().toString());
                }
                private void showPopupMenu(View view,String data,String extradata) {
                    // inflate menu
                    PopupMenu popup = new PopupMenu(view.getContext(),view );
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_pedidos, popup.getMenu());
                    popup.setOnMenuItemClickListener(new MyMenuItemClickListener(view.getContext(),data,extradata));
                    popup.show();
                }

            });


        }


        @Override
        public void onClick(View v) {

        }

    }


    List<Person> persons;

    RVAdapterRojo(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemrojo, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).nombre);
        personViewHolder.personAge.setText(persons.get(i).direccion);
        personViewHolder.personTel.setText(persons.get(i).telefono);
        personViewHolder.data.setText(persons.get(i).data);
        personViewHolder.extradata.setText(persons.get(i).extradata);
        personViewHolder.personPhoto.setImageResource(R.drawable.boton_redondo_rojo);
        //personViewHolder.itemView = persons.get(i).photoId);


    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
    static class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private Context context;
        private String data;
        private String extradata;

        private int position;
        public MyMenuItemClickListener(Context context, String data,String extradata) {
            this.context=context;
            this.data = data;
            this.extradata= extradata;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            AlertDialog.Builder dialog;
            switch (menuItem.getItemId()) {


                case R.id.opcion1: // ver pedido
                    Intent intent = new Intent(context,jesu.acondesa_servicio_al_cliente.VerPedidoActivity.class);
                    //poner variable en el Bundle del intent para ser usada en la otra activity
                    //Toast.makeText(context, "Enviado:"+data, Toast.LENGTH_SHORT).show();
                    intent.putExtra("datapedido", data);
                    //Toast.makeText(context,"rvadapterrojo:"+ data, Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    return true;
                        case R.id.opcion2: // abandonar pedido
                        //al abandonar un pedido se debe iniciar una nueva ventana donde se indique las razones por las cuales
                            dialog = new AlertDialog.Builder(context);
                            dialog.setTitle("Abandonar Pedido");
                            dialog.setMessage("¿Está seguro de abandonar este pedido?");
                            dialog.setCancelable(false);

                            dialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    // accion al presionar el boton OK haciendo switch a la variable action
                                    abandonarPedido(context,data);
                                }
                            });
                            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    // accion al presionar el boton cancelar

                                }
                            });

                            dialog.show();
                    return true;

                case R.id.opcion3: //anular pedido
                    dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Anular Pedido");
                    dialog.setMessage("¿Está seguro de anular este pedido?");
                    dialog.setCancelable(false);

                        dialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialoginterface, int i) {
                                // accion al presionar el boton OK haciendo switch a la variable action
                                anularPedido(context,data);
                            }
                        });

                        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialoginterface, int i) {
                                // accion al presionar el boton cancelar

                            }
                        });

                    dialog.show();

                    return true;
                default:
            }
            return false;
        }

    }

    private static void anularPedido(final Context context, String datapedido){
        try {
            JSONObject pedido = new JSONObject(datapedido);

            RequestQueue peticiones = Volley.newRequestQueue(context);
            String url = "http://movilwebacondesa.com/movilweb/app3/AnularPedido.php?idrutero="+
                    pedido.getString("idrutero")+"&idvendedor="+pedido.getString("idvendedor");
            final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
            // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
            // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
            final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
            // se muestra error, hay que manejar este evento

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject resp) {
                            try {
                                if(resp.getString("validacion").equals("OK")){
                                    Toast.makeText(context, "Pedido Anulado con Exito", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(context, "Pedido no se pudo anular", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Error al conectar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            peticion.setShouldCache(true);
            peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            peticiones.add(peticion);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void abandonarPedido(final Context context, String datapedido){
        try {
            JSONObject pedido = new JSONObject(datapedido);

            RequestQueue peticiones = Volley.newRequestQueue(context);
            String url = "http://movilwebacondesa.com/movilweb/app3/AbandonarPedido.php?idrutero="+
                    pedido.getString("idrutero")+"&idvendedor="+pedido.getString("idvendedor");
            final int MAX_TIMEOUT_CONECTION = 60000;//tiempo en milisegundos para el tiempo de espera
            // , si se supera este tiempo y no se recibe respuesta, se reintenta la peticion tantas veces como este configurada
            // hay que manejar este evento para permitir al usuario reintentar la conexion manualmente
            final int MAX_RETRYS_CONECTION = 3; //numero maximo de reintentos de conexion, despues de superar el numero de intentos,
            // se muestra error, hay que manejar este evento

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.POST,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject resp) {
                            try {
                                if(resp.getString("validacion").equals("OK")){
                                    Toast.makeText(context, "Pedido Anulado con Exito", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(context, "Pedido no se pudo anular", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Error al conectar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            peticion.setShouldCache(true);
            peticion.setRetryPolicy(new DefaultRetryPolicy(MAX_TIMEOUT_CONECTION,MAX_RETRYS_CONECTION,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            peticiones.add(peticion);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

