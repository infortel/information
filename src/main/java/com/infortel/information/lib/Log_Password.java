/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.lib;

import com.infortel.slibrary.*;
/**
 *
 * @author leon
 */
public class Log_Password {
//******************************************************************************
    private static final int MAX_SIZE=100000;
//******************************************************************************
    public static void addLog(String xmlLine) {
        /*
        ListString list=new ListString(false);
        
        String error=list.loadFromFile(Constants.FILENAME_LOG);
        list.insert(0, xmlLine);
        
        while (list.size()>MAX_LINES) {
            list.remove(list.size()-1);
        } 
        
        error=list.saveToFile(Constants.FILENAME_LOG);
        */
        SFile.appendTextToFile(Constants.FILENAME_LOG_PASSWORD, xmlLine+"\n");
        
        //Free Space.
        long size=SFile.getFileSize(Constants.FILENAME_LOG_PASSWORD);
        if (size>MAX_SIZE) {
            ListString list=new ListString(false);

            String error=list.loadFromFile(Constants.FILENAME_LOG_PASSWORD);

            for (int i=0; i<list.size()/2; i++) {
                list.remove(i);
            }

            error=list.saveToFile(Constants.FILENAME_LOG_PASSWORD);
        }
        
    }
//******************************************************************************
    public static ListString getLogContext() {
        ListString list=new ListString(false);
        list.loadFromFile(Constants.FILENAME_LOG_PASSWORD);
        return list;
    }
//******************************************************************************
}
