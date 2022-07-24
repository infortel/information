/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.lib;

import com.infortel.slibrary.ListString;
import com.infortel.slibrary.SDate;
import com.infortel.slibrary.SFile;

/**
 *
 * @author leon
 */
public class Log {
//******************************************************************************
    private static final int MAX_SIZE=100000;
//******************************************************************************
    public static void add(String xmlLine) {
        SDate date=new SDate().setNow();
        addLog1(date.getString("yyyy-MM-dd HH:mm:ss ")+xmlLine);
    }
//******************************************************************************
    public static void addLog1(String xmlLine) {
        /*
        ListString list=new ListString(false);
        
        String error=list.loadFromFile(Constants.FILENAME_LOG);
        list.insert(0, xmlLine);
        
        while (list.size()>MAX_LINES) {
            list.remove(list.size()-1);
        } 
        
        error=list.saveToFile(Constants.FILENAME_LOG);
        */
        SFile.appendTextToFile(Constants.FILENAME_LOG, xmlLine+"\n");
        
        //Free Space.
        long size=SFile.getFileSize(Constants.FILENAME_LOG);
        if (size>MAX_SIZE) {
            ListString list=new ListString(false);

            String error=list.loadFromFile(Constants.FILENAME_LOG);

            for (int i=0; i<list.size()/2; i++) {
                list.remove(i);
            }

            error=list.saveToFile(Constants.FILENAME_LOG);
        }
        
    }
//******************************************************************************
    public static ListString getLogContext() {
        ListString list=new ListString(false);
        list.loadFromFile(Constants.FILENAME_LOG);
        return list;
    }
//******************************************************************************
    
}
