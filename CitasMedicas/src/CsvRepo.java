package com.clinica.repo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvRepo {
    protected final Path path;
    protected final String header;

    protected CsvRepo(Path path, String header) {
        this.path = path;
        this.header = header;
    }

    public void ensureFile() throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.write(path, (header + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            return;
        }
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.isEmpty() || !lines.get(0).equals(header)) {
            List<String> out = new ArrayList<>();
            out.add(header);
            Files.write(path, out, StandardCharsets.UTF_8);
        }
    }

    protected List<String> readAll() throws IOException {
        ensureFile();
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    protected void writeAll(List<String> lines) throws IOException {
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    protected static String esc(String s) { return (s == null ? "" : s.replace(",", " ")); }
}
