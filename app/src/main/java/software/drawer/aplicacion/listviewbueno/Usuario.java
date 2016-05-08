package software.drawer.aplicacion.listviewbueno;

public class Usuario
    {
        private int codigo;
        private String nombre;
        private String email;
        private String contraseña;

        public Usuario(){}
        public Usuario(int codigo, String nombre, String email, String contraseña){
            setCodigo(codigo);
            setNombre(nombre);
            setEmail(email);
            setContraseña(contraseña);
        }
        public int getCodigo() {
            return codigo;
        }
        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getContraseña() {
            return contraseña;
        }
        public void setContraseña(String contraseña) {
            this.contraseña = contraseña;
        }
    }

