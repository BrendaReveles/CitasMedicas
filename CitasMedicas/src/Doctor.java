package com.clinica.domain;

public class Doctor {
    private long id;
    private String nombre;
    private String cedula;
    private String especialidad;

    public Doctor() {}

    public Doctor(long id, String nombre, String cedula, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.especialidad = especialidad;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override public String toString() {
        return "Doctor{id=" + id + ", nombre='" + nombre + "', cedula='" + cedula + "', esp='" + especialidad + "'}";
    }
}

package com.clinica.domain;