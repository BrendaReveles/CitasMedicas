public class Usuario {
    private long id;
    private String nombre;
    private String username;
    private String passwordHash;
    private Rol rol;

    public Usuario() {}
    public Usuario(long id, String nombre, String username, String passwordHash, Rol rol) {
        this.id = id; this.nombre = nombre; this.username = username; this.passwordHash = passwordHash; this.rol = rol;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}

