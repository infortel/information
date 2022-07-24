/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.lib;

import com.infortel.slibrary.ListStringSet;
import com.infortel.slibrary.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author leon
 */
public class Users {
//******************************************************************************
    //public static Users self = null;
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
        public String getString() {
            return "User: login="+login+" password="+password;
        }
        //**********************************************************************
    }


    public ListStringSet userRecords;
    public String dummy;
    
//******************************************************************************
    public Users(GeneralParams p) {
        if (p==null) p=GeneralParams.active;
        readData(p);       
    }
//**************************************
    private synchronized void readData(GeneralParams p) {
        SString d_file=new SString();
        String error=d_file.loadFromFile(Constants.FILENAME_USERS);
        
        try {
            if (error==null) {
                readDataJson(p,d_file);
            } else {
                General.sendMessage(p,"Error loading configuration file: "+error
                        +"\n\nWorking Directory: "+System.getProperty("user.dir"));
            }
        } catch (Exception e) {
            //Do nothing.
        }
    }
//**************************************
    private synchronized void readDataJson(GeneralParams p, SString d_file) {
        try {
            userRecords=new ListStringSet(true);
            JSONParser parser = new JSONParser();

            JSONObject root=(JSONObject) parser.parse(d_file.str());
            JSONArray users=(JSONArray)root.get(TAG_USERS);
            for (int u=0; u<users.size(); u++) {
                JSONObject user=(JSONObject)users.get(u);
                
                UserRec rec=new UserRec();
                rec.login=(String)user.get(TAG_USERS_ITEM_LOGIN);
                rec.password=(String)user.get(TAG_USERS_ITEM_PASSWORD);
                rec.name=(String)user.get(TAG_USERS_ITEM_NAME);
                rec.access=(String)user.get(TAG_USERS_ITEM_ACCESS);
                userRecords.add(rec.login, rec);      
            }

        } catch (Exception e) {
            try {
                General.sendMessage(p,"Error loading configuration file for user: "+e.getMessage());
            } catch (Exception e1) {};
        }
    }
//**************************************
    /*
    private synchronized void readDataXML(GeneralParams p, SString d_file) {
        try {
            userRecords=new ListStringSet(true);
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

        } catch (Exception e) {

        }
    }
    */
//******************************************************************************
    public String getString() {
        String result="";
        for (int i=0; i<userRecords.size(); i++) {
            result+=((UserRec)(userRecords.get(i).object)).getString();
        }
        return result;
    }
//******************************************************************************
    public void saveData(GeneralParams p) {
        
        JSONObject root=new JSONObject();
        JSONArray users=new JSONArray();
        root.put(TAG_USERS, users);
        
        for (int i=0; i<userRecords.size(); i++) {
            UserRec rec=(UserRec)userRecords.get(i).object;
            
            JSONObject user=new JSONObject();
            user.put(TAG_USERS_ITEM_LOGIN, rec.login);
            user.put(TAG_USERS_ITEM_PASSWORD, rec.password);
            user.put(TAG_USERS_ITEM_NAME, rec.name);
            user.put(TAG_USERS_ITEM_ACCESS, rec.access);
            users.add(user);
        }
        
        String jstring=root.toJSONString();
        
        String pretty=new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(jstring));
        SString source=new SString(pretty);
        String error=source.saveToFile(Constants.FILENAME_USERS);
        if (error!=null) {
            try {General.sendMessage(p, "Error: could not save data: "+error);} catch (Exception e) {};
        }
        readData(p);
        
    }
//******************************************************************************
    /*
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
    */
//******************************************************************************
    public UserRec getUser(String log) {
        StringSet set=userRecords.get(log);
        if (set==null) return null;
        return (UserRec)set.object;
    }
//******************************************************************************
}
