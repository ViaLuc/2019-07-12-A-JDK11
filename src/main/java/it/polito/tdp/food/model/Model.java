package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private List<Food> cibi;
	private Graph<Food, DefaultWeightedEdge> grafo;
	
	public Model() {
		
	}
	
	public List<Food> getFoods(int portion) {
		FoodDao dao = new FoodDao();
		this.cibi= dao.getFoodsByPortionNumber(portion);
		
		this.grafo= new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.cibi);
		
		for(Food f1 : this.cibi)
		{
			for(Food f2 : this.cibi)
			{
				if(!f1.equals(f2) && f1.getFood_code()<f2.getFood_code())
				{
					Double peso = dao.getcalorieCongiunte(f1,f2);
					if(peso!=null)
					{
						Graphs.addEdgeWithVertices(this.grafo, f1, f2, peso);
					}
				}
			}
		}
		
		return this.cibi;
	}
	
	public List<FoodCalories> elencoCibiConnessi(Food f) {
		
		List<FoodCalories> result = new ArrayList<FoodCalories>();
		
		List<Food> vicini = Graphs.neighborListOf(this.grafo, f);
		
		for(Food v : vicini)
		{
			Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(f,v));
			result.add(new FoodCalories(v, peso));
		}
		
		Collections.sort(result);
		return result;
	}
	
	public String simula(Food cibo, int K) {
		Simulator sim = new Simulator(this.grafo,this);
		sim.setK(K);
		sim.init(cibo);
		sim.run();
		
		String messaggio= String.format("Preparati %d cibi in %f minuti\n", sim.getCibiPreparati(), sim.getTempoPreparazione());
		
		return messaggio;
	}

}
