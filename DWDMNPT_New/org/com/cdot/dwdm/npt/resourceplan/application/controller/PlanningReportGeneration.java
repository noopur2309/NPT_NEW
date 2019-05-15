package application.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import application.MainMap;
import application.constants.MapConstants;
import application.model.Bom;
import application.model.CardInfo;
import application.model.Circuit;
import application.model.Equipment;
import application.model.IpSchemeLink;
import application.model.IpSchemeNode;
import application.model.Link;
import application.model.LinkWavelength;
import application.model.LinkWavelengthInfo;
import application.model.Network;
import application.model.Node;
import application.model.PatchCord;
import application.service.DbService;
import application.service.IPv4;



@Service
	public class PlanningReportGeneration {
	public static Logger logger = Logger.getLogger(ResourcePlanning.class.getName());
	
	
	
			/** creating a new document object in which the whole pdf file is made*/
			Document document = new Document(PageSize.A4, 40, 40, 70, 70);
			int chartWidth= 500;
		    int chartHeight= 300;
		    PdfWriter writer;//writer for adding various components to pdf
		  
		    int count=0; 
		   
	   
		    DbService dbService;
		    PlanningReportUtils planningReportUtils= new PlanningReportUtils();;
		    int networkid=0;/// =1;
	   
		    private static Font chapterfont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
	               Font.BOLD);
	    
		    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	               Font.BOLD);
		    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 14,
	               Font.NORMAL);
		    private static Font textfont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
		               Font.NORMAL);
		   
	    
	   
	    public PlanningReportGeneration(DbService dbService) {
	        super();
	        this.dbService = dbService;           
//	        logger = logger.getLogger(PlanningReportGeneration.class.getName());
	    }

	    public DbService getDbService() {
	        return dbService;
	    }

	    public void setDbService(DbService dbService) {
	        this.dbService = dbService;
	    }
	    
	   
	   
	    /*function to generate the pdf*/
	    public void createReport(HashMap<String,Object> networkInfoMap)
	    {
	    	
	           try
	           		{ 

	        	   			networkid = (int) networkInfoMap.get(MapConstants.NetworkId);
	        	   			
	        	   			logger.info(" createReport : networkId " + networkid);
	        	   			
	        	   			long timeStart = System.currentTimeMillis(); 
	        	   			String home = System.getProperty("user.home");	        	   				        	   			
	           				
	        	   			writer=PdfWriter.getInstance(document, new FileOutputStream(home+"/Downloads/PlanningReport.pdf"));
	        	   			MyFooter footerEvent = new MyFooter();
	        	   		   writer.setPageEvent(footerEvent);
	        	   			document.open();
	        	   			/*calling function to create front page*/
	        	   			planningReportUtils.frontpage(document);
	        	   			planningReportUtils.createTOC(document);
	        	   			
	        	   			
	        	   			/*calling function to show network design information */
	        	   			displayNetworkDesign(document,generateNetworkDesignData(),count++);
	        	   			
	        	   			
	        	   			/*calling function to display network inputs*/
	        	   			displayNetworkInputs(document,generateNetworkInputs(),count++);
	        	   			
	        	   			
	        	   			/*calling function to display network outputs */
	        	   			displayNetworkOutputs(document,generateNetworkOutputs(),count++);
	        	   			
	        	   			
	        	   			
	        	   			/*calling function to display Network Equipment */
	        	   			displayNetworkEquipment(document,generateNetworkEquipment(),count,writer);
//	        	   		 planningReportUtils.createTable(section,generateBomTableData(),generateBomTableHeader(),"Bom Table");
	        	   			
	        	   		
	        	   			document.close();
	        	   			writer.close();
	        	   			long timeEnd = System.currentTimeMillis();
	        	   			
	        	   			logger.info("**************"+"PlanningReportGenerationTime :"+(timeEnd-timeStart)+" MilliSeconds"+"**************");
	        	   			
	           		}
	           	catch (Exception e)
	           		{
	           				e.printStackTrace();
	           		}
	    }
	   
	    
	   
	  
	   /* function to fetch Network information from the database*/
	   public  Object[] generateNetworkDesignData(){
		   String nameOfNetwork;
		   int noOfNodes;
		   String topology= dbService.getNetworkService().FindNetwork(networkid).getTopology();
		   int ilas;
		   int roadms;
		   int tes;
		   int twobrodum;
		   
		   //getting nodes data from the database through dbservice.
		   List <Node> node=dbService.getNodeService().FindAll(networkid);	
		   //calculating number of nodes
		   noOfNodes = node.size();
		   //getting network name from the database
		   Network network=dbService.getNetworkService().FindNetwork(networkid);
		   nameOfNetwork = network.getNetworkName();
		   
		   
		   			//getting topology from the database
		  
		   
		   			//getting ilas from the database
		   ilas=dbService.getNodeService().NodeTypeCount(networkid, MapConstants.ila);
		   			//getting roadms from the database
		   roadms=dbService.getNodeService().NodeTypeCount(networkid, MapConstants.roadm);
		   			//getting tes from the database
		   tes=dbService.getNodeService().NodeTypeCount(networkid, MapConstants.te);
		   			//getting twobrodum from the database
		   twobrodum=dbService.getNodeService().NodeTypeCount(networkid, MapConstants.twoBselectRoadm);
		   
		   //filling data in Object array to be returned
		 
		   Object[] data = new Object[10];
		   data[0] =nameOfNetwork;
		   data[1] =noOfNodes;
		   data[2] =topology;
		   data[3]=ilas;
		   data[4]= roadms;
		   data[5]= tes;
		   data[6]=twobrodum;
		   return data;
	   }
	   
	   public Object[] generateNetworkDesignFields(){
		 
			   Object[] data= new Object[7];
			   data[0]="Name ";
			   data[1]="No of Nodes ";
			   data[2]="Topology ";
			   data[3]="Total ILA's ";
			   data[4]="Total ROADM's";
			   data[5]="Total TE's ";
			   data[6]="Total Broadcast and Select ROADM's ";
					  
			   return data;
		   
	   }
	  
	   public Object[] generateNetworkInputsFields(){
			 
		   Object[] data= new Object[2];
		   data[0]="Total Circuits";
		   data[1]="Total Circuit Capacity(G)";
		  
				  
		   return data;
	   
   }
	   public Object[] generateNetworkOutputsFields(){
			 
		   Object[] data= new Object[3];
		   data[0]="Total Optical Paths in Network";
		   data[1]="Minimum number of Wavelengths on a link ";
		   data[2]="Maximum number of Wavelengths on a link ";
			  
		   return data;
	   
   }
	   public Object[] generateNetworkEquipmentFields(){
			 
		   Object[] data= new Object[3];
		   data[0]="Total Network Cost($)";
		   data[2]="Maximum Node Cost($) ";
		   data[1]="Minimum Node Cost($) ";
			  
		   return data;
	   
   }
	   
	   /*function to fetch network design information from the database*/
	   public  Object[] generateNetworkInputs(){
		  
		   int totalCircuits;
		   int circuitCapacity;		  
		  
		   										//getting circuits data from the database through dbservice.
		  List <Circuit> circuit=dbService.getCircuitService().FindAll(networkid);
		   
		   
		 //calculating number of circuits		
		  totalCircuits=circuit.size();
		   
		  //getting circuit capacity from the database
		  circuitCapacity=dbService.getCircuitService().CircuitCapacity(networkid);
		  
		  //filling data in Object array to be returned
		   Object[] data = new Object[4];
		   data[0] =totalCircuits;
		   data[1] =circuitCapacity;
		  return data;
	   }
	   
	   /* function to add network information to the document*/
	   public void displayNetworkDesign(Document document,Object[] info,int count) throws DocumentException, IOException{
		   Chapter chapter;
		   Paragraph para ;
		   Section section;

           chapter = new Chapter(count+1);
           planningReportUtils.createChapter(count,chapter,writer);
           
           para = new Paragraph("Network Map",subFont);
           para.setIndentationLeft(50);
           para.add(Chunk.NEWLINE);
           System.out.println(System.getProperty("java.io.tmpdir")+"/NetworkMap.png");
           Image img = Image.getInstance(System.getProperty("java.io.tmpdir")+"/NetworkMap.png");
    	   img.scaleToFit(280, 280);
           img.setAlignment(Image.LEFT);  	
           para.add(img);
           section = chapter.addSection(para);
           
           
           para = new Paragraph("General Information",subFont);
           para.setIndentationLeft(50);
           section = chapter.addSection(para);
           /*Calling function to display the general information about the network*/
           planningReportUtils.createDataTable(section,generateNetworkDesignFields(),generateNetworkDesignData());

           para=new Paragraph("Node Wise Details",subFont);
           section=chapter.addSection(para);
           para.setIndentationLeft(50);
           planningReportUtils.addEmptyLine(para, 1);
           /*calling the function to create the table for node information*/
           planningReportUtils.createTable(section, generateNodeTabledata(), generateNodeTableHeader(), "Node Table");

           para=new Paragraph("Link Wise Details",subFont);
           section=chapter.addSection(para);
           para.setIndentationLeft(50);
           planningReportUtils.addEmptyLine(para, 1);
  			/*calling the function to create the table for Link information*/
           planningReportUtils.createTable(section,generateLinkTableData(),generateLinkTableHeader(),"Link Table");
           
           
           
           
           document.add(chapter);
           
	   }
	   public void displayNetworkInputs(Document document,Object[] info,int count) throws DocumentException, IOException{
		  
		   Section section;
		   Paragraph para;
		  
		   para=new Paragraph("Network Inputs",chapterfont);
		 
		   para.setAlignment(Element.ALIGN_CENTER);
		   chapterfont.setColor(BaseColor.BLUE);
		   Chapter chapter = new Chapter(count+1);
		   planningReportUtils.createChapter(count,chapter,writer);
		   planningReportUtils.addEmptyLine(para, 2);
		  
		   
		    para = new Paragraph("General Information",subFont);
		    para.setIndentationLeft(50);
		    planningReportUtils.addEmptyLine(para, 1);
		   para.setAlignment(Element.ALIGN_LEFT);
		   section = chapter.addSection(para);
		   planningReportUtils.createDataTable(section,generateNetworkInputsFields(),generateNetworkInputs());
		   para= new Paragraph("Circuit Wise Details: ",subFont);
		   para.setIndentationLeft(50);
		   section = chapter.addSection(para);
		   planningReportUtils.addEmptyLine(para,1);
		   planningReportUtils.createTable(section,generateCircuitTableData(),generateCircuitTableHeader(),"Circuit Table");
		   
		   document.add(chapter);
		   
		   
	   }
	   public void displayNetworkOutputs(Document document,Object[] info,int count) throws DocumentException, SQLException, IOException{
		  
		   Section section;
		  
		   Paragraph para;
		   para=new Paragraph("Network Outputs",chapterfont);
		
		   para.setAlignment(Element.ALIGN_CENTER);
		   chapterfont.setColor(BaseColor.BLUE);
		   Chapter chapter = new Chapter(count+1);
		   planningReportUtils.createChapter(count,chapter,writer);		  	
		    para = new Paragraph("General Information",subFont);
		    para.setIndentationLeft(50);		   
		   para.setAlignment(Element.ALIGN_LEFT);
		   section = chapter.addSection(para);
		   planningReportUtils.createDataTable(section,generateNetworkOutputsFields(),generateNetworkOutputs());
		   document.add(chapter);
		   para=new Paragraph("LinkWise Wavelength Population",subFont);
			para.setIndentationLeft(50);
			section=chapter.addSection(para);
		
			planningReportUtils.addDescription(section, "The Chart below depicts the distribution of total number of wavelengths on individual links in the network");

			document.add(section);
			 planningReportUtils.addEmptyLine(para, 2);
 			/*calling function to generate bar chart*/
			
			 planningReportUtils.writeChartToPDF(section,planningReportUtils.generateBarChart(generateLinkwiseWavelengthchartdata()," Wavelength Population On Links","LinkId","Number Of Wavelengths"/*,"green"*/),chartWidth,chartHeight,writer.getVerticalPosition(true)-chartHeight-20,writer);
 			 planningReportUtils.addEmptyLine(para, 1);
 			 
 			 document.newPage();
 			 planningReportUtils.addEmptyLine(para, 2);
		   para=new Paragraph("Wavelength and Circuit Map",subFont);
 			para.setIndentationLeft(50);
 			
 			section=chapter.addSection(para);
// 			planningReportUtils.addDescription(section, "Inventory information includes the details of the cards/components allocated on per node basis.");
 			
 			
 			 planningReportUtils.addEmptyLine(para, 1);
 			para=new Paragraph("");
		   /*calling function to display node wise card info data */
		   displaycardInfoTableData(section);
		   document.add(section);
		   
		   
		   
		   
		   document.newPage();
		   para=new Paragraph("NodeWise Add/Drop Wavelength Population",subFont);
			para.setIndentationLeft(50);
			section=chapter.addSection(para);
			planningReportUtils.addDescription(section, "The Chart below depicts the distribution of total number of wavelengths(add/drop) on individual nodes in the network");
			document.add(section);
			 planningReportUtils.addEmptyLine(para, 2);
	 			/*calling function to generate bar chart*/
			
			planningReportUtils.writeChartToPDF(section,planningReportUtils.GenerateNodeWaveClusterChart(dbService,generateNodewiseWavelengthchartdata()," Wavelength Population On Network","NodeId","Number Of Wavelengths"/*,"green"*/,networkid),chartWidth,chartHeight,writer.getVerticalPosition(true)-chartHeight-20,writer);
			
			document.newPage();
			para=new Paragraph("Linkwise Wavelength Utilization",subFont);
			para.setIndentationLeft(50);
			section=chapter.addSection(para);
			document.add(section);
//			document.newPage();
			planningReportUtils.writeChartToPDF(section,planningReportUtils.GenerateLinkWaveClusterChart(dbService," Wavelength Population On Network","LinkId","Traffic(Gbps)"/*,"green"*/,networkid),chartWidth,600,writer.getVerticalPosition(true)-600-20,writer);
		  
			document.newPage();
  			para = new Paragraph("Nodewise Directionwise Wavelength and Traffic Chart",subFont);
            para.setIndentationLeft(50);
            section=chapter.addSection(para);
            document.add(section);
            planningReportUtils.writeChartToPDF(section,planningReportUtils.GenerateNodeLinkWaveClusterChart(dbService,"Node/Direction Wavelength Utilization","","Total Traffic(g)"/*,"orange"*/,networkid),chartWidth,650,writer.getVerticalPosition(true)-650-20,writer);
          
		   
  			document.newPage();
  			para=new Paragraph("Node Wise Ip Scheme",subFont);
  			para.setIndentationLeft(50);
  			
  			section=chapter.addSection(para);
  			planningReportUtils.addDescription(section,"The table below gives the details of the IP scheme generated for the nodes in the network.");

  				//calling function to display nodewise ip scheme
  			displayNodewiseipScheme(section);
  			document.add(section);
  			para=new Paragraph("Link Wise Ip Scheme",subFont);
  			
  			para.setIndentationLeft(50);
  			section=chapter.addSection(para);
  			planningReportUtils.addDescription(section,"The table below gives the details of the IP scheme generated for the Links in the network.");

  				//calling function to display linkwise ip scheme
  			displayLinkwiseipScheme(section);
  			
		   document.add(section);
		   para=new Paragraph("Inventory Information",subFont);
 			
 			para.setIndentationLeft(50);
 			section=chapter.addSection(para);
 			generateInventoryTableData( section);
 			document.add(section);
 			document.newPage();
 			 para=new Paragraph("Chassis Information",subFont);
  			
  			para.setIndentationLeft(50);
  			section=chapter.addSection(para);
  			document.add(section);
  			try{
 			planningReportUtils.chassisImage(document);/** DBG => For Chassis String Generation*/
  			}
  			catch(Exception e){
  				logger.info("The chassis Images were not found......");
  			}
	   
	   }
	   
	   /*function to fetch network design information from the database*/
	  
	public  Object[] generateNetworkOutputs() throws SQLException{
		  
		   int maxwave;
		   int minwave;
		  
		   				//getting maximum wavelength data from the database through dbservice.
		   maxwave=dbService.getLinkWavelengthInfoService().MaxWave();
		   		  
		   				//getting minimum wavelength data from the database through dbservice.
		   minwave=dbService.getLinkWavelengthInfoService().MinWave();
		  										//getting total optical path  data from the database through dbservice.
		   //List <LinkWavelength> totalOpticalpaths=dbService.getOpticalPathService().FindAll(); 
		   //totalOpticalpaths = totalOpticalpaths.FindAll(1,1);
		   				//filling data in Object array to be returned
		   List<LinkWavelengthInfo> linkwv = dbService.getLinkWavelengthInfoService().FindAll(networkid);
		   
	  
		   Object[] data = new Object[4];
		  
		  
		   data[2] =maxwave;
		   data[1] =minwave;
		   data[0] =linkwv.size();
		
		   return data;
	   }
	   
	   
	   /*function to generate nodewise ip scheme data*/
	   public  void displayNodewiseipScheme(Section section) throws DocumentException{
		   List<Node> node=dbService.getNodeService().FindAll(networkid);
		   
		  

		   List<Object> data = new ArrayList<Object>();
		
		   for(int j=0;j<node.size();j++){			   
			   
			   List<IpSchemeNode> nodeip = dbService.getIpSchemeNodeService().FindIpSchemeNodewise(networkid, node.get(j).getNodeId());

			 
			
			   for(int i=0;i<nodeip.size();i++){
							   
//							data[i][0]=nodeip.get(i).getNetworkId();
							data.add(nodeip.get(i).getNetworkId());
							data.add(nodeip.get(i).getNodeId());
							data.add(IPv4.integerToStringIP(nodeip.get(i).getLctIp()));
							data.add(IPv4.integerToStringIP(nodeip.get(i).getRouterIp()));
							data.add(IPv4.integerToStringIP(nodeip.get(i).getScpIp()));
							data.add(IPv4.integerToStringIP(nodeip.get(i).getMcpIp()));
//							data[i][1]=nodeip.get(i).getNodeId();
//							data[i][2]=IPv4.integerToStringIP(nodeip.get(i).getLctIp());
//							data[i][3]=IPv4.integerToStringIP(nodeip.get(i).getRouterIp());
//							data[i][4]=IPv4.integerToStringIP(nodeip.get(i).getScpIp());
//							data[i][5]=IPv4.integerToStringIP(nodeip.get(i).getMcpIp());
//							data[i][6]=nodeip.get(i).getMcpSubnet();
//							data[i][7]=nodeip.get(i).getMcpGateway();
//							data[i][8]=nodeip.get(i).getRsrvdIp1();
//							data[i][9]=nodeip.get(i).getRsrvdIp2();
				
				
						   }
//			  createTable(section, data, generateIpSchemeNodeHeader(), "Node Ip Scheme For Node  "+(j+1));
			 
			  
		   }
		   planningReportUtils.displaynodewiseschemeTable(section, data, generateIpSchemeNodeHeader(), "NodeWise Ip Scheme",0);
		   
		  
	   }
	   
	   /*function to generate ipschemenode table header*/
	   public String[] generateIpSchemeNodeHeader(){
		   String[] header={"NetworkId","NodeId","LctIp","RouterIp","ScpIp","McpIp"/*,"McpSubnet","McpGateway","RsrvdIp1","RsrvdIp2"*/};
		   return header;
	   }

	   /*function to generate Link wise ip scheme data*/
	   public  void displayLinkwiseipScheme(Section section) throws DocumentException{
		   List<Link> link=dbService.getLinkService().FindAll(networkid);
		   List<Object> data = new ArrayList<Object>();
		   for(int j=0;j<link.size();j++){
			   
			   
			   List<IpSchemeLink> linkip = dbService.getIpSchemeLinkService().FindIpSchemeLinkwise(networkid, link.get(j).getLinkId());
//			   Object[][] data = new Object[linkip.size()][6];		 
			
			   for(int i=0;i<linkip.size();i++){
							   
				   data.add(linkip.get(i).getNetworkId());
				   data.add(linkip.get(i).getLinkId());
				   data.add(IPv4.integerToStringIP(linkip.get(i).getLinkIp()));
				   data.add(IPv4.integerToStringIP(linkip.get(i).getSrcIp()));
				   data.add(IPv4.integerToStringIP(linkip.get(i).getDestIp()));
				   data.add(IPv4.integerToStringIP(linkip.get(i).getSubnetMask()));
//							data[i][0]=linkip.get(i).getNetworkId();
//							data[i][1]=linkip.get(i).getLinkId();
//							data[i][2]=IPv4.integerToStringIP(linkip.get(i).getLinkIp());
//							data[i][3]=IPv4.integerToStringIP(linkip.get(i).getSrcIp());
//							data[i][4]=IPv4.integerToStringIP(linkip.get(i).getDestIp());
//							data[i][5]=IPv4.integerToStringIP(linkip.get(i).getSubnetMask());
//							data[i][6]=nodeip.get(i).getMcpSubnet();
//							data[i][7]=nodeip.get(i).getMcpGateway();
//							data[i][8]=nodeip.get(i).getRsrvdIp1();
//							data[i][9]=nodeip.get(i).getRsrvdIp2();
				

						   }
//			  createTable(section, data, generateIpSchemeLinkHeader(), "Link Ip Scheme For Link  "+(j+1));
			  
		   }
		   planningReportUtils.displaynodewiseschemeTable(section, data, generateIpSchemeLinkHeader(), "Link Ip Scheme",0);
		  
	   }
	   /*function to generate ipschemelink table header*/
	   public String[] generateIpSchemeLinkHeader(){
		   String[] header={"NetworkId","LinkId","LinkIp","End1 Ip","End2 Ip","SubnetMask"};
		   return header;
	   }
	
	/*function to display network equipments */
	   public void displayNetworkEquipment(Document document,Object[] info,int count,PdfWriter writer) throws DocumentException, SQLException, IOException{
		   Chapter chapter;
         
           Section section;
         
           Paragraph para = new Paragraph("Network Equipment",chapterfont);
           chapterfont.setColor(BaseColor.BLUE);
           para.setAlignment(Element.ALIGN_CENTER);
           chapter = new Chapter(count+1);
           planningReportUtils.createChapter(count,chapter,writer);
           planningReportUtils.addEmptyLine(para,1);
          
           para = new Paragraph("General Information",subFont);
           para.setIndentationLeft(50);
         
           section=chapter.addSection(para);
           planningReportUtils.createDataTable(section,generateNetworkEquipmentFields(),generateNetworkEquipment());
           document.add(chapter);
    
           para = new Paragraph("NodeWise Price Distribution",subFont);
           para.setIndentationLeft(50);
           section=chapter.addSection(para);
           planningReportUtils.addDescription(section, "The graph below depicts the total price of each node in the network.");

           document.add(section);
           
           planningReportUtils.addEmptyLine(para, 2);

 			/*function to display node v\s price bar chart */
           planningReportUtils.writeChartToPDF(section,planningReportUtils.generateBarChart(generateNodePricechartdata(),"Node Price Chart","NodeId","Total Price($)"/*,"orange"*/),chartWidth,chartHeight,writer.getVerticalPosition(true)-chartHeight-20,writer);
 			document.newPage();
 			para = new Paragraph("Nodewise Typical And Actual Power",subFont);
            para.setIndentationLeft(50);
            section=chapter.addSection(para);
            document.add(section);
           
            planningReportUtils.addEmptyLine(para, 2);

  			/*calling Function to display comparitive chart between typical power and total power*/
            planningReportUtils.writeChartToPDF(section,planningReportUtils.GenerateClusterChart(dbService,generatePowerchartdata(),"Typical Power and Total power chart","NodeId","Power(Watt)",networkid),chartWidth,chartHeight,writer.getVerticalPosition(true)-chartHeight-20,writer);
  			
           //generateNodewisecluster();
  			
            document.newPage();
           para = new Paragraph("Nodewise BOM",subFont);
           para.setIndentationLeft(50);
           section=chapter.addSection(para);
          
          
           planningReportUtils.createTable(section,generateBomTableData(),generateBomTableHeader(),"Bom Table");
           planningReportUtils.addEmptyLine(para,1);
           document.add(section);
           para = new Paragraph("Nodewise Cabling Information",subFont);
//           planningReportUtils.createTable(section,generateBomTableData(),generateBomTableHeader(),"NodeWise Cabeling Table");
           para.setIndentationLeft(50);
           section=chapter.addSection(para);
           displayNodewiseCablingData(section);
         
           document.add(section);
           logger.info("The Control is here...in Equip");
           
           
//           planningReportUtils.GenerateNodeLinkWaveClusterChart(dbService);
           
	   }
	   
	   /*function to generate data for network Equipment */
	   
	   public  Object[] generateNetworkEquipment(){
		  int networkcost;
		  int mincost;
		  int maxcost;
		  
		   
		   				//getting network cost from the database through dbservice.
		  networkcost=dbService.getViewServiceRp().TotalCost(networkid);
		  				//calculating maximumcost of nodes
		  maxcost=dbService.getViewServiceRp().MaxTotalPrice(networkid);
		 
		  				//calculating minimum cost of the nodes
		  mincost=dbService.getViewServiceRp().MinTotalPrice(networkid);
		  
		   
		   				//filling data in Object array to be returned
		  	Object[] data = new Object[3];
		   data[0] =networkcost;
		   data[1] =mincost;
		   data[2] =maxcost;
		  
		   return data;
	   }
	   /*function to generate the data for the node table*/
	    public Object[][] generateNodeTabledata()
	    {
	        List <Node> node=dbService.getNodeService().FindAll(networkid);
	        Object[][] data = new Object[node.size()][10];
	        	//making array for node table data
         

	        for (int i = 0; i < node.size(); i++)
	        {
//	            data[i][0]=node.get(i).getNetworkId();
	            data[i][0]=node.get(i).getNodeId();
	            data[i][1]=node.get(i).getSiteName();
	            data[i][2]=node.get(i).getStationName();
	            data[i][3]=planningReportUtils.findnodetype(node.get(i).getNodeType());
	            data[i][4]=node.get(i).getNodeSubType();
	            data[i][5]=node.get(i).getDegree();
	            data[i][6]=node.get(i).getIp();
	            data[i][7]=planningReportUtils.typeisgne(node.get(i).getIsGne());
//	            data[i][9]=node.get(i).getVlanTag();
//	            data[i][10]=node.get(i).getEmsSubnet();
//	            data[i][11]=node.get(i).getEmsGateway();
//	            data[i][12]=node.get(i).getIpV6Add();
	            data[i][8]=planningReportUtils.getNodeCapacity(node.get(i).getCapacity());
//	            data[i][9]=node.get(i).getDirection();
	            data[i][9]=node.get(i).getOpticalReach();
	            
	        }
	        return data;
	    }
	    /* function to generate the node table header data*/
	    public String[] generateNodeTableHeader(){
	    	 String[] header = {"NodeId","SiteName","StationName","NodeType","NodeSubType",
		        		"Degree","Ip","IsGne","Capacity","OpticalReach"};
	    	 return header;
	    }
	    /* function to generate data for link table*/
	   public Object[][] generateLinkTableData(){
		   List <Link> link=dbService.getLinkService().FindAll(networkid);
		   				//making array for link table data
		   Object[][] data = new Object[link.size()][16];
		   for(int i=0;i<link.size();i++){
//			   data[i][0]=link.get(i).getNetworkId();
			   data[i][0]=link.get(i).getLinkId();
			   data[i][1]=link.get(i).getSrcNode();
			   data[i][2]=link.get(i).getDestNode();
			   data[i][3]=planningReportUtils.getcolour(link.get(i).getColour());
			   data[i][4]=link.get(i).getMetricCost();
			   data[i][5]=link.get(i).getLength();
//			   data[i][7]=link.get(i).getSpanLoss();
//			   data[i][6]=link.get(i).getCapacity();
			   data[i][6]=link.get(i).getFibreType();
			   data[i][7]=link.get(i).getSrlgId();
			   data[i][8]=link.get(i).getNSplices();
			   data[i][9]=link.get(i).getLossPerSplice();
			   data[i][10]=link.get(i).getNConnector();
			   data[i][11]=link.get(i).getLossPerConnector();
			   data[i][12]=link.get(i).getCalculatedSpanLoss();
			   data[i][13]=link.get(i).getSpanLossCoff();
//			   data[i][15]=link.get(i).getCdCoff();
			   data[i][14]=link.get(i).getCd();
//			   data[i][17]=link.get(i).getPmdCoff();
			   data[i][15]=link.get(i).getPmd();
//			   data[i][21]=link.get(i).getSrcNodeDirection();
//			   data[i][22]=link.get(i).getDestNodeDirection();
		   }
		   return data;
		  		  		   
	   }
	   /*function to generate data for the header of Link table*/
	   public String[] generateLinkTableHeader(){
		   String[] header={"LinkId","SrcNode","DestNode",
				   		"Colour","MetricCost","Length(KM)"/*,"Capacity"*/,
				   		"FibreType","SrlgId","NSplices","LossPerSplice(DB)",
				   		"NConnector","LossPerConnector","CalculatedSpanLoss",
				   		"SpanLossCoff",/*"CdCoff",*/"Cd(ps/nm)"/*,"PmdCoff"*/,"Pmd"};
		   return header;
		   }
	   
	   /* function to generate data for Circuit table*/
	   public Object[][] generateCircuitTableData(){
		   List <Circuit> circuit=dbService.getCircuitService().FindAll(networkid);
		   				//making array for circuit table data
		   Object[][] data = new Object[circuit.size()][11];
		   for(int i=0;i<circuit.size();i++){
			   data[i][0]=circuit.get(i).getNetworkId();
			   data[i][1]=circuit.get(i).getCircuitId();
			   data[i][2]=circuit.get(i).getSrcNodeId();
			   data[i][3]=circuit.get(i).getDestNodeId();
			   data[i][4]=circuit.get(i).getRequiredTraffic();
			   data[i][5]=circuit.get(i).getTrafficType();
			   data[i][6]=circuit.get(i).getProtectionType();
			   data[i][7]=circuit.get(i).getClientProtectionType();
			   data[i][8]=circuit.get(i).getColourPreference();
			   data[i][9]=circuit.get(i).getPathType();
			   data[i][10]=circuit.get(i).getLineRate();
			 
		   }
		   return data;
		  		  		   
	   }
	   /*function to generate data for the header of Circuit table*/
	   public String[] generateCircuitTableHeader(){
		   String[] header={"NetworkId","CircuitId","SrcNodeId",
				   		"DestNodeId","RequiredTraffic(G)","TrafficType","ProtectionType",
				   		"ClientProtectionType","ColourPreference","PathType","LineRate(G)"};
		   return header;
		   }
	   
	   /* function to generate data for LinkwiseWavelengthPopulation  bar chart*/
	   public Object[][] generateLinkwiseWavelengthchartdata() throws SQLException{
		   					//fetching data from link table
		   List <Link> link=dbService.getLinkService().FindAll(networkid);
		  
		   			//Creating a Object array for storing data to be passed to the chart
		   Object[][] a = new Object[link.size()][3];
			  for(int i=0;i<link.size();i++)
			  {
				  
				   int linkid=link.get(i).getLinkId();
				 
				   LinkWavelength lw = dbService.getLinkWavelengthService().Find(networkid, linkid);
				   int wavelength = dbService.getLinkWavelengthInfoService().FindTotalWavelengths(networkid, linkid);
				   try
				   {
					   		//fetching wavelengths from linkWavelength table
				   String waves= lw.getWavelengths();
				   
				  if(waves!=null){
					  		//Separating wavelengths by(,) received as a string
				 
				   			//integer to store the number of wavelengths
//				   int height = WavelengthList.size();
				   		
				   			//filling data to the array
							   		a[i][0]=wavelength;
								   a[i][1]="Wavelengths";
								   a[i][2]=linkid;
				  }
				   }catch(NullPointerException e)
				   {
					   			//Displaying the linkid at which an empty wavelength is present
					   logger.info("Empty wavelength field was found at "+linkid);
				   }
		  
		  }
		   
		   return a;
	   }
//	   /* function to generate data for LinkwiseWavelengthTraffic  bar chart*/
//	   public Object[][] generateLinkwiseTrafficchartdata() throws SQLException{
//		   					
//		   List<Link> link = dbService.getLinkService().FindAll();
//		   						//fetching data from link wavelength info table
//		 
//		  
//		   			//Creating a Object array for storing data to be passed to the chart
//		   Object[][] a = new Object[linkWvInfo.size()][3];
//			  for(int i=0;i<link.size();i++)
//			  {
//				  List <LinkWavelengthInfo> linkWvInfo=dbService.getLinkWavelengthInfoService().FindAllByLink(networkid, link.get(i).getLinkId());
//				  int traffic = (int) linkWvInfo.get(i).getTraffic(); 
//				  int wavelength = linkWvInfo.get(i).getWavelength();
//				  
//				   try
//				   {
//					   		//fetching wavelengths from linkWavelength table
//				   String waves= lw.getWavelengths();
//				   
//				  if(waves!=null){
//					  		//Separating wavelengths by(,) received as a string
//				 
//				   			//integer to store the number of wavelengths
////				   int height = WavelengthList.size();
//				   		
//				   			//filling data to the array
//							   		a[i][0]=wavelength;
//								   a[i][1]="Wavelengths";
//								   a[i][2]=linkid;
//				  }
//				   }catch(NullPointerException e)
//				   {
//					   			//Displaying the linkid at which an empty wavelength is present
//					   logger.info("Empty wavelength field was found at "+linkid);
//				   }
//		  
//		  }
//		   
//		   return a;
//	   }
	   
	   /* function to generate data for LinkwiseWavelengthPopulation  bar chart*/
	   public Object[][] generateNodewiseWavelengthchartdata() throws SQLException{
		   					//fetching data from link table
		   List <Node> node=dbService.getNodeService().FindAll(networkid);
		  
		   			//Creating a Object array for storing data to be passed to the chart
		   Object[][] a = new Object[node.size()][8];
			  for(int i=0;i<node.size();i++)
			  {
				  
				   int nodeid=node.get(i).getNodeId();
				 
//				   LinkWavelength lw = dbService.getLinkWavelengthService().Find(networkid, linkid);
				   int wavelengthEast = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.EAST);
				   int wavelengthWest = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.WEST);
				   int wavelengthNorth = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.NORTH);
				   int wavelengthSouth = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.SOUTH);
				   int wavelengthNE = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.NE);
				   int wavelengthNW = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.NW);
				   int wavelengthSE = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.SE);
				   int wavelengthSW = dbService.getCardInfoService().FindCountWorkingMpns(networkid, nodeid,MapConstants.SW);

				   try
				   {
					   		//fetching wavelengths from linkWavelength table
//				   String waves= lw.getWavelengths();
				   
//				  if(waves!=null){
					  		//Separating wavelengths by(,) received as a string
				 
				   			//integer to store the number of wavelengths
//				   int height = WavelengthList.size();
//				   		if(wavelength>0){
				   			//filling data to the array
							   		a[i][0]=wavelengthEast;
								   a[i][1]=wavelengthWest;
								   a[i][2]=wavelengthNorth;
								   a[i][3]=wavelengthSouth;
								   a[i][4]=wavelengthNE;
								   a[i][5]=wavelengthNW;
								   a[i][6]=wavelengthSE;
								   a[i][7]=wavelengthSW;
								  
//				  }
				   }catch(NullPointerException e)
				   {
					   			//Displaying the linkid at which an empty wavelength is present
					   logger.info("Empty wavelength field was found at "+nodeid);
				   }
		  
		  }
		   
		   return a;
	   }
	   
	   /* function to generate data for node v\s price bar chart*/
	   public Object[][] generateNodePricechartdata() throws SQLException{
		   			//fetching data from node table
		   List <Node> node=dbService.getNodeService().FindAll(networkid);
		   			//creating a array to store data for bar chart
		   Object[][] a = new Object[node.size()][3];
		   		for(int i = 0; i < node.size(); i++){
		   							//fetching total price from bom table for each node
		        	   List<Map<String, Object>>  Price= dbService.getViewServiceRp().FindsumTotalPrice(networkid,node.get(i).getNodeId());
		        	  
		        		  			//creating a list to store the total price fetched
		        		   List<Object> Totalprice=new ArrayList<Object>();
		        		   			//traversing the list to find out totalPrice
						   for (Map<String, Object> abc : Price) 
						   {													 								   
								       for (Map.Entry<String, Object> entry : abc.entrySet()) 
										       {
//								    	   			nodeid.add(entry.getKey());
								    	   			Totalprice.add(entry.getValue());
										          
										       	}
						   }
						   		//filling data in array to be sent
						   a[i][0]= Totalprice.get(i);
				  			a[i][1]="Total Price";
				  			
				  			a[i][2]=node.get(i).getNodeId();
				  		
   
				   			}
	   return a;
	   }
	   
	   
	   /*function to generate data for typical power and power consumption clustered chart*/
	   public Object[][] generatePowerchartdata() throws SQLException{
		   				//fetching data from node table
		   List <Node> node=dbService.getNodeService().FindAll(networkid);
		   		   		//creating a object array to store the data
		   Object[][] a = new Object[node.size()][3];
		   
				   			
				   for(int i = 0; i < node.size(); i++){
					   				//fetching typical power data from the bom table
		        	   List<Map<String, Object>> typicalpower= dbService.getViewServiceRp().FindTotalTypicalPower(networkid,node.get(i).getNodeId());
		        	   				//fetching total power data from the bom table
		        	   List<Map<String, Object>> totalpower= dbService.getViewServiceRp().FindTotalPowerConsumption(networkid,node.get(i).getNodeId());
		        		  			//creating two liststo store the typical power and total power to be passed to the graph
		        	   		List<Object> Typicalpower=new ArrayList<Object>();
		        		   List<Object> Totalpower=new ArrayList<Object>(); 
		        		   			//traversing the typical power list to find out the typical power
						   for (Map<String, Object> abc : typicalpower) 
						   {							  						 								   
								       for (Map.Entry<String, Object> entry : abc.entrySet()) 
										       {
//								    	   			nodeid.add(entry.getKey());
								    	   Typicalpower.add(entry.getValue());
										          
										       	}
						   }
						   		//traversing the total power list to find out the total power
						   for (Map<String, Object> abc : totalpower) 
						   {							  						 								   
								       for (Map.Entry<String, Object> entry : abc.entrySet()) 
										       {
//								    	   			nodeid.add(entry.getKey());
								    	   Totalpower.add(entry.getValue());
										          
										       	}
						   }
						   //filling data in the array to be sent for the graph						  						   
						   a[i][0]= Typicalpower.get(i);
				  			a[i][1]=node.get(i).getNodeId();
				  			a[i][2]=Totalpower.get(i);						   
				   }
				   return a;
																										//		   Integer totalPrice[] =new Integer[bomData.size()];
				   }					
	   
	   /* function to fetch data for nodeWise bom*/
	   public Object[][] generateBomTableData(){
		   			//fetching data from the bom table
			List<Bom> bomData = dbService.getViewServiceRp().FindBoM(networkid);
					//creating a object array to store data 
			Object[][] data = new Object[bomData.size()][11];
					//filling data in the array
			for (int i = 0; i < bomData.size(); i++) {
				data[i][0] = bomData.get(i).getNodeId();
				data[i][1] = bomData.get(i).getStationName();
				data[i][2] = bomData.get(i).getSiteName();
				data[i][3] = bomData.get(i).getName();
				data[i][4] = bomData.get(i).getQuantity();
				data[i][5] = bomData.get(i).getPrice();
				data[i][6] = bomData.get(i).getTotalPrice();
				data[i][7] = bomData.get(i).getPartNo();
				data[i][8] = bomData.get(i).getPowerConsumption();
				data[i][9] = bomData.get(i).getTypicalPower();
				data[i][10] = bomData.get(i).getRevisionCode();
//				data[i][11] = bomData.get(i).getCategory();
				//data[i][12] = bomData.get(i).getLocation();
			}
	        
	        return data;
	   }
	   
	 
	   /* function to generate the Bom table header data*/
	    public String[] generateBomTableHeader(){
	    	 String[] header = {"NodeId","StationName","SiteName","Name","Quantity",
		        		"Price","TotalPrice","PartNo","PowerConsumption","TypicalPower","RevisionCode"/*,"Category"*/};

	    	 return header;
	    }
	    
	    /* function to fetch data for nodeWise cabeling Information*/
		   public void displayNodewiseCablingData(Section section) throws DocumentException{
			   List<Node> node= dbService.getNodeService().FindAll(networkid);
			   Equipment equipment;
			   
			   for(int j=0;j<node.size();j++){
			   			//fetching data from the pathcord table
				List<PatchCord> patchData = dbService.getPatchCordService().FindAll(networkid,node.get(j).getNodeId());
						//creating a object array to store data 
				Object[][] data = new Object[patchData.size()][12];
						//filling data in the array
				if(!patchData.isEmpty()){
				for (int i = 0; i < patchData.size(); i++) {
//					data[i][0] = patchData.get(i).getNodeKey();
					data[i][0] = patchData.get(i).getNodeId();
					data[i][1] = patchData.get(i).getEquipmentId();
					equipment= dbService.getEquipmentService().findEquipmentById(patchData.get(i).getEquipmentId());
					data[i][2]=equipment.getPartNo();
					data[i][3] = patchData.get(i).getCordType();
					data[i][4] = patchData.get(i).getEnd1CardType();
					data[i][5] = patchData.get(i).getEnd1Location();
					data[i][6] = patchData.get(i).getEnd1Port();
					data[i][7] = patchData.get(i).getEnd2CardType();
					data[i][8] = patchData.get(i).getEnd2Location();
					data[i][9] = patchData.get(i).getEnd2Port();
					data[i][10] = patchData.get(i).getLength();
					data[i][11] = patchData.get(i).getDirectionEnd1();
//					data[i][11] = patchData.get(i).getCategory();
					//data[i][12] = patchData.get(i).getLocation();
				}
				 planningReportUtils.createTable(section, data, generateCableTableHeader(), "Cabling Information Table for Node "+(j+1));
			   }
			   }
		        
		       
		   }
		   /* function to generate the Cable table header data*/
		    public String[] generateCableTableHeader(){
		    	 String[] header = {"NodeId","Equipment","Chord Type","Part Code","Source Card Type","source Location",
			        		"Source Port","Target Card Type","Target Location","Target Port","Length","Direction"/*,"Category"*/};

		    	 return header;
		    }
	   
	  
	    /* function to fetch data for nodeWise cardInfo*/
		   public void displaycardInfoTableData(Section section) throws DocumentException{	
			   			//fetching node data from the database
			   List <Node> node=dbService.getNodeService().FindAll(networkid);			    
			           for(int i = 0; i < node.size(); i++){		//traversing the list to find out data
			        	   List<Map<String, Object>>  cardInfo= dbService.getCardInfoService().FgetCircuitMatix(networkid,node.get(i).getNodeId());
			        	  
			        		   List<String> header=new ArrayList<String>();
			        		   List<Object> data=new ArrayList<Object>();;
			        	  
							   for (Map<String, Object> abc : cardInfo) 
							   {								 
									       for (Map.Entry<String, Object> entry : abc.entrySet()) 
											       {											    												    	   
											        	   header.add(entry.getKey());
											        	   data.add(entry.getValue());											          
											       	}
							   }
							   					//passing the data generated for each node to the function to display the table for individual nodes
							   planningReportUtils.createCardInfoTable(section, data, header, "For Node ",i+1);							   
			           }			           
		   }
		   /* function to generate the cardInfo table header data*/
		    public String[] generatecardInfoTableHeader(){
		    	 String[] header = {"String","Object",};
		    	 return header;
		    }
		    
	   
	 /* function to display header and footer*/
	        class MyFooter extends PdfPageEventHelper {
	            Font footerfont = new Font(Font.FontFamily.UNDEFINED, 9, Font.ITALIC);
	            Font headerfont = new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD);

	            public void onEndPage (PdfWriter writer, Document document) {
	            	
	            	
	                PdfContentByte cb = writer.getDirectContent();
	                Phrase header = new Phrase("Network Planning Report V01", headerfont);//setting header text
	                
	                
	                Phrase footer = new Phrase(" NPT Copyright @ CDOT,2017 ", footerfont);//setting footer text
	                ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
	                        header,
	                         document.right() -document.rightMargin()- 140,
	                        document.top() + 10, 0);//setting position and displaying header
	              
	                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
	                        footer,
	                        (document.right() - document.left()) / 2 + document.leftMargin(),
	                        document.bottom() - 10, 0);//setting position and displaying footer
	               
	                //drawing footer
	                cb.setLineWidth(2.0f);	// Make a bit thicker than 1.0 default 
	                cb.setGrayStroke(0f); // 0 = black, 1 = white 
	                float x = 72f; 
	                float y = 72f; 
	                cb.moveTo(x,         y); 
	                cb.lineTo(x + 72f*6, y); 
	                cb.stroke(); 
	                
	                //drawing header
	                cb.setLineWidth(2.0f);	// Make a bit thicker than 1.0 default 
	                cb.setGrayStroke(0f); // 0 = black, 1 = white 
	                float hx = document.left()+document.leftMargin(); 
	                float hy = document.top() + 5; 
	                cb.moveTo(hx,         hy); 
	                cb.lineTo(hx + 72f*6, hy); 
	                cb.stroke(); 
	            }
	        }
	        
	        /* function to fetch data for nodeWise inventory information*/
	 	   public void generateInventoryTableData(Section section) throws DocumentException{
	 		   
	 		   List<Node> node= dbService.getNodeService().FindAll(networkid);
	 		   			//fetching data from the cardinfo table
	 		 
	 		   for(int i=0;i<node.size();i++){
	 			  
	 			List<CardInfo> cardData = dbService.getCardInfoService().FindAll(networkid,node.get(i).getNodeId());
	 			 Object[][] data= new Object[cardData.size()][11];
//	 			logger.info(cardData);
	 					//creating a object array to store data 
	 			 for(int j=0;j<cardData.size();j++){
	 					//filling data in the array
	 			
//	 				data.add(cardData.get(i).getNodeKey());
//	 				data.add(cardData.get(i).getRack());
//	 				data.add( cardData.get(i).getSbrack());
//	 				data.add(cardData.get(i).getCard());
//	 				data.add(cardData.get(i).getCardType());
//	 				data.add(cardData.get(i).getDirection());
//	 				data.add( cardData.get(i).getWavelength());
//	 				data.add( cardData.get(i).getDemandId());
//	 				data.add( cardData.get(i).getEquipmentId());
//	 				data.add( cardData.get(i).getStatus());
	 				data[j][0]=cardData.get(j).getNetworkId();
	 				data[j][1]=cardData.get(j).getNodeId();
	 				data[j][2]=cardData.get(j).getRack();
	 				data[j][3]=cardData.get(j).getSbrack();
	 				data[j][4]=cardData.get(j).getCard();
	 				data[j][5]=cardData.get(j).getCardType();
	 				if((data[j][6]=cardData.get(j).getDirection())==null){
	 					data[j][6]=" ";
	 				}
	 				
	 				data[j][7]=cardData.get(j).getWavelength();
	 				data[j][8]=cardData.get(j).getDemandId();
	 				data[j][9]=cardData.get(j).getEquipmentId();
	 				if((data[j][10]=cardData.get(j).getStatus())==null){
	 					data[j][10]=" ";
	 				}
	 				
	 						
	 				if(data[j]==null){
	 					logger.info("There was a null value at node............"+i);
	 				}
	 						
	 			 }
	 				 
		 		planningReportUtils.createTable(section, data, generateInventoryTableHeader(), "Inventory Information for nodes ");	
//	 			 planningReportUtils.displaynodewiseschemeTable(section, data, generateInventoryTableHeader(), "Inventory Information for node ",node.get(i).getNodeId());

	 	   }
        
	 	   }
	 	  public String[] generateInventoryTableHeader(){
		    	 String[] header = {"NetworkId","NodeId","Rack","Subrack","Card","Card Type","Direction",
			        		"Wavelength","DemandId","EquipmentId","Status"};

		    	 return header;
		    }
}


