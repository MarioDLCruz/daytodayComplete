package mx.ita.daytoday.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mx.ita.daytoday.R;

//el viewholder nos permite realizar la conexion con las notas que han sido
// registradas en la base de dato y realizar acciones como con el método oncItemClick
//el cual nos sirve para poder ir a otra actividad al presionar en un
//elemento de la lista de notas. Por ejemplo ver el detalle de la nota
//el metodo onItemOnClick que nos sirve para mantener presionado por una breve
//de segundos un elemento de la lista de notas y poder realizar acciones
//como la eliminación o actualización de una nota
public class ViewHolder_Nota extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolder_Nota.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);/*SE EJECUTA AL PRESIONAR EN EL ITEM*/
        void onItemLongClick(View view, int position);/*SE EJECUTA AL MOMENTO DE MANTENER PRESIONADO 7S EL ITEM*/

    }

    public void setOnClickListener(ViewHolder_Nota.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_Nota(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getBindingAdapterPosition());
                return false;
            }
        });
    }

    //el metodo setearDatos declaramos las vistas de las que vamos a hacer uso, conexion con el
    //diseño
    public void SetearDatos(Context context, String idNota, String uid_usuario,
                            String correo_usuario, String fecha_hora_registro,
                            String titulo, String descripcion, String fecha_nota,
                            String estado ){


        //declarar las vistas
        TextView id_nota_item, uid_usuario_item, correo_usuario_item,
                fecha_hora_registro_item, titulo_item, descripcion_item, fecha_item, estado_item;

        ImageView Tarea_Finalizada_Item, Tarea_No_Finalizada_Item;

        //establecer la conexion con el item
        id_nota_item = mView.findViewById(R.id.id_nota_item);
        uid_usuario_item = mView.findViewById(R.id.uid_usuario_item);
        correo_usuario_item = mView.findViewById(R.id.correo_usuario_item);
        fecha_hora_registro_item = mView.findViewById(R.id.fecha_hora_registro_item);
        titulo_item = mView.findViewById(R.id.titulo_item);
        descripcion_item = mView.findViewById(R.id.descripcion_item);
        fecha_item = mView.findViewById(R.id.fecha_item);
        estado_item = mView.findViewById(R.id.Estado_Item);

        Tarea_Finalizada_Item = mView.findViewById(R.id.Tarea_Finalizada_Item);
        Tarea_No_Finalizada_Item = mView.findViewById(R.id.Tarea_No_Finalizada_Item);

        //setear la informacion dentro del item
        id_nota_item.setText(idNota);
        uid_usuario_item.setText(uid_usuario);
        correo_usuario_item.setText(correo_usuario);
        fecha_hora_registro_item.setText(fecha_hora_registro);
        titulo_item.setText(titulo);
        descripcion_item.setText(descripcion);
        fecha_item.setText(fecha_nota);
        estado_item.setText(estado);

        //GESTIONAMOS EL COLOR DEL ESTADO
        if (estado.equals("Finalizado")){
            Tarea_Finalizada_Item.setVisibility(View.VISIBLE);
        }else {
            Tarea_No_Finalizada_Item.setVisibility(View.VISIBLE);
        }
    }
}
