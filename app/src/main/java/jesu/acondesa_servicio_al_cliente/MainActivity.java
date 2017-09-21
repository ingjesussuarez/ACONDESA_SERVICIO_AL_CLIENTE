package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
    SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas

    String usuario = "";
    String password = "";

    boolean firstTimeSplash = false;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", "none");
        password = sharedPreferences.getString("password", "none");
        firstTimeSplash = sharedPreferences.getBoolean("firstTimeSplash", false);

        //al resumir la aplicacion, obtener los datos de login guardados en sharedpreferences,
        // para mantener la ultima sesion y asi el ultimo estado de la app

        if (password.equals("none") && usuario.equals("none")) {
            //enviar al login

            finish();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        //al detenerse la app, guardar los datos de login en sharedpreferences
        Context context = MainActivity.this;
        sharedPreferences = context.getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstTimeSplash",firstTimeSplash);
        editor.putString("usuario", usuario);
        editor.putString("password", password);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.drawable.acondesaazul);
        Context context = MainActivity.this;
        sharedPreferences = context.getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        usuario = sharedPreferences.getString("usuario", "none");
        password = sharedPreferences.getString("password", "none");
        firstTimeSplash = sharedPreferences.getBoolean("firstTimeSplash", false);

        if (password.equals("none") && usuario.equals("none")) {
            //enviar al login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);
            MainActivity.this.finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        moveTaskToBack(true);
    }

    private void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Salir");
        dialog.setMessage("¿Está seguro de cerrar esta sesión?");
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sharedpreferences = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                MainActivity.this.finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
            }
        });
        dialog.setCancelable(false);
        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_grupo) {
            Intent MainActivity3 = new Intent(getApplicationContext(), jesu.acondesa_servicio_al_cliente.cliente.class);
            startActivity(MainActivity3);
            return true;
        }
        if (id == R.id.action_apagar) {
            this.logout();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        //una variable compartida para saber si se hizo algun registro en pedidos y actualizar la lista de pedidos
        private boolean hayPedidosPendientes = false;

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        resumen re = null;
        pedidos pe = null;
        ruta ru = null;
        String hayPedidosPendientes = "no";
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.re = null;
            this.ru = null;
            this.pe = null;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            sharedPreferences = getApplicationContext().getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
            hayPedidosPendientes = sharedPreferences.getString("hayPedidosPendientes", "no");
            switch (position) {
                case 0: //if(ru == null)
                            ru = new ruta();
                        return  ru;
                case 1: //if(pe == null)
                            pe = new pedidos();
                        //if(hayPedidosPendientes == "si")
                          //  pe.llenarpedidos();
                        return pe;
                case 2: //if(re == null)
                            re = new resumen();
                        return re;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ruta";

                case 1:
                    return "pedidos";
                case 2:
                    return "resumen";
            }
            return null;
        }


    }


}
