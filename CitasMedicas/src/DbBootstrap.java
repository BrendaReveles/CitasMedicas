import java.io.IOException;
import java.nio.file.Path;



public final class DbBootstrap {
    private DbBootstrap() {}
    public static void ensureAll(Path dbDir) throws IOException {
        new UsuarioRepo(dbDir).ensureFile();
        new DoctorRepo(dbDir).ensureFile();
        new PacienteRepo(dbDir).ensureFile();
        new CitaRepo(dbDir).ensureFile();
    }
}

