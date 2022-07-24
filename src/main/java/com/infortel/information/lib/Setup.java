/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.lib;

import com.infortel.slibrary.SString;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author leon
 */
public class Setup {
    public static class TAG {
        public final static String DB_SERVER="db_server";
        public final static String DB_LOGIN="db_login";
        public final static String DB_PASSWORD="db_password";
        public final static String NS1_API_KEY="ns1_api_key";
        public final static String NS1_ZONE="ns1_zone";
        public final static String NS1_DOMAIN="ns1_domain";
        public final static String ND1_RECORD="ns1_record";
    }
    public static String[] TAGS={TAG.DB_SERVER,TAG.DB_LOGIN,TAG.DB_PASSWORD
            ,TAG.NS1_API_KEY,TAG.NS1_ZONE,TAG.NS1_DOMAIN,TAG.ND1_RECORD
    };
//******************************************************************************
//******************************************************************************
    private JSONObject data;
//******************************************************************************
    public Setup() {
        readData();
    }
//******************************************************************************
    private synchronized void readData() {
        SString d_file=new SString();
        String error=d_file.loadFromFile(Constants.FILENAME_SETUP);
        
        try {
            if (error==null) {
                JSONParser parser = new JSONParser();
                data=(JSONObject) parser.parse(d_file.str());
            } else {
                Log.add("Error loading configuration file: "+error
                        +" Working Directory:"+System.getProperty("user.dir")
                        +" Filename:"+Constants.FILENAME_SETUP
                );
                data=new JSONObject();
            }
        } catch (Exception e) {
            data=new JSONObject();
        }
    }
//******************************************************************************
    public String get(String tag) {
        try {
            return (String)data.get(tag);
        } catch (Exception e) {
            return null;
        }
    }
//******************************************************************************
    public void set(String tag, String value) {
        try {
            data.put(tag,value);
        } catch (Exception e) {
            //do nothing
        }
    }
//******************************************************************************
    public void commit() {
        String jstring=data.toJSONString();
        
        String pretty=new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(new com.google.gson.JsonParser().parse(jstring));
        SString source=new SString(pretty);
        String error=source.saveToFile(Constants.FILENAME_SETUP);
        if (error!=null) {
            Log.add("Error: could not save data: "+error);
        }
    }
//******************************************************************************
}
