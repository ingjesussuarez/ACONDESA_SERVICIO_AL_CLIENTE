package jesu.acondesa_servicio_al_cliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class cliente extends AppCompatActivity {
    public List<Person> persons;
    public RecyclerView rv;
    private RecyclerView.LayoutManager llm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        persons.add(new Person("Jesus Suarez", "Calle 115 17 10", R.mipmap.carrito_compras));
        persons.add(new Person("Jorbel Perez", "Cra 45 70 55", R.mipmap.carrito_compras));
        persons.add(new Person("Adelita Casalins", "Cra 50 74 208", R.mipmap.carrito_compras));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }

}
