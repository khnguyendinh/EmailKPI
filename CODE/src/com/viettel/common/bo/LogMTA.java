package com.viettel.common.bo;

import java.util.Date;
import java.util.List;

public class LogMTA {

    private String time;
    //thong tin ví dụ postfix/lmtp[7709]: 3236060DF2D: 
    private String inforAction;
    //action from gui tu, to gửi đến VD to=<quanglh2@viettel.com.vn>,
    private String action;
    //relay=mailbox.viettel.com.vn[10.30.182.154]:7025
    private String relay;
    //delay=0.13
    private float delay;
    //delays=0.02/0.01/0/0.1
    private String delays;
   
    //dsn=2.1.5,
    private String dsn;
    //email người tương tác
    private String nameEmail;
    //ket qua thuc hien cho vào queue hoặc Delivery OK VD (250 2.1.5 Delivery OK)
    private String result;
     //status status=sent
    private String status;
    //danh sach nguoi nhan khi FWD
    private List<String> listReceiveFWD;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInforAction() {
        return inforAction;
    }

    public void setInforAction(String inforAction) {
        this.inforAction = inforAction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDsn() {
        return dsn;
    }

    public void setDsn(String dsn) {
        this.dsn = dsn;
    }

    public String getNameEmail() {
        return nameEmail;
    }

    public void setNameEmail(String nameEmail) {
        this.nameEmail = nameEmail;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LogMTA{" + "time=" + time + ", inforAction=" + inforAction + ", action=" + action + ", relay=" + relay + ", delay=" + delay + ", delays=" + delays + ", status=" + status + ", dsn=" + dsn + ", nameEmail=" + nameEmail + ", result=" + result + '}';
    }

    public List<String> getListReceiveFWD() {
        return listReceiveFWD;
    }

    public void setListReceiveFWD(List<String> listReceiveFWD) {
        this.listReceiveFWD = listReceiveFWD;
    }

    
        
}
