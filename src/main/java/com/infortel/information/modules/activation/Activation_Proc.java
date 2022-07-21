/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.infortel.information.modules.activation;

import com.infortel.activation_library.main.Activation_Data;
import com.infortel.activation_library.main.Get_Key;
import com.infortel.slibrary.SConvert;
import com.infortel.slibrary.SGeneral;
import com.infortel.slibrary.SString;
import com.infortel.slibrary.ListString;
import com.infortel.slibrary.SDate;
import com.infortel.slibrary.SInt;
import com.infortel.slibrary.SDBQuery;
import javax.servlet.http.*;
import java.io.*;
import com.siscon.common.client.Field;
import com.siscon.common.client.Table;
import com.infortel.information.lib.Constants;
import com.infortel.information.lib.Database;
/**
 *
 * @author guillermo
 */
public class Activation_Proc {
//******************************************************************************
    public static final int ACTIVATION_MAXIMUM_AUTHORIZATION_MONTHS=3;
    public static final int ACTIVATION_MAXIMUM_PASTDUE_ALLOWED_MONTHS=4;
    public static final int ACTIVATION_POWER_FACTOR=2;  //Can be to the power of 2 or 3, 4...
    public static final String EXCLUDE_THIS_LEDGER_CODE="116.185";
//******************************************************************************
    private static final String HREF_CODIGO="code";
    private static final String HREF_PASSWORD="confirmation";

    //private static final String COL1="<td>&nbsp";
    //private static final String COL1r="<td align='right'>&nbsp";
    //private static final String COL2="&nbsp<td>";
    private static final String COL1="<td>";
    private static final String COL1r="<td align='right'>";
    private static final String COL2="<td>";
    private static final double MARGIN_AMOUNT=100.00;
//******************************************************************************
    private PrintWriter out;
    private SDBQuery qAgent;
    private SDBQuery qAccounts;
    private String agent_code;
    private boolean manualExtensionAuthorized;
    HttpServletRequest request;
    SDate version;
    SDate expires;
    int daysActivated;
    int pastDueDays;
    int activationCount;
    SString licenceDetail;
    SDate now;
    String codesGroup;
    SDate fecha_extension_licencia;
//******************************************************************************
    public boolean reportOk(String text) {
        if (text==null) return true;
        else {
            out.println("<br>Error: "+text);
            return false;
        }
    }
//******************************************************************************
    public Activation_Proc(HttpServletRequest request, HttpServletResponse response) {

        try {

            out = response.getWriter();

            this.request=request;

            Database.checkCreateConnection();
            agent_code=request.getParameter(HREF_CODIGO);
            int computerId=Get_Key.getComputerId();
            out.println("<br>"+Constants.getVersionSummary()+"-"+computerId);
            out.println("<br>");

            if ((computerId==Constants.COMPUTER_PROGRAM_ID) || (Constants.DEBUGGING)) {

                qAgent=new SDBQuery(Database.connection,null);
                qAgent.sqlText.append("Select * from "+Database.tab(Table.agentes)
                        +" where codigo='"+agent_code+"'"
                        );
                if (reportOk(qAgent.open(true))) {
                    if (qAgent.next()) {
                        processLogin();
                    } else {
                        out.println("<br>Código no pudo ser encontrado");
                    }
                }
            } else {
                out.println("<br>Error en código de computadora");
            }
        } catch (Throwable e) {
            out.println("<br>"+e.getMessage());
        }
    }
//******************************************************************************
    private void processLogin() {

        now=new SDate().setNow();

        //agent_code=qAgent.getString(Field.General.CODIGO);
        String passwordDbRaw=qAgent.getString(Field.Agentes.CLAVE);
        
        int passwordHRefRaw=now.getDays();
        for (int i=0; i<passwordDbRaw.length(); i++) passwordHRefRaw+=(int)passwordDbRaw.charAt(i);
        String passwordDb=""+passwordHRefRaw;
        
        String passwordHRef=request.getParameter(HREF_PASSWORD);

        if ((SString.equal(passwordHRef,Constants.PASSWORD_INFORTEL))
                || (SString.equal(passwordHRef,passwordDb))
            ) {

            manualExtensionAuthorized=false;
            boolean ok=true;
            getCodesForTheGroup();
            SString codes=new SString(codesGroup);
            evaluateDates_start();
            while (codes.length()>0) {
                String code=codes.nextStr(";");
                if (SString.hasChar(code)) {
                    if (ok) ok=openAccountList(code);
                    evaluateDates_process();
                    presentAccountStatement(code);
                    qAccounts.closeQuery();
                }
            }
            evaluateDates_finish();
            if (!ok) out.println("<br>No se pudo obtener estado de cuenta del cliente");
            else {

                //Verify manual extension.
                if (fecha_extension_licencia!=null) if (fecha_extension_licencia.isAfter(now)) {
                    int daysActivatedExt=fecha_extension_licencia.getDays()-now.getDays();
                    if (daysActivatedExt>31) daysActivatedExt=0;
                    if (daysActivated<daysActivatedExt) {
                        daysActivated=daysActivatedExt;
                        expires=new SDate(now).addDays(daysActivated);
                        manualExtensionAuthorized=true;
                    }
                }
                
                //if(daysActivated<=0) getManualExtension();

                presentIntroduction();
                if (daysActivated>0) presentActivations();
                addAudit();
            } 
        } else out.println("<br>Clave invalida");
    }
//******************************************************************************
    private void presentIntroduction() {

        out.println("<br>Cliente: "
                        +qAgent.getString(Field.General.NOMBRE)+"    ("+agent_code+")"
                        +"<br>");

        out.println("<br>");

        if (pastDueDays>0) {
            out.println("Sus cuentas se encuentran con una mora de "+(pastDueDays/30)+" meses y "+pastDueDays%30+" días<br>");
        } else {
            out.println("Sus cuentas se encuentran al día.<br>");
        }
        
        if (fecha_extension_licencia==null) {
            out.println("No existe ninguna extensión.<br>");
        } else {
            out.println("Ultima Ext: "+fecha_extension_licencia.getString("dd-MM-yyyy")+"<br>");
        }

        if (daysActivated>0) {
            if (manualExtensionAuthorized) {
                out.println("Nota: **** Activación especial autorizada **** <br>");
                out.println("<br>");
            }
            out.println("Activado por = "+daysActivated+" días hasta el "+expires.getString("dd/MM/yyyy")+"<br>");
        } else {
            out.println("Favor comunicarse con INFORTEL <br>");
            out.println("su empresa presenta una morosidad importante.<br>");
        }
    }
//******************************************************************************
    /*
    private void getManualExtension() {
        licenceDetail=new SString();

        ListString list=new ListString();
        String error=list.loadFromFile(inter.Parameters.ACTIVATION_FILE_DATA);
        if (error!=null) reportOk("No se pudo leer el archivo de activaciones\n"+"\n"+error);
        //inter.Activation_data list=inter.Activation_data.list;
        for (int i=0; i<list.size(); i++) {
            SString line=new SString(list.get(i));
            line.trim();
            String tag=line.nextStr("\t");
            if (tag.equals("ext")) {
                String code=line.nextStr("\t");
                String name=line.nextStr("\t");
                String stDate=line.nextStr("\t");
                if (SString.equal(code, agent_code)) {
                    SDate extensionDate=new SDate(stDate);
                    int days=extensionDate.getDays()-now.getDays();
                    if (days>daysActivated) daysActivated=days;
                }
            }
        }

        if ((daysActivated>45) || (daysActivated<=0)) daysActivated=0;
        if (daysActivated>0) {
            manualExtensionAuthorized=true;
            expires=new SDate(now);
            expires.addDays(daysActivated);
        }
    }
     */
//******************************************************************************
    private void getCodesForTheGroup() {
        codesGroup=null;
        ListString list=new ListString();
        String error=null;
        
        if (Constants.DEBUGGING) error=list.loadFromFile(Constants.FILENAME_ACTIVATIONS_DEBUG);
        else error=list.loadFromFile(Constants.FILENAME_ACTIVATIONS);
        
        if (error!=null) reportOk("No se pudo leer el archivo de activaciones\n"+"\n"+error);
        for (int i=0; i<list.size(); i++) {
            Activation_Data data=new Activation_Data();
            SString line=new SString(list.get(i));
            line.trim();
            String tag=line.nextStr("\t");
            if (tag.equals("key")) {
                data.code=line.nextStr("\t");
                data.name=line.nextStr("\t");
                data.model=SConvert.strToInt(line.nextStr("\t"));
                data.serial=SConvert.strToInt(line.nextStr("\t"));
                data.users=SConvert.strToInt(line.nextStr("\t"));
                data.computerid=line.nextStr("\t");
                data.options=SConvert.strToInt(line.nextStr("\t"));
                if ((";"+data.code+";").indexOf(";"+agent_code+";")>=0) {
                    codesGroup=data.code;
                }
            }
        }
    }
//******************************************************************************
    private void presentActivations() {
        out.println("<br>");
        out.println("ACTIVACIONES<br>");
        out.println("<table border='1'>");

        out.println("<tr bgcolor='#CCCCCC'>");

        out.println(COL1+"CODE"+COL2);
        out.println(COL1+"NAME"+COL2);
        out.println(COL1+"MODELO"+COL2);
        out.println(COL1+"SERIAL"+COL2);
        out.println(COL1+"USUARIOS"+COL2);
        out.println(COL1+"VERSION"+COL2);
        out.println(COL1+"VENCIMIENTO"+COL2);
        out.println(COL1+"COMPUTER_ID"+COL2);
        out.println(COL1+"OPCIONES"+COL2);
        out.println(COL1+"ACTIVACION"+COL2);

        out.println("</tr>");

        licenceDetail=new SString();

        ListString list=new ListString();

        String error=null;
        if (Constants.DEBUGGING) error=list.loadFromFile(Constants.FILENAME_ACTIVATIONS_DEBUG);
        else error=list.loadFromFile(Constants.FILENAME_ACTIVATIONS);
        
        if (error!=null) reportOk("No se pudo leer el archivo de activaciones\n"+"\n"+error);
        //inter.Activation_data list=inter.Activation_data.list;
        activationCount=0;
        for (int i=0; i<list.size(); i++) {
            Activation_Data data=new Activation_Data();
            SString line=new SString(list.get(i));
            line.trim();
            String tag=line.nextStr("\t");
            if (tag.equals("key")) {
                data.code=line.nextStr("\t");
                data.name=line.nextStr("\t");
                data.model=SConvert.strToInt(line.nextStr("\t"));
                data.serial=SConvert.strToInt(line.nextStr("\t"));
                data.users=SConvert.strToInt(line.nextStr("\t"));
                data.computerid=line.nextStr("\t");
                data.options=SConvert.strToInt(line.nextStr("\t"));
                if ((";"+data.code+";").indexOf(";"+agent_code+";")>=0) {
                    activationCount++;
                    //processKey(data);
                    String activationNumber=com.infortel.activation_library.main.Get_Key.getKey(data, version, expires);
                    presentActivation(data,version,expires,activationNumber);
                    
                    if (licenceDetail.length()>0) licenceDetail.append(";");
                    licenceDetail.append("M"+data.model+"S"+data.serial+"U"+data.users+"I"+data.computerid+"O"+data.options);
                }
            }
        }
        out.println("</table><br>");
        if (activationCount==0) out.println("No tiene licencias asignadas en el modo automatizado.<br>");
    }
//***************************************

//***************************************
    private void presentActivation(Activation_Data data, SDate version, SDate expires, String activationNumber) {
        out.println("<tr>");
        String dateFormat="yyyyMMdd";
        if (data.model==64) dateFormat="dd/MM/yyyy";
        out.println(COL1+data.code+COL2);
        out.println(COL1+data.name+COL2);
        out.println(COL1+data.model+COL2);
        out.println(COL1+data.serial+COL2);
        out.println(COL1+data.users+COL2);
        out.println(COL1+version.getString(dateFormat)+COL2);
        out.println(COL1+expires.getString(dateFormat)+COL2);
        out.println(COL1+data.computerid+COL2);
        out.println(COL1+data.options+COL2);
        out.println(COL1+activationNumber+COL2);

        out.println("</tr>");
    }
//******************************************************************************
    /*
    private int getModel(int serial) {
        if (serial<100) return 1032;
        if (serial<1000) return 64;
        if (serial>=1000) return 1002;
        return 64;
    }
    * 
    */
//**********************************************************************
    private boolean openAccountList(String agentCode) {
        try {
            qAccounts=new SDBQuery(Database.connection,null);
            String where = " from " + Database.tab(Table.asientos)+" A where ASI."
                        + Field.Asientos.NUMERO_DOCUMENTO + "=A." + Field.Asientos.NUMERO_DOCUMENTO + whereConditionAll("ASI.","A.", agentCode);
            qAccounts.sqlText.assign("SELECT \n"
                +"ASI." + Field.Asientos.NUMERO_DOCUMENTO + "\n"
                +",ASI." + Field.Asientos.CODIGO_CONTABLE + "\n"
                +",(Select min(" + Field.Asientos.FECHA + ")" + where + ") as " + Field.Asientos.FECHA + "\n"
                +",(Select sum(" + Field.Asientos.MONTO + ")" + where + ") as " + Field.Asientos.MONTO + "\n"
                +",(Select sum(" + Field.Asientos.MONTO_DENOMINACION + ")" + where + " and " + Field.Asientos.DENOMINACION + ">0) as " + Field.Asientos.MONTO_DENOMINACION + "\n"
                +",(Select max(" + Field.Asientos.VENCE + ")" + where + ") as " + Field.Asientos.VENCE + "\n"
                +",(Select max(" + Field.Asientos.DENOMINACION + ")" + where + ") as " + Field.Asientos.DENOMINACION + "\n"
                +",(Select max(" + Field.Asientos.TEXTO_REFERENCIA + ")" + where + ") as " + Field.Asientos.TEXTO_REFERENCIA + "\n"
                +",(Select max(" + Field.Asientos.CANTIDAD + ")" + where + ") as " + Field.Asientos.CANTIDAD + "\n"
                +",(Select max(" + Field.Asientos.UNIDAD + ")" + where + ") as " + Field.Asientos.UNIDAD + "\n"
                +",(Select max(" + Field.Asientos.CODIGO_COSTO + ")" + where + ") as " + Field.Asientos.CODIGO_COSTO + "\n"
                +",(Select " + Field.General.NOMBRE + " from " + Database.tab(Table.contabilidad) + " C where C." + Field.General.CODIGO + "=ASI." + Field.Asientos.CODIGO_CONTABLE + ") as " + Field.General.NOMBRE + "\n"
                +",(Select " + Field.Contabilidad.TIPO_AUXILIAR + " from " + Database.tab(Table.contabilidad) + " C where C." + Field.General.CODIGO + "=ASI." + Field.Asientos.CODIGO_CONTABLE + ") as " + Field.Contabilidad.TIPO_AUXILIAR + "\n"
                +" from " + Database.tab(Table.asientos) + " ASI, " + Database.tab(Table.contabilidad) + " CON \n"
                +" WHERE 1=1"
                +whereConditionAll(null,"ASI.", agentCode)
                +" AND ASI." + Field.Asientos.CODIGO_CONTABLE + "=CON." + Field.General.CODIGO + "\n"
                +" AND CON." + Field.Contabilidad.TIPO_AUXILIAR + "=" + Field.Contabilidad.Tipo_auxiliar.CUENTASEFECTOS + "\n"
                +" AND CON." + Field.General.CODIGO + "<>'" + EXCLUDE_THIS_LEDGER_CODE + "'\n"
                +" AND ("+"\n"
                   +"(Select abs(sum(" + Field.Asientos.MONTO + "))"
                    +" from "+ Database.tab(Table.asientos)+ " A where "
                        +" ASI."     + Field.Asientos.NUMERO_DOCUMENTO + "=A." + Field.Asientos.NUMERO_DOCUMENTO
                        +" and ASI." + Field.Asientos.CODIGO_CONTABLE + "=A." + Field.Asientos.CODIGO_CONTABLE
                        +" and ASI." + Field.Asientos.CODIGO_AGENTE + "=A." + Field.Asientos.CODIGO_AGENTE
                        +" and ASI." + Field.General.PROCESO + "=A." + Field.General.PROCESO
                    + ")>"+MARGIN_AMOUNT+"\n"
                   +" OR (Select +abs(sum(" + Field.Asientos.MONTO_DENOMINACION + "))"
                    +" from "+ Database.tab(Table.asientos)+ " A where "
                        +" ASI."     + Field.Asientos.NUMERO_DOCUMENTO + "=A." + Field.Asientos.NUMERO_DOCUMENTO
                        +" and ASI." + Field.Asientos.CODIGO_CONTABLE + "=A." + Field.Asientos.CODIGO_CONTABLE
                        +" and ASI." + Field.Asientos.CODIGO_AGENTE + "=A." + Field.Asientos.CODIGO_AGENTE
                        +" and ASI." + Field.General.PROCESO + "=A." + Field.General.PROCESO
                        +" and A."+Field.General.DENOMINACION+">0"
                    + ")>"+MARGIN_AMOUNT+"\n"
                +")"+"\n"
                +" GROUP BY ASI." + Field.Asientos.NUMERO_DOCUMENTO + ", ASI." + Field.Asientos.CODIGO_CONTABLE + "\n"
                );
            qAccounts.setOperation_ReadAny();
            qAccounts.open(true);
            //out.println(qAccounts.sqlText.str());

            return (qAccounts.openOk);
        } catch (Exception e) {
            return false;
        }
    }
//************************************
    private static String whereConditionAll(String extAlias, String alias, String agent_code) {
        SString st = new SString();
        String newLine;
        if (alias.length() == 2) newLine = "";
        else newLine = "\n";
        st.append(" AND " + alias + Field.Asientos.CODIGO_AGENTE + "='" + agent_code + "'" + newLine);
        st.append(" AND " + alias + Field.General.PROCESO + ">=" + Field.General.Proceso.EMITIDO + newLine);
        if (extAlias!=null) st.append(" AND " + alias + Field.Asientos.CODIGO_CONTABLE + "="+extAlias + Field.Asientos.CODIGO_CONTABLE+ newLine);
        return st.str();
    }
//******************************************************************************
    private void evaluateDates_start() {
        expires=new SDate(now);
        pastDueDays=0;
    }
//******************************************************************************
    private void evaluateDates_process() {
        qAccounts.beforeFirst();
        while (qAccounts.next()) {
            double amount=qAccounts.getDouble(Field.Asientos.MONTO);
            SDate accExpires=qAccounts.getDateTime(Field.Asientos.VENCE);
            if (accExpires!=null) {
                int accPastDue=now.getDays()-accExpires.getDays();
                if (amount>0.001) {
                    if (accPastDue>pastDueDays) pastDueDays=accPastDue;
                }
            }
        }

    }
//******************************************************************************
    private void evaluateDates_finish() {
        double activationMonths=0;
        if (pastDueDays/30<ACTIVATION_MAXIMUM_PASTDUE_ALLOWED_MONTHS) {
            activationMonths=Math.pow(Math.abs((pastDueDays/30.0)-ACTIVATION_MAXIMUM_PASTDUE_ALLOWED_MONTHS),ACTIVATION_POWER_FACTOR)
                *ACTIVATION_MAXIMUM_AUTHORIZATION_MONTHS
                /Math.pow(ACTIVATION_MAXIMUM_PASTDUE_ALLOWED_MONTHS, ACTIVATION_POWER_FACTOR)
                ;
        }

        if (activationMonths>ACTIVATION_MAXIMUM_AUTHORIZATION_MONTHS) activationMonths=ACTIVATION_MAXIMUM_AUTHORIZATION_MONTHS;
        if (daysActivated<0) daysActivated=0;
        daysActivated=(int)Math.round(30*activationMonths);
        expires.addDays(daysActivated);
        version=new SDate(now);
        version.addDays(-pastDueDays);
    }
//******************************************************************************
    private void presentAccountStatement(String code) {
        try {
            //Present Title
            String name="??? No se ubicó el código ???";
            SDBQuery queryName=new SDBQuery(Database.connection,null);
            queryName.assign("Getting the name", "Select nombre,fecha_extension_licencia from "+Database.tab(Table.agentes)
                    +" where codigo='"+code+"'");
            queryName.open(true);
            if (queryName.next()) {
                name=queryName.getString(1);
                if (fecha_extension_licencia==null) fecha_extension_licencia=queryName.getDateTime(2);
            }

            out.println("<br>");
            out.println("Estado de cuenta:  "+code+"  /  "+name);
            out.println("<br>Nota: No se incluyen cuentas menores a "+MARGIN_AMOUNT);
            out.println("<table border='1'>");

            out.println("<tr bgcolor='#CCCCCC'>");
            out.println(COL1+"FECHA"+COL2);
            out.println(COL1+"NUMERO"+COL2);
            out.println(COL1+"MONTO"+COL2);
            out.println(COL1+"MONTO_$"+COL2);
            out.println(COL1+"VENCE"+COL2);
            out.println(COL1+"MORA(días)"+COL2);
            out.println("</tr>");

            double total=0;
            double total_den=0;
            qAccounts.beforeFirst();
            while (qAccounts.next()) {
                out.println("<tr>");
                double amount=qAccounts.getDouble(Field.Asientos.MONTO);
                double amount_den=qAccounts.getDouble(Field.Asientos.MONTO_DENOMINACION);

                SDate accExpires=qAccounts.getDateTime(Field.Asientos.VENCE);
                if (accExpires==null) accExpires=qAccounts.getDateTime(Field.Asientos.FECHA);
                if (accExpires==null) accExpires=new SDate().setNow();

                SDate date=qAccounts.getDateTime(Field.Asientos.FECHA);
                if (date==null) date=new SDate();

                total+=amount;
                total_den+=amount_den;
                int accPastDue=now.getDays()-accExpires.getDays();
                out.println(COL1+date.getString("dd/MM/yyyy")+COL2);
                out.println(COL1+qAccounts.getString(Field.Asientos.NUMERO_DOCUMENTO)+COL2);
                out.println(COL1r+SConvert.numberToStr("#,##0.00",amount)+COL2);
                out.println(COL1r+"$"+SConvert.numberToStr("#,##0.00",amount_den)+COL2);
                out.println(COL1+accExpires.getString("dd/MM/yyyy")+COL2);
                out.println(COL1r+accPastDue+COL2);

                out.println("</tr>");
            }

            out.println("<tr bgcolor='#CCCCCC'>");
            out.println(COL1+""+COL2);
            out.println(COL1+"TOTAL"+COL2);
            out.println(COL1r+SConvert.numberToStr("#,##0.00",total)+COL2);
            out.println(COL1r+"$"+SConvert.numberToStr("#,##0.00",total_den)+COL2);
            out.println(COL1+""+COL2);
            out.println("</tr>");

            out.println("</table><br>");

            
        } catch (Exception e) {
            out.print("Error: "+SGeneral.exceptionToString(e));
        }
    }
//******************************************************************************
    private void addAudit() {
        SDBQuery ref=new SDBQuery(Database.connection,null);
        ref.sqlText.assign("select * from "+Database.tab(Table.auditoria)+" where 0=1");
        ref.open(true);

        String detail=licenceDetail.str()+" Act="+daysActivated+" Mora="+pastDueDays;

        SDBQuery query=new SDBQuery(Database.connection,null);
        query.sqlText.assign(
                "insert into "+Database.tab(Table.auditoria)
                +"("
                    +Field.Auditoria.CODIGO
                    +","+Field.Auditoria.CODIGO_AGENTE
                    +","+Field.Auditoria.CONFIRMACION
                    +","+Field.Auditoria.DETALLE
                    +","+Field.Auditoria.EQUIPO
                    +","+Field.Auditoria.FECHA_INICIO
                    +","+Field.Auditoria.FECHA_FIN
                    +","+Field.Auditoria.FECHA_VERSION
                    +","+Field.Auditoria.MODIFICACIONES
                    +","+Field.Auditoria.PANTALLA
                    +","+Field.Auditoria.PROCESO
                    +","+Field.Auditoria.REFERENCIA
                    +","+Field.Auditoria.TABLA
                    +","+Field.Auditoria.VERSION
                +")"
                +" values ("
                    /*CODIGO*/          +"null"
                    /*CODIGO_AGENTE*/   +","+query.objectToSql(null,agent_code, ref.getColumnPrecision(Field.Auditoria.CODIGO_AGENTE))
                    /*CONFIRMACION*/    +","+"null"
                    /*DETALLE*/         +","+query.objectToSql(null,detail, ref.getColumnPrecision(Field.Auditoria.DETALLE))
                    /*EQUIPO*/          +","+query.objectToSql(null,request.getRemoteAddr(), ref.getColumnPrecision(Field.Auditoria.EQUIPO))
                    /*FECHA_INICIO*/    +","+query.objectToSql(null,now)
                    /*FECHA_FIN*/       +","+query.objectToSql(null,expires)
                    /*FECHA_VERSION*/   +","+query.objectToSql(null,version)
                    /*MODIFICACIONES*/  +","+query.objectToSql(null,new SInt(0))
                    /*PANTALLA*/        +","+query.objectToSql(null,null, ref.getColumnPrecision(Field.Auditoria.PANTALLA))
                    /*PROCESO*/         +","+query.objectToSql(null,new SInt(0))
                    /*REFERENCIA*/      +","+query.objectToSql(null,null, ref.getColumnPrecision(Field.Auditoria.REFERENCIA))
                    /*TABLA*/           +","+query.objectToSql(null,null, ref.getColumnPrecision(Field.Auditoria.TABLA))
                    /*VERSION*/         +","+query.objectToSql(null,new SInt(1000))
                +")"
                );
        reportOk(query.open(false));
    }
//******************************************************************************
}
