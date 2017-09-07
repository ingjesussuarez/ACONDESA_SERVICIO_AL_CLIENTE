package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by RAFAEL on 06/09/2017.
 */

class MyJavaScriptInterface {
    Context mContext;


    MyJavaScriptInterface(Context c) {
        mContext = c; // contexto
    }

    public MyJavaScriptInterface() {

    }



    @JavascriptInterface
    public void cargarDatosCliente() {
        //ESTE METODO ES OPCIONAL EN CASO DE QUE QUIERA CARGAR DESDE AQUI LOS DATOS DEL CLIENTE Y ENVIARLOS AL WEBVIEW

    }

    @JavascriptInterface
    public void cargarProductos() {
        //ESTE METODO ES OPCIONAL EN CASO DE QUE QUIERA CARGAR DESDE AQUI LOS PRODUCTOS Y ENVIARLOS AL WEBVIEW


    }

    @JavascriptInterface
    public void enviarPedido() {
        //ESTE METODO ES OPCIONAL EN CASO DE QUE QUIERA SUBIRSE DESDE AQUI EL FORMULARIO DE PEDIDO
        //se recibe desde el webview los datos a ser enviados y desde aqui debe hacerse una peticion http con volley
    }

    @JavascriptInterface
    public void showToast(String msg, int duracion) {
        //funcion que se invoca desde el webview y recibe un string de mensaje y un entero para la duracion en ms,
        // para mostrar un toast en la app
        Toast.makeText(mContext, msg, duracion).show();
    }


}
