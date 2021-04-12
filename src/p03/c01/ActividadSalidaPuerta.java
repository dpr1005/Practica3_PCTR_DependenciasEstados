package src.p03.c01;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ActividadSalidaPuerta implements Runnable{

    // Declaración de variables
    private static final int NUMSALIDAS = 20;
    private String puerta;
    private IParque parque;

    public ActividadSalidaPuerta(String puerta, IParque parque) {
        this.puerta = puerta;
        this.parque = parque;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for(int i = 0; i < NUMSALIDAS; i++) {
            try{
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5)*1000);
                parque.salirDelParque(puerta);
            } catch (InterruptedException ex) {
                Logger.getGlobal().log(Level.INFO, "Salida Interrumpida.");
                Logger.getGlobal().log(Level.INFO, ex.toString());
                return;
            }
        }
    }

}
