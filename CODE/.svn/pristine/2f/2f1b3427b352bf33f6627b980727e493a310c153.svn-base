package com.viettel.common.bo;

import java.util.Date;
import java.util.List;

public class LogEmailMTA {
    private String type_log ;
    private String time;
    //milisecond sau thoi gian
    private long miliSecond;
    //thong tin ví dụ [qtp1904616195-113547:https://10.30.182.154:443/service/soap/GetMsgRequest] 
    //MTA :thong tin ví dụ postfix/lmtp[7709]: 3236060DF2D: 
    private String inforAction;

    //action voi EMAIL : read,search,delete 
    //MTA : send ,receive action from gui tu, to gửi đến VD to=<quanglh2@viettel.com.vn>,
    private String action;
    //status action INFOR,WARNING 
    //MTA status=sent
    private String status;
    private String name_email;
    private String ip;
    private String oIp;
    private String ua;
    private long elapsed;
    //ket qua thuc hien
    //MTA ket qua thuc hien cho vào queue hoặc Delivery OK VD (250 2.1.5 Delivery OK)
    private String result;
    
    
    //MTA
    //relay=mailbox.viettel.com.vn[10.30.182.154]:7025
    private String relay;
    //delay=0.13
    private float delay;
    //delays=0.02/0.01/0/0.1
    private String delays;
   
    //dsn=2.1.5,
    private String dsn;
    //MTA danh sach nguoi nhan khi FWD
    private List<String> listReceiveFWD;
    
    public long getMiliSecond() {
        return miliSecond;
    }

    public void setMiliSecond(long miliSecond) {
        this.miliSecond = miliSecond;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName_email() {
        return name_email;
    }

    public void setName_email(String name_email) {
        this.name_email = name_email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getoIp() {
        return oIp;
    }

    public void setoIp(String oIp) {
        this.oIp = oIp;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }


    public String getInforAction() {
        return inforAction;
    }

    public void setInforAction(String inforAction) {
        this.inforAction = inforAction;
    }

    public String getRelay() {
        return relay;
    }

    public void setRelay(String relay) {
        this.relay = relay;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public String getDelays() {
        return delays;
    }

    public void setDelays(String delays) {
        this.delays = delays;
    }

    public String getDsn() {
        return dsn;
    }

    public void setDsn(String dsn) {
        this.dsn = dsn;
    }

    public List<String> getListReceiveFWD() {
        return listReceiveFWD;
    }

    public void setListReceiveFWD(List<String> listReceiveFWD) {
        this.listReceiveFWD = listReceiveFWD;
    }

    public String getType_log() {
        return type_log;
    }

    public void setType_log(String type_log) {
        this.type_log = type_log;
    }

    @Override
    public String toString() {
        return "LogEmailMTA{" + "type_log=" + type_log + ", time=" + time + ", miliSecond=" + miliSecond + ", inforAction=" + inforAction + ", action=" + action + ", status=" + status + ", name_email=" + name_email + ", ip=" + ip + ", oIp=" + oIp + ", ua=" + ua + ", elapsed=" + elapsed + ", result=" + result + ", relay=" + relay + ", delay=" + delay + ", delays=" + delays + ", dsn=" + dsn + ", listReceiveFWD=" + listReceiveFWD + '}';
    }
    
        
}
