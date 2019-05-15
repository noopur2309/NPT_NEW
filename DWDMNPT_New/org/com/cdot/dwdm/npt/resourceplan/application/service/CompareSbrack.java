package application.service;

import java.util.Comparator;

import application.model.Card;
import application.model.Sbrack;

public class CompareSbrack implements Comparator<Sbrack>{
	 
	 @Override
	    public int compare(Sbrack e1, Sbrack e2) {
	        if(e1.getId() > e2.getId()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }	
}  