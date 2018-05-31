/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtatrace;

import com.viettel.common.util.CommonUtils;
import com.viettel.mail.db.QueryUtils;
import com.viettel.mail.util.ActionThread;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author thanhbv4
 */
public class ReadFile extends ActionThread {
    

    public static void main(String[] args) throws InstantiationException, SQLException, IllegalAccessException {
        {
            try {
//                String url = "jdbc:mysql://10.58.71.227:3306/MTA";
//                String user = "mta";
//                String password = "mta#2017";
//                
//                try {
//                    // Load the Connector/J driver
//                    Class.forName("com.mysql.jdbc.Driver").newInstance();
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
//                }
//// Establish connection to MySQL
//                Connection conn = DriverManager.getConnection(url, user, password);

                String host = "zimbra.viettel.com.vn";
                java.util.Properties properties = System.getProperties();
                properties.setProperty("mail.smtp.host", host);
                Session session = Session.getDefaultInstance(properties);
                MimeMessage email = null;
                File folder = new File("/opt/zimbra/store/incoming/");
                File[] listOfFiles = folder.listFiles();
                List<Object[]> objects = new ArrayList<>();
                
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        FileInputStream fis = null;
                        try {
                            System.out.println(file.getName());
                            fis = new FileInputStream(file);
                            email = new MimeMessage(session, fis);

                            if (email != null) {
                                Object[] obj = new Object[9];

                                obj[0] = email.getMessageID();
                                if (email.getFrom() != null) {
                                    obj[1] = CommonUtils.convertAddessesToString(Arrays.asList(email.getFrom()));
                                }
                                obj[2] = email.getSubject();
                                obj[3] = email.getFileName();
                                obj[4] = email.getSentDate();
                                obj[6] = file.getName();
                                String fileName = file.getPath();
                                if (obj[0] == null) {
                                    obj[5] = 0;
                                    Date date = new Date();
                                    LocalDate localDate = LocalDate.now();
                                    String dateTime = DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate);
//                                    System.out.println(dateTime);
                                    String dirName1 = "/opt/zimbra/store/incoming/";
                                    String dirName = dirName1.concat(dateTime);
                                    Path path = Paths.get(dirName);
                                    Files.createDirectories(path);
                                    System.out.println("Dir Name" + path);
                                    File fileCopy = new File(fileName);
                                    File destinationDir = new File(dirName);
                                    FileUtils.copyFileToDirectory(fileCopy, destinationDir, true);
                                    obj[6] = dirName.concat("\\").concat(file.getName());
                                    System.out.println("Filename to copy: " +obj[6]);       
                                } else {
                                    obj[5] = 1;
                                }
       
                                // lấy danh sách người nhận và insert từng email vào trường người nhận
                                if (email.getAllRecipients() != null) {
                                    //obj[7] = email.getAllRecipients();
                                    obj[7] = Arrays.asList(email.getAllRecipients());
////                                    String part[] = null;
//                                    int idx = 0;
//                                    System.out.println("Length Recipients: " + parts.length);
//                                    for (idx = 0; idx < parts.length; idx++) {
//                                     // String  part1 = parts[idx];
//                                        System.out.println("receiver:" + parts[idx]);
//                                        obj[7] = parts[idx];
//                                    }

//                                    String part1 = parts[0];
//                                  String part2 = parts[1];
//                                      System.out.println("receiver 1 :" +part2);
                                }
                                obj[8] = obj[0];
                                objects.add(obj);
                            }
                            System.out.println("content type: " + email.getContentType());
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (MessagingException ex) {
                            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                fis.close();
                            } catch (IOException ex) {
                                Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

//                    if (file.delete()) {
//                        System.out.println(file.getName() + " is deleted!");
//
//                    } else {
//                        System.out.println("Delete operation is failed.");
//                    }
                    }
                }
                QueryUtils queryUtils = new QueryUtils();
                queryUtils.insertMtaIntoDatabase(objects);
                queryUtils.insertMtaTransHistory(objects);
                //queryUtils.insertMtaTransDetail(objects);

                System.out.println("Thực hiện insert thành công!");
            } catch (Exception ex) {
                Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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
        return NO_SLEEP;
    }

    @Override
    protected void action() throws Exception {
    }

}
