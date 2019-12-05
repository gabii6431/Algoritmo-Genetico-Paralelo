/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nathy
 */
public class ThreadVerifica extends Thread{
    public void run(){
        synchronized(this){
            System.out.println("entrou thread");
            notify();
        }
    }
}
