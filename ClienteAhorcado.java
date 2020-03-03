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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class ClienteAhorcado implements Constantes {

    private Socket cliente;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private Datos datos;
    private int id;

    public ClienteAhorcado() {

    }

    public static void main(String[] args) {
        new ClienteAhorcado().listen();
    }

    private void listen() {
        final int PUERTO = 6000;
        final String HOST = "localhost";

        Gson gson;
        Scanner scanner;

        try {
            cliente = new Socket(HOST, PUERTO);
            dataIn = new DataInputStream(cliente.getInputStream());
            dataOut = new DataOutputStream(cliente.getOutputStream());
            datos = new Datos();
            gson = new Gson();
            scanner = new Scanner(System.in);

            String json = dataIn.readUTF();
            datos = gson.fromJson(json, Datos.class);
            System.out.println("Eres el jugador " + datos.getTurno());

            id = datos.getTurno();

            json = dataIn.readUTF();
            datos = gson.fromJson(json, Datos.class);//lee estado
            System.out.println("Comenzando partida");
            char letra;
            while (datos.getEstado() == EN_PARTIDA) {
                System.out.println("Id: " + id);
                System.out.println("Turno: " + datos.getTurno());
                if (datos.getTurno() == id) {
                    System.out.println("\nElige una letra");
                    letra = scanner.nextLine().charAt(0);
                    datos.setLetra(letra);
                    json = gson.toJson(datos);
                    dataOut.writeUTF(json);
                    dataOut.flush();//manda letra
                } else {
                    System.out.println("Jugando jugador " + datos.getTurno());
                }
                json = dataIn.readUTF();//lee aciertos, fallos y turno
                datos = gson.fromJson(json, Datos.class);
                dibujaAhorcado(datos.getFallos());
                mostrarTablero();
                if (datos.isGana()) {
                    System.out.println("Felicidades, los jugadores han ganado");
                }
                if (datos.isPierde()) {
                    System.out.println("Lo sentimos, los jugadores han perdido");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClienteAhorcado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cliente.close();
                dataIn.close();
                dataOut.close();
            } catch (IOException ex) {
                Logger.getLogger(ClienteAhorcado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void mostrarTablero() {
        for (int i = 0; i < datos.getTablero().length; i++) {
            System.out.print(datos.getTablero()[i] + " ");
            //System.out.print("_ ");
        }
    }

    private void dibujaAhorcado(int fallos) {
        switch (fallos) {
            case 0:
                System.out.println(".___________     ");
                System.out.println("|           |    ");
                System.out.println("|                ");
                System.out.println("|                ");
                System.out.println("|                ");
                System.out.println("|                ");
                System.out.println("|                ");
                break;
            case 1:
                System.out.println(".___________     ");
                System.out.println("|           |    ");
                System.out.println("|          ()    ");
                System.out.println("|                ");
                System.out.println("|                ");
                System.out.println("|                ");
                System.out.println("|                ");
                break;

            case 2:
                System.out.println(".___________     ");
                System.out.println("|           |    ");
                System.out.println("|          ()    ");
                System.out.println("|          ||    ");
                System.out.println("|          ||    ");
                System.out.println("|                ");
                System.out.println("|                ");
                break;

            case 3:
                System.out.println(".___________     ");
                System.out.println("|           |    ");
                System.out.println("|          ()    ");
                System.out.println("|          ||\\  ");
                System.out.println("|          ||    ");
                System.out.println("|                ");
                System.out.println("|                ");
                break;

            case 4:
                System.out.println(".___________     ");
                System.out.println("|           |    ");
                System.out.println("|          ()    ");
                System.out.println("|         /||\\  ");
                System.out.println("|          ||    ");
                System.out.println("|                ");
                System.out.println("|                ");
                break;

            case 5:
                System.out.println(".___________     ");
                System.out.println("|           |    ");
                System.out.println("|          ()    ");
                System.out.println("|         /||\\  ");
                System.out.println("|          ||    ");
                System.out.println("|            \\  ");
                System.out.println("|                ");
                break;

            case 6:
                System.out.println(".___________     ");
                System.out.println("|           |    ");
                System.out.println("|          ()    ");
                System.out.println("|         /||\\  ");
                System.out.println("|          ||    ");
                System.out.println("|          / \\  ");
                System.out.println("|                ");
                break;
        }
    }
}
