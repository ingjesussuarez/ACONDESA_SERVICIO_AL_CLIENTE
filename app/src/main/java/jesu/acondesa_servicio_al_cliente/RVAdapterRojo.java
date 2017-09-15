package jesu.acondesa_servicio_al_cliente;

import android.content.Context;
import android.content.Intent;
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
            switch (menuItem.getItemId()) {

                case R.id.opcion1: // ver pedido
                    Intent intent = new Intent(context,jesu.acondesa_servicio_al_cliente.VerPedidoActivity.class);
                    //poner variable en el Bundle del intent para ser usada en la otra activity
                    //Toast.makeText(context, "Enviado:"+data, Toast.LENGTH_SHORT).show();
                    intent.putExtra("datapedido", data);
                    //Toast.makeText(context,"rvadapterrojo:"+ data, Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    return true;
                case R.id.opcion2:
                    /*
                    mDataSet.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,mDataSet.size());
                    Toast.makeText(MainActivity.context, "Done for now", Toast.LENGTH_SHORT).show();*/
                    return true;

                default:
            }
            return false;
        }

    }

}

