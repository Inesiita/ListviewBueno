package software.drawer.aplicacion.listviewbueno;

/**
 * Created by Iness on 14/04/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends Activity
{
    private UsuariosSQLiteHelper usdbh;
    private SQLiteDatabase db;

    private ListView lstLista;
    private Usuario[] datosLista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Referenciamos los Controles
        lstLista = (ListView)findViewById(R.id.LstLista);

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 4);

        //Asociamos el menu contextual a la lista
        registerForContextMenu(lstLista);

        cargarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflar Menú Principal
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ppal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.MnuNuevo:
                Intent intent = new Intent(this,frmNuevoRegistro.class);
                startActivity(intent);
                return true;

            case R.id.MnuSalir:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

        menu.setHeaderTitle(((Usuario)lstLista.getAdapter().getItem(info.position)).getNombre());

        inflater.inflate(R.menu.menu_ctx_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.CtxLstEditar:
                Intent intent = new Intent(this,frmNuevoRegistro.class);
                Bundle bundle = new Bundle();
                bundle.putInt("codigo", datosLista[info.position].getCodigo());
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            case R.id.CtxLstEliminar:
                db = usdbh.getReadableDatabase();
                if (db != null){
                    db.execSQL("DELETE FROM Usuarios WHERE codigo = " + info.position + "");
                    db.close();
                }
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void cargarLista()
    {
        db = usdbh.getReadableDatabase();
        if (db != null)
        {
            Cursor c = db.rawQuery("SELECT codigo,nombre,email,contraseña FROM Usuarios ORDER BY codigo ASC", null);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()){
                //Recorremos el cursor hasta que no haya más registros
                datosLista = new Usuario[c.getCount()];
                int i = 0;
                do {
                    datosLista[i] = new Usuario();
                    datosLista[i].setCodigo(c.getInt(0));
                    datosLista[i].setNombre(c.getString(1));
                    datosLista[i].setEmail(c.getString(2));
                    datosLista[i].setContraseña(c.getString(3));
                    i++;
                } while (c.moveToNext());
                AdaptadorUsuarios adaptador = new AdaptadorUsuarios(this);
                lstLista.setAdapter(adaptador);
            }
        }
    }

    @SuppressWarnings("unchecked")
    class AdaptadorUsuarios extends ArrayAdapter
    {
        Activity context;
        AdaptadorUsuarios(Activity context)
        {
            super(context, R.layout.listitem_usuario, datosLista);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            View item = convertView;
            ViewHolder holder;
            if (item == null)
            {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_usuario, null);

                holder = new ViewHolder();
                holder.codigo = (TextView)item.findViewById(R.id.LblCodigo);
                holder.nombre = (TextView)item.findViewById(R.id.LblNombre);
                holder.email = (TextView)item.findViewById(R.id.LblEmail);
                holder.contraseña = (TextView)item.findViewById(R.id.LblContraseña);

                item.setTag(holder);
            }else{
                holder = (ViewHolder)item.getTag();
            }
            holder.codigo.setText(datosLista[position].getCodigo() + "");
            holder.nombre.setText(datosLista[position].getNombre());
            holder.email.setText(datosLista[position].getEmail());
//            holder.contraseña.setText(datosLista[position].getContraseña());
            return(item);
        }
    }
    static class ViewHolder {
        TextView codigo;
        TextView nombre;
        TextView email;
        TextView contraseña;

    }
}