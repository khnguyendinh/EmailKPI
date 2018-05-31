/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;

import org.apache.log4j.Logger;

/**
 *
 * @author vas_sontt14
 */
public class Start {

//    private static Logger logger = Logger.getLogger(Start.class.getName());

    public static Integer start(String[] strings) {
        SystemManager.getInstance().start();
        return null;
    }

    public void controlEvent(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        try {
            Start.start(args);
        } catch (Throwable ex) {
          //  ex.printStackTrace();
            System.exit(0);
        }
    }
}
