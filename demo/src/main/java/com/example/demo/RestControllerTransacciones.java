package com.example.demo;


import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController()
@RequestMapping("transaccion")
public class RestControllerTransacciones {

    private final ServiceTransacciones servicioTransacciones;
    private final ServiceNodo servicioNodo;
   // public static  Set<Transaccion> pool = new HashSet<>(); //prueba
    
    
    
    
    /*public static Set<Transaccion> getPool() { // prueba
    	return pool;
    }*/

    @Autowired
    public RestControllerTransacciones(ServiceTransacciones servicioTransacciones, ServiceNodo servicioNodo) {
        this.servicioTransacciones = servicioTransacciones;
        this.servicioNodo = servicioNodo;
    }

    /**
     * Obtener el pool de transacciones pendientes de ser incluidas en un bloque
     * @return JSON pool de transacciones
     */
    @RequestMapping(method = RequestMethod.GET)
    //@ResponseBody
    PoolTransacciones getPoolTransacciones() {
    	System.out.println("Request: getPoolTransacciones");
        return servicioTransacciones.getPoolTransacciones();
    }


    /**
     * Añadir una transaccion al pool
     * @param transaccion Transaccion a ser añadida
     * @param propagar si la transaccion debe ser propaga a otros nodos en la red
     * @param response codigo 202 si la transaccion es añadida al pool, 406 en otro caso
     */
    @RequestMapping(method = RequestMethod.POST)
    void añadirTransaccion(@RequestBody Transaccion transaccion, @RequestParam(required = false) Boolean propagar, HttpServletResponse response) {
    	
       /* System.out.println("Añadir transaccion con hash" + Base64.encodeBase64String(transaccion.getHash()));
        System.out.println("Añadir transaccion con info " + Base64.encodeBase64String(transaccion.getContenidoTransaccion()));
        System.out.println("Añadir transaccion emisor " + Base64.encodeBase64String(transaccion.getEmisor()));*/ // no daban bn los parametros
        
        boolean exito = servicioTransacciones.añadirTransaccion(transaccion);

        if (exito) {
            System.out.println("Transaccion validada y añadida.");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            /*RestTemplate restTemplate = new RestTemplate();
            pool.add(transaccion);
            restTemplate.getForObject("http://localhost:8080" + "/nodo/actualizar", String.class);*/
            
            
            if (propagar != null && propagar) {
            	System.out.println("Se propaga la transacción");
                servicioNodo.emitirPeticionPostNodosVecinos("transaccion", transaccion);
            }
        } else {
            System.out.println("Transaccion invalida y no añadida.");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

}
