import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class DoctorRepo extends CsvRepo {
    private final Map<Long, Doctor> data = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    public DoctorRepo(Path dbDir) throws IOException {
        super(dbDir.resolve("doctores.csv"), "id,nombre,cedula,especialidad");
        load();
    }

    private void load() throws IOException {
        for (String line : readAll()) {
            if (line.startsWith("id,")) continue;
            if (line.isBlank()) continue;
            String[] p = line.split(",", -1);
            long id = Long.parseLong(p[0]);
            Doctor d = new Doctor(id, p[1], p[2], p[3]);
            data.put(id, d);
            seq.set(Math.max(seq.get(), id + 1));
        }
    }

    private void persist() throws IOException {
        List<String> out = new ArrayList<>();
        out.add(header);
        for (Doctor d : data.values()) {
            out.add(String.join(",",
                String.valueOf(d.getId()),
                esc(d.getNombre()),
                esc(d.getCedula()),
                esc(d.getEspecialidad())
            ));
        }
        writeAll(out);
    }

    public synchronized Doctor add(Doctor d) throws IOException {
        d.setId(seq.getAndIncrement());
        data.put(d.getId(), d);
        persist();
        return d;
    }

    public boolean exists(long id) { return data.containsKey(id); }
    public Doctor get(long id) { return data.get(id); }
    public Collection<Doctor> all(){ return data.values(); }
}
