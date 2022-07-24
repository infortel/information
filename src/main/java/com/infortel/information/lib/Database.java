/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.infortel.information.lib;
import com.infortel.slibrary.SDBConnection;
import com.infortel.slibrary.SDBConnectionParams;
import java.sql.*;
import com.siscon.common.client.Table;
/**
 *
 * @author guillermo
 */
public class Database {
    public static SDBConnection connection;
//******************************************************************************
    public static void checkCreateConnection() {
        if (connection==null) {
            SDBConnectionParams params=new SDBConnectionParams();
            params.server=Filing.get().setup.get(Setup.TAG.DB_SERVER);
            params.server_title=Constants.DATABASE_SERVER_TITLE;
            params.databaseName=Constants.DATABASE_NAME;
            params.login=Filing.get().setup.get(Setup.TAG.DB_LOGIN);
            params.password=Filing.get().setup.get(Setup.TAG.DB_PASSWORD);
            params.typeName="mssql";
            params.initializeParams();
            connection=new SDBConnection(params,null);
        }
    }
//******************************************************************************
    public static String tab(Table table) {
        return Constants.COMPANY_CODE+"_"+table.typeName;
    }
//******************************************************************************
}
