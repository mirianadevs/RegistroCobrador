package unison.registrocobrador;
public class Corte {
    private String id;
    private String idCliente;
    private String idCobrador;
    private float montoPagado;
    private java.sql.Date fecha;

    // Constructor
    public Corte(String id, String idCliente, String idCobrador, float montoPagado, java.sql.Date fecha) {
        this.id = id;
        this.idCliente = idCliente;
        this.idCobrador = idCobrador;
        this.montoPagado = montoPagado;
        this.fecha = fecha;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdCobrador() {
        return idCobrador;
    }

    public void setIdCobrador(String idCobrador) {
        this.idCobrador = idCobrador;
    }

    public float getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(float montoPagado) {
        this.montoPagado = montoPagado;
    }

    public java.sql.Date getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }
}
