import javax.microedition.rms.*;
import javax.microedition.lcdui.*;
/**
 *
 * @author sangprabo
 */
class Store {
    private RecordStore store;
    public static final String storageName = "SoqGaoelData";
    private int amount;
    
    Store (String storageName, int amount) {
        store = null;
        this.amount = amount;
        
    }
    
    RecordStore getStore() {
        return store;
    }
    
    int getNumOfRecords() {
        try {
            return store.getNumRecords();
        } catch (RecordStoreException rse){
            return -1;// it means something wrong with the Recordstore
        }
    }
    
    int getNextRecordID() {
        try {
            return this.getStore().getNextRecordID();
        } catch (RecordStoreException rse){
            return 1;
        }
        
    }
    
    boolean open(){
        try {
            store = RecordStore.openRecordStore(storageName, true);
        } catch (RecordStoreException rse) {
        }
        if (store == null)
            return false;
        try {
            if (this.getStore().getNumRecords() <=0)
                this.initiateData();
        } catch (RecordStoreNotOpenException rse){
            
        }
        return true;
    }
    
    void initiateData(){
        this.saveRecord(0, "aA@ÄÅ4α∆äåáà");
        this.saveRecord(0, "bβ8B");
        this.saveRecord(0, "cC©");
        this.saveRecord(0, "dD");
        this.saveRecord(0, "eΣ€èéE");
        this.saveRecord(0, "fF");
        this.saveRecord(0, "g6G9");
        this.saveRecord(0, "hH");
        this.saveRecord(0, "i1!ìI");
        this.saveRecord(0, "jJ");
        this.saveRecord(0, "kK");
        this.saveRecord(0, "l!L");
        this.saveRecord(0, "mM");
        this.saveRecord(0, "nΠN");
        this.saveRecord(0, "öøòo0θO");
        this.saveRecord(0, "pP");
        this.saveRecord(0, "qQ");
        this.saveRecord(0, "RГ®r");
        this.saveRecord(0, "$s§5S");
        this.saveRecord(0, "t+T");
        this.saveRecord(0, "Uüùu");
        this.saveRecord(0, "vV");
        this.saveRecord(0, "wW");
        this.saveRecord(0, "xX");
        this.saveRecord(0, "yY¥");
        this.saveRecord(0, "zZ");
        this.saveRecord(0, "?¿");

    }
    
    boolean close() {
        try {
            if (store != null){
                store.closeRecordStore();
            }
        } catch (RecordStoreException rse){
            return false;
        }
        return true;
    }
    
    void delete() {
        try {
            store.deleteRecordStore(storageName);
        } catch (RecordStoreNotFoundException rse) {
        } catch (RecordStoreException rse){   
        }
    }
    
    String[][] readMultipleRows(int numOfRows) {
        int i=0;
        int j=0;
        String[][] returnDataRows = new String[numOfRows][3];
        try {
            if (store.getNumRecords() >0){ // it means the record store is not empty
                RecordEnumeration re = store.enumerateRecords(null, null, false);
                while (re.hasNextElement() && (i < numOfRows)){
                    if ((j%3) == 0)
                        returnDataRows[i][2] = new String(re.nextRecord());//get the Qustion value
                    else if (j%3 == 1)
                        returnDataRows[i][1] = new String(re.nextRecord());//get the Answer value
                    else 
                        returnDataRows[i][0] = new String(re.nextRecord());//get the Grade value
                    if ((j%3) == 2)
                        i++;
                    j++;
                }
                re.destroy();
            }
            return returnDataRows;
        }catch (Exception e){
            return returnDataRows;
        }
    }
    
    String[][] readMultipleRowsWithID(int numOfRows){
        int i,j, rID;
        i = 0;
        j = 0;
        String[][] returnDataRows = new String[numOfRows][4];
        try {
            if (store.getNumRecords() >0){ // it means the record store is not empty
                RecordEnumeration re = store.enumerateRecords(null, null, false);
                while (re.hasNextElement() && (i < numOfRows)){
                    if (j%4 == 3){
                        i++;
                    }else if ((j%4) == 0){
                        rID = re.nextRecordId();
                        returnDataRows[i][3] = readRecord(rID);//get the Grade value
                    }else if (j%4 == 1){
                        rID = re.nextRecordId();
                        returnDataRows[i][2] = readRecord(rID);//get the Answer value
                    } else{
                        rID = re.nextRecordId();
                        returnDataRows[i][0] = String.valueOf((rID + 2)/3);//get the 'rowID' value, yes Qepeng was wrong. They should be 1,2,3 not 1,4,7,10
                        returnDataRows[i][1] = readRecord(rID);//get the Question value
                    }
                    j++;
                }
                re.destroy();
            }
            return returnDataRows;
        }catch (Exception e){
            return null;
        }
        
    }
    
    String[] readMultipleRecords(int numOfRec){//get mulltiple records from rms, start from recID 1 to numOfRec, return String[]
        
        if (numOfRec > this.getNumOfRecords())
            numOfRec = this.getNumOfRecords();
        String[] returnedData = new String[numOfRec];
        for (int i=1;i<=numOfRec;i++){
            if (!this.readRecord(i).equals("empty")){
                int kecoa = i-1;
                returnedData[kecoa] = this.readRecord(i);
            }
        }
        return returnedData;
        
    }
    
    void saveRecord (int recID, String str) {
        byte[] rec = str.getBytes();
        try {
            if (recID == 0){
                store.addRecord(rec, 0, rec.length);
            } else {
                store.setRecord(recID, rec, 0, rec.length);
            }
        } catch (RecordStoreException rse){
            
        }
    }
    
    String readRecord(int recID) {
        try {
            byte[] recData = new byte[amount];
            int len;
            
            len = store.getRecord(recID, recData, 0);
            return new String(recData, 0, len);
        } catch (RecordStoreException rse){
            return "empty";
        }
    }
    
    void delRecord(int recID) {
        try {
            store.deleteRecord(recID);
        } catch (RecordStoreException rse){
            
        }
    }

    
}
