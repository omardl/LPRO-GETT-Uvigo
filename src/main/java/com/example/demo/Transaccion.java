package com.example.demo;



import com.google.common.primitives.Longs;  // esta en guava
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.util.Date;
import java.lang.Object;

/*
 * La informacion principal en una transaccion incluye:
 * 	- Hash de la transaccion
 * 	- El emisor
 * 	- El destinatario
 * 	- La cantidad a ser transferida
 * 	- El timestamp de cuando fue creada
 * 	- La firma con la clave privada del emisor
 * */

public class Transaccion {

    // Hash de la transaccion e identificador inico de esta
    private byte[] hash;

    // Clave publica del emisor de la transaccion
    private byte[] emisor;

    // Clave publica del destinatario de la transacci�n
    private byte[] destinatario;

    // Valor a ser transferido
    private double cantidad;  //ELIMINAR???

    // Firma con la clave privada para verificar que la transaccion fue realmente enviada por el emisor
    private byte[] firma;

    // Timestamp de la creacion de la transaccion en milisegundos desde el 1/1/1970
    private long timestamp;

    // Constructores
    public Transaccion() {
    }

    public Transaccion(byte[] emisor, byte[] receptor, double cantidad, byte[] firma) {
        this.emisor = emisor;
        this.destinatario = receptor;
        this.cantidad = cantidad;
        this.firma = firma;
        this.timestamp = System.currentTimeMillis();
        this.hash = calcularHashTransaccion();
    }

    // Getters y setters
    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getEmisor() {
        return emisor;
    }

    public void setEmisor(byte[] emisor) {
        this.emisor = emisor;
    }

    public byte[] getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(byte[] destinatario) {
        this.destinatario = destinatario;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firma) {
        this.firma = firma;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * El contenido de la transaccion que es firmado por el emisor con su clave privada
     * @return byte[] Array de bytes representando el contenido de la transaccion
     */
    public byte[] getContenidoTransaccion() {
    	byte[] contenido = ArrayUtils.addAll(String.valueOf(cantidad).getBytes());
    	contenido = ArrayUtils.addAll(contenido, emisor);
    	contenido = ArrayUtils.addAll(contenido, destinatario);
    	contenido = ArrayUtils.addAll(contenido, firma);
    	contenido = ArrayUtils.addAll(contenido, Longs.toByteArray(timestamp));        
        return contenido;
    }
    
    
    /**
     * El contenido de la transaccion que es firmado por el emisor con su clave privada
     * @return byte[] Array de bytes representando el contenido de la transaccion
     */
    public byte[] getContenidoFirma() {
    	byte[] contenido = ArrayUtils.addAll(String.valueOf(cantidad).getBytes());
    	contenido = ArrayUtils.addAll(contenido, emisor);
    	contenido = ArrayUtils.addAll(contenido, destinatario);
    	contenido = ArrayUtils.addAll(contenido, Longs.toByteArray(timestamp));        
        return contenido;
    }
    
    

    /**
     * Calcular el hash del contenido de la transaccion y que pasa a ser el identificador de la transaccion
     * @return Hash SHA256
     */
    public byte[] calcularHashTransaccion() {
        return DigestUtils.sha256(getContenidoTransaccion());
    }

    /**
     * Comprobar si una transaccion es valida
     * @return true si tiene un hash valido y la firma es valida
     */
    public boolean esValida() {
        // verificar hash
        if (!Arrays.equals(getHash(), calcularHashTransaccion())) {
        	System.out.println("Transacción no valida, hash distinto");
            return false;
        }
        
        // verificar firma
        try {
            if (UtilidadesFirma.validarFirma(getContenidoFirma(), getFirma(), emisor)==false) {
            	System.out.println("Transacción no valida, firma inválida");
                return false;
            }
        } catch (Exception e) {
        	System.out.println("Transacción no valida, error en try validar firma");
            return false;
        }

        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaccion tr = (Transaccion) o;

        return Arrays.equals(hash, tr.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash); // nos pasa en array del hash a un codigo legible
    }

    @Override
    public String toString() {
        return "{" + hash + ", " + emisor + ", " + destinatario + ", " + cantidad + ", " + firma + ", " + new Date(timestamp) + "}";
    }
}
