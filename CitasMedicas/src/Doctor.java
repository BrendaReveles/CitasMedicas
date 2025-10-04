public class Doctor {
    private long id;
    private String nombre;
    private String cedula;
    private String especialidad;

    public Doctor(long id, String nombre, String cedula, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return String.format(
            "Doctor{id=%d, nombre='%s', cedula='%s', esp='%s'}",
            id, nombre, cedula, especialidad
        );
    }
}
