package com.example.demo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/*
 * El pool de transacciones mantiene una coleccion de transacciones que estan pendientes de ser incluidas en un bloque y añadidas a la cadena
 */
public class PoolTransacciones {

	private Set<Transaccion> pool = new HashSet<>(); //SET no permite tener elementos repetidos, no tenia definido el tipo
	
	/**
     * Añadir una transaccion al pool
     * @param transaccion Transaccion a ser añadida
     * @return true si la transaccion es valida y es añadida al pool
     */
    public synchronized boolean add(Transaccion transaccion) {
        if (transaccion.esValida()) {
        	pool.add(transaccion);
            return true;
        }
        return false;
    }

    /**
     * Eliminar una transaccion del pool
     * @param transaccion Transaccion a eliminar
     */
    public void eliminar(Transaccion transaccion) {
    	pool.remove(transaccion);
    }

    /**
     * Comprobar si el pool contiene todas las transacciones de una lista de transacciones
     * @param transacciones Lista de transacciones a comprobar
     * @return true si todas las transacciones de la coleccion estan en el pool
     */
    public boolean contieneTransacciones(Collection <Transaccion> transacciones) { // no tenia definido el tipo
        return pool.containsAll(transacciones);
    }
    
    
    public Set<Transaccion> getPool() {
		return this.pool;
	}
    
    
} 