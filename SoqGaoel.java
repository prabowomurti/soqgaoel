import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Random;

/**
 * @author prabowo.murti AT gmail.com
 */
public class SoqGaoel extends MIDlet implements CommandListener{

    private Display display;
    private Command cmExit, cmOk, cmAbout, cmPref;
    TextBox tbClip;
    private Alert al;
    private Preference preference;

    public SoqGaoel() {
        display = Display.getDisplay(this);
        cmOk = new Command("Continue", Command.OK, 1);
        cmExit = new Command("Exit", Command.EXIT, 1);
        cmAbout = new Command("About", Command.ITEM, 1);
        cmPref = new Command("Pref", Command.ITEM, 1);

        tbClip = new TextBox("Message", "", 156, TextField.ANY);
        tbClip.addCommand(cmOk);
        tbClip.addCommand(cmExit);
        tbClip.addCommand(cmAbout);
        tbClip.addCommand(cmPref);
        tbClip.setCommandListener(this);
    }
            
    public void startApp() {
        display.setCurrent(tbClip);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    
    public void commandAction (Command c, Displayable d){
        if (c == cmOk){
            String smsPort = getAppProperty("SMS-Port");
            String oriMsg = tbClip.getString();
            Generate generate = new Generate(this, display, oriMsg, smsPort);
            display.setCurrent(generate);
        }else if (c == cmAbout){
            this.al = new Alert("About", "SoqGaoel is just created for fun, and it's free! Thanks for using :)\nPrabowo Murti\nwww.prabowomurti.com", null, AlertType.INFO);
            al.setTimeout(Alert.FOREVER);
            display.setCurrent(al);
        }else if (c == cmPref){
            Preference preference = new Preference(this, display);
            display.setCurrent(preference);
            
        }else if (c == cmExit) {
            exitMidlet();
        }
    }
    
    public void exitMidlet() {
        destroyApp(false);
        notifyDestroyed();
    }
    
}
