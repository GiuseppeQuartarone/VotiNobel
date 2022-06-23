package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private Set<Esame> migliore; //ci serve il set perchè l'ordine non conta, creiamo questo set per confrontare le migliori
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();		
	}


	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		//ripristino soluzione migliore
		migliore = new HashSet<>();
		mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<>();
		//cerca\(parziale,0,m);
		cerca2(parziale,0,m);
		
		return migliore;	
	}
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//Controllare i casi terminali
				int sommaCrediti = sommaCrediti(parziale);
				if(sommaCrediti > m) // soluzione non valida
					return;
			    if (sommaCrediti == m) {
			    	//soluzione valida! controlliamo se è la migliore (fin qui)
			    	double mediaVoti = calcolaMedia(parziale);
			    	if(mediaVoti > mediaMigliore) {
			    	//migliore = parziale; così copiamo il riferimento, non va bene, perchè dobbiamo farne una copia
			    	migliore = new HashSet<>(parziale);
			    	mediaMigliore = mediaVoti;
			    	}
			    	return;
			    }
			
			//sicuramente, crediti < m
			if(L==esami.size()) //fine di un ramo
				return;
			
			//provo ad aggiungere esami[L]
			parziale.add(esami.get(L)); //con i set non avremmo potuto farlo
			cerca2(parziale, L+1, m);
			
			//provo a "non aggiungere" esami[L]
			parziale.remove(esami.get(L));
			cerca2(parziale,L+1,m); 
			//usando il livello come indice nella lista di esami di partenza, scorro questi esami in ordine
		
	}


	/*
	 * COMPLESSITA' N! perché esploriamo tutti i casi, ed è più grande di un'esponenziale
	 */
	

	//il pattern è molto simile in tutti gli esercizi
	private void cerca(Set<Esame> parziale, int L, int m) { //funzione ricorsiva
		//Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > m) // soluzione non valida
			return;
	    if (sommaCrediti == m) {
	    	//soluzione valida! controlliamo se è la migliore (fin qui)
	    	double mediaVoti = calcolaMedia(parziale);
	    	if(mediaVoti > mediaMigliore) {
	    	//migliore = parziale; così copiamo il riferimento, non va bene, perchè dobbiamo farne una copia
	    	migliore = new HashSet<>(parziale);
	    	mediaMigliore = mediaVoti;
	    	}
	    	return;
	    }
	
	//sicuramente, crediti < m
	if(L==esami.size()) //fine di un ramo
		return;
	
	//generiamo i sotto-problemi
	for(Esame e : esami) {
		if(!parziale.contains(e)) { //solito pattern : aggiungo, faccio andare avanti la ricorsione e tolgo per fare backtracking
			parziale.add(e);
			cerca(parziale, L+1, m);
			parziale.remove(e); //funziona con il set perchè non ci sono duplicati, in una lista avremmo dovuto rimuovere l'ultimo oggetto aggiunto alla listam facendo una remove nella posizione lista.lentgh-1
		}
	}
	
	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
