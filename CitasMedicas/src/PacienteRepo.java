import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;



public class PacienteRepo extends CsvRepo {
    private final Map<Long, Paciente> data = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    public PacienteRepo(Path dbDir) throws IOException {
        super(dbDir.resolve("pacientes.csv"), "id,nombre,telefono,correo");
        load();
    }

    private void load() throws IOException {
        for (String line : readAll()) {
            if (line.startsWith("id,")) continue;
            if (line.isBlank()) continue;
            String[] p = line.split(",", -1);
            long id = Long.parseLong(p[0]);
            Paciente pa = new Paciente(id, p[1], p[2], p[3]);
            data.put(id, pa);
            seq.set(Math.max(seq.get(), id + 1));
        }
    }

    private void persist() throws IOException {
        List<String> out = new ArrayList<>();
        out.add(header);
        for (Paciente p : data.values()) {
            out.add(String.join(",",
                String.valueOf(p.getId()),
                esc(p.getNombre()),
                esc(p.getTelefono()),
                esc(p.getCorreo())
            ));
        }
        writeAll(out);
    }

    public synchronized Paciente add(Paciente p) throws IOException {
        p.setId(seq.getAndIncrement());
        data.put(p.getId(), p);
        persist();
        return p;
    }

    public boolean exists(long id) { return data.containsKey(id); }
    public Paciente get(long id) { return data.get(id); }
    public Collection<Paciente> all(){ return data.values(); }
}
