/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.lib;

import com.infortel.activation_library.main.Get_Key;
import com.infortel.slibrary.SString;

/**
 *
 * @author leon
 */
public class Constants {
    
    public static final boolean DEBUGGING=false;
    public static final int COMPUTER_PROGRAM_ID=77536614; //Production
    //public static final int COMPUTER_PROGRAM_ID=85456949;  //Development.
    
    private final static String VERSION="2019-06-10";
    public final static boolean FLAGS_ON=false;
    public final static String TEST=null;
    //public final static String TEST="www";
    //public final static String TEST="adm";
    
    public final static String SYSTEM_LOGIN="iwdfklkadf";
    public final static String SYSTEM_PASSWORD="sdkjiowernv";
    
    
    public final static String ADM_PARAM_SYSTEM_LOGIN="slogin";
    public final static String ADM_PARAM_SYSTEM_PASSWORD="spassword";
    
    //public static final String DIRECTORY_DATA="d:/a/";
    public static final String DIRECTORY_DATA=getDirectoryData();
    public static final String DIRECTORY_DATA_WINDOWS="c:/information/data/";
    public static final String DIRECTORY_DATA_LINUX="/aux/data/";
    public static final String DIRECTORY_INFORMATION="information/";
    
    public static final String FILENAME_ACTIVATIONS=DIRECTORY_DATA+DIRECTORY_INFORMATION+"activations.txt";
    public static final String FILENAME_ACTIVATIONS_DEBUG=DIRECTORY_DATA+DIRECTORY_INFORMATION+"activations.txt";
    public static final String FILENAME_FORWARD=DIRECTORY_DATA+DIRECTORY_INFORMATION+"forward.xml";
    public static final String FILENAME_LOG=DIRECTORY_DATA+DIRECTORY_INFORMATION+"log.xml";
    public static final String FILENAME_USERS=DIRECTORY_DATA+DIRECTORY_INFORMATION+"users.xml";
    
    public static final String DOMAIN_PREFIX_WWW="www";
    public static final String INDEX_FILE_INFORTEL="infortel/index.html";
    public static final String DIRECTORY_SOURCE_WWW=DIRECTORY_DATA+"www/";
    public static final String DIRECTORY_SOURCE_WWW_INDEX_FILE=DIRECTORY_SOURCE_WWW+INDEX_FILE_INFORTEL;
    
    public static final char SPECIAL_CHAR_PREFIX='@';

    public static class Adm_param_get_siscon_password {
        public final static String USER_LOGIN="user_login";
        public final static String USER_NAME="user_name";
        public final static String CODE="code";
        public final static String IP="ip";
        public final static String COMPANY_NAME="company_name";
        public final static String COMPANY_CODE="company_code";
        public final static String DATE="date";
    }
    
    
    public static final String DATABASE_SERVER_TITLE="Servidor local";
    public static final String DATABASE_SERVER="localhost";
    public static final String DATABASE_SERVER_TEST="192.168.1.5";
    public static final String DATABASE_LOGIN="sa";
    public static final String DATABASE_PASSWORD="letrofni.2022";
    public static final String DATABASE_NAME="infortel";
    public static final String COMPANY_CODE="infortel";

    public static final String PASSWORD_INFORTEL="locomotora";
//******************************************************************************
    public static String getDirectoryData() {
        if (Application.isLinux()) return DIRECTORY_DATA_LINUX; else return DIRECTORY_DATA_WINDOWS;
    }
//******************************************************************************   
    public static String getVersionSummary() {
        int computerId=Get_Key.getComputerId();
        String st="Infortel CA - Version "+Constants.VERSION+" Program ID:"+COMPUTER_PROGRAM_ID;
        if (COMPUTER_PROGRAM_ID==computerId) {
            st+=" ***";
        } else {
            st+=" - Computer ID="+computerId;
        }
        return st;
    }
//******************************************************************************   

}
