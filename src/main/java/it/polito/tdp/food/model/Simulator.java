package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulator {
	
	//MODELLO
	private List<Stazione> stazioni;
	private List<Food> cibi;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Model model;
	
	
	//PARAMETRI SIMULAZIONE
	
	private int K = 5; //stazioni disponibili
	
	//RISULTATI
	
	private Double tempoPreparazione;
	private int cibiPreparati;
	
	//CODA
	
	private PriorityQueue<Event> queue;
	
	public Simulator(Graph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.grafo = grafo;
		this.model=model;
	}
	
	public void init(Food partenza) {
		
		this.cibi= new ArrayList<Food>(grafo.vertexSet());
		
		for(Food cibo: this.cibi)
			cibo.setPreparazione(StatoPreparazione.DA_PREPARARE);
		
		this.stazioni= new ArrayList<Stazione>();
		this.cibiPreparati=0;
		for(int i=0; i<K; i++)
		{
			this.stazioni.add(new Stazione(true,null));
		}
		
		this.tempoPreparazione=0.0;
		
		this.queue= new PriorityQueue<Event>();
		List<FoodCalories> vicini = model.elencoCibiConnessi(partenza);
		
		for(int i=0; i<K && i<vicini.size(); i++)
		{
			this.stazioni.get(i).setLibera(false);
			this.stazioni.get(i).setFood(vicini.get(i).getFood());
			
			Event e = new Event(vicini.get(i).getCalories(),EventType.FINE_PREPARAZIONE,this.stazioni.get(i),vicini.get(i).getFood());
			queue.add(e);
		}
		
		
		
	}
	
	public void run() {
		
		while(!queue.isEmpty())
		{
			Event e = queue.poll();
			processEvent(e);
		}
		
	}
	
	public void processEvent(Event e) {
		switch(e.getType()) {
		
		case INIZIO_PREPARAZIONE:
			List<FoodCalories> vicini = model.elencoCibiConnessi(e.getFood());
			FoodCalories prossimo=null;
			
			for(FoodCalories vicino:vicini)
			{
				if(vicino.getFood().getPreparazione()==StatoPreparazione.DA_PREPARARE)
				{
					prossimo=vicino;
					break;
				}
			}
			
			if(prossimo!=null)
			{
				prossimo.getFood().setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(prossimo.getFood());
				
				Event e2 = new Event(e.getTempo()+prossimo.getCalories(),EventType.FINE_PREPARAZIONE,e.getStazione(),prossimo.getFood());
				this.queue.add(e2);
			}
			break;
			
		case FINE_PREPARAZIONE:
			this.cibiPreparati++;
			this.tempoPreparazione=e.getTempo();
			
			e.getStazione().setLibera(true);
			e.getFood().setPreparazione(StatoPreparazione.PREPARATO);
			
			Event e2 = new Event(e.getTempo(), EventType.INIZIO_PREPARAZIONE, e.getStazione(), e.getFood());
			this.queue.add(e2);
			break;
		}
	}

	public Double getTempoPreparazione() {
		return tempoPreparazione;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}
	
	public int getCibiPreparati() {
		return this.cibiPreparati;
	}
	

}
