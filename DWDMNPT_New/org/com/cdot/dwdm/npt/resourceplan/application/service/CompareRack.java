package application.service;

import java.util.Comparator;

import application.model.Rack;
import application.model.Sbrack;

public class CompareRack implements Comparator<Rack>{
	 
	 @Override
	    public int compare(Rack e1, Rack e2) {
	        if(e1.getId() > e2.getId()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }	
}  