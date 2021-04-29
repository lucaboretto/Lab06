package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	List <Rilevamento> rilevamenti;
	MeteoDAO dao;
	int costoMin;
	List<Citta> citta = new ArrayList<>();
	List<Citta> result;
	
	public Model() {
		 dao = new MeteoDAO();
		 rilevamenti = new ArrayList<Rilevamento>(dao.getAllRilevamenti());	
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		String s = dao.getUmiditaMedie(mese);
		return s;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new ArrayList<Citta>();
		costoMin = 999999999;
		
		citta.add(new Citta("Genova", dao.getAllRilevamentiLocalitaMese(mese, "Genova")));
		citta.add(new Citta("Milano", dao.getAllRilevamentiLocalitaMese(mese, "Milano")));
		citta.add(new Citta("Torino", dao.getAllRilevamentiLocalitaMese(mese, "Torino")));
	
		for(Citta c: citta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		boolean spostamento = false;
		int contaGiorniFermo = 0;
		
		ricorsione(0, parziale, 0, citta, spostamento, contaGiorniFermo);
		
		
		return result;
	}
	
	private boolean controllaCitta() {
		int cont = 0;
		for(Citta c: citta) {
			if(c.getCounter()>0)
				cont++;
		}
		if(cont == citta.size())
			return true;
		return false;
	}
	
	private void ricorsione(int livello, List<Citta> parziale, int costo, List<Citta> citta, boolean spostamento, int contaGiorniFermo){
		
		if(livello == 15) {
			if(costo < costoMin && this.controllaCitta()) {
				costoMin = costo;
				result = new ArrayList<>(parziale);
			}
			return;
		}else {
			for(Citta c: citta) {
				if(c.getCounter()<this.NUMERO_GIORNI_CITTA_MAX) {   //citta ancora disponibile
					if(contaGiorniFermo < this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
						//ricorsione da fermo
						costo += c.getRilevamenti().get(livello).getUmidita();
						contaGiorniFermo++;
						spostamento = false;
						parziale.add(c);
						c.setCounter(c.getCounter()+1);
						ricorsione(livello+1, parziale, costo, citta, spostamento, contaGiorniFermo);
					}else {
						if(parziale.size()>0) {		
							if(!(parziale.get(parziale.size()-1).equals(c))) {   //spostamento
								costo += this.COST;
								spostamento = true;
								contaGiorniFermo = 0;
							}else {
								spostamento = false;
							}
							costo += c.getRilevamenti().get(livello).getUmidita();
							contaGiorniFermo++;
							parziale.add(c);
							c.setCounter(c.getCounter()+1);
							ricorsione(livello+1, parziale, costo, citta, spostamento, contaGiorniFermo);
					}
					}
					//backtracking
						
					costo -= c.getRilevamenti().get(livello).getUmidita();	
					c.setCounter(c.getCounter()-1);
					parziale.remove(c);
					
					contaGiorniFermo--;
					if(spostamento) {
						contaGiorniFermo = 0;
						costo -= this.COST;	
						spostamento = false;
					}
					
				}			
     		}
		}
	}
}
