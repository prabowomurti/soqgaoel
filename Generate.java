import java.io.IOException;
import javax.microedition.rms.*;
import javax.microedition.lcdui.*;
import java.util.Random;
import javax.microedition.io.*;
import javax.wireless.messaging.*;

/**
 * @author sangprabo
 */
public class Generate extends Form implements CommandListener, Runnable{
    
    private Display display;
    private SoqGaoel midlet;
    private String oriMsg, smsPort, addr;
    
    private Command cmSend, cmBack, cmExit, cmRegenerate;
    private TextField tfPhone;
    private StringItem siMsg;
    
    
    public Generate(SoqGaoel midlet, Display display, String msg, String smsPort){
        super("Generate Text Message");
        this.midlet = midlet;
        this.display = display;
        this.oriMsg = msg;
        this.smsPort = smsPort;
        this.siMsg = new StringItem("", encode(oriMsg));
        
        cmSend = new Command("Send", Command.OK, 1);
        cmBack = new Command("Back", Command.BACK, 1);
        cmExit = new Command("Exit", Command.EXIT, 1);
        cmRegenerate = new Command("Regenerate", Command.OK, 1);
        tfPhone = new TextField("Phone Number", "", 30, TextField.PHONENUMBER);
        
        
        this.addCommand(cmSend);
        this.addCommand(cmBack);
        this.addCommand(cmExit);
        this.addCommand(cmRegenerate);
        this.append(tfPhone);
        this.append(siMsg);
        this.setCommandListener(this);
        
    }
    
    public void commandAction (Command c, Displayable d) {
        if (c == cmSend) {
            if (tfPhone.size() == 0) {
                Alert al = new Alert("Error", "Phone Number Required", null, AlertType.ERROR);
                al.setTimeout(Alert.FOREVER);
                display.setCurrent(al);
            } else {
                this.addr = "sms://" + tfPhone.getString();
                //if (!smsPort.equals("")) this.adr += ":" + smsPort;
                new Thread(this).start();
                
            }
        }else if (c == cmBack) {
            display.setCurrent(midlet.tbClip);
        }else if (c == cmRegenerate){
            siMsg.setText(encode(oriMsg));
        }else if (c == cmExit){
            midlet.exitMidlet();
        }
    }
    
    public static String encode(String msg) {
        String encmsg = "";
        final String letters = "abcdefghijklmnopqrstuvwxyz?";
        int max = msg.length();
        char ch;;
        String choices;
        String[] choicesData = new String[27];
        
        Store store = new Store(Store.storageName, 100);
        if (store.open()){
            choicesData = store.readMultipleRecords(27);
            store.close();
        }
        
        
        for (int i=0; i<max; i++ ){
            ch = msg.charAt(i);
            int position = letters.indexOf(String.valueOf(ch));
            if (position != -1) {//it means ch exists in letters
                choices = choicesData[position];
            }else {// cannot found ch in letters
                choices = String.valueOf(ch);
            }
            
            if (choices.length() == 1){
                encmsg += choices;
            }else {
                encmsg += choices.charAt(getRand(choices.length()));
            }
        }
        
        return encmsg;
    }
    //http://www.j2meforums.com/forum/index.php?PHPSESSID=71879c428d09385ed29d2e8535a09e40&topic=492.msg1472#msg1472
    private static final Random randomizer = new Random();
    
    public static final int getRand(int gLength){
        int rndmNumber = Math.abs(randomizer.nextInt());
        return (rndmNumber % gLength);
    }

    public void run() {
        MessageConnection smsconn = null;
        
        try {
            smsconn = (MessageConnection) Connector.open(addr);
            TextMessage txtmessage = (TextMessage) smsconn.newMessage(MessageConnection.TEXT_MESSAGE);
            txtmessage.setAddress(addr);
            txtmessage.setPayloadText(siMsg.getText());
            smsconn.send(txtmessage);
            
            Alert al = new Alert("Info", "Message Sent", null, AlertType.INFO);
            al.setTimeout(Alert.FOREVER);
            display.setCurrent(al);
        } catch (Throwable t) {
            System.out.println("Error: " + t.toString());
            Alert al = new Alert("Error", "Failed", null, AlertType.ERROR);
            al.setTimeout(Alert.FOREVER);
            display.setCurrent(al);
        }
        
        if (smsconn != null){
            try {
                smsconn.close();
            } catch (IOException e){
            }
        }
    }
}
