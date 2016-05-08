package software.drawer.aplicacion.listviewbueno;

/**
 * Created by Iness on 14/04/2016.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class frmNuevoRegistro extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtEmail;
    private EditText txtContraseña;
    private EditText txtConfirContraseña;
    private TextView lblMensaje;
    private Button btnAceptar;

    private UsuariosSQLiteHelper usdbh;
    private SQLiteDatabase db;

    private boolean editando;
    private int usuarioSeleccionado;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nuevo_registro);

        inicializar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("codigo")) {
            editando = true;
            cargarDatos(bundle.getInt("codigo"));
        }
    }

    private void inicializar(){
        //Obtenemos las referencias de los controles
        txtNombre = (EditText)findViewById(R.id.TxtNombre);
        txtEmail = (EditText)findViewById(R.id.TxtEmail);
        txtContraseña = (EditText)findViewById(R.id.TxtContraseña);
        txtConfirContraseña = (EditText)findViewById(R.id.TxtConfirContraseña);
        lblMensaje = (TextView)findViewById(R.id.LblMensaje);
        btnAceptar = (Button)findViewById(R.id.BtnAceptar);

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 4);

        //Asignar los eventos necesarios
        asignarEventos();
    }

    private void asignarEventos(){
        btnAceptar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lblMensaje.setText("");
                if (txtNombre.getText().toString().equals("")){
                    lblMensaje.setTextColor(Color.RED);
                    lblMensaje.setText("El Nombre no puede estar vacio");
                }else if (txtEmail.getText().toString().equals("")){
                    lblMensaje.setTextColor(Color.RED);
                    lblMensaje.setText("El Email no puede estar vacio");
                }else if (txtContraseña.getText().toString().equals("")){
                    lblMensaje.setTextColor(Color.RED);
                    lblMensaje.setText("La contraseña no puede estar vacio");
                }else if (txtConfirContraseña.getText().toString().equals("")){
                    lblMensaje.setTextColor(Color.RED);
                    lblMensaje.setText("La contraseña no puede estar vacio");
                }else {

                        lblMensaje.setTextColor(Color.GREEN);
                        lblMensaje.setText("Guardando...");
                    }
                    if (editando) {
                        actualizarRegistro();
                    } else {
                        if (txtContraseña.getText().toString().equals(txtConfirContraseña.getText().toString())) {
                            guardarRegistro();
                        } else {
                            lblMensaje.setTextColor(Color.RED);
                            lblMensaje.setText("Las contraseñas son distintas");
                        }

                }
            }
        });
    }

    private void guardarRegistro(){
        //Conectamos con la base de datos para escribir
        db = usdbh.getWritableDatabase();
        if (db != null){
            db.execSQL("INSERT INTO Usuarios (nombre, email, contraseña) VALUES ('"
                    + txtNombre.getText().toString() + "','"
                    + txtEmail.getText().toString() + "','"
                    + txtContraseña.getText().toString()
                    + "')");
            //Cerramos la base de datos
            db.close();
            lblMensaje.setTextColor(Color.GREEN);
            lblMensaje.setText("Usuario Guardado !");
            startActivity(new Intent(frmNuevoRegistro.this, Main.class));
        }
    }

    private void actualizarRegistro(){
        //Conectamos con la base de datos para escribir
        db = usdbh.getWritableDatabase();
        if (db != null){
            db.execSQL("UPDATE Usuarios SET nombre = '" + txtNombre.getText().toString() + "', "
                    + "email = '" + txtEmail.getText().toString() + "contraseña = '" + txtContraseña.getText().toString() + "' WHERE codigo = "+ usuarioSeleccionado);
            //Cerramos la base de datos
            db.close();
            lblMensaje.setTextColor(Color.GREEN);
            lblMensaje.setText("Usuario Actualizado !");

            finish();
        }
    }

    private void cargarDatos(int codigo){
        db = usdbh.getWritableDatabase();
        if (db != null){
            String[] args = new String[] {codigo+""};
            Cursor cursor = db.rawQuery("SELECT nombre, email, contraseña FROM Usuarios WHERE codigo = ?",args);

            if (cursor.moveToFirst()){
                txtNombre.setText(cursor.getString(0));
                txtEmail.setText(cursor.getString(1));
                txtContraseña.setText(cursor.getString(2));
                usuarioSeleccionado = codigo;
            }else{
                lblMensaje.setTextColor(Color.RED);
                lblMensaje.setText("Error: No se encuentra el registro " + codigo);
            }

            db.close();
        }
    }
}