/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahorcadoBC;

/**
 *
 * @author Vicky
 */
public class Datos implements Constantes{
    private int estado;
    private int fallos;
    private int aciertos;
    private char letra;
    private boolean gana, pierde;
    private int turno;
    private char[] tablero;
    
    public Datos(){
        this.estado = ESPERANDO;
        this.fallos = 0;
        this.aciertos = 0;
        this.gana = false;
        this.tablero = new char[]{};
        this.turno = 1;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getFallos() {
        return fallos;
    }

    public void setFallos(int fallos) {
        this.fallos = fallos;
    }

    public int getAciertos() {
        return aciertos;
    }

    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public char getLetra() {
        return letra;
    }

    public void setLetra(char letra) {
        this.letra = letra;
    }

    public boolean isGana() {
        return gana;
    }

    public void setGana(boolean gana) {
        this.gana = gana;
    }

    public boolean isPierde() {
        return pierde;
    }

    public void setPierde(boolean pierde) {
        this.pierde = pierde;
    }

    public char[] getTablero() {
        return tablero;
    }

    public void setTablero(char[] tablero) {
        this.tablero = tablero;
    }
}
