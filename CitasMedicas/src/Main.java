import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

// OJO: Este Main asume que ya existen estas clases en CitasMedicas/src:
// UsuarioRepo, DoctorRepo, PacienteRepo, CitaRepo, AuthService, CitaService,
// y las entidades Doctor, Paciente, Cita (más los enums EstadoCita y, si aplica, Rol).

public class Main {
    public static void main(String[] args) throws Exception {
        Path db = Path.of("db");

        // Repos
        UsuarioRepo usuarioRepo = new UsuarioRepo(db);
        DoctorRepo doctorRepo   = new DoctorRepo(db);
        PacienteRepo pacienteRepo = new PacienteRepo(db);
        CitaRepo citaRepo       = new CitaRepo(db);


        AuthService auth        = new AuthService(usuarioRepo);
        CitaService citaService = new CitaService(citaRepo, doctorRepo, pacienteRepo);

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("**Gestión de citas médicas**");
            while (true) {
                System.out.print("Usuario: ");
                String u = sc.nextLine().trim();
                System.out.print("Contraseña: ");
                String p = sc.nextLine().trim();
                Usuario admin = auth.loginAdmin(u, p);
                if (admin != null) break;
                System.out.println("Credenciales sin permisos. Intenta de nuevo.\n");
            }

            int op;
            do {
                System.out.println("""
                    
                    ** Menú **
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
                op = raw.isEmpty() ? -1 : Integer.parseInt(raw);

                try {
                    switch (op) {
                        case 1 -> {
                            System.out.print("Nombre: ");       String n = sc.nextLine();
                            System.out.print("Cédula: ");       String c = sc.nextLine();
                            System.out.print("Especialidad: "); String e = sc.nextLine();
                            Doctor d = new Doctor(0, n, c, e);
                            DoctorRepo.add(d);
                            System.out.println("Doctor creado: " + d);
                        }
                        case 2 -> {
                            System.out.print("Nombre: ");   String n = sc.nextLine();
                            System.out.print("Teléfono: "); String t = sc.nextLine();
                            System.out.print("Correo: ");   String m = sc.nextLine();
                            Paciente p = new Paciente(0, n, t, m);
                            pacienteRepo.add(p);
                            System.out.println("Paciente creado: " + p);
                        }
                        case 3 -> {
                            System.out.print("Doctor ID: ");   long dId = Long.parseLong(sc.nextLine());
                            System.out.print("Paciente ID: "); long pId = Long.parseLong(sc.nextLine());
                            System.out.print("Fecha (YYYY-MM-DD): "); LocalDate f = LocalDate.parse(sc.nextLine());
                            System.out.print("Hora (HH:MM): ");        LocalTime h = LocalTime.parse(sc.nextLine());
                            System.out.print("Notas: "); String notas = sc.nextLine();
                            Cita c = citaService.crearCita(dId, pId, LocalDateTime.of(f, h), notas);
                            System.out.println("Cita creada: " + c);
                        }
                        case 4 -> DoctorRepo.all().forEach(System.out::println);
                        case 5 -> pacienteRepo.all().forEach(System.out::println);
                        case 6 -> citaRepo.all().forEach(System.out::println);
                        case 7 -> {
                            System.out.print("ID de cita a cancelar: ");
                            long id = Long.parseLong(sc.nextLine());
                            citaService.cancelar(id);
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
}
