/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahorcadoBC;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class HiloServidorAhorcado extends Thread implements Constantes {

    private final int id;
    private final Socket jugador;
    private final DataOutputStream dataOut;
    private DataInputStream dataIn;
    private final Reglas reglas;
    private Gson gson;
    private Datos datos;
    private boolean gana;
    private boolean pierde;

    public HiloServidorAhorcado(int id, Socket jugador, DataOutputStream dataOut, Reglas reglas) {
        this.id = id;
        this.jugador = jugador;
        this.dataOut = dataOut;
        this.reglas = reglas;
    }

    @Override
    public void run() {
        gson = new Gson();
        datos = new Datos();
        try {
            dataIn = new DataInputStream(jugador.getInputStream());
            String json;
            datos.setTurno(1);
            datos.setEstado(EN_PARTIDA);
            datos.setTablero(new char[reglas.getPalabra().length()]);
            while (true) {
                reglas.setToca(false);
                System.out.println("Entra hilo " + this.getName() + " con id " + id);
                System.out.println("Turno: " + reglas.getTurno());
                System.out.println("Datos: " + datos.getTurno());
                if (reglas.getTurno() == id) {
                    System.out.println("Entra if hilo " + id);
                    json = dataIn.readUTF();//lee letra
                    datos = gson.fromJson(json, Datos.class);
                }
                System.out.println("sale if hilo " + id);
                reglas.compruebaJugadaSynchronized(id, datos.getLetra());//compruebaJugada
                gana = reglas.compruebaGana();
                pierde = reglas.compruebaPierde();

                datos.setTablero(reglas.completaTablero(datos.getLetra()));
                datos.setAciertos(reglas.getAciertos());
                datos.setFallos(reglas.getFallos());
                datos.setTurno(reglas.getTurno());

                if (gana) {
                    reglas.setFin(true);
                    datos.setGana(true);
                    datos.setEstado(FINALIZADO);
                    json = gson.toJson(datos);//envia ganador
                    dataOut.writeUTF(json);
                    dataOut.flush();
                    break;
                }
                if (pierde) {
                    reglas.setFin(true);
                    datos.setPierde(true);
                    datos.setEstado(FINALIZADO);
                    json = gson.toJson(datos);//envia perdedor
                    dataOut.writeUTF(json);
                    dataOut.flush();
                    break;
                }
                json = gson.toJson(datos);
                dataOut.writeUTF(json);//manda aciertos, fallos y turno
                dataOut.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloServidorAhorcado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                jugador.close();
                dataIn.close();
                dataOut.close();
            } catch (IOException ex) {
                Logger.getLogger(HiloServidorAhorcado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
