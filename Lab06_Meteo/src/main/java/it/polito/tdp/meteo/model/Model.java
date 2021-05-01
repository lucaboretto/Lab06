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
		
		citta.add(new Citta("Genova", dao.getAllRilevamentiLocalitaMese(mese, "Genova")));
		citta.add(new Citta("Milano", dao.getAllRilevamentiLocalitaMese(mese, "Milano")));
		citta.add(new Citta("Torino", dao.getAllRilevamentiLocalitaMese(mese, "Torino")));
	
		for(Citta c: citta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		
		boolean blocco = false;
		boolean spostamento = false;
		int contaGiorniFermo = 0;
		
		ricorsione(0, parziale, contaGiorniFermo);
		
		
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
	
	
	private int calcolaCosto(List<Citta> parziale) {
		
		int costo = 0;
		//calcola alla fine, quando cambia citta fai +100
		
		costo += citta.get(0).getRilevamenti().get(0).getUmidita();
		
		for(int i=1; i<parziale.size(); i++) {
			Citta c = parziale.get(i);
			costo += c.getRilevamenti().get(i).getUmidita();
			
			//spostamento
			if(!c.equals(parziale.get(i-1)))
				costo += COST;
		}
		return costo;
	}
	
	private boolean cittaValida(List<Citta> parziale, Citta c) {
		
		boolean disponibile = false;
		boolean valida = false;
		
		if(c.getCounter() < NUMERO_GIORNI_CITTA_MAX)
			disponibile = true;
		
		//controllo sui giorni minimi
		if(parziale.size()>3) {
			if(!c.equals(parziale.get(parziale.size()-1))) {  //se l'ultima citta è diversa da quella che voglio mettere
				Citta cit = parziale.get(parziale.size()-1);
					if(parziale.get(parziale.size()-2).equals(cit) && parziale.get(parziale.size()-3).equals(cit))
						valida = true;
			}
			else {  //la citta è uguale all'ultima che ho aggiunto
				valida = true;
			}
		}
		else
			valida = true;
		if(disponibile && valida)
			return true;
		else
			return false;
	}
	
	
	private void ricorsione(int livello, List<Citta> parziale, int contaGiorniFermo){
		// livello = giorno su cui sto lavorando (livello 0 = "giorno 0", primo giorno)
		if(livello == 15) {
			int costo = this.calcolaCosto(parziale);
			if(result == null || (costo < costoMin && this.controllaCitta())) {
				costoMin = costo;
				result = new ArrayList<>(parziale);
			}
			return;
		}else {
			for(Citta c: citta) {
				if(this.cittaValida(parziale, c)) { //citta disponibile	
					parziale.add(c);
					c.setCounter(c.getCounter()+1);
					ricorsione(livello+1, parziale, contaGiorniFermo);	
					parziale.remove(parziale.size()-1);
					c.setCounter(c.getCounter()-1);	
				}		
			}
		}		
					
	}			
					
					
					
					
				// SECONDO TENTATIVO
				
				/*	if(livello == 0) {
						//primo giorno, parziale è vuoto
						c.setCounter(c.getCounter()+1);
						parziale.add(c);
						costo += c.getRilevamenti().get(livello).getUmidita();
						contaGiorniFermo++;
						ricorsione(livello+1, parziale, costo, spostamento, contaGiorniFermo);
						parziale.remove(c);
						c.setCounter(c.getCounter()-1);
						costo -= c.getRilevamenti().get(livello).getUmidita();
						contaGiorniFermo--;
					}
					else { // non è il primo giorno, potrei spostarmi. Devo considerare entrambi casi
						if(contaGiorniFermo<3 && !(c.equals(parziale.get(livello-1))))  // non può spostarsi
							return;
						if(!c.equals(parziale.get(livello-1))) {  //spostamento, aggiungere +100 al costo
							spostamento = true;
							contaGiorniFermo = 0;
						}
						
						costo += c.getRilevamenti().get(livello).getUmidita();
						if(spostamento) {
							costo += COST;
							spostamento = false;
						}
						parziale.add(c);
						contaGiorniFermo++;
						c.setCounter(c.getCounter()+1);
						ricorsione(livello+1, parziale, costo, spostamento, contaGiorniFermo);
						parziale.remove(c);
						c.setCounter(c.getCounter()-1);
						costo -= c.getRilevamenti().get(livello).getUmidita();
						contaGiorniFermo--;
					}
					
						
				}
				
				
				
				//se citta diversa da quella di prima, resetta il contatore dei giorni fermo, aumenta il costo
				
				//entra ancora
				//rimuovi
			}
		}*/
		
		
		//PRIMO TENTATIVO
				
		/*if(livello == 15) {
			if(costo < costoMin && this.controllaCitta()) {
				costoMin = costo;
				result = new ArrayList<>(parziale);
			}
			return;
		}else {
			for(Citta c: citta) {
				if(c.getCounter()<this.NUMERO_GIORNI_CITTA_MAX) {   //citta ancora disponibile
					if(contaGiorniFermo <= this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
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
		}*/
		
	
}
