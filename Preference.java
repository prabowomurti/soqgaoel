/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;


/**
 *
 * @author sangprabo
 */

class Preference extends Form implements CommandListener {
    
    private Display display;
    private SoqGaoel midlet;
    
    private String storageName = "SoqGaoelData";
    
    private Command cmBack, cmSave, cmReset;
    private TextField[] tfSubstitute = new TextField[27];
    
    private TextField kecoa;
    //private TextField tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9, tf10, tf11, tf12, tf13, tf14, tf15, tf16, tf17, tf18, tf19, tf20, tf21, tf22, tf23, tf24, tf25, tf26, tf27;
    private String[] choices = new String[27];
    
    public Preference (SoqGaoel midlet, Display display){
        super("Preference");
        this.display = display;
        this.midlet = midlet;
        
        cmBack = new Command("Back",null, Command.CANCEL, 1);
        cmSave = new Command("Save", null, Command.OK, 2);
        cmReset = new Command("Set Default", null, Command.OK, 3);
        addCommand(cmBack);
        addCommand(cmSave);
        addCommand(cmReset);
        setCommandListener(this);
        
        
        Store store = new Store(storageName, 100);
        if (store.open()){
            this.choices = null;
            this.choices = store.readMultipleRecords(27);
            store.close();
        }
        
        String letters = "abcdefghijklmnopqrstuvwxyz?";
        append("Letters substitution possibilities");
        for (int i=0;i<27;i++){
            tfSubstitute[i] = new TextField(String.valueOf(letters.charAt(i)),this.choices[i], 15, TextField.ANY);
            append(tfSubstitute[i]);
        }
    }
    
    public void commandAction (Command c, Displayable s){
        if (c == cmBack){
            display.setCurrent(midlet.tbClip);
        }else if (c == cmSave){
            Store store = new Store(storageName, 100);
            if (store.open()){
                for (int i=0;i<27;i++){
                    store.saveRecord(i+1, tfSubstitute[i].getString());
                }
                store.close();
            }
            Alert al = new Alert("Saved", "Preference saved", null, AlertType.INFO);
            al.setTimeout(Alert.FOREVER);
            display.setCurrent(al);
        }else if (c == cmReset){
            Store store = new Store(Store.storageName, 100);
            store.delete();
            store.open();
            store.close();
            Preference pref = new Preference(midlet, display);
            display.setCurrent(pref);
        }
    }
}
