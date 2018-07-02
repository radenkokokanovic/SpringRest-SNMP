package com.example.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.springframework.stereotype.Service;

import com.example.model.MetodeRaisecom;
import com.jcraft.jsch.JSchException;


//Main servise to work with modems through OID-s SNMP2
@Service
public class UpitiDB {
	public static String iniFile = "C:\\Users\\Radenko\\Desktop\\podaciOKonekcijiNaBaze.ini";
	
	
	
public static void selectURLRaisecom() throws SQLException, ClassNotFoundException, InvalidFileFormatException, IOException, JSchException, DecoderException {
		
		Statement stmt;
	
		Ini ini = new Ini(new File(iniFile));
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(ini.get("ermines", "ermines.url"),
				ini.get("ermines", "ermines.user"), ini.get("ermines", "ermines.password"));

		stmt = conn.createStatement();
		String sql;

		ResultSet rs;
		sql = "select adresaolta,indexrestart\r\n" + 
				"from public.onu_podaci where sn in (\r\n" + 
				"select sn\r\n" + 
				"from public.onu_podaci\r\n" + 
				"where (onu_podaci.imeolta='Trnjaci01' or onu_podaci.imeolta='Trnjaci02' or onu_podaci.imeolta='Trnjaci03') and onu_podaci.status_konf!=12)";
		rs = stmt.executeQuery(sql);
		Map<String, String> map = new HashMap<String, String>();
		while (rs.next()) {
			MetodeRaisecom metode=new MetodeRaisecom();
			
			metode.restartModema(rs.getString("adresaolta"), rs.getString("indexrestart"));
			
		//	System.out.println(rs.getString("adresaolta")+" "+rs.getString("indexrestart"));
			
		//	map.put(rs.getString("adresaolta"), rs.getString("indexrestart"));
		}

		conn.close();
		stmt.close();
		rs.close();
		

	}

public  String prikazSvihSnagaRx () throws JSchException, IOException, DecoderException, ClassNotFoundException, SQLException
{
	String indexRestart = null;
	String indexRx=null;
	MetodeRaisecom metode=new MetodeRaisecom();
	String imeOltaRaisecom = "192.168.0.141";
	



	Statement stmt;
	
	Ini ini = new Ini(new File(iniFile));
	Class.forName("org.postgresql.Driver");
	Connection conn = DriverManager.getConnection(ini.get("ermines", "ermines.url"),
			ini.get("ermines", "ermines.user"), ini.get("ermines", "ermines.password"));

	stmt = conn.createStatement();
	String sql;

	ResultSet rs;
	sql = "select concat(onu_podaci.slotid,'/',onu_podaci.ponid,'/',onu_podaci.onuid) as pon,onu_podaci.indexrestart\r\n" + 
			"from public.onu_podaci\r\n" + 
			"where onu_podaci.imeolta='Crnjelovo02'\r\n" + 
			"order by pon";
	rs = stmt.executeQuery(sql);
	Map<String, String> map = new HashMap<String, String>();
	String bf="";
	while (rs.next()) {
		
		
	//	metode.restartModema(rs.getString("adresaolta"), rs.getString("indexrestart"));
		indexRestart=rs.getString("indexrestart");
		indexRx = indexRestart.substring(0, 3) + indexRestart.substring(6, 8) + "001";

		
		System.out.println(rs.getString("pon")+"  "+metode.prikazRX(imeOltaRaisecom.trim(), indexRx.trim()));
		bf=bf+rs.getString("pon")+"  "+metode.prikazRX(imeOltaRaisecom.trim(), indexRx.trim());
	//	System.out.println(rs.getString("adresaolta")+" "+rs.getString("indexrestart"));
		
	//	map.put(rs.getString("adresaolta"), rs.getString("indexrestart"));
	}
	
	conn.close();
	stmt.close();
	rs.close();
	return bf;
}

}
