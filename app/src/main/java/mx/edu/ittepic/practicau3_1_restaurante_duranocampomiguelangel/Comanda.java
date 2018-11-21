package mx.edu.ittepic.practicau3_1_restaurante_duranocampomiguelangel;



public class Comanda {
    private String id, fecha,estatus;
    private String platillos;
    private String bebidas;
    private float total;
    private int nomesa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getPlatillos() {
        return platillos;
    }

    public void setPlatillos(String platillos) {
        this.platillos = platillos;
    }

    public String getBebidas() {
        return bebidas;
    }

    public void setBebidas(String bebidas) {
        this.bebidas = bebidas;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getNomesa() {
        return nomesa;
    }

    public void setNomesa(int nomesa) {
        this.nomesa = nomesa;
    }
}
