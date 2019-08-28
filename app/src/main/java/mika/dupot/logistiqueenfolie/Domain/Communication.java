package mika.dupot.logistiqueenfolie.Domain;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Communication {

    public String encodeMessage(Hashtable<String, String> hashMessage) {

        ArrayList<String> listKeyVal = new ArrayList<String>();

        Set<Map.Entry<String, String>> setHm = hashMessage.entrySet();
        Iterator<Map.Entry<String, String>> it = setHm.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();


            listKeyVal.add(e.getKey() + "=" + e.getValue());
        }

        return TextUtils.join("##", listKeyVal);
    }


    //encode/decode message
    public Hashtable<String, String> decodeMessage(String msg_) {

        Hashtable<String, String> hashMessage = new Hashtable<String, String>();

        String[] listKeyVal = msg_.split("##");
        for (int i = 0; i < listKeyVal.length; i++) {
            String[] keyVal = listKeyVal[i].split("=");

            hashMessage.put(keyVal[0].toString(), keyVal[1].toString());
        }

        return hashMessage;
    }
}
