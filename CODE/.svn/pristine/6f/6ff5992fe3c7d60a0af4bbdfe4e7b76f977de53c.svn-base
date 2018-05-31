/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thanhbv4
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File folder = new File("D:\\sourcecode\\MtaTrace\\0\\");
        File[] listOfFiles = folder.listFiles();
        List<Object[]> objects = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                FileInputStream fis = null;

//                System.out.println(file.getName());
                fis = new FileInputStream(file);

                String stringName = file.getName();
                String[] parts = stringName.split("-");
                String part1 = parts[0];
//                System.out.println("Tên các file là: " + part1);

                int a = 0;
                int b = Integer.parseInt(part1);
                a = b % 9;
                System.out.println("Module :" +a);
                if(a==0 || a ==1 || a ==2){
                    System.out.println("Các file có mod là 0,1,2 là" +stringName);
                    
                }
                if(a==3 || a==4 || a ==5){
                    System.out.println("Các file có mod là 3,4,5 là : " + stringName);
                }
                if(a==6 || a==7||a ==8){
                    System.out.println("các file có mod là 6,7,8 là : " + stringName);
                }
                    
            }
        }
    }
}
