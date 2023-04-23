package com.example.demo;



import java.net.URL;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ServiceTransacciones {

	// Pool de transacciones con transacciones pendientes de ser incluidas en un bloque
	private PoolTransacciones poolTransacciones = new PoolTransacciones();

	@Autowired
	public ServiceTransacciones() {
	}

	public PoolTransacciones getPoolTransacciones() {
		return poolTransacciones;
	}

	/**
	 * Añadir transaccion al pool
	 * 
	 * @param transaccion Transaccion a ser añadida
	 * @return true si la transaccion es valida y es añadida al pool
	 */
	public synchronized boolean añadirTransaccion(Transaccion transaccion) {        
		return poolTransacciones.add(transaccion); // antes ponia añadir transaccion no add 
	}

	/**
	 * Eliminar una transaccion del pool
	 * 
	 * @param transaccion Transaccion a ser eliminada
	 */
	public void eliminarTransaccion(Transaccion transaccion) {
		poolTransacciones.eliminar(transaccion); 
		
	}

	/**
	 * Comprobar si el pool contiene una lista de transacciones
	 * 
	 * @param transacciones Transacciones a ser verificadas
	 * @return true si todas las transacciones estan en el pool
	 */
	public boolean contieneTransacciones(Collection<Transaccion> transacciones) { //definido tipo
		return poolTransacciones.contieneTransacciones(transacciones);
	}
	
	/**
	 * Descargar pool de transacciones desde otro nodo
	 * 
	 * @param urlNodo Nodo al que pedir las transacciones
	 * @param restTemplate RestTemplate a usar
	 */
	public void obtenerPoolTransacciones(URL urlNodo, RestTemplate restTemplate) {
		PoolTransacciones poolTransacciones = restTemplate.getForObject(urlNodo + "/transaccion", PoolTransacciones.class);
		this.poolTransacciones = poolTransacciones;
		System.out.println("Obtenido pool de transacciones de nodo " + urlNodo + ".\n");
	}
}
