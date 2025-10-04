package com.clinica.repo;

import com.clinica.domain.Rol;
import com.clinica.domain.Usuario;
import com.clinica.util.Password;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UsuarioRepo extends CsvRepo {
    private final Map<Long, Usuario> data = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    public UsuarioRepo(Path dbDir) throws IOException {
        super(dbDir.resolve("usuarios.csv"), "id,nombre,username,passwordHash,rol");
        load();
        bootstrapAdminIfEmpty();
    }

    private void load() throws IOException {
        for (String line : readAll()) {
            if (line.startsWith("id,")) continue;
            if (line.isBlank()) continue;
            String[] p = line.split(",", -1);
            long id = Long.parseLong(p[0]);
            Usuario u = new Usuario(id, p[1], p[2], p[3], Rol.valueOf(p[4]));
            data.put(id, u);
            seq.set(Math.max(seq.get(), id + 1));
        }
    }

    private void persist() throws IOException {
        List<String> out = new ArrayList<>();
        out.add(header);
        for (Usuario u : data.values()) {
            out.add(String.join(",",
                String.valueOf(u.getId()),
                esc(u.getNombre()),
                esc(u.getUsername()),
                u.getPasswordHash(),
                u.getRol().name()
            ));
        }
        writeAll(out);
    }

    public synchronized Usuario add(Usuario u) throws IOException {
        u.setId(seq.getAndIncrement());
        data.put(u.getId(), u);
        persist();
        return u;
    }

    public Usuario findByUsername(String username) {
        for (Usuario u : data.values()) if (u.getUsername().equals(username)) return u;
        return null;
    }

    private void bootstrapAdminIfEmpty() throws IOException {
        boolean hasAdmin = data.values().stream().anyMatch(u -> u.getRol() == Rol.ADMIN);
        if (!hasAdmin) {
            Usuario admin = new Usuario(0, "Administrador", "admin", Password.hash("admin123"), Rol.ADMIN);
            add(admin);
        }
    }
}
