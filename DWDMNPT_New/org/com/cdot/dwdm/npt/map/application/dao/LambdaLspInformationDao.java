/**
 * @author hp
 * @date 24th May, 2018
 */

package application.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.LambdaLspInformation;


@Component
public class LambdaLspInformationDao{
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public static Logger logger = Logger.getLogger(ResourcePlanConstants.DatabasePrintStr);
	
		 
	 public List<LambdaLspInformation> findAll(int networkId){
			String sql = "SELECT * FROM LambdaLspInformation where NetworkId = ?";
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<LambdaLspInformation> routes  = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LambdaLspInformation.class), networkId);
			logger.debug("LambdaLspInformation FindAll  : "+ routes.toString());
			return routes;
	}
	 
	 public LambdaLspInformation findLSP(int networkid, int ForwardingAdjacency)
	 { 
		 System.out.println("here"+networkid  +ForwardingAdjacency);
		 String sql = "SELECT * FROM LambdaLspInformation where NetworkId = ?   and ForwardingAdj = ? ";
		
		
		try
		{LambdaLspInformation Lsp  =(LambdaLspInformation) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(LambdaLspInformation.class),
		networkid,ForwardingAdjacency);
		logger.debug("LambdaLspInformation findLsp  : "+ Lsp.toString());
		return Lsp;
		}
		catch(EmptyResultDataAccessException e) {
    		return null;
    	}	
		 
		 
		 
	 }
	 
	 

	public LambdaLspInformation findLsp(int networkid, int demandId, String Path){
		String sql = "SELECT * FROM LambdaLspInformation where NetworkId = ? and DemandId =?  and Path=? ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		LambdaLspInformation routes  =(LambdaLspInformation) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(LambdaLspInformation.class),
												 networkid, demandId,Path);
		logger.debug("LambdaLspInformation findLsp  : "+ routes.toString());
		return routes;
}

	 
	 public void insertLambdaLspInformation(final LambdaLspInformation lspInformation) throws SQLException {
	
		logger.debug("Insert LambdaLspInformation : "+lspInformation.toString());
		 String sql = "INSERT into LambdaLspInformation(NetworkId, DemandId, RoutePriority,"+
		 			   "Path, LambdaLspTunnelId, LspId, ForwardingAdj) VALUES ( ?,?,?,?,?,?,?)";

		try{

						
				jdbcTemplate.update(
						sql,
						new Object[] { lspInformation.getNetworkId(), lspInformation.getDemandId(), 
							lspInformation.getRoutePriority(),
							lspInformation.getPath(),
							lspInformation.getLambdaLspTunnelId(), 
							lspInformation.getLspId(),
							lspInformation.getForwardingAdj()
							});

		}
		catch(Exception e){
					logger.error("Error in insertOtnLspInformation : "+e);
					e.printStackTrace();
		}
	 }


	    
	// public void updateLambdaLspInformation(LambdaLspInformation lspInformation){       	              
	       
	// 	logger.debug("Update LambdaLspInformation : "+lspInformation.toString());
		
	   
	// 	String sql = "update LambdaLspInformation set OtnLspTunnelId=?  where NetworkId= ? and DemandId= ? and RoutePriority = ? and"
	// 				  +" Path = ?  and ServiceType = ? ;";
					
	// 	   try
	// 	   {
	// 		   jdbcTemplate.update(sql, lspInformation.getOtnLspTunnelId(), lspInformation.getNetworkId(), lspInformation.getDemandId(),
	// 		   lspInformation.getRoutePriority(), lspInformation.getPath(), lspInformation.getServiceType());	       
	// 	   }
	// 	   catch(Exception e) {
	// 		   logger.error("updateLambdaLspInformation error : "+e);
	// 	   }	
	// };   

	 public void deleteByNetworkId(int networkid) {
	        logger.info("Delete LambdaLspInformation");
	        String sql = "delete from LambdaLspInformation where NetworkId = ?";
	        jdbcTemplate.update(sql, networkid);      
	}
	 
	 public void deleteLambdaLspInformation(int networkid, int demandid) {
	        logger.info("Delete LambdaLspInformation"+ networkid + " and "+ demandid);
	        String sql = "delete from LambdaLspInformation where NetworkId = ? and DemandId = ? ";
	        jdbcTemplate.update(sql, networkid, demandid);      
	}
		

	public Map<String,Object> findMaxTunnelIdLambdaLspInformation(int networkId){
		String sql = "select Max(LambdaLspTunnelId) as LambdaLspTunnelId from LambdaLspInformation"+
				      " where NetworkId = ?";		
		
		Map<String,Object> routes  = jdbcTemplate.queryForMap(sql, networkId);
		logger.debug("LambdaLspInformation FindMaxLspTunnelId  : "+ routes.toString());
		return routes;
	}


	public void copyLambdaLspInformationInBrField(int networkidGrField, int networkidBrField ) throws SQLException {			
		 	
		String sql = "insert into LambdaLspInformation (NetworkId, DemandId, RoutePriority,"+
		"Path, LambdaLspTunnelId, LspId,ForwardingAdj) select ?, DemandId, RoutePriority,"+
		 			   "Path, LambdaLspTunnelId, LspId,ForwardingAdj from LambdaLspInformation where NetworkId = ? ";
		logger.info("copyLambdaLspInformationInBrField: "+sql); 	     
		jdbcTemplate.update( sql,networkidBrField,networkidGrField);
	}

	public int count(int networkid)
	{				
		 String sql = "select count(*) from LambdaLspInformation where NetworkId = ? "; 
		 return jdbcTemplate.queryForObject(sql, int.class, networkid);		  		        
	} 
	

	/**
	 * finds the list of added/new Lambda Lsp in brownfield
	 * 
	 * @param networkidGrField
	 * @param networkidBrField
	 * @sample query :
	 *  select * from 
	        		(select bf.NetworkId, bf.DemandId, bf.RoutePriority, bf.Path, bf.LambdaLspTunnelId,bf.LspId,
                    bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, 
	        		gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  
	        		( select * from LspInformation where NetworkId = 35 ) as bf 
	        		left join 
	        		( select * from LspInformation where NetworkId = 27  ) as gf 
	        		on gf.DemandId = bf.DemandId ) as t 
					where t.DemandIdGf is NULL and t.NetworkIdGf is NULL;
	 * @author  hp
	 * @created 31st May, 2018 	
	 * @return
	 */
	public List<LambdaLspInformation> findAddedLambdaLspInBrField(int networkidGrField, int networkidBrField) {
			String sql = "select * from"+ 
						"	(select bf.NetworkId, bf.DemandId, bf.RoutePriority, bf.Path, bf.LambdaLspTunnelId,bf.LspId,"+						
						"	bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, "+
						"	gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  "+
						"	( select * from LambdaLspInformation where NetworkId = ? ) as bf "+
						"	left join "+
						"	( select * from LambdaLspInformation where NetworkId = ?  ) as gf "+
						"	on gf.DemandId = bf.DemandId ) as t "+
						"	where t.DemandIdGf is NULL and t.NetworkIdGf is NULL";



		List<LambdaLspInformation> lambdaLspInformation;
		try {
			return lambdaLspInformation = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LambdaLspInformation.class), networkidBrField, networkidGrField);
		}
		catch (Exception e) {
			return null;
		}
	}



	public int findMaxTunnelIdLambdaLspInformation(int  brownFieldNetworkid,int  greenFieldNetworkid, int  demandId/*, int  wavelengthNo*/){		
		
		String sql;
		int LambdaLspTunnelId=0;

		try{

				/**First check from the working path, because protection will have the same lambdalspid */
				sql = "select LambdaLspTunnelId from LambdaLspInformation  where NetworkId = ? and DemandId =?   and RoutePriority=?";
				LambdaLspTunnelId  = jdbcTemplate.queryForObject(sql, int.class, brownFieldNetworkid,demandId/*,wavelengthNo*/, MapConstants.I_ONE );

				
		}
		catch(Exception e){/**Else give the maximum id from table */
			
			logger.error(e);

			/**Maxumum id out of the two lsp tables */
			sql = "SELECT MAX(T.Id)+1 AS LambdaLspTunnelId "+
							"FROM ( "+
								"SELECT LambdaLspTunnelId AS Id "+
								"FROM LambdaLspInformation where NetworkId=?  or NetworkId=? "+
								"UNION ALL "+
								"SELECT OtnLspTunnelId AS Id "+
								"FROM OtnLspInformation  where NetworkId=?    or NetworkId=? "+
								") AS T";
			

			// sql = "select Max(LambdaLspTunnelId)+1 as LambdaLspTunnelId from LambdaLspInformation"+
			// 			" where NetworkId = ?";		

			LambdaLspTunnelId  = jdbcTemplate.queryForObject(sql, int.class, brownFieldNetworkid, greenFieldNetworkid, brownFieldNetworkid, greenFieldNetworkid);	
			
		
		}		
		

		logger.debug("OtnLsLambdaLspInformationpInformation FindMaxLspTunnelId  : "+ LambdaLspTunnelId);
	
		return LambdaLspTunnelId;
	}
	
	
	
	/**
	 * finds the list of  Deleted Lambda Lsp in brownfield
	 * 
	 * @param networkidGrField
	 * @param networkidBrField
	 * @sample query :
	 *  select * from 
	        		(select gf.NetworkId, gf.DemandId,gf.RoutePriority, gf.Path, gf.LambdaLspTunnelId,gf.LspId,
                    bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, 
	        		gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  
	        		( select * from LambdaLspInformation where NetworkId = 1 ) as gf 
	        		left join 
	        		( select * from LambdaLspInformation where NetworkId = 2  ) as bf 
	        		on gf.DemandId = bf.DemandId ) as t 
	        		where t.DemandIdBf is  NULL and t.Path LIKE '1,%' ; 
	 * @author  hp
	 * @created 12th July, 2018 	
	 * @return
	 */
	public List<LambdaLspInformation> findDeletedLambdaLspInBrField(int networkidGrField, int networkidBrField, int ...sourceNode ) {
			
		String pathLike = "";
		
		String sql = "select * from \n" + 
					"	        		(select gf.NetworkId, gf.DemandId,gf.RoutePriority, gf.Path, gf.LambdaLspTunnelId,gf.LspId,\n" + 
					"                    bf.NetworkId as NetworkIdBf, bf.DemandId as DemandIdBf, \n" + 
					"	        		gf.NetworkId as NetworkIdGf, gf.DemandId as DemandIdGf from  \n" + 
					"	        		( select * from LambdaLspInformation where NetworkId = ? ) as gf \n" + 
					"	        		left join \n" + 
					"	        		( select * from LambdaLspInformation where NetworkId = ?  ) as bf \n" + 
					"	        		on gf.DemandId = bf.DemandId ) as t \n" + 
					"	        		where t.DemandIdBf is  NULL  ";


			
		if(sourceNode.length>MapConstants.I_ZERO) {
		
			pathLike = sourceNode[MapConstants.I_ZERO]+",%";
			
			sql+= " and t.Path LIKE ?";

			
		}
		
			
			
		List<LambdaLspInformation> lambdaLspInformation;
		try {
			
			if(pathLike.equalsIgnoreCase(""))
			
				return lambdaLspInformation = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LambdaLspInformation.class), networkidGrField, networkidBrField);
			else
				return lambdaLspInformation = jdbcTemplate.query(sql, new BeanPropertyRowMapper(LambdaLspInformation.class), networkidGrField, networkidBrField,pathLike);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public int  updateSchema(String sql){		
		System.out.println("updateSchema : "+sql);
		try {
			
			jdbcTemplate.query(sql, new BeanPropertyRowMapper(LambdaLspInformation.class));
			logger.debug("LambdaLspInformation updateSchema : Schema Updated Successfully ... ");
			System.out.println("LambdaLspInformation updateSchema : Schema Updated Successfully ... ");
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		} 
		 
	}	
	
	public int findMaxForwardingAdjLambdaLspInformation(int  brownFieldNetworkid,int  greenFieldNetworkid){		
		
		String sql;
		int ForwardingAdj=0;

		try{

			/**Maxumum id out of the two lsp tables */
			sql = "SELECT MAX(T.Id)+1 AS ForwardingAdj "+
							"FROM ( "+
								"SELECT ForwardingAdj AS Id "+
								"FROM LambdaLspInformation where NetworkId=?  or NetworkId=? "+								
								") AS T";			


			ForwardingAdj  = jdbcTemplate.queryForObject(sql, int.class, brownFieldNetworkid, greenFieldNetworkid);	

				
		}
		catch(Exception e){/**Else give the maximum id from table */
			
			logger.error(e);		
		}		
		

		logger.debug("OtnLsLambdaLspInformationpInformation FindMaxForwardingAdj  : "+ ForwardingAdj);
	
		return ForwardingAdj;
	}
	    

	 	 
}
