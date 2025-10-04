package com.clinica.domain;

public class Paciente {
    private long id;
    private String nombre;
    private String telefono;
    private String correo;

    public Paciente() {}
    public Paciente(long id, String nombre, String telefono, String correo) {
        this.id = id; this.nombre = nombre; this.telefono = telefono; this.correo = correo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @Override public String toString() {
        return "Paciente{id=" + id + ", nombre='" + nombre + "', tel='" + telefono + "', correo='" + correo + "'}";
    }
}
