package jesu.acondesa_servicio_al_cliente;

import android.app.Application;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
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
        ImageView personPhoto;
        ImageButton menuButton;
        private List personasList;
        private ItemClickListener clickListener;


        PersonViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.cv);
            view = itemView;
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personTel = (TextView)itemView.findViewById(R.id.person_tel);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            menuButton = new ImageButton(itemView.getContext());
            menuButton= (ImageButton) view.findViewById(R.id.imageButton);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view,2);
                }
                private void showPopupMenu(View view,int position) {
                    // inflate menu
                    PopupMenu popup = new PopupMenu(view.getContext(),view );
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_ruta, popup.getMenu());
                    popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
                    popup.show();
                }

            });
            view.setOnClickListener(new View.OnClickListener() {
               @Override public void onClick(View v) {
                    //item clicked
                   //AQUI RAFA ARROJAS UNA ACTIVIDAD
                   //Intent cliente= new Intent(context, jesu.acondesa_servicio_al_cliente.cliente.class);
                   //context.startActivity(cliente);
                    //AQUI RAFA ARROJAS UN TOAST dependiendo el item que toques
                    Toast.makeText(context, personName.getText(), Toast.LENGTH_SHORT).show();
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
        personViewHolder.personPhoto.setImageResource(R.mipmap.carrito_compras);
        //personViewHolder.itemView = persons.get(i).photoId);


    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
    static class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int position) {
            //this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.opcion1:
                    // mDataSet.remove(position);
                    //notifyItemRemoved(position);
                    // notifyItemRangeChanged(position,mDataSet.size());
                    /*
                    mySharedPreferences.saveStringPrefs(Constants.REMOVE_CTAGURY,RemoveCategory,MainActivity.context);
                    Toast.makeText(MainActivity.context, "Add to favourite", Toast.LENGTH_SHORT).show();*/
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

