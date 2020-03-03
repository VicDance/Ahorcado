/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahorcadoBC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class Reglas {

    private String palabra;
    private char[] tablero;
    private String[] arrayPalabras;
    private List<String[]> listaPalabras;
    private int turno;
    private boolean fin;
    private int aciertos, fallos;
    private boolean gana;
    private boolean pierde;
    private char letra;
    private boolean toca = false;

    public Reglas() {
        arrayPalabras = new String[]{"reingenieria", "cubeta", "tunelizacion", "protocolo",
            "puertos", "conexion", "broadcasting", "direccion", "internet", "router", "switch", "wifi", "estandar",
            "socket", "transporte", "enlace", "capas", "arquitectura", "cliente", "servidor", "proxy", "firewall", "redes",
            "LAN", "WAN", "MAN", "hub", "concentrador", "datagrama", "puente", "fibra", "TCP", "UDP", "mascara", "gateway",
            "servidor", "DNS", "cliente", "conmutacion", "circuito", "satelite", "coaxial", "microondas", "seÃ±al", "ingrarrojos",
            "token", "anillo", "bus", "control", "flujo", "congestion", "enrutamiento", "aplicacion", "correo", "peertopeer",
            "reingenieria", "cubeta", "tunelizacion", "protocolo", "puertos", "conexion", "broadcasting", "direccion", "internet",
            "router", "switch", "wifi", "estandar", "socket", "transporte", "enlace", "capas", "arquitectura", "cliente", "servidor",
            "proxy", "firewall", "redes", "LAN", "WAN", "MAN", "hub", "concentrador", "datagrama", "puente", "fibra", "TCP", "UDP",
            "mascara", "gateway", "servidor", "DNS", "cliente", "conmutacion", "circuito", "satelite", "coaxial", "microondas",
            "infrarrojos", "token", "anillo", "bus", "control", "flujo", "congestion", "enrutamiento", "aplicacion", "correo", "peertopeer"};
        listaPalabras = new ArrayList<String[]>();
        listaPalabras.add(arrayPalabras);
        Collections.shuffle(listaPalabras);
        this.palabra = listaPalabras.get(0)[0];
        System.out.println(palabra);
        this.tablero = new char[palabra.length()];
        this.turno = 1;
        this.fin = false;
        this.aciertos = 0;
        this.fallos = 0;
        this.gana = false;
        this.pierde = false;
    }

    public String getPalabra() {
        return palabra;
    }

    public char[] getTablero() {
        return tablero;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public int getAciertos() {
        return aciertos;
    }

    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }

    public int getFallos() {
        return fallos;
    }

    public void setFallos(int fallos) {
        this.fallos = fallos;
    }

    public char getLetra() {
        return letra;
    }

    public void setLetra(char letra) {
        this.letra = letra;
    }

    public boolean isToca() {
        return toca;
    }

    public void setToca(boolean toca) {
        this.toca = toca;
    }

    public synchronized void compruebaJugadaSynchronized(int id, char letra) {
        boolean acertada = false;
        while (turno != id) {
            try {
                if(fin){
                    break;
                }
                System.out.println("Duerme hilo " + id);
                wait();
                System.out.println("Despierta hilo " + id);
            } catch (InterruptedException ex) {
                Logger.getLogger(Reglas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while (!toca) {
            for (int i = 0; i < palabra.length(); i++) {
                if (letra == palabra.charAt(i)) {
                    aciertos++;
                    acertada = true;
                }
            }
            if (!acertada) {
                fallos++;
            }
            turno++;
            if (turno > 3) {
                turno = 1;
            }
            toca = true;
            notifyAll();
            //System.out.println("Despierta hilo " + Thread.currentThread().getName());
            System.out.println("Sale comprueba hilo " + id);
            //System.out.println("Entra el hilo " + id);
        }
    }

    public char[] completaTablero(char letra) {
        for (int i = 0; i < palabra.length(); i++) {
            if (palabra.charAt(i) == letra) {
                tablero[i] = letra;
            } else {
                if (tablero[i] != '_' || tablero[i] != ' ') {
                } else {
                    tablero[i] = '_';
                }
            }
        }
        return tablero;
    }
    
    public boolean compruebaGana() {
        if (aciertos == palabra.length()) {
            gana = true;
        }
        return gana;
    }

    public boolean compruebaPierde() {
        if (fallos == 6) {
            pierde = true;
        }
        return pierde;
    }
}
