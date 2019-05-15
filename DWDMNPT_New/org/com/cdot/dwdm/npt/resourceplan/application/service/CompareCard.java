package application.service;

import java.util.Comparator;

import application.model.Card;

public class CompareCard implements Comparator<Card>{
	 
	 @Override
	    public int compare(Card e1, Card e2) {
	        if(e1.getId() > e2.getId()){
	            return 1;
	        } else if(e1.getId() < e2.getId())
	            return -1;
	        else if(e1.getId() == e2.getId())
	        {
	        	return 0;
	        }
			return 1;              
	        
	    }	
}  
