package com.example.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/*
 *  This class includes all methods for working with modems.
	The methods relate to the return of temperature from the modem as well as the power rx and tx.
	Modifying the modem, deleting modems, adding a profile to the modem, and any other operations needed to play the modem in operation.
	Calculating an index modem through which the modem accesses and personalizes the modem.
 */

public class MetodeRaisecom {
	public Modem onu;
	public ArrayList<Modem> listaModema=new ArrayList<>();
	public ArrayList<Modem> listaLegalizovanihModema=new ArrayList<Modem>();
	public ArrayList<RaisecomModem> lista=new ArrayList<RaisecomModem>();
	
	public static void main(String[] args) throws JSchException, IOException, DecoderException {
		
	  
	MetodeRaisecom metode=new MetodeRaisecom();
		
		
	
		String indexRestart="10100025";
		System.out.println(indexRestart.substring(0, 3));
		System.out.println(indexRestart.substring(6,8));
		indexRestart=indexRestart.substring(0, 3)+indexRestart.substring(6,8)+"001";
		System.out.println(indexRestart);
}
	
	
	

	
	
	public float getRxPower(float power)
	{
		float rx=0;
		if (0<=power && power<=32767)
		{
			rx=(power-15000)/500;
		}
		else if (32769<=power && power<=65535)
		{
		rx=(power-65536-15000)/500;
		}
		return rx;
	}
	
	public float getTxPower(float power)
	{
		float tx=0;
		if (0<=power && power<=32767)
		{
			tx=(power-15000)/500;
		}
		else if (32769<=power && power<=65535)
		{
		tx=(power-65536-15000)/500;
		}
		return tx;
	}
	
	public float getTemp(float temp)
	{
		
		return temp/256;
	}
	
	
	public  int  restartIndex (String port)
	{
		
		
		int index;
		System.out.println(port.substring(0, port.indexOf("/")));
		int prviBroj=Integer.parseInt(port.substring(0, port.indexOf("/")));
		int prvaKosa=port.indexOf("/");
		System.out.println(port.substring(prvaKosa+1,port.indexOf("/", prvaKosa+1)));
		int drugiBroj=Integer.parseInt(port.substring(prvaKosa+1,port.indexOf("/", prvaKosa+1)));
		int drugaKosa=port.indexOf("/", prvaKosa+1);
		//System.out.println(drugaKosa);
		System.out.println(port.substring(drugaKosa+1,port.length()));
		int treciBroj=Integer.parseInt(port.substring(drugaKosa+1,port.length()));
		
		System.out.println(prviBroj*10000000+drugiBroj*100000+treciBroj);
		index=prviBroj*10000000+drugiBroj*100000+treciBroj;
		return index;
	}
	
	public  String modemIndex (String port)
	{
		
		
	
		System.out.println(port.substring(0, port.indexOf("/")));
		int prviBroj=Integer.parseInt(port.substring(0, port.indexOf("/")));
		int prvaKosa=port.indexOf("/");
		System.out.println(port.substring(prvaKosa+1,port.indexOf("/", prvaKosa+1)));
		int drugiBroj=Integer.parseInt(port.substring(prvaKosa+1,port.indexOf("/", prvaKosa+1)));
		int drugaKosa=port.indexOf("/", prvaKosa+1);
		//System.out.println(drugaKosa);
		System.out.println(port.substring(drugaKosa+1,port.length()));
		int treciBroj=Integer.parseInt(port.substring(drugaKosa+1,port.length()));
		
		System.out.println(prviBroj*8388608+drugiBroj*65536+treciBroj+268435456);
		String index=Integer.toString(prviBroj*8388608+drugiBroj*65536+treciBroj+268435456);
		return index;
	}
	
	
	
	public  ArrayList<Modem> nelegalizovaniModemi(String olt,String nazivOlta) throws JSchException, IOException, DecoderException
	{
	
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("password", "192.168.0.125", 22);
		 session.setPassword("#password#ISP!");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.2.2.1.1";
	
	 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		
		
		 channel.connect();
	//	 HashMap<String,String> hm=new HashMap<String,String>();  
		 String msg="";
		 int i=0;
		 String index;
		 String ponID="";
		 String sn="";
			String isjeceno = "";
			String prefix="";
		 while((msg=in.readLine())!=null)
		 {
			 i++;
			 	try {
			 		index=msg.substring(oid.length()+2, msg.indexOf(".", oid.length()+2));
					
					
				     switch (index) {
				         case "65":
				        	 ponID = "1/1";
				             break;
				         case "66":
				        	 ponID = "1/2";
				             break;
				         case "67":
				        	 ponID = "1/3";
				             break;
				         case "68":
				        	 ponID = "1/4";
				             break;
				         case "193":
				        	 ponID = "3/1";
				             break;
				         case "194":
				        	 ponID = "3/2";
				             break;
				         case "195":
				        	 ponID = "3/3";
				             break;
				         case "196":
				        	 ponID = "3/4";
				             break;
				         default:
				             throw new IllegalArgumentException("GRESKA " );
				     }
				     
				} catch (Exception e) {
					// TODO: handle exception
				}
			
				     
				     
			 if (msg.lastIndexOf(":")>0)
			 {
				 
				if(msg.substring(msg.lastIndexOf(":")+2).length()==48)
				{
					
			    byte[] bytes = Hex.decodeHex(msg.substring(msg.lastIndexOf(":")+2).replaceAll(" ", "").toCharArray());
			    
			 
					  onu=new Modem();
					  System.out.println(new String(bytes, "UTF-8"));
					  onu.setNazivOlta(nazivOlta);
						 onu.setAdresaOlta(olt);
					  onu.setSnModema(new String(bytes, "UTF-8").trim());
					  onu.setPonID(ponID);
					  onu.setRedniBroj(listaModema.size()+1);
					listaModema.add(onu);
				}else
				{
			
				
				
				
				isjeceno=msg.substring(msg.lastIndexOf(":")+2).replaceAll(" ", "");
				
				sn=isjeceno.substring(1, isjeceno.length()-1);
				 byte[] bytes = Hex.decodeHex(sn.substring(0, 8).toCharArray());
				 prefix=new String(bytes, "UTF-8");
				 onu=new Modem();
				 onu.setNazivOlta(nazivOlta);
				 onu.setAdresaOlta(olt);
				 onu.setSnModema(sn.trim());
				 onu.setPonID(ponID);
				 onu.setRedniBroj(listaModema.size()+1);
				listaModema.add(onu);
				
				}
			
			 
		 }
		 
		 }
		 channel.disconnect();
		 session.disconnect();
		 return listaModema;
		
	}
	
	public  ArrayList<String> nelegalizaniPORT(String olt,String nazivOlta) throws JSchException, IOException, DecoderException
	{
	
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("password", "192.168.0.125", 22);
		 session.setPassword("#password#ISP!");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		ArrayList<String> listaIndexa=new ArrayList<String>();
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.2.2.1.1";
	
	//	 System.out.println("duzina je "+duzina.length());
	//	 channel.setCommand("snmpset -v 2c -c private 192.168.0.103 "+oid);
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		
		
		 channel.connect();
	//	 HashMap<String,String> hm=new HashMap<String,String>();  
		 String msg=null;
		 int i=0;

		   String ponID = null;
		   String index;
		 String port="";
		 while((msg=in.readLine())!=null)
		 {
			 System.out.println(msg);
			System.out.println(msg.substring(oid.length()+2, msg.indexOf(".", oid.length()+2)));
			index=msg.substring(oid.length()+2, msg.indexOf(".", oid.length()+2));
			
			
			     switch (index) {
			         case "65":
			        	 ponID = "1/1";
			             break;
			         case "66":
			        	 ponID = "1/2";
			             break;
			         case "67":
			        	 ponID = "1/3";
			             break;
			         case "68":
			        	 ponID = "1/4";
			             break;
			         case "193":
			        	 ponID = "3/1";
			             break;
			         case "194":
			        	 ponID = "3/2";
			             break;
			         case "195":
			        	 ponID = "3/3";
			             break;
			         case "196":
			        	 ponID = "3/4";
			             break;
			         default:
			             throw new IllegalArgumentException("GRESKA " );
			     }
			
		 }
		 
		
		 channel.disconnect();
		 session.disconnect();
		listaIndexa.add(ponID);
		
		return listaIndexa;
	}
	
	
	//Pronalazenje kljuca u hashMapi preko vrijednosti 
	/*
	 * 	Map<Integer, String> testMap = new HashMap<Integer, String>();
    testMap.put(10, "a");
    testMap.put(20, "b");
    testMap.put(30, "c");
    testMap.put(40, "d");
    for (Entry<Integer, String> entry : testMap.entrySet()) {
        if (entry.getValue().equals("c")) {
            System.out.println(entry.getKey());
        }
    }
	 */
	
	
	
	
	public  void legalizovaniModemi(String olt,String nazivOlta) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.2";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			
			 i++;
			 String IndexModem;
			 String snModem;
			 IndexModem=(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.2.", "").replaceAll("STRING:",""));
		 
			 snModem=IndexModem.substring(0, IndexModem.indexOf(" ")).trim();
			if (snModem.length()>2)
			{

			  onu=new Modem();
			  onu.setRedniBroj(listaLegalizovanihModema.size()+1);
			  onu.setAdresaOlta(olt);
			  onu.setIndexModema(IndexModem.substring(0, IndexModem.indexOf(" ")));
			  onu.setSnModema(IndexModem.substring(IndexModem.indexOf("\"")+1,IndexModem.length()-1));
			  onu.setNazivOlta(nazivOlta);
			  listaLegalizovanihModema.add(onu);
			}
		 }
		 channel.disconnect();
		 System.out.println("Broj modema na oltu je ==> "+i);
		 session.disconnect();

		
	}
	
	
	
	
	public ArrayList<ModemDetalji> getDetalji ()
	{
		ArrayList<ModemDetalji> listaDetalja=new ArrayList<ModemDetalji>();
		
		
		return listaDetalja;
	}
	
	
	public static void   prikazSvihLineProfila(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.6";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			//System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.6.", "").replaceAll("INTEGER:",""));
			i++;
			 String IndexModem;
			 String snModem;
			// System.out.println((msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.2.", "").replaceAll("STRING:","")));
			 IndexModem=(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.6.", "").replaceAll("INTEGER:",""));
			// System.out.println("===== "+IndexModem);
			System.out.println("INDEX MODEMA JE "+IndexModem.substring(0, IndexModem.indexOf(" ")));
	 System.out.println("LINE JE = "+IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim());
			 
			 
			 
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	public String  prikazRX(String olt,String indexRestart) throws JSchException, IOException, DecoderException
	{
		
	//	System.out.println("Poslali ste ============= "+olt+indexRestart);
		MetodeRaisecom metode=new MetodeRaisecom();
		 String IndexModem=null;
		 String snModem;
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.6.3.1.1.16";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid+"."+indexRestart);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 String RX;
		 try {
			
		
		// System.out.println("OID========= "+"snmpwalk -v 2c -c private "+olt +" "+oid+"."+indexRestart);
		 while((msg=in.readLine())!=null)
		 { 
			//System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.6.", "").replaceAll("INTEGER:",""));
			i++;
			
			// System.out.println((msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.2.", "").replaceAll("STRING:","")));
			 IndexModem=(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.3.1.1.16.", "").replaceAll("INTEGER:",""));
	 
			 
		 }
		 try {
			 RX=Float.toString(metode.getRxPower(Float.parseFloat(IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim())));

		} catch (Exception e) {
			RX="NEMA SNAGE";
		}
		 channel.disconnect();
		 session.disconnect();
		 } catch (Exception e) {
				System.out.println();
				RX="NEMA SNAGE";
			}
		// return Float.toString(metode.getRxPower(Float.parseFloat(IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim())));
		 	return RX;
	}
	
	public String  prikazTX(String olt,String indexRestart) throws JSchException, IOException, DecoderException
	{
		MetodeRaisecom metode=new MetodeRaisecom();
		 String IndexModem=null;
		 String snModem;
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.6.3.1.1.17";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid+"."+indexRestart);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 String TX;
		 try {
			
		
		 while((msg=in.readLine())!=null)
		 { 
			//System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.6.", "").replaceAll("INTEGER:",""));
			i++;
	 IndexModem=(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.3.1.1.17.", "").replaceAll("INTEGER:",""));
			// System.out.println("===== "+IndexModem);
			System.out.println("INDEX MODEMA JE "+IndexModem.substring(0, IndexModem.indexOf(" ")));
	 System.out.println("TX JE = "+IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim());
			// System.out.println("TX FROM TX METOD "+metode.getTxPower(Float.parseFloat(IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim())));
			 
			 
		 }try {
			 TX=Float.toString(metode.getTxPower(Float.parseFloat(IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim())));

		} catch (Exception e) {
			TX="NEMA SNAGA";
		}
		 channel.disconnect();
		 session.disconnect();
		 } catch (Exception e) {
				// TODO: handle exception
			 e.printStackTrace();
			 TX="NEMA SNAGE";
			}
	return TX;
	}
	
	public String   prikazTemperatura(String olt,String indexRestart) throws JSchException, IOException, DecoderException
	{
		MetodeRaisecom metode=new MetodeRaisecom();
		 String IndexModem=null;
		 String snModem;
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.6.3.1.1.18";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid+"."+indexRestart);
		 channel.connect();
		 String msg=null;
		 String Temp = null;
		 int i=0;
		 try {
			
		
		 while((msg=in.readLine())!=null)
		 { 
			i++;
			
			 IndexModem=(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.3.1.1.18.", "").replaceAll("INTEGER:",""));
			// System.out.println("===== "+IndexModem);
			System.out.println("INDEX MODEMA JE "+IndexModem.substring(0, IndexModem.indexOf(" ")));
		 System.out.println("Temperatura JE = "+IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim());
		//	 System.out.println("Temperatura FROM Temperatura METOD "+metode.getTemp(Float.parseFloat(IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim())));
			 try {
				Temp=Float.toString(metode.getTemp(Float.parseFloat(IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim())));

			} catch (Exception e) {
				Temp="Nema temperature";
			}
			 
		 }
		 channel.disconnect();
		 session.disconnect();
		 } catch (Exception e) {
				Temp="Nema temperature";
			}
		// return Float.toString(metode.getTemp(Float.parseFloat(IndexModem.substring(IndexModem.indexOf("= ")+1,IndexModem.length()).trim())));
		return Temp;
	}
	
	
	public  void prikazSvihServiceProfila(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.8";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.8.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	
	
	public  String prikazSvihStatusStanjaModema(String olt,String index) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		 String resenje=null;
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.17";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid+"."+index);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			 
			 resenje=msg.substring(msg.indexOf(":")+1).trim();
			//System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.17.", "").replaceAll("INTEGER:",""));
			if (resenje.equals("1"))
			{
				resenje=("ONLINE");
			}else if (resenje.equals("2"))
			{
				resenje=("PENNDING");
			}
			else
			{
				resenje=("OFFLINE");
			}
		 }
		 channel.disconnect();
		 session.disconnect();
		
		 return resenje;
	}
	
	
	public  String prikazSvihStatusStanjaOperacijeModema(String olt,String index) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		 String resenje=null;
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.18";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid+"."+index);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			 
			 resenje=msg.substring(msg.indexOf(":")+1).trim();
			//System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.18.", "").replaceAll("INTEGER:",""));
			
			if (resenje.equals("1"))
			{
				resenje=("Aktivan servis");
			}else
			{
				resenje=("Servis nije aktivan");
			}
			
		 }
		 channel.disconnect();
		 session.disconnect();
		 return resenje;
		
	}
	
	public  void prikazSvihStatusStanjaRestartModema(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.6.1.1.1.23";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.23.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	public  void resetRaisecom(String olt,String index) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.6.1.1.1.23";
		 channel.setCommand("snmpset -v 2c -c private "+olt +" "+oid+"."+index+" i 1");
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.23.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	public  void prikazNazivaLineProfila(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.7.2.1.1.2";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.7.2.1.1.2.", "").replaceAll("STRING:",""));
			System.out.println("ID JE "+msg.replaceAll("iso.3.6.1.4.1.8886.18.3.7.2.1.1.2.", "").replaceAll("STRING:","").substring(0, 1));
			System.out.println("SN JE "+msg.replaceAll("iso.3.6.1.4.1.8886.18.3.7.2.1.1.2.", "").replaceAll("STRING:","").substring(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.7.2.1.1.2.", "").replaceAll("STRING:","").indexOf("\""), msg.replaceAll("iso.3.6.1.4.1.8886.18.3.7.2.1.1.2.", "").replaceAll("STRING:","").length()));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	
	
	public  void prikazNazivServiceProfila(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.7.4.1.1.2";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		
		 channel.connect();
		 String msg=null;
		 int i=0;
		 if (in.readLine()==null)
		 {
			 
			 System.out.println("OLT NE ODGOVARA");
			 System.out.println("Adresa je OLT-A "+olt);
		 }else
		 {
		 while((msg=in.readLine())!=null)
		 { 
				System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.7.4.1.1.2.", "").replaceAll("STRING:",""));
		
		 }
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	
	
	
	public  void prikazIndexaZaRestartModema(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.6.1.1.1.4";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	public  void prikazSuspendStatusaSvihModema(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.18";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.18.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	public  void brisanjeModema(String olt,String index) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.19";
		 channel.setCommand("snmpset -v 2c -c private "+olt +" "+oid+"."+index+" i 6");
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	public  void saveConf(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.1.2.1.1.0";
		 channel.setCommand("snmpset -v 2c -c private "+olt +" i 2");
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println("SINHRONIZACIJA OLT-A -----> ");
			System.out.println(msg.replaceAll("iso.33.6.1.4.1.8886.1.2.1.1.0", "").replaceAll("INTEGER:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	public  void Su(String olt) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.1.2.1.1.0";
		 channel.setCommand("snmpset -v 2c -c private "+olt +" i 2");
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println("SINHRONIZACIJA OLT-A -----> ");
			System.out.println(msg.replaceAll("iso.33.6.1.4.1.8886.1.2.1.1.0", "").replaceAll("INTEGER:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	
	public  void restartModema(String olt,String indexRestart) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.6.1.1.1.23";
		 channel.setCommand("snmpset -v 2c -c private "+olt +" "+oid+"."+indexRestart+" i 1");
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	public  void SuspendModemaON(String olt,String index) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.18";
		 channel.setCommand("snmpset -v 2c -c private "+olt +" "+oid+"."+index+" i 2");
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.18.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	public  void SuspendModemaOFF(String olt,String index) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.18";
		 channel.setCommand("snmpset -v 2c -c private "+olt +" "+oid+"."+index+" i 1");
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.18.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	public  void StatusSuspendModema(String olt,String index) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.18";
		 channel.setCommand("snmpwalk -v 2c -c private "+olt +" "+oid+"."+index);
		 channel.connect();
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null)
		 { 
			 
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.1.3.1.1.18.", "").replaceAll("STRING:",""));
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	public  void kreiranjeModema(String olt,String index,String hexSN,int lineProfil,int serviceProfil) throws JSchException, IOException, DecoderException
	{
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 String oid=".1.3.6.1.4.1.8886.18.3.1.3.1.1.2";
		 String lineprofilOID="1.3.6.1.4.1.8886.18.3.1.3.1.1.6";
		 String serviceprofilOID="1.3.6.1.4.1.8886.18.3.1.3.1.1.8";
		 String createOID="1.3.6.1.4.1.8886.18.3.1.3.1.1.19";
		System.out.println("snmpset -v 2c -c private "+olt +" "+oid+"."+index+" s "+hexSN+" "+lineprofilOID+"."+index+" i "+lineProfil+" "+serviceprofilOID+"."+index+" i "+serviceProfil+" "+createOID+"."+index+" i 4");
		 channel.setCommand("snmpset -v 2c -c private "+olt +" "+oid+"."+index+" s "+hexSN+" "+lineprofilOID+"."+index+" i "+lineProfil+" "+serviceprofilOID+"."+index+" i "+serviceProfil+" "+createOID+"."+index+" i 4");
		 channel.connect();
	//	 String poruka="snmpset -v 2c -c private "+olt +" "+oid+"."+index+" x "+hexSN+" "+lineprofilOID+"."+index+" i "+lineProfil+" "+serviceprofilOID+"."+index+" i "+serviceProfil+" "+createOID+"."+index+" i 4";
		// System.out.println(poruka);
		 String msg=null;
		 int i=0;
		 String [] niz=new String[4];
		 String [] nizReplace=new String[4];
		 nizReplace[0]="iso.3.6.1.4.1.8886.18.3.1.3.1.1.2.293732356 = STRING: ";
		 nizReplace[1]="iso.3.6.1.4.1.8886.18.3.1.3.1.1.6.293732356 = INTEGER: ";
		 nizReplace[2]="iso.3.6.1.4.1.8886.18.3.1.3.1.1.8.293732356 = INTEGER: ";
		 nizReplace[3]="iso.3.6.1.4.1.8886.18.3.1.3.1.1.19.293732356 = INTEGER: ";
		 try {
			 while((msg=in.readLine())!=null)
			 { 
				//System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").replaceAll("STRING:",""));
				niz[i]=msg;
				i++;
			 }
			 int j=0;
			 for (String x: niz)
			 {
				 System.out.println("Poruka je "+j+" "+x.replace(nizReplace[j], ""));
				 j++;
			 }
		} catch (Exception e) {
			System.out.println("Greska!");
		}
		
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	
	
	public static void snmpKomande(String oid) throws JSchException, IOException, DecoderException
	{
		
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 
		 String duzina="iso."+oid;
	//	 System.out.println("duzina je "+duzina.length());
	//	 channel.setCommand("snmpset -v 2c -c private 192.168.0.103 "+oid);
		 channel.setCommand("snmpwalk -v 2c -c private 192.168.0.103 "+oid);
		
		
		 channel.connect();
		
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null){

			 
			 if (msg.lastIndexOf(":")>0)
			 {

				if(msg.substring(msg.lastIndexOf(":")+2).length()==48)
				{
					
			    byte[] bytes = Hex.decodeHex(msg.substring(msg.lastIndexOf(":")+2).replaceAll(" ", "").toCharArray());
			    
			      System.out.println("SN U HEX: "+msg.substring(msg.lastIndexOf(":")+2).replaceAll(" ", "").substring(0, msg.substring(msg.lastIndexOf(":")+2).replaceAll(" ", "").length()-8));
					  System.out.println("SN modema: "+new String(bytes, "UTF-8"));
					  System.out.println("Indeks modema "+i);
					  if (i==1)
					  {
						//	snmpSetKomande(".1.3.6.1.4.1.8886.18.3.1.3.1.1.2.277086230 x '"+msg.substring(msg.lastIndexOf(":")+2).replaceAll(" ", "").substring(0, msg.substring(msg.lastIndexOf(":")+2).replaceAll(" ", "").length()-8)+"' 1.3.6.1.4.1.8886.18.3.1.3.1.1.6.277086230 i 3 1.3.6.1.4.1.8886.18.3.1.3.1.1.8.277086230 i 3 1.3.6.1.4.1.8886.18.3.1.3.1.1.19.277086230 i 4");

					  }
				}
			 }
			 else
				 
			 {
				 System.out.println(msg.substring(msg.lastIndexOf("=")+2));
			 }
			 i++;
		String delims = "[ ]+";

		
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	public static void snmpCommand(String olt,String oid) throws JSchException, IOException, DecoderException
	{
		MetodeRaisecom metoda=new MetodeRaisecom();
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 
		 String duzina="iso."+oid;
	//	 System.out.println("duzina je "+duzina.length());
	//	 channel.setCommand("snmpset -v 2c -c private 192.168.0.103 "+oid);
		 channel.setCommand("snmpwalk -v 2c -c private "+olt+" "+oid);
		
		
		 channel.connect();
		
		 String msg=null;
		 int i=0;
		 int prvi;
		 int drugi;
		 int treci;
		 String sn;
		 while((msg=in.readLine())!=null){
			 System.out.println("OLT---->"+olt);

			System.out.println("restart index "+msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0,8).toString());
			System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").toString().replaceAll("STRING:","").substring(13,25).toString());
		//	System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0, 1).toString() +" "+msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(2, 3).toString());
			sn=msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").toString().replaceAll("STRING:","").substring(13,25).toString();
			prvi=Integer.parseInt(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0, 1).toString());
			drugi=Integer.parseInt(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(2, 3).toString());
			treci=Integer.parseInt(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0, 8).toString())-(prvi*10000000+drugi*100000);
			
			System.out.println(prvi+"/"+drugi+"/"+treci + " SN =>"+sn);
			String port=prvi+"/"+drugi+"/"+treci;
			System.out.println("Index modema "+metoda.modemIndex(port));
		
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	
	public ArrayList<RaisecomModem> ListaModema(String imeOlta,String olt,String oid) throws JSchException, IOException, DecoderException
	{
		 MetodeRaisecom metoda=new MetodeRaisecom();
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 
		 String duzina="iso."+oid;
		 channel.setCommand("snmpwalk -v 2c -c private "+olt+" "+oid);
		
		
		 channel.connect();
		
		 String msg=null;
		 int i=0;
		 int prvi;
		 int drugi;
		 int treci;
		 String sn;
		 while((msg=in.readLine())!=null){
			// System.out.println("OLT---->"+olt);
			 RaisecomModem modem=new RaisecomModem();
			 String indexRestart=msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0,8).toString();
		//	System.out.println("index Restart "+msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").toString().substring(8));
		//	System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").toString().replaceAll("STRING:","").substring(13,25).toString());
		//	System.out.println(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0, 1).toString() +" "+msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(2, 3).toString());
			sn=msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").toString().replaceAll("STRING:","").substring(13,25).toString();
			prvi=Integer.parseInt(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0, 1).toString());
			drugi=Integer.parseInt(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(2, 3).toString());
			treci=Integer.parseInt(msg.replaceAll("iso.3.6.1.4.1.8886.18.3.6.1.1.1.4.", "").substring(0, 8).toString())-(prvi*10000000+drugi*100000);
			

			String port=prvi+"/"+drugi+"/"+treci;
	
			String indexModema=metoda.modemIndex(port);
			modem.setImeOlta(imeOlta);
			modem.setAdresaOlta(olt);
			modem.setIndexModema(indexModema);
			modem.setIndexRestart(indexRestart);
			modem.setSlotID(String.valueOf(prvi));
			modem.setPonID(String.valueOf(drugi));
			modem.setOnuID(String.valueOf(treci));
			modem.setSnModemi(sn);
			modem.setTipModema("2");
			
			lista.add(modem);
		
		 }
		 channel.disconnect();
		 session.disconnect();
		return lista;
	}
	
	
	public static void snmpSetKomande(String oid) throws JSchException, IOException, DecoderException
	{
		
		 JSch jsch=new JSch();
		 Session session=jsch.getSession("user", "192.168.0.125", 22);
		 session.setPassword("password");
		 Properties config = new Properties();
		 config.put("StrictHostKeyChecking", "no");
		 session.setConfig(config);
		 session.connect();
		
		 ChannelExec channel=(ChannelExec) session.openChannel("exec");
		 BufferedReader in=new BufferedReader(new
		 InputStreamReader(channel.getInputStream()));
		 
		 String duzina="iso."+oid;
	
		 channel.setCommand("snmpset -v 2c -c private 192.168.0.103 "+oid);
		
		
		 channel.connect();
		
		 String msg=null;
		 int i=0;
		 while((msg=in.readLine())!=null){

			 System.out.println(msg);
			 if (msg.lastIndexOf(":")>0)
			 {


				 System.out.println(msg.substring(msg.lastIndexOf(":")+2));
			 }
			 else
				 
			 {
				 System.out.println(msg.substring(msg.lastIndexOf("=")+2));
			 }
			 i++;
		String delims = "[ ]+";
		

		
		 }
		 channel.disconnect();
		 session.disconnect();
		
	}
	
	
	public static void MetodaWalk(String Metoda,String Community,String Olt,String OID,String kod)
	{
		System.out.println("snmp"+Metoda+" -v 2c -c "+Community+" "+Olt+ " ."+OID+"."+kod);
	}
	
	public static void MetodaSet(String metoda,String community,String olt,String oid,String kod,String type,String value)
	{
		System.out.println("snmp"+metoda+" -v 2c -c "+community+" "+olt+ " ."+oid+"."+kod+ " "+type+ " "+value);
	}
	public ArrayList<Modem> getListaLegalizovanihModema ()
	{
		return listaLegalizovanihModema;
	}
	
	
	

}
