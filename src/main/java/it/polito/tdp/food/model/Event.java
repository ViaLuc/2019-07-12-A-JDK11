package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {
	
	public enum EventType {
		INIZIO_PREPARAZIONE,
		FINE_PREPARAZIONE,
	}
	
	private Double tempo; //minuti
	private EventType type;
	private Stazione stazione;
	private Food food;
	
	public Event(Double tempo, EventType type, Stazione stazione, Food food) {
		super();
		this.tempo = tempo;
		this.type=type;
		this.stazione = stazione;
		this.food = food;
	}

	public Double getTempo() {
		return tempo;
	}

	public Stazione getStazione() {
		return stazione;
	}

	public Food getFood() {
		return food;
	}

	@Override
	public int compareTo(Event o) {
		return this.tempo.compareTo(o.tempo);
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	
	
	

}
