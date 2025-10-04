package com.clinica.service;

import com.clinica.domain.*;
import com.clinica.repo.CitaRepo;
import com.clinica.repo.DoctorRepo;
import com.clinica.repo.PacienteRepo;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class CitaService {
    private final CitaRepo citas;
    private final DoctorRepo doctores;
    private final PacienteRepo pacientes;

    private final Duration DURACION = Duration.ofMinutes(30);

    public CitaService(CitaRepo c, DoctorRepo d, PacienteRepo p) {
        this.citas = c; this.doctores = d; this.pacientes = p;
    }

    public Cita crearCita(long doctorId, long pacienteId, LocalDateTime fechaHora, String notas) throws IOException {
        if (fechaHora.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("No se permiten citas en el pasado.");
        if (!doctores.exists(doctorId)) throw new IllegalArgumentException("El doctor no existe.");
        if (!pacientes.exists(pacienteId)) throw new IllegalArgumentException("El paciente no existe.");
        if (hayTraslape(doctorId, fechaHora)) throw new IllegalArgumentException("Conflicto de horario para el doctor.");

        Cita c = new Cita(0, doctorId, pacienteId, fechaHora, EstadoCita.PROGRAMADA, notas);
        return citas.add(c);
    }

    public boolean hayTraslape(long doctorId, LocalDateTime nueva) {
        for (Cita c : citas.all()) {
            if (c.getDoctorId() != doctorId) continue;
            if (c.getEstado() == EstadoCita.CANCELADA) continue;
            LocalDateTime ini = c.getFechaHora();
            LocalDateTime fin = ini.plus(DURACION);
            boolean solapa = !nueva.isBefore(ini) && nueva.isBefore(fin);
            if (solapa) return true;
        }
        return false;
    }

    public void cancelar(long citaId) throws IOException {
        Cita c = citas.findById(citaId).orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        c.setEstado(EstadoCita.CANCELADA);
        citas.update(c);
    }
}

