
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;




public class CitaRepo extends CsvRepo {
    private final Map<Long, Cita> data = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    public CitaRepo(Path dbDir) throws IOException {
        super(dbDir.resolve("citas.csv"), "id,doctorId,pacienteId,fechaHora,estado,notas");
        load();
    }

    private void load() throws IOException {
        for (String line : readAll()) {
            if (line.startsWith("id,")) continue;
            if (line.isBlank()) continue;
            String[] p = line.split(",", -1);
            long id = Long.parseLong(p[0]);
            long doctorId = Long.parseLong(p[1]);
            long pacienteId = Long.parseLong(p[2]);
            LocalDateTime fh = LocalDateTime.parse(p[3]);
            EstadoCita estado = EstadoCita.valueOf(p[4]);
            String notas = p.length >= 6 ? p[5] : "";
            Cita c = new Cita(id, doctorId, pacienteId, fh, estado, notas);
            data.put(id, c);
            seq.set(Math.max(seq.get(), id + 1));
        }
    }

    private void persist() throws IOException {
        List<String> out = new ArrayList<>();
        out.add(header);
        for (Cita c : data.values()) {
            out.add(String.join(",",
                String.valueOf(c.getId()),
                String.valueOf(c.getDoctorId()),
                String.valueOf(c.getPacienteId()),
                c.getFechaHora().toString(),
                c.getEstado().name(),
                esc(c.getNotas())
            ));
        }
        writeAll(out);
    }

    public synchronized Cita add(Cita c) throws IOException {
        c.setId(seq.getAndIncrement());
        data.put(c.getId(), c);
        persist();
        return c;
    }

    public synchronized void update(Cita c) throws IOException {
        data.put(c.getId(), c);
        persist();
    }

    public Collection<Cita> all() { return data.values(); }
    public Optional<Cita> findById(long id){ return Optional.ofNullable(data.get(id)); }
}

