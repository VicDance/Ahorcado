/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahorcadoBC;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class ServidorAhorcado implements Constantes{

    public ServidorAhorcado() {

    }

    public static void main(String[] args) {
        new ServidorAhorcado().listen();
    }

    private void listen() {
        final int PUERTO = 6000;
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Conectado al servidor: " + servidor);
        } catch (IOException ex) {
            System.out.println("IMPOSIBLE CONECTAR CON EL SERVIDOR " + servidor);
            System.exit(-1);
        }

        Socket jugador1 = null, jugador2 = null, jugador3 = null;
        DataOutputStream dataOut1 = null, dataOut2 = null, dataOut3 = null;
        Gson gson = null;
        Reglas reglas;
        Datos datos;

        System.out.println("Servidor Ahorcado prestando servicio...");
        int contPartida = 1;
        try {
            gson = new Gson();
            reglas = new Reglas();
            datos = new Datos();
            while (true) {
                System.out.println("Esperando jugadores para la partida " + contPartida);
                jugador1 = servidor.accept();
                System.out.println("Jugador " + JUGADOR1 + " conectado a la partida " + contPartida);
                dataOut1 = new DataOutputStream(jugador1.getOutputStream());
                datos.setTurno(JUGADOR1);
                String json = gson.toJson(datos);
                dataOut1.writeUTF(json);
                dataOut1.flush();
                HiloServidorAhorcado hilo1 = new HiloServidorAhorcado(JUGADOR1, jugador1, dataOut1, reglas);
                
                jugador2 = servidor.accept();
                System.out.println("Jugador " + JUGADOR2 + " conectado a la partida " + contPartida);
                dataOut2 = new DataOutputStream(jugador2.getOutputStream());
                datos.setTurno(JUGADOR2);
                json = gson.toJson(datos);
                dataOut2.writeUTF(json);
                dataOut2.flush();
                HiloServidorAhorcado hilo2 = new HiloServidorAhorcado(JUGADOR2, jugador2, dataOut2, reglas);
                
                jugador3 = servidor.accept();
                System.out.println("Jugador " + JUGADOR3 + " conectado a la partida " + contPartida);
                System.out.println("Comenzando partida");
                dataOut3 = new DataOutputStream(jugador3.getOutputStream());
                datos.setTurno(JUGADOR3);
                json = gson.toJson(datos);
                dataOut3.writeUTF(json);
                dataOut3.flush();
                HiloServidorAhorcado hilo3 = new HiloServidorAhorcado(JUGADOR3, jugador3, dataOut3, reglas);
                
                datos.setTablero(new char[reglas.getPalabra().length()]);
                datos.setTurno(reglas.getTurno());
                datos.setEstado(EN_PARTIDA);//manda estado
                datos.setTablero(new char[reglas.getPalabra().length()]);
                json = gson.toJson(datos);
                dataOut1.writeUTF(json);
                dataOut1.flush();
                dataOut2.writeUTF(json);
                dataOut2.flush();
                dataOut3.writeUTF(json);
                dataOut3.flush();
                
                hilo1.start();
                hilo2.start();
                hilo3.start();
                
                contPartida++;
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorAhorcado.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                servidor.close();
                
                jugador1.close();
                jugador2.close();
                jugador3.close();
                
                dataOut1.close();
                dataOut2.close();
                dataOut3.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorAhorcado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
