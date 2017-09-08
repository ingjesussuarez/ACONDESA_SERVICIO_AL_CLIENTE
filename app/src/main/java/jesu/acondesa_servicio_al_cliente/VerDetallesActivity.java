package jesu.acondesa_servicio_al_cliente;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import org.json.JSONException;
import org.json.JSONObject;

public class VerDetallesActivity extends AppCompatActivity {

    MapView mapview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Obtener datos desde Bundle
        Bundle bundle = getIntent().getExtras();
        String dataJson = bundle.getString("datacliente");
        JSONObject object = null;
        try {
            object = new JSONObject(dataJson);

            if(object != null){
                TextView codcliente = (TextView) findViewById(R.id.codigo_cliente);
                TextView nomcliente = (TextView) findViewById(R.id.nombre_cliente);
                TextView dircliente = (TextView) findViewById(R.id.direccion_cliente);
                TextView nomtercero = (TextView) findViewById(R.id.nombretercero_cliente);
                TextView zona = (TextView) findViewById(R.id.zona_cliente);
                TextView telefono = (TextView) findViewById(R.id.telefono_cliente);

                //los id's o indices aqui utilizados deben coincidir con los retornados desde PHP
                codcliente.setText(object.getString("idsucursal"));
                nomcliente.setText(object.getString("nombresucursal"));
                dircliente.setText(object.getString("direccion"));
                nomtercero.setText(object.getString("nombretercero"));
                zona.setText(object.getString("zonadesc"));
                telefono.setText(object.getString("telefono"));
                final String longitud = "232422424";//object.getString("longitud");
                final String latitud = "-353353434";//object.getString("latitud");
                Button ubicacion = (Button) findViewById(R.id.ubicacion_btn);

                ubicacion.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://www.google.com/maps/dir/"+longitud+","+latitud+"/"+longitud+","+latitud+",19z");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });


            }
        } catch (JSONException e) {
            //error al parsear JSON
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
