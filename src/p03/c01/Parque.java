package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parque implements IParque{

	private int aforoMaximo;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque(int aforo) {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		aforoMaximo = aforo;
	}


	@Override
	public synchronized void entrarAlParque(String puerta){
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		comprobarAntesDeEntrar();
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		checkInvariante();
		
		notifyAll();
		
	}
	
	public synchronized void salirDelParque(String puerta) {
		
		comprobarAntesDeSalir();
		
		// Disminuimos el contador total y el individual
		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		checkInvariante();
		
		notifyAll();
		
	}
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
		Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresPuerta += iterPuertas.nextElement();
		}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parque.";
		assert contadorPersonasTotales >= 0: "INV: El aforo no puede ser negativo.";
		assert contadorPersonasTotales <= aforoMaximo: "INV: El aforo no puede ser superior al máximo.";
	}

	protected synchronized void comprobarAntesDeEntrar(){
		while (contadorPersonasTotales == aforoMaximo) {
			try {
				wait();
			} catch (InterruptedException ex) {
				Logger.getGlobal().log(Level.WARNING, "Entrada interrumpida, aforo limitado.");
				Logger.getGlobal().log(Level.WARNING, ex.toString());
			}
		}
	}

	protected synchronized void comprobarAntesDeSalir(){
		while (contadorPersonasTotales <= 0) {
			try {
				wait();
			} catch (InterruptedException ex) {
				Logger.getGlobal().log(Level.WARNING, "Salida interrumpida, se necesitan mÃ¡s personas. Remaining: " + contadorPersonasTotales);
				Logger.getGlobal().log(Level.WARNING, ex.toString());
			}
		}
	}


}
