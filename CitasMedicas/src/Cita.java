import java.time.LocalDateTime;

public class Cita {
    private long id;
    private long doctorId;
    private long pacienteId;
    private LocalDateTime fechaHora;
    private EstadoCita estado;
    private String notas;

    public Cita() {}

    public Cita(long id, long doctorId, long pacienteId, LocalDateTime fechaHora, EstadoCita estado, String notas) {
        this.id = id; this.doctorId = doctorId; this.pacienteId = pacienteId;
        this.fechaHora = fechaHora; this.estado = estado; this.notas = notas;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getDoctorId() { return doctorId; }
    public void setDoctorId(long doctorId) { this.doctorId = doctorId; }
    public long getPacienteId() { return pacienteId; }
    public void setPacienteId(long pacienteId) { this.pacienteId = pacienteId; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    @Override public String toString() {
        return "Cita{id=" + id + ", doctorId=" + doctorId + ", pacienteId=" + pacienteId +
               ", fechaHora=" + fechaHora + ", estado=" + estado + ", notas='" + notas + "'}";
    }
}

