package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	
	public List<Food> getFoodsByPortionNumber(int portions){
		
		String sql= "SELECT f.food_code, f.display_name, COUNT(DISTINCT p.portion_id) AS CNT " + 
				"FROM food AS f, `portion` AS p " + 
				"WHERE f.food_code=p.food_code " + 
				"GROUP BY f.food_code " + 
				"HAVING CNT = ? " +
				"ORDER BY f.display_name ASC";
		
		List<Food> result = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, portions);
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
					result.add(new Food(res.getInt("food_code"), res.getString("display_name")));
				
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public Double getcalorieCongiunte(Food f1, Food f2) {
		String sql ="SELECT f1.food_code, f2.food_code, f1.condiment_code, " + 
				"	AVG(c.condiment_calories) AS peso " + 
				"FROM food_condiment AS f1, food_condiment AS f2, condiment AS c " + 
				"WHERE f1.condiment_code=f2.condiment_code " + 
				"	AND c.condiment_code=f1.condiment_code " + 
				"	AND f1.id<>f2.id " + 
				"	AND f1.food_code = ? " + 
				"	AND f2.food_code = ? " + 
				"GROUP BY f1.food_code, f2.food_code ";
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, f1.getFood_code());
			st.setInt(2, f2.getFood_code());
			
			ResultSet res = st.executeQuery() ;
			
			Double cal = null;
			if(res.first())
			{
				cal = res.getDouble("peso");
			}
			conn.close();
			return cal;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
}
