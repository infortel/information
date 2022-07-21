/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.lib;

import com.infortel.slibrary.ListStringSet;
import com.infortel.slibrary.SString;
import com.infortel.slibrary.SXml;

/**
 *
 * @author leon
 */
public class Forwarding_Data {
//******************************************************************************
    public static Forwarding_Data self=null;
//******************************************************************************
    private final static String TAG_GENERAL_DATA="data";
    public final static String TAG_DUMMY="dummy";
    private final static String TAG_REDIRECTS="redirects";
    public final static String TAG_REDIRECTS_ITEM="item";
    public final static String TAG_REDIRECTS_ITEM_PREFIX="prefix";
    public final static String TAG_REDIRECTS_ITEM_URL="url";
    public final static String TAG_REDIRECTS_ITEM_TYPE="type";
    public final static String TAG_REDIRECTS_ITEM_DESCRIPTION="description";
    public static class Type {
        public final static String REDIRECT="r";
        public final static String FORWARD="f";
        public final static String DEFAULT=REDIRECT;
    }
    public final static String TAG_REDIRECTS_ITEM_PASSWORD="password";    
//******************************************************************************

    public static class RedirectRec {

        public String prefix;
        public String type;
        public String url;
        public String description;
        public String password;
    }


    public ListStringSet redirectRecords;
    public String dummy;
    
//******************************************************************************
    public Forwarding_Data(GeneralParams p) {
        
        readData(p);
        
    }
//**************************************
    private synchronized void readData(GeneralParams p) {
        SString d_file=new SString();
        String error=d_file.loadFromFile(Constants.FILENAME_FORWARD);
        redirectRecords=new ListStringSet(true);
        try {
            if (error!=null) General.sendMessage(p,"Error loading configuration file: "+error
                    +"\n\nWorking Directory: "+System.getProperty("user.dir"));
            else {
                SString d_data=new SString(SXml.get_nEsc(d_file.str(),TAG_GENERAL_DATA));
                
                dummy=SXml.get(d_data.str(), TAG_DUMMY);
                
                SString d_redirects=new SString(SXml.get_nEsc(d_data.str(),TAG_REDIRECTS));
                
                boolean finish=false;
                do {
                    SString d_item=new SString(SXml.get(d_redirects,TAG_REDIRECTS_ITEM,true,true));
                    if (d_item.length()==0) finish=true;
                    else {
                        RedirectRec rec=new RedirectRec();
                        rec.prefix=SXml.get(d_item, TAG_REDIRECTS_ITEM_PREFIX);
                        rec.type=SXml.get(d_item, TAG_REDIRECTS_ITEM_TYPE);
                        rec.url=SXml.get(d_item, TAG_REDIRECTS_ITEM_URL);
                        rec.description=SXml.get(d_item, TAG_REDIRECTS_ITEM_DESCRIPTION);
                        rec.password=SXml.get(d_item, TAG_REDIRECTS_ITEM_PASSWORD);
                        redirectRecords.add(rec.prefix, rec);      
                    }
                } while (!finish);
            }
        } catch (Exception e) {

        }
    }
//******************************************************************************
    public void saveData(GeneralParams p) {
        SString xml=new SString("\n");
        xml.append(SXml.set(TAG_DUMMY, dummy)+"\n");
        
        SString xml_redirects=new SString("\n");
        SString st=new SString();
        for (int i=0; i<redirectRecords.size(); i++) {
            RedirectRec rec=(RedirectRec)redirectRecords.get(i).object;
            st.clear();
            if (SString.hasChar(rec.prefix)) st.append(SXml.set(TAG_REDIRECTS_ITEM_PREFIX, rec.prefix));
            if (SString.hasChar(rec.prefix)) st.append(SXml.set(TAG_REDIRECTS_ITEM_URL, rec.url));
            if (SString.hasChar(rec.description)) st.append(SXml.set(TAG_REDIRECTS_ITEM_DESCRIPTION, rec.description));
            if (SString.hasChar(rec.type)) st.append(SXml.set(TAG_REDIRECTS_ITEM_TYPE, rec.type));
            if (SString.hasChar(rec.password)) st.append(SXml.set(TAG_REDIRECTS_ITEM_PASSWORD, rec.password));
            xml_redirects.append(SXml.set_nEsc(TAG_REDIRECTS_ITEM, st.str())+"\n");
            
        }
        
        xml.append(SXml.set_nEsc(TAG_REDIRECTS, xml_redirects.str()));
        xml.assign(SXml.set_nEsc(TAG_GENERAL_DATA,xml.str()+"\n"));
        String error=xml.saveToFile(Constants.FILENAME_FORWARD);
        if (error!=null) {
            try {General.sendMessage(p, "Error: could not save data: "+error);} catch (Exception e) {};
        }
        readData(p);
        
    }
//******************************************************************************
}
