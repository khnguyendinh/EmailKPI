/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;


/**
 *
 * @author SonDV10
 */
public class Stop {

    public static int stop(int exitCode) {
        SystemManager.getInstance().stop();
        return 0;
    }

    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        try {
            Stop.stop(0);
        } catch (Throwable ex) {
           // ex.printStackTrace();
        }
    }
}
