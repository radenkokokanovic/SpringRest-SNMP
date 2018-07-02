package com.example.model;


//Class for Mapping modem POJO
public class Modem implements Cloneable{


	String nazivOlta;
	String snModema;
	String ponID;
	String adresaOlta;
	int redniBroj;
	String indexModema;
	String filter;
	
	
	
	public String getFilter() {
		return getNazivOlta()+getAdresaOlta()+getPonID()+getIndexModema()+getSnModema()+String.valueOf(getRedniBroj());
	}





	public String getIndexModema() {
		return indexModema;
	}


	public void setIndexModema(String indexModema) {
		this.indexModema = indexModema;
	}


	public int getRedniBroj() {
		return redniBroj;
	}


	public void setRedniBroj(int redniBroj) {
		this.redniBroj = redniBroj;
	}


	public String getAdresaOlta() {
		return adresaOlta;
	}


	public void setAdresaOlta(String adresaOlta) {
		this.adresaOlta = adresaOlta;
	}


	public String getPonID() {
		return ponID;
	}


	public void setPonID(String ponID) {
		this.ponID = ponID;
	}


	public Modem() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getNazivOlta() {
		return nazivOlta;
	}
	public void setNazivOlta(String nazivOlta) {
		this.nazivOlta = nazivOlta;
	}
	public String getSnModema() {
		return snModema;
	}
	public void setSnModema(String snModema) {
		this.snModema = snModema;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

