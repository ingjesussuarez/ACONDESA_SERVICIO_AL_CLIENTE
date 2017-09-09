package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
   // private static Context context;
    //public RVAdapter(Context context) {
    //    this.context = context;
    //}
    Context context;



    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public MenuView.ItemView currentItem;
        Context context;
        CardView cv;
        TextView personName;
        TextView personAge;
        TextView personTel;
        TextView personId;
        TextView data;
        TextView extradata;
        ImageView personPhoto;
        ImageButton menuButton;
        public static final String MIS_PREFERENCIAS = "myPref"; // constante usada para guardar sesiones y/o variables compartidas
        SharedPreferences sharedPreferences; //contenedor de sesiones y/o variables compartidas
        private ItemClickListener clickListener;


        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = itemView.findViewById(R.id.cv);
            view = itemView;
            personName = itemView.findViewById(R.id.person_name);
            personAge = itemView.findViewById(R.id.person_age);
            personTel = itemView.findViewById(R.id.person_tel);
            personPhoto = itemView.findViewById(R.id.person_photo);
            personId = itemView.findViewById(R.id.person_id);
            data = itemView.findViewById(R.id.person_data);
            extradata = itemView.findViewById(R.id.extra_data);
            menuButton = new ImageButton(itemView.getContext());
            menuButton= view.findViewById(R.id.imageButton);


            final String finalDatavendedor = extradata.getText().toString();

            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view,data.getText().toString(), finalDatavendedor);
                }
                private void showPopupMenu(View view,String datacliente,String datavendedor) {
                    // inflate menu
                    PopupMenu popup = new PopupMenu(view.getContext(),view );
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_ruta, popup.getMenu());
                    popup.setOnMenuItemClickListener(new MyMenuItemClickListener(view.getContext(),datacliente,datavendedor));
                    popup.show();
                }

            });
            view.setOnClickListener(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    menuButton.callOnClick();
                }
            });

        }

        void setOnClickListeners(){
            personName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }


    List<Person> persons;

    RVAdapter(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).nombre);
        personViewHolder.personAge.setText(persons.get(i).direccion);
        personViewHolder.personTel.setText(persons.get(i).telefono);
        personViewHolder.personId.setText(persons.get(i).id_person);
        personViewHolder.data.setText(persons.get(i).data);
        personViewHolder.extradata.setText(persons.get(i).extradata);
        personViewHolder.personPhoto.setImageResource(R.mipmap.carrito_compras);
        int color = Color.rgb(255,255,255);
        if(persons.get(i).hizopedido)
            color = Color.rgb(238,254,205);
        else if(!persons.get(i).hizopedido && persons.get(i).visitado){
            color = Color.rgb(238,254,205);
        }

        personViewHolder.cv.setCardBackgroundColor(color);
        //personViewHolder.itemView = persons.get(i).photoId);


    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
    static class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private Context context;
        private String datacliente;
        private String datavendedor;

        public MyMenuItemClickListener(Context context,String datacliente,String datavendedor) {
            this.datacliente = datacliente;
            this.datavendedor = datavendedor;
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Intent intent;
            switch (menuItem.getItemId()) {

                case R.id.opcion1://ver cliente
                    intent = new Intent(context, jesu.acondesa_servicio_al_cliente.VerDetallesActivity.class);
                    //poner variable en el Bundle del intent para ser usada en la otra activity
                    intent.putExtra("datacliente",datacliente);
                    context.startActivity(intent);
                    return true;
                case R.id.opcion2:
                    intent = new Intent(context, jesu.acondesa_servicio_al_cliente.RegistrarPedido.class);
                    //poner variable en el Bundle del intent para ser usada en la otra activity
                    intent.putExtra("datacliente",datacliente);
                    //poner variable en el Bundle del intent para ser usada en la otra activity
                    intent.putExtra("datavendedor",datavendedor);

                    context.startActivity(intent);
                    return true;

                default:
            }
            return false;
        }

    }



}

