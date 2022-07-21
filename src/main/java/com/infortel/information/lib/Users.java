/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.lib;

import com.infortel.slibrary.ListStringSet;
import com.infortel.slibrary.*;
import com.infortel.slibrary.SXml;

/**
 *
 * @author leon
 */
public class Users {
//******************************************************************************
    public static Users self = null;
//******************************************************************************
    private final static String TAG_GENERAL_USERS="general_users";
    private final static String TAG_USERS="users";
    private final static String TAG_USERS_ITEM="item";
    public final static String TAG_USERS_ITEM_LOGIN="login";
    public final static String TAG_USERS_ITEM_NAME="name";
    public final static String TAG_USERS_ITEM_PASSWORD="password";
    public final static String TAG_USERS_ITEM_ACCESS="access";
    public static class Permit {
        public final static String RED="red/";
        public final static String RED_INFORTEL_PASS_SISCON=RED+"siscon";
        public final static String RED_ADM=RED+"adm";
    }
    private final static String TAG_DUMMY="dummy";    
//******************************************************************************

    public static class UserRec {

        public String login;
        public String password;
        public String name;
        public String access;
        //**********************************************************************
        public boolean hasAccessTo(String ckaccess) {
            ckaccess=";"+ckaccess+";";
            String access1=";"+access;
            return (ckaccess.indexOf(access1)>=0);
        }
        //**********************************************************************
    }


    public ListStringSet userRecords;
    public String dummy;
    
//******************************************************************************
    public Users(GeneralParams p) {
        
        readData(p);
        
    }
//**************************************
    private synchronized void readData(GeneralParams p) {
        SString d_file=new SString();
        String error=d_file.loadFromFile(Constants.FILENAME_USERS);
        userRecords=new ListStringSet(true);
        try {
            if (error!=null) General.sendMessage(p,"Error loading configuration file: "+error
                    +"\n\nWorking Directory: "+System.getProperty("user.dir"));
            else {
                SString d_data=new SString(SXml.get_nEsc(d_file.str(),TAG_GENERAL_USERS));
                
                dummy=SXml.get(d_data.str(), TAG_DUMMY);
                
                SString d_redirects=new SString(SXml.get_nEsc(d_data.str(),TAG_USERS));
                
                boolean finish=false;
                do {
                    SString d_item=new SString(SXml.get(d_redirects,TAG_USERS_ITEM,true,true));
                    if (d_item.length()==0) finish=true;
                    else {
                        UserRec rec=new UserRec();
                        rec.login=SXml.get(d_item, TAG_USERS_ITEM_LOGIN);
                        rec.password=SXml.get(d_item, TAG_USERS_ITEM_PASSWORD);
                        rec.name=SXml.get(d_item, TAG_USERS_ITEM_NAME);
                        rec.access=SXml.get(d_item, TAG_USERS_ITEM_ACCESS);
                        userRecords.add(rec.login, rec);      
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
        for (int i=0; i<userRecords.size(); i++) {
            UserRec rec=(UserRec)userRecords.get(i).object;
            st.clear();
            if (SString.hasChar(rec.login)) st.append(SXml.set(TAG_USERS_ITEM_LOGIN, rec.login));
            if (SString.hasChar(rec.password)) st.append(SXml.set(TAG_USERS_ITEM_PASSWORD, rec.password));
            if (SString.hasChar(rec.name)) st.append(SXml.set(TAG_USERS_ITEM_NAME, rec.name));
            if (SString.hasChar(rec.access)) st.append(SXml.set(TAG_USERS_ITEM_ACCESS, rec.access));
            xml_redirects.append(SXml.set_nEsc(TAG_USERS_ITEM, st.str())+"\n");
            
        }
        
        xml.append(SXml.set_nEsc(TAG_USERS, xml_redirects.str()));
        xml.assign(SXml.set_nEsc(TAG_GENERAL_USERS,xml.str()+"\n"));
        String error=xml.saveToFile(Constants.FILENAME_USERS);
        if (error!=null) {
            try {General.sendMessage(p, "Error: could not save data: "+error);} catch (Exception e) {};
        }
        readData(p);
        
    }
//******************************************************************************
    public UserRec getUser(String log) {
        StringSet set=userRecords.get(log);
        if (set==null) return null;
        return (UserRec)set.object;
    }
//******************************************************************************
}
