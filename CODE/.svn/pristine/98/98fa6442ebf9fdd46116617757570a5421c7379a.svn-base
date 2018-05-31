/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;

import com.viettel.mail.util.ActionThread;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author thanhbv4
 */
public class GetCurrentDateTime extends ActionThread{

//    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onExecuting() throws Exception {
    
    }

    @Override
    protected void onKilling() {
        
    }

    @Override
    protected void onException(Exception e) {
        
    }

    @Override
    protected long sleeptime() throws Exception {
        return 24*60*60*1000;
    }

    @Override
    protected void action() throws Exception {
        Date date = new Date();
        LocalDate localDate = LocalDate.now();
        String dateTime = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
        System.out.println(dateTime);
        String dirName1 = "D:\\sourcecode\\MtaTrace\\";
        String dirName = dirName1.concat(dateTime);
        Path path = Paths.get(dirName);
        Files.createDirectories(path);
        System.out.println("Dir Name" +path);
        
    }
}
