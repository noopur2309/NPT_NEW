package application.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import application.constants.ResourcePlanConstants;
import application.model.Card;
import application.model.NodeSystem;
import application.model.Rack;
import application.model.Sbrack;



@Service
public class CircuitMatrixService {
	
	
	public Card cardPool(String type, int id)
	{
		SortedMap<Integer,String> mapport = new TreeMap();
		if(type.equalsIgnoreCase(ResourcePlanConstants.CardMuxponderCGM))
		{			
			mapport.put(1, ResourcePlanConstants.Card);
			mapport.put(2, ResourcePlanConstants.Card);
			mapport.put(3, ResourcePlanConstants.Card);
			mapport.put(4, ResourcePlanConstants.Card);
			mapport.put(5, ResourcePlanConstants.Card);
			mapport.put(6, ResourcePlanConstants.Card);
			mapport.put(7, ResourcePlanConstants.Card);
			mapport.put(8, ResourcePlanConstants.Card);
			mapport.put(9, ResourcePlanConstants.Card);
			mapport.put(10, ResourcePlanConstants.Card);	
		}		
		Card card = new Card(id,type,mapport);
		return card;		
	}
	
	public Sbrack sbrackPool(int id)
	{
		SortedMap<Integer,Card> mapsbr = new TreeMap<Integer,Card>();		
		mapsbr.put(1,cardPool(ResourcePlanConstants.CardMuxponderCGM,1));
		mapsbr.put(2,cardPool(ResourcePlanConstants.CardMuxponderCGM,2));
		mapsbr.put(3,cardPool(ResourcePlanConstants.CardMuxponderCGM,3));
		mapsbr.put(4,cardPool(ResourcePlanConstants.CardMuxponderCGM,4));
		mapsbr.put(5,cardPool(ResourcePlanConstants.CardMuxponderCGM,5));
		mapsbr.put(6,cardPool(ResourcePlanConstants.CardMuxponderCGM,6));
		mapsbr.put(7,cardPool(ResourcePlanConstants.CardMuxponderCGM,7));
		mapsbr.put(8,cardPool(ResourcePlanConstants.CardMuxponderCGM,8));
		mapsbr.put(9,cardPool(ResourcePlanConstants.CardMuxponderCGM,9));
		mapsbr.put(10,cardPool(ResourcePlanConstants.CardMuxponderCGM,10));
		mapsbr.put(11,cardPool(ResourcePlanConstants.CardMuxponderCGM,11));
		mapsbr.put(12,cardPool(ResourcePlanConstants.CardMuxponderCGM,12));
		mapsbr.put(13,cardPool(ResourcePlanConstants.CardMuxponderCGM,13));
		mapsbr.put(14,cardPool(ResourcePlanConstants.CardMuxponderCGM,14));
		Sbrack sbrack = new Sbrack(id,mapsbr);
		return sbrack;		
	}
	
	
	public Rack rackPool(int id)
	{
		SortedMap<Integer,Sbrack> maprack = new TreeMap<Integer,Sbrack>();
		maprack.put(1,sbrackPool(1));
		maprack.put(2,sbrackPool(2));
		maprack.put(3,sbrackPool(3));
		Rack rack = new Rack(id,maprack);
		return rack;		
	}
	
	public NodeSystem fInitiateEmersonSystemPool()
	{
		SortedMap<Integer,Rack> mapsystem = new TreeMap<Integer,Rack>();
		mapsystem.put(1,rackPool(1));
		mapsystem.put(2,rackPool(2));
		mapsystem.put(3,rackPool(3));
		mapsystem.put(4,rackPool(4));
		mapsystem.put(5,rackPool(5));		
		NodeSystem sys = new NodeSystem(1,mapsystem);
		return sys;
	}
	public void fInitiatePools()
	{
		
	}
	
//	public void fInitiatePoolstemp5()
//	{
//		SortedSet<Integer> set = new TreeSet();
//		set.add(1);
//	    set.add(2);
//	    set.add(3);
//	    set.add(4);
//		Card card1 = new Card(1,1,set);
//		Card card2 = new Card(2,1,set);
//		Card card3 = new Card(3,1,set);
//		Card card4 = new Card(4,1,set);
//		Card card5 = new Card(5,1,set);
//		SortedSet<Card> setsbr = new TreeSet<Card>( new CompareCard());
//		setsbr.add(card1);
//		setsbr.add(card2);
//		setsbr.add(card3);
//		setsbr.add(card4);
//		setsbr.add(card5);
//		Sbrack sbrack1 = new Sbrack(3,setsbr);
//		
//		SortedSet<Integer> set1 = new TreeSet();
//		set1.add(5);
//	    set1.add(2);
//	    set1.add(3);
//	    set1.add(4);
//		Card card11 = new Card(1,1,set1);
//		Card card21 = new Card(3,1,set1);
//		Card card31 = new Card(4,1,set1);
//		
//		SortedSet<Card> setsbr1 = new TreeSet<Card>( new CompareCard());
//		setsbr1.add(card11);
//		setsbr1.add(card21);
//		setsbr1.add(card31);		
//		Sbrack sbrack2 = new Sbrack(2,setsbr1);
//		
//		SortedSet<Sbrack> setrack = new TreeSet(new CompareSbrack());
//		setrack.add(sbrack1);
//		setrack.add(sbrack2);
//		Rack rack1 = new Rack(1,setrack);
//		
//		System.out.println("CircuitMatrixService.fInitiatePools()"+rack1.toString());
//		
//		System.out.println("CircuitMatrixService.fInitiatePools() first sbrack is "+ rack1.getSbrack().first().getId());
//		System.out.println("CircuitMatrixService.fInitiatePools() first port is "+ rack1.getSbrack().first().getSlots().first().getPorts().first().toString());
//	}
	
	
	
	
	public void fInitiatePoolstemp4()
	{
		HashMap<Integer,String> sbrack=new HashMap<Integer, String>();
		sbrack.put(1,"sbrack");
		sbrack.put(59,"sbrack");
		sbrack.put(3,"sbrack");
		sbrack.put(7,"sbrack");
		SortedMap ascSortedMap = new TreeMap();
		 System.out.println("\nSorted Map in descending order......");
		 SortedMap desSortedMap = new TreeMap(
				 new Comparator() {
					 public int compare(Integer o1, Integer o2) {
						 return o2.compareTo(o1);
					 }

					@Override
					public int compare(Object o1, Object o2) {
						// TODO Auto-generated method stub
						return ((Integer) o2).compareTo((Integer) o1);
					}					
				 });
		 desSortedMap.putAll(sbrack);
		 ascSortedMap.putAll(sbrack);
		 System.out.println("CircuitMatrixService.fInitiatePoolsMy() the asc map" + ascSortedMap.toString());
		 System.out.println("CircuitMatrixService.fInitiatePoolsMy() the desc map" + desSortedMap.toString());
		 
		 	        
	}
	
	
	
	public void fInitiatePoolstemp3()
	{
		HashMap<Integer,String> card1=new HashMap<Integer, String>();
		HashMap<Integer,String> card2=new HashMap<Integer, String>();		
		HashMap<Integer, HashMap<Integer, String>> sbrack = new HashMap<Integer, HashMap<Integer, String>>();
				
		for (int j = 0; j < 12; j++) {
			
			if(sbrack.size()<14)
			{
				if(card1.size()<10)
				{
					card1.put(card1.size()+1, "porttype");					
					
				}	
				else
				{				
					if(card2.size()<10)
					{
						card2.put(card2.size()+1, "porttype");						
					}				
				}
			}
		}
		
		sbrack.put(1, card1);
		sbrack.put(2, card2);		
		System.out.println("CircuitMatrixService.fInitiatePools() card1 "+card1.toString());
		System.out.println("CircuitMatrixService.fInitiatePools() card2 "+card2.toString());
		
		System.out.println("CircuitMatrixService.fInitiatePools() size of subrack "+sbrack.size());
		System.out.println("CircuitMatrixService.fInitiatePools() size of subrack first card size "+sbrack.get(1).size());
		System.out.println("CircuitMatrixService.fInitiatePools() size of subrack second card size "+sbrack.get(2).size());			
	}
	
	public void fInitiatePoolstemp2()
	{
		
		HashSet<Integer> sbrack1 = new HashSet<Integer>();
		for (int j = 0; j < 15; j++) {
			sbrack1.add(j);			
		}
		HashSet<Integer> sbrack2 = new HashSet<Integer>();
		for (int j = 0; j < 15; j++) {
			sbrack2.add(j);			
		}
		HashSet<Integer> sbrack3 = new HashSet<Integer>();
		for (int j = 0; j < 15; j++) {
			sbrack3.add(j);			
		}
		
		HashSet<HashSet<Integer>> rack1 =new HashSet<HashSet<Integer>>();
		rack1.add(sbrack1);
		rack1.add(sbrack2);
		rack1.add(sbrack3);	
		
		for (Iterator iterator = rack1.iterator(); iterator.hasNext();) {
			HashSet<Integer> hashSet = (HashSet<Integer>) iterator.next();
			for (Iterator iterator2 = hashSet.iterator(); iterator2.hasNext();) {
				Integer integer = (Integer) iterator2.next();
				System.out.println("CircuitMatrixService.fInitiatePools() id "+integer);				
			}			
		}
		
		for (Iterator iterator = rack1.iterator(); iterator.hasNext();) {
			HashSet<Integer> hashSet = (HashSet<Integer>) iterator.next();
			for (Iterator iterator2 = hashSet.iterator(); iterator2.hasNext();) {
				Integer integer = (Integer) iterator2.next();
				System.out.println("CircuitMatrixService.fInitiatePools() id "+integer);				
			}			
		}
	}
	
	public void fCheck()
	{
		
	}
	
	public void fInitiatePoolstmep()
	{
		HashMap<Integer,String> port0=new HashMap<Integer, String>();
		HashMap<Integer,String> port1=new HashMap<Integer, String>();
		HashMap<Integer,String> port2=new HashMap<Integer, String>();
		HashMap<Integer,String> port3=new HashMap<Integer, String>();
		port0.put(0, "cardtype");
		port1.put(1, "type1");
		port2.put(2, "type2");
		port3.put(3, "type3");		
		
		HashMap<Integer, HashMap<Integer, String>> card1 = new HashMap<Integer, HashMap<Integer, String>>();
		card1.put(0, port0);
		card1.put(1, port1);
		card1.put(2, port2);
		card1.put(3, port3);
		
		HashMap<Integer,String> port01=new HashMap<Integer, String>();
		port01.put(0, "cardtype2");
		HashMap<Integer, HashMap<Integer, String>> card2 = new HashMap<Integer, HashMap<Integer, String>>();
		card2.put(0, port01);
		card2.put(1, port1);
		card2.put(2, port2);
		card2.put(3, port3);
		
				
		System.out.println("CircuitMatrixService.fInitiatePools()....card1 "+card1.toString());		
		
		HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>> sbrack = new HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>>();
		sbrack.put(1, card1);
		sbrack.put(2, card2);
				
		System.out.println("CircuitMatrixService.fInitiatePools()....sbrack now"+sbrack.toString());		
		
//		HashMap<Integer,String> card2=new HashMap<Integer, String>();
//		card2.put(3, "port");
//		card2.put(4, "port");
//		
//		HashMap<Integer,String> sbrackname=new HashMap<Integer, String>();
//		sbrackname.put(1, "tpntype1");
//		sbrackname.put(2, "tpntype2");
//		
//		H
//		HashMap<Integer, HashMap<Integer, String>> sbrack = new HashMap<Integer, HashMap<Integer, String>>();
//		HashMap<Integer, HashMap<Integer, String>> sbrack2 = new HashMap<Integer, HashMap<Integer, String>>();
//		sbrack.put(1, card1);
//		sbrack.put(2, card2);
//		
//		sbrack2.put(3, card2);
//		
//		HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>> rack = new HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>>();
//		rack.put(1, sbrack);		
//		rack.put(2, sbrack2);
//		
//		System.out.println("CircuitMatrixService.fInitiatePools()"+rack.toString());
//			
//		
			
		
		
		
		
	}
	
	
	
	
	

}



 