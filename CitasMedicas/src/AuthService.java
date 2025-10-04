public class AuthService {
    private final UsuarioRepo usuarios;

    public AuthService(UsuarioRepo usuarios) { this.usuarios = usuarios; }

    public Usuario loginAdmin(String username, String password) {
        Usuario u = usuarios.findByUsername(username);
        if (u == null) return null;
        if (!Password.hash(password).equals(u.getPasswordHash())) return null;
        return (u.getRol() == Rol.ADMIN) ? u : null;
    }
}
