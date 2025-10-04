package com.clinica.app;

import com.clinica.auth.AuthService;
import com.clinica.domain.Cita;
import com.clinica.domain.Doctor;
import com.clinica.domain.Paciente;
import com.clinica.domain.Usuario;
import com.clinica.repo.*;
import com.clinica.service.CitaService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Path db = Path.of("db");
        // Asegura archivos CSV con encabezado
        DbBootstrap.ensureAll(db);

        // Repos
        UsuarioRepo usuarioRepo = new UsuarioRepo(db);
        DoctorRepo doctorRepo = new DoctorRepo(db);
        PacienteRepo pacienteRepo = new PacienteRepo(db);
        CitaRepo citaRepo = new CitaRepo(db);

        // Servicios
        AuthService auth = new AuthService(usuarioRepo);
        CitaService citas = new CitaService(citaRepo, doctorRepo, pacienteRepo);

        // CLI
        try (Scanner sc = new Scanner(System.in)) {
            // Login solo admins
            Usuario actual;
            while (true) {
                System.out.print("Usuario: ");
                String u = sc.nextLine().trim();
                System.out.print("Contraseña: ");
                String p = sc.nextLine().trim();
                actual = auth.loginAdmin(u, p);
                if (actual != null) break;
                System.out.println("Credenciales inválidas o sin permisos.\n");
            }

            int op;
            do {
                System.out.println("""
                    \n=== Sistema de Citas (Admin) ===
                    1) Alta doctor
                    2) Alta paciente
                    3) Crear cita
                    4) Listar doctores
                    5) Listar pacientes
                    6) Listar citas
                    7) Cancelar cita
                    0) Salir
                    """);
                System.out.print("> ");
                String raw = sc.nextLine().trim();
                op = raw.isBlank() ? -1 : Integer.parseInt(raw);

                try {
                    switch (op) {
                        case 1 -> altaDoctor(sc, doctorRepo);
                        case 2 -> altaPaciente(sc, pacienteRepo);
                        case 3 -> crearCita(sc, citas);
                        case 4 -> doctorRepo.all().forEach(System.out::println);
                        case 5 -> pacienteRepo.all().forEach(System.out::println);
                        case 6 -> citaRepo.all().forEach(c -> {
                            String info = String.format("Cita{id=%d, doctorId=%d, pacienteId=%d, fechaHora=%s, estado=%s, notas=%s}",
                                    c.getId(), c.getDoctorId(), c.getPacienteId(), c.getFechaHora(), c.getEstado(), c.getNotas());
                            System.out.println(info);
                        });
                        case 7 -> {
                            System.out.print("ID de cita a cancelar: ");
                            long id = Long.parseLong(sc.nextLine());
                            citas.cancelar(id);
                            System.out.println("Cita cancelada.");
                        }
                        case 0 -> System.out.println("Hasta luego.");
                        default -> System.out.println("Opción inválida.");
                    }
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            } while (op != 0);
        }
    }

    static void altaDoctor(Scanner sc, DoctorRepo repo) throws Exception {
        System.out.print("Nombre: "); String n = sc.nextLine();
        System.out.print("Cédula: "); String c = sc.nextLine();
        System.out.print("Especialidad: "); String e = sc.nextLine();
        Doctor d = new Doctor(0, n, c, e);
        repo.add(d);
        System.out.println("Doctor creado: " + d);
    }

    static void altaPaciente(Scanner sc, PacienteRepo repo) throws Exception {
        System.out.print("Nombre: "); String n = sc.nextLine();
        System.out.print("Teléfono: "); String t = sc.nextLine();
        System.out.print("Correo: "); String m = sc.nextLine();
        Paciente p = new Paciente(0, n, t, m);
        repo.add(p);
        System.out.println("Paciente creado: " + p);
    }

    static void crearCita(Scanner sc, CitaService svc) throws Exception {
        System.out.print("Doctor ID: ");
        long dId = Long.parseLong(sc.nextLine());
        System.out.print("Paciente ID: ");
        long pId = Long.parseLong(sc.nextLine());
        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate f = LocalDate.parse(sc.nextLine());
        System.out.print("Hora (HH:MM): ");
        LocalTime h = LocalTime.parse(sc.nextLine());
        System.out.print("Notas: ");
        String notas = sc.nextLine();

        LocalDateTime fh = LocalDateTime.of(f, h);
        Cita c = svc.crearCita(dId, pId, fh, notas);
        System.out.println("Cita creada: " + c);
    }
}
