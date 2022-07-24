/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.support.ns1;

import com.infortel.information.lib.Filing;
import com.infortel.information.lib.Setup;
import com.infortel.slibrary.SGetWeb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author leon
 */
public class Ns1 {
//******************************************************************************
    private final static String ERROR_PREFIX="?Error: ";
    private final static String API_KEY="t8D5XL3zb2xvA66N4vvP";
//******************************************************************************
    public static String ipValid(String ip) {
        if (ip==null) return ("Invalid IP, is null");
        if ((ip.length()<=0) || (ip.length()>16)) return "Invalid IP: "+ip;
        return null;
    }
//******************************************************************************
    public static String get_public_ip() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            String error=ipValid(ip);
            if (error!=null) throw new Exception(error);
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }
//******************************************************************************
    public static String get_ns1_Ip(String setIP) throws Exception {
        String result=null;
        String error=null;
        String content_request=null;
        String api_key=Filing.get().setup.get(Setup.TAG.NS1_API_KEY);//62daf13683d8ac008eaf8143
        String zone=Filing.get().setup.get(Setup.TAG.NS1_ZONE);
        String domain=Filing.get().setup.get(Setup.TAG.NS1_DOMAIN);
        String record=Filing.get().setup.get(Setup.TAG.ND1_RECORD);
        try {
            SGetWeb web=new SGetWeb();
            web.addSiteProperty("X-NSONE-Key", API_KEY);
            String type="GET";
            if (setIP!=null) {
                type="POST";
                content_request="{\"answers\": [{\"answer\": [\""+setIP+"\"],\"id\": \""+api_key+"\"}]}";
            }
            String url="https://api.nsone.net/v1/zones/"+zone+"/"+domain+"/"+record;
            result=web.connect(url, type, content_request);
            if (web.getErrors()!=null) {
                error=web.getErrors()+" url:"+url;
            } else {
                JSONParser parser = new JSONParser();
                
                JSONObject root=(JSONObject) parser.parse(result);
                JSONArray answers=(JSONArray)root.get("answers");
                JSONObject answerRec0=(JSONObject)(answers.get(0));
                JSONArray answerIPs=(JSONArray)answerRec0.get("answer");
                result=(String)answerIPs.get(0);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            error=e.getMessage();
        }
        if (error!=null) {
            result=ERROR_PREFIX+error;
            if (content_request!=null) result+=" content_request="+content_request;
            throw new Exception(result);
        }

        return result;
    }
//******************************************************************************
    
}
