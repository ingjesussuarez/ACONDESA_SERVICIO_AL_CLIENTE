package jesu.acondesa_servicio_al_cliente;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import org.json.JSONException;
import org.json.JSONObject;

public class VerDetallesActivity extends AppCompatActivity {

    MapView mapview;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtener datos desde Bundle
        Bundle bundle = getIntent().getExtras();
        String dataJson = bundle.getString("datacliente");
        JSONObject object = null;
        try {
            object = new JSONObject(dataJson);

            if (object != null) {
                TextView codcliente = (TextView) findViewById(R.id.codigo_cliente);
                TextView nomcliente = (TextView) findViewById(R.id.nombre_cliente);
                TextView dircliente = (TextView) findViewById(R.id.direccion_cliente);
                TextView nomtercero = (TextView) findViewById(R.id.nombretercero_cliente);
                TextView zona = (TextView) findViewById(R.id.zona_cliente);
                final TextView telefono = (TextView) findViewById(R.id.telefono_cliente);

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
                Button sms = (Button) findViewById(R.id.sms_btn);
                Button call = (Button) findViewById(R.id.llamar_btn);

                ubicacion.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://www.google.com/maps/dir/" + longitud + "," + latitud + "/" + longitud + "," + latitud + ",19z");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                sms.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        sendSMS2(telefono.getText().toString());
                    }
                });
                call.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    call(telefono.getText().toString());
                    }
                });
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendSMS(telefono.getText().toString());
                    }
                });

            }
        } catch (JSONException e) {
            //error al parsear JSON
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendSMS(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.ACTION_SENDTO, phoneNumber);

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phoneNumber);
            //smsIntent.putExtra("sms_body","message");
            startActivity(smsIntent);
        }
    }

    private void sendSMS2(String phoneNumber) {
        Uri SMS_URI = Uri.parse("smsto:" + phoneNumber); //Replace the phone number
        Intent sms = new Intent(Intent.ACTION_VIEW, SMS_URI);
        //sms.putExtra("sms_body","This is test message"); //Replace the message witha a vairable
        startActivity(sms);
    }

    private void call(String phoneNumber) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                dialogo("IMPORTANTE","Para que esta aplicacion pueda hacer llamadas, debe concederle permisos manualmente." +
                        " Para ello, dirijase a configuracion de la aplicacion/permisos y active el permiso para llamadas","Ok",MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);

                // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    startActivity(callIntent);

                }


                return;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisos de llamada concedidos! Ya puede hacer llamadas desde la aplicacion", Toast.LENGTH_LONG).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Permisos de llamada denegados! No podrá hacer llamadas desde la aplicacion", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void dialogo(String title, String msg,String pbtn,int action){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(pbtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

}
