package application.controller;


import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.awt.DefaultFontMapper;
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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import application.constants.MapConstants;
import application.constants.ResourcePlanConstants;
import application.model.Link;
import application.model.LinkWavelengthInfo;
import application.model.Node;
import application.service.DbService;

public class PlanningReportUtils {
	private static final CategoryItemLabelGenerator CategoryItemLabelGenerator = null;

	int networkid;

	private static Font chapterfont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
			Font.BOLD);

	//	    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	//            Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			Font.NORMAL);
	private static BaseFont  baseFont ;
	Font tablefont=new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.BOLD);    
	Font tableDataFont=new Font(Font.FontFamily.TIMES_ROMAN, 9,
			Font.NORMAL);
	List<String> chaptername= new ArrayList<String>();//List for storing the chapter names to be displayed on table of contents
	List<String> sectionname= new ArrayList<String>();

	// table to store placeholder for all chapters and sections
	private final Map<String, PdfTemplate> tocPlaceholder = new HashMap<>();

	// store the chapters and sections with their title here.
	private final Map<String, Integer>     pageByTitle    = new HashMap<>();

	public void onChapter(final PdfWriter writer, final Document document, final float paragraphPosition, final Paragraph title)
	{
		this.pageByTitle.put(title.getContent(), writer.getPageNumber());
	}


	public void onSection(final PdfWriter writer, final Document document, final float paragraphPosition, final int depth, final Paragraph title)
	{
		this.pageByTitle.put(title.getContent(), writer.getPageNumber());
	}
	/*function to create frontPage*/
	public void frontpage(Document document) throws DocumentException, MalformedURLException, IOException
	{
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 40,Font.BOLDITALIC);
		Rectangle layout = new Rectangle(PageSize.A4);
		layout.setBackgroundColor(new BaseColor(255,255,255)); //Background color
		layout.setBorderColor(BaseColor.BLACK);  //Border color
		layout.setBorderWidth(6);      //Border width  
		layout.setBorder(Rectangle.BOX);  //Border on 4 sides
		document.add(layout);						   
		Image img=Image.getInstance(getClass().getClassLoader().getResource("static/images/cdot.png"));	/**Access from class loader to support in jar too */
		img.scaleToFit(70, 70);
		img.setAbsolutePosition(510, 750);
		document.add(img);
		Paragraph title;
		catFont.setColor(BaseColor.BLUE);
		title=new Paragraph("Network Planning Report",catFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingBefore(6);
		document.add(title);
		img=Image.getInstance(getClass().getClassLoader().getResource("static/images/networ.jpg"));	/**Access from class loader to support in jar too */
		img.setAlignment(Element.ALIGN_CENTER);
		//		            img.scaleToFit(300, 300);
		document.add(img);
	}
	/** DBG => For Chassis String Generation*/
	public void chassisImage(Document document) throws DocumentException, MalformedURLException, IOException
	{
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 40,Font.BOLDITALIC);
		Rectangle layout = new Rectangle(PageSize.A4);
		layout.setBackgroundColor(new BaseColor(255,255,255)); //Background color
		//		           layout.setBorderColor(BaseColor.BLACK);  //Border color
		//		           layout.setBorderWidth(6);      //Border width  
		//		           layout.setBorder(Rectangle.BOX);  //Border on 4 sides
		document.add(layout);
		//		            Image img=Image.getInstance("cdot.png");
		//		            img.scaleToFit(70, 70);
		//		            img.setAbsolutePosition(510, 750);
		//		            document.add(img);
		Paragraph title;
		catFont.setColor(BaseColor.BLUE);
		title=new Paragraph("Network Planning Report",catFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingBefore(6);
		//		            document.add(title);
		Image img=Image.getInstance("rackimage_1_1.png");
		img.setAlignment(Element.ALIGN_CENTER);
		img.scaleToFit(300, 500);
		img.setAbsolutePosition(30, 150);
		document.add(img);
	}

	/*function to display the general information*/
	public  void createDataTable(Section section,Object[] data,Object[] dbdata)
			throws DocumentException
	{   
		Font tableDataFont=new Font(Font.FontFamily.TIMES_ROMAN, 14,
				Font.NORMAL);
		tablefont.setColor(BaseColor.BLACK);		        
		tableDataFont.setColor(BaseColor.BLACK);
		PdfPTable table = new PdfPTable(2);


		for(int i=0;i<2;i++)
		{
			//generating individual cells for the table
			PdfPCell headerCell=new PdfPCell(new Phrase(""));
			headerCell.setVerticalAlignment(Element.ALIGN_TOP);
			headerCell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			headerCell.setBorder(0);
			//adding header data cells to table
			table.addCell(headerCell);
		}
		for(int i=0;i<data.length;i++)
		{

			PdfPCell dataCell=new PdfPCell(new Phrase(data[i].toString(),tableDataFont));
			dataCell.setBorder(0);
			dataCell.setHorizontalAlignment(50);
			table.addCell(dataCell);
			PdfPCell dbdataCell=new PdfPCell(new Phrase(":"+dbdata[i].toString(),tableDataFont));
			dbdataCell.setBorder(0);

			table.addCell(dbdataCell);

		}		       
		//adding table to section to display it.
		section.add(table);
	}
	/* function to add a empty line in the pdf page*/
	public  void addEmptyLine(Paragraph paragraph,int number)
	{
		for(int i=0;i<number;i++)
		{
			paragraph.add(new Paragraph(" "));
		}
	}
	/* function to display the tables*/
	public void createTable(Section section,Object[][] data,String[] header,String TableType)
			throws DocumentException
	{   			    	
		Paragraph para = new Paragraph(TableType,smallBold );
		para.setIndentationLeft(75);

		tablefont.setColor(BaseColor.BLACK);
		tableDataFont.setColor(BaseColor.DARK_GRAY);
		section.addSection(para,0);
		addEmptyLine(para,1);    

		PdfPTable table = new PdfPTable(header.length);

		try{
			for(int i=0;i<header.length;i++)
			{
				//generating individual cells for the table
				PdfPCell headerCell=new PdfPCell(new Phrase(header[i].toString(),tablefont));
				headerCell.setVerticalAlignment(Element.ALIGN_TOP);
				headerCell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				//adding header data cells to table
				table.addCell(headerCell);

			}


			for(int i=0;i<data.length;i++)
			{ 

				for(int j=0;j<data[0].length;j++ )
				{

					PdfPCell dataCell=new PdfPCell(new Phrase(data[i][j].toString(),tableDataFont));
					table.addCell(dataCell);
				}

			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}

		//adding table to section to display it.
		section.add(table);		       

	}
	/* function to display card information on each node*/
	public  void createCardInfoTable(Section section,List<Object> data,List<String> header,String TableType,int node)
			throws DocumentException
	{   
		int fault=0;
		Paragraph para = new Paragraph(TableType+node,smallBold);
		para.setIndentationLeft(75);         
		addEmptyLine(para,1);
		tablefont.setColor(BaseColor.BLACK);
		tableDataFont.setColor(BaseColor.DARK_GRAY);
		//creating table with 8 headings/columns
		PdfPTable table = new PdfPTable(14);	       
		int i;
		int j;

		try{			//making heading cells for the table
			for(i=0;i<14;i++)
			{
				if(header.get(i)!=null)
				{
					//generating individual cells for the table
					String headerData;

					if(i==8){

						headerData="Node Id";
					}
					else{
						headerData=header.get(i).toString();
					}


					PdfPCell headerCell=new PdfPCell(new Phrase(headerData,tablefont));
					headerCell.setVerticalAlignment(Element.ALIGN_TOP);
					headerCell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
					headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					//adding header data cells to table
					table.addCell(headerCell);


				}

			}				//making table data cells for the table
			for(j=0;j<data.size();j++)
			{
				if( data.get(j) !=null)
				{
					String tableData;
					if(j==8||((j%8)-6==0)){
						tableData = data.get(j).toString();
						tableData.replaceAll((networkid+"_"+node), Integer.toString(node));
						//			    			   tableData = Integer.toString(node);
					}
					else{
						tableData = data.get(j).toString();
					}


					table.addCell(new Phrase(tableData,tableDataFont));

				}
			}

		}
		catch(Exception e){		//incase data is not present at a particular node
			System.out.println("There was a null value encountered at some node: "+node);
			fault=1;
		}

		if(fault!=1){		//if the empty value is encountered then no data is displayed
			section.addSection(para,0);
		}		       


		//adding table to section to display it.
		section.add(table);

	}
	/*function to display the ip scheme tables*/
	public void displaynodewiseschemeTable(Section section,List<Object> data,String[] header,String TableType, int node)
			throws DocumentException
	{   

		Paragraph para;
		if(node>0){
			para = new Paragraph(TableType+node,smallBold );
		}
		else{
			para = new Paragraph(TableType,smallBold );
		}
		para.setIndentationLeft(75);

		tablefont.setColor(BaseColor.BLACK);
		tableDataFont.setColor(BaseColor.DARK_GRAY);
		section.addSection(para,0);
		addEmptyLine(para,1);    

		PdfPTable table = new PdfPTable(header.length);

		for(int i=0;i<header.length;i++)
		{
			//generating individual cells for the table
			PdfPCell headerCell=new PdfPCell(new Phrase(header[i].toString(),tablefont));
			headerCell.setVerticalAlignment(Element.ALIGN_TOP);
			headerCell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
			headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			//adding header data cells to table
			table.addCell(headerCell);

		}
		try{
			for(int i=0;i<data.size();i++)
			{


				table.addCell(new Phrase(data.get(i).toString(),tableDataFont));


			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}

		//adding table to section to display it.
		section.add(table);		       

	}

	/* function to generate a bar chart */
	public  JFreeChart generateBarChart(Object[][] data,String heading,String XaxisText,String YaxisText/*,String barColour*/ ) {

		//generating a dataSet for the bar chart
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		try
		{

			for(int i=0;i<data.length;i++)
			{
				if(data[i][0]!=null && data[i][1]!=null && data[i][2]!=null)

					//adding data to the dataSet    
					dataSet.setValue(Double.parseDouble(data[i][0].toString()), data[i][1].toString(),data[i][2].toString());
			}
		}catch(NullPointerException e)
		{
			System.out.println("wavelength values not found for some link");
		}
		//generating bar chart with createBarChart function
		JFreeChart chart = ChartFactory.createBarChart
				(null, XaxisText, YaxisText,
						dataSet, PlotOrientation.VERTICAL, true, true, false);
		chart.setTitle(
				new org.jfree.chart.title.TextTitle(heading,
						new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10)
						)
				);
		final CategoryPlot plot = chart.getCategoryPlot();




		//         StackedBarRenderer renderer = new StackedBarRenderer(false);
		//         renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		//         renderer.setBaseItemLabelsVisible(true);
		//         chart.getCategoryPlot().setRenderer(renderer);


		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		chart.getCategoryPlot().setRenderer(renderer);


		//         final CategoryAxis domainAxis = plot.getDomainAxis();
		//	        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		//         plot.getRangeAxis().setLowerBound(1);
		//         ((BarRenderer)plot.getRenderer()).setBarPainter(new StandardBarPainter());

		//         BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
		//         if(barColour=="black"){
		//         r.setSeriesPaint(0, Color.black);}
		//         if(barColour=="red"){
		//	         r.setSeriesPaint(0, Color.red);}
		//         if(barColour=="orange"){
		//	         r.setSeriesPaint(0, Color.orange);}
		//         if(barColour=="green"){
		//	         r.setSeriesPaint(0, Color.green);}
		//         if(barColour=="gray"){
		//	         r.setSeriesPaint(0, Color.gray);}
		//         final BarRenderer renderer1 = (BarRenderer) plot.getRenderer();
		//	        renderer1.setDrawBarOutline(false);
		//	        renderer1.setItemMargin(0.00);


		return chart;
	}
	/*function to insert bar chart in a pdf*/ 
	public void writeChartToPDF(Section section,JFreeChart chart, int width, int height,float Pageheight,PdfWriter writer) throws DocumentException
	{

		PdfContentByte contentByte = writer.getDirectContent();
		PdfTemplate template = contentByte.createTemplate(width, height);
		@SuppressWarnings("deprecation")
		Graphics2D graphics2d = template.createGraphics(width, height,
				new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,height);

		chart.draw(graphics2d, rectangle2d);
		graphics2d.dispose();
		contentByte.addTemplate(template, 50, Pageheight);


		//document.add((Element) contentByte);

	}
	/*function to create cluster bar chart*/
	public  void writeClusterChartToPDF(Document document,JFreeChart chart, int width, int height,PdfWriter writer) throws DocumentException
	{

		PdfContentByte contentByte = writer.getDirectContent();
		PdfTemplate template = contentByte.createTemplate(width, height);
		@SuppressWarnings("deprecation")
		Graphics2D graphics2d = template.createGraphics(width, height,
				new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,height);

		chart.draw(graphics2d, rectangle2d);
		final CategoryPlot plot = chart.getCategoryPlot();
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setItemMargin(0.00);
		CategoryAxis domain = plot.getDomainAxis();
		domain.setLowerMargin(0.25);
		domain.setUpperMargin(0.25);
		graphics2d.dispose();
		contentByte.addTemplate(template, 0, 0);

		//document.add((Element) contentByte);

	}
	/** function to display the clustered bar chart*/
	public  JFreeChart GenerateClusterChart(DbService dbService,Object[][] data,String heading,String XaxisText,String YaxisText,int networkid) 
	{
		//fetching data from node table
		List <Node> node=dbService.getNodeService().FindAll(networkid);
		//creating a dataset to be used by clustered bar chart

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		for(int i=0;i<node.size();i++){
			//traversing the received data to fill in the dataset
			dataSet.addValue(Integer.parseInt(data[i][0].toString()),"Typical Power",data[i][1].toString());
			dataSet.addValue(Integer.parseInt(data[i][2].toString()),"Power Comsumption",data[i][1].toString());						   				   
		}

		//creating a bar chart
		JFreeChart chart = ChartFactory.createBarChart
				(heading, XaxisText, YaxisText,
						dataSet, PlotOrientation.HORIZONTAL, true, true, false);
		chart.setTitle(
				new org.jfree.chart.title.TextTitle(heading,
						new java.awt.Font("SansSerif", java.awt.Font.BOLD, 10))

				);
		final CategoryPlot plot = chart.getCategoryPlot();
		//	       StackedBarRenderer renderer = new StackedBarRenderer(true);
		//	         renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		//	         renderer.setBaseItemLabelsVisible(true);
		//	         chart.getCategoryPlot().setRenderer(renderer);
		//	         
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		NumberFormat formatter = null;
		String labelFormat = null;
		CategoryAxis axis = plot.getDomainAxis();
		axis.DEFAULT_AXIS_LABEL_FONT.getSize();
		//				renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(labelFormat, formatter));
		renderer.setBaseItemLabelsVisible(true);
		//		        ItemLabelPosition il=new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.TOP_CENTER  );
		//		        renderer.setBasePositiveItemLabelPosition(il);
		CategoryItemLabelGenerator generator = CategoryItemLabelGenerator;
		//		        renderer.setBaseItemLabelGenerator( generator.generateColumnLabel(dataSet, 1));
		//		        renderer.getSeriesItemLabelGenerator(Integer.parseInt(generator.generateLabel(dataSet, 1, 1)));
		//		        renderer.getSeriesItemLabelGenerator(Integer.parseInt(data[1][0].toString()));
		//to stick the bars together
		renderer.setItemMargin(0.00);		       
		return chart;

	}
	/** function to display the clustered bar chart for nodewise wavelength information*/
	public  JFreeChart GenerateNodeWaveClusterChart(DbService dbService,Object[][] data,String heading,String XaxisText,String YaxisText,int networkid) 
	{
		//fetching data from node table
		List <Node> node=dbService.getNodeService().FindAll(networkid);
		//creating a dataset to be used by clustered bar chart

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		for(int i=0;i<node.size();i++){
			//traversing the received data to fill in the dataset

			dataSet.addValue(Integer.parseInt(data[i][0].toString()),"East",String.valueOf(node.get(i).getNodeId()));
			dataSet.addValue(Integer.parseInt(data[i][1].toString()),"West",String.valueOf(node.get(i).getNodeId()));
			dataSet.addValue(Integer.parseInt(data[i][2].toString()),"North",String.valueOf(node.get(i).getNodeId()));
			dataSet.addValue(Integer.parseInt(data[i][3].toString()),"South",String.valueOf(node.get(i).getNodeId()));
			dataSet.addValue(Integer.parseInt(data[i][4].toString()),"NE",String.valueOf(node.get(i).getNodeId()));
			dataSet.addValue(Integer.parseInt(data[i][5].toString()),"NW",String.valueOf(node.get(i).getNodeId()));
			dataSet.addValue(Integer.parseInt(data[i][6].toString()),"SE",String.valueOf(node.get(i).getNodeId()));
			dataSet.addValue(Integer.parseInt(data[i][7].toString()),"SW",String.valueOf(node.get(i).getNodeId()));

		}

		//creating a bar chart
		JFreeChart chart = ChartFactory.createStackedBarChart
				(heading, XaxisText, YaxisText,
						dataSet, PlotOrientation.VERTICAL, true, true, false);
		chart.setTitle(
				new org.jfree.chart.title.TextTitle(heading,
						new java.awt.Font("SansSerif", java.awt.Font.BOLD, 10))

				);
		final CategoryPlot plot = chart.getCategoryPlot();



		StackedBarRenderer renderer1 = new StackedBarRenderer(false);
		renderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer1.setBaseItemLabelsVisible(true);
		chart.getCategoryPlot().setRenderer(renderer1);
		NumberFormat formatter = null;
		String labelFormat = null;

		return chart;

	}
	/** function to display the clustered bar chart for nodewise linkwise wavelength information
	 * @return */
	public  JFreeChart GenerateNodeLinkWaveClusterChart(DbService dbService,/*Object[][] data,*/String heading,String XaxisText,String YaxisText,int networkid) 
	{
		//fetching data from node table
		System.out.println("The Control Has reached The function.....");
		//		 List <Node> node=dbService.getNodeService().FindAll(networkid);
		String dir;
		int wvln ;
		int Linkid;
		float traffic;
		int nodeid;

		List<Node> node= dbService.getNodeService().FindAll(networkid);
		for(int i=0;i<node.size();i++){
			List<Map<String,Object>> lw = dbService.getLinkWavelengthInfoService().FindNodeWiseAllLambdaInfo(networkid, node.get(i).getNodeId());
			for (Map<String, Object> lmap : lw) {

				dir =(String) lmap.get("Direction");
				wvln = (int)lmap.get("Wavelength");
				Linkid = (int)lmap.get("linkid");
				traffic = (float)lmap.get("Traffic");

				System.out.println("Direction  "+dir);
				System.out.println("wavelength "+wvln);
				System.out.println("Linkid   "+Linkid);
				System.out.println("Traffic   "+traffic);
			}
		}
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		/*for(int i=0;i<node.size();i++){

			 System.out.println("The nodeid being processed is "+(i+1));
			 int linkid = dbService.getLinkService().FindLinkId(node.get(i).getNodeId());
			 System.out.println("This is the linkid selected "+linkid);
			 for(int j=0;j<linkid;j++){


			   List <LinkWavelengthInfo> linkWvInfo=dbService.getLinkWavelengthInfoService().FindAllByLink(networkid, linkid);
			   System.out.println("the lnkwavelengthinfo is "+linkWvInfo);
			   String s1 = String.valueOf(linkid)+"("+String.valueOf(linkWvInfo.get(j).getWavelength())+")";
			   String s2 =String.valueOf(node.get(i).getNodeId());
			   System.out.println(s1);
			   System.out.println(s2);*/
		//			   dataSet.addValue(linkWvInfo.get(j).getTraffic(),s1,s2);

		for(int i=0;i<node.size();i++){
			List<Map<String,Object>> lw = dbService.getLinkWavelengthInfoService().FindNodeWiseAllLambdaInfo(networkid, node.get(i).getNodeId());
			for (Map<String, Object> lmap : lw) {

				dir =(String) lmap.get("Direction");
				wvln = (int)lmap.get("Wavelength");
				Linkid = (int)lmap.get("linkid");
				traffic = (float)lmap.get("Traffic");
				dataSet.addValue(traffic, node.get(i).getNodeId()+"("+wvln+")",String.valueOf(node.get(i).getNodeId()));
			}


		}
		//dataSet.addValue(10,"1(40)","1"/**linkid*/);
		/** dataSet.addValue(10,"1(40)","2");
			   dataSet.addValue(10,"1(40)","3");




			   dataSet.addValue(10,"1(30)","1");
			   dataSet.addValue(10,"1(30)","2");
			   dataSet.addValue(10,"1(30)","3");

			   dataSet.addValue(10,"1(20)","1");
			   dataSet.addValue(10,"1(20)","2");
			   dataSet.addValue(10,"1(20)","3");

			   dataSet.addValue(10,"2(40)","1");
//			   dataSet.addValue(10,"36(40)","1");
			   dataSet.addValue(10,"2(40)","2");
			   dataSet.addValue(10,"2(40)","3");

			   dataSet.addValue(10,"2(30)","1");
//			   dataSet.addValue(14,"36(30)","1");
			   dataSet.addValue(10,"2(30)","2");
			   dataSet.addValue(10,"2(30)","3");

			   dataSet.addValue(10,"2(20)","1");
//			   dataSet.addValue(12,"56(50)","1");
			   dataSet.addValue(10,"2(20)","2");
			   dataSet.addValue(10,"2(20)","3");

			   dataSet.addValue(10,"3(40)","1");
			   dataSet.addValue(10,"3(40)","2");
			   dataSet.addValue(10,"3(40)","3");

			   dataSet.addValue(10,"3(30)","1");
			   dataSet.addValue(10,"3(30)","2");
			   dataSet.addValue(10,"3(30)","3");

			   dataSet.addValue(10,"3(20)","1");
			   dataSet.addValue(10,"3(20)","2");
			   dataSet.addValue(10,"3(20)","3");*/

		/* }

}*/


		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		/* for(int i=0;i<node.size();i++){
			 int linkid = dbService.getLinkService().FindLinkId(node.get(i).getNodeId());
			 for(int j=0;j<linkid;j++){
			   List <LinkWavelengthInfo> linkWvInfo=dbService.getLinkWavelengthInfoService().FindAllByLink(networkid, linkid);

			 String s1 = String.valueOf(linkid)+"("+String.valueOf(linkWvInfo.get(j).getWavelength())+")";
			 String s2 =String.valueOf(node.get(i).getNodeId());*/



		//		 List<Node> node1= dbService.getNodeService().FindAll(networkid);
		SubCategoryAxis domainAxis = new SubCategoryAxis("Direction/ Nodeid");
		domainAxis.setCategoryMargin(0.05);
		//	        domainAxis.addSubCategory("1");
		//	        domainAxis.addSubCategory("2");
		//	        domainAxis.addSubCategory("3");

		KeyToGroupMap map = new KeyToGroupMap("G1");
		for(int i1=0;i1<node.size();i1++){
			nodeid = node.get(i1).getNodeId();

			List<Map<String,Object>> lw1 = dbService.getLinkWavelengthInfoService().FindNodeWiseAllLambdaInfo(networkid, node.get(i1).getNodeId());
			for (Map<String, Object> lmap : lw1) {



				dir =(String) lmap.get("Direction");
				wvln = (int)lmap.get("Wavelength");
				Linkid = (int)lmap.get("linkid");
				traffic = (float)lmap.get("Traffic");

				System.out.println("Direction  "+dir);
				System.out.println("wavelength "+wvln);
				System.out.println("Linkid   "+Linkid);
				System.out.println("Traffic   "+traffic);


				map.mapKeyToGroup(nodeid+"("+wvln+")", dir/**linkid*/);
			}
		}
		/** domainAxis.addSubCategory(MapConstants.NORTH);
	        domainAxis.addSubCategory(MapConstants.SOUTH);
	        domainAxis.addSubCategory(MapConstants.EAST);
	        domainAxis.addSubCategory(MapConstants.WEST);
	        domainAxis.addSubCategory(MapConstants.NE);
	        domainAxis.addSubCategory(MapConstants.NW);
	        domainAxis.addSubCategory(MapConstants.SE);
	        domainAxis.addSubCategory(MapConstants.SW);*/
		domainAxis.addSubCategory(1);
		domainAxis.addSubCategory(2);
		domainAxis.addSubCategory(3);
		domainAxis.addSubCategory(4);
		domainAxis.addSubCategory(5);
		domainAxis.addSubCategory(6);
		domainAxis.addSubCategory(7);
		domainAxis.addSubCategory(8);

		//	        map.mapKeyToGroup(s1, s2);
		/* map.mapKeyToGroup("1(40)", "G1"*//**linkid*//*);
		 *//**(nodeid(wavelength),linkid*/
		/**map.mapKeyToGroup("1(40)", "G1");
	        map.mapKeyToGroup("1(30)", "G1");
	        map.mapKeyToGroup("1(20)", "G1");

	        map.mapKeyToGroup("2(40)", "G2");
	        map.mapKeyToGroup("2(30)", "G2");
	        map.mapKeyToGroup("2(20)", "G2");

	        map.mapKeyToGroup("3(40)", "G3");
	        map.mapKeyToGroup("3(30)", "G3");
	        map.mapKeyToGroup("3(20)", "G3");*/


		/*map.mapKeyToGroup("1(30)", "2");
	        map.mapKeyToGroup("1(40)", "2");
	        map.mapKeyToGroup("1(20)", "2");

	        map.mapKeyToGroup("2(35)", "2");
	        map.mapKeyToGroup("2(40)", "2");
	        map.mapKeyToGroup("3(25)", "2");

	        map.mapKeyToGroup("2(37)", "2");
	        map.mapKeyToGroup("2(40)", "2");
	        map.mapKeyToGroup("2(24)", "2");*/
		//	        map.mapKeyToGroup("Product 1 (Asia)", "G1");
		//	        map.mapKeyToGroup("Product 1 (Middle East)", "G1");
		//	        map.mapKeyToGroup("Product 2 (US)", "G2");
		//	        map.mapKeyToGroup("Product 2 (Europe)", "G2");
		//	        map.mapKeyToGroup("Product 2 (Asia)", "G2");
		//	        map.mapKeyToGroup("Product 2 (Middle East)", "G2");
		//	        map.mapKeyToGroup("Product 3 (US)", "G3");
		//	        map.mapKeyToGroup("Product 3 (Europe)", "G3");
		//	        map.mapKeyToGroup("Product 3 (Asia)", "G3");
		//	        map.mapKeyToGroup("Product 3 (Middle East)", "G3");

		renderer.setSeriesToGroupMap(map); 
		/*}
		 }*/







		//		   final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		//
		//		   List<Link> link = dbService.getLinkService().FindAll();
		//		   for(int j=0;j<link.size();j++){
		//		   List <LinkWavelengthInfo> linkWvInfo=dbService.getLinkWavelengthInfoService().FindAllByLink(networkid, link.get(j).getLinkId());
		////		   System.out.println("PlanningReportUtils.GenerateLinkWaveClusterChart() Size "+linkWvInfo.size());
		//		   
		//		   for(int i=0;i<linkWvInfo.size();i++){
		//			   
		//			   		//traversing the received data to fill in the dataset
		////			   int wavelength = dbService.getLinkWavelengthInfoService().FindTotalWavelengths(link.get(i).getLinkId());
		//			   
		//
		//			   dataSet.addValue(linkWvInfo.get(i).getTraffic(),String.valueOf(linkWvInfo.get(i).getWavelength()),String.valueOf(linkWvInfo.get(i).getLinkId()));
		//
		//			   }
		//		   }
		//		   
		//		   			//creating a bar chart
		JFreeChart chart = ChartFactory.createBarChart
				(heading, XaxisText, YaxisText,
						dataSet, PlotOrientation.VERTICAL, true, true, false);
		chart.setTitle(
				new org.jfree.chart.title.TextTitle(heading,
						new java.awt.Font("SansSerif", java.awt.Font.BOLD, 10))

				);
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setDomainAxis(domainAxis);
		renderer.setItemMargin(0.0);


		//	       final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		//		        renderer.setDrawBarOutline(false);
		//		        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		/* StackedBarRenderer renderer1 = new StackedBarRenderer(false);
		         renderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		         renderer1.setBaseItemLabelsVisible(true);*/
		chart.getCategoryPlot().setRenderer(renderer);
		NumberFormat formatter = null;
		String labelFormat = null;
		//				 CategoryAxis axis = plot.getDomainAxis();
		//				 axis.DEFAULT_AXIS_LABEL_FONT.getSize();
		//				renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(labelFormat, formatter));
		//		         renderer.setBaseItemLabelsVisible(true);
		//		        ItemLabelPosition il=new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.TOP_CENTER  );
		//		        renderer.setBasePositiveItemLabelPosition(il);
		CategoryItemLabelGenerator generator = CategoryItemLabelGenerator;
		//		        renderer.setBaseItemLabelGenerator( generator.generateColumnLabel(dataSet, 1));
		//		        renderer.getSeriesItemLabelGenerator(Integer.parseInt(generator.generateLabel(dataSet, 1, 1)));
		//		        renderer.getSeriesItemLabelGenerator(Integer.parseInt(data[1][0].toString()));
		//to stick the bars together
		//		        renderer.setItemMargin(0.00);		       
		return chart;

	}

	public  JFreeChart GenerateLinkWaveClusterChart(DbService dbService,/*Object[][] data,*/String heading,String XaxisText,String YaxisText,int networkid) 
	{
		//fetching data from node table
		List <Node> node=dbService.getNodeService().FindAll(networkid);
		//creating a dataset to be used by clustered bar chart

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		List<Link> link = dbService.getLinkService().FindAll(networkid);
		for(int j=0;j<link.size();j++){
			List <LinkWavelengthInfo> linkWvInfo=dbService.getLinkWavelengthInfoService().FindAllByLink(networkid, link.get(j).getLinkId());


			for(int i=0;i<linkWvInfo.size();i++){

				//traversing the received data to fill in the dataset
				//			   int wavelength = dbService.getLinkWavelengthInfoService().FindTotalWavelengths(link.get(i).getLinkId());


				dataSet.addValue(linkWvInfo.get(i).getTraffic(),String.valueOf(linkWvInfo.get(i).getWavelength()),String.valueOf(linkWvInfo.get(i).getLinkId()));

			}
		}

		//creating a bar chart
		JFreeChart chart = ChartFactory.createBarChart
				(heading, XaxisText, YaxisText,
						dataSet, PlotOrientation.VERTICAL, true, true, false);
		chart.setTitle(
				new org.jfree.chart.title.TextTitle(heading,
						new java.awt.Font("SansSerif", java.awt.Font.BOLD, 10))

				);
		final CategoryPlot plot = chart.getCategoryPlot();

		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		StackedBarRenderer renderer1 = new StackedBarRenderer(false);
		renderer1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer1.setBaseItemLabelsVisible(true);
		chart.getCategoryPlot().setRenderer(renderer1);
		NumberFormat formatter = null;
		String labelFormat = null;
		CategoryAxis axis = plot.getDomainAxis();
		axis.setMaximumCategoryLabelLines(10);
		//				 axis.DEFAULT_AXIS_LABEL_FONT.getSize();
		//				renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(labelFormat, formatter));
		//		         renderer.setBaseItemLabelsVisible(true);
		//		        ItemLabelPosition il=new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.TOP_CENTER  );
		//		        renderer.setBasePositiveItemLabelPosition(il);
		CategoryItemLabelGenerator generator = CategoryItemLabelGenerator;
		//		        renderer.setBaseItemLabelGenerator( generator.generateColumnLabel(dataSet, 1));
		//		        renderer.getSeriesItemLabelGenerator(Integer.parseInt(generator.generateLabel(dataSet, 1, 1)));
		//		        renderer.getSeriesItemLabelGenerator(Integer.parseInt(data[1][0].toString()));
		//to stick the bars together
		//		        renderer.setItemMargin(0.00);		       
		return chart;

	}


	/*function to display the table of contents*/
	public void createTOC(Document document) throws DocumentException
	{
		//filling chapternames in the list declared globally 
		chaptername.add("Network Design");
		/*sectionname.add("General Information");
		sectionname.add("Network Map");
		sectionname.add("NodeWise Details");
		sectionname.add("Link Wise details ");*/
		
		chaptername.add("Network Inputs");
		/*sectionname.add("General Information");
		sectionname.add("Circuit Wise Details");*/
		

		chaptername.add("Network Outputs");
		/*sectionname.add("General Information");
		sectionname.add("LinkWise Wavelength population");
		sectionname.add("Wavelength and circuit Map");
		sectionname.add("Nodewise IP Scheme");
		sectionname.add("Linkwise IP Scheme");
		*/
		
		chaptername.add("Network Equipment");
		/*sectionname.add("General Infomation");
		sectionname.add("Nodewise Power Consumption/Cost");
		sectionname.add("NodeWise Bom");*/


		// displaying the heading for table of contents page
		final Chapter intro = new Chapter(new Paragraph("TABLE OF CONTENTS ", chapterfont), 0);
		intro.setIndentationLeft(150);
		intro.setNumberDepth(0);

		document.add(intro);
		for (int i = 0; i < chaptername.size(); i++)
		{
			// Write "Chapter i"
			String title = chaptername.get(i);
			Chunk chunk = new Chunk(title).setLocalGoto(title);
			document.add(new Paragraph(chunk));

			// Add a placeholder for the page reference
			document.add(new VerticalPositionMark() {
				@Override
				public void draw(final PdfContentByte canvas, final float llx, final float lly, final float urx, final float ury, final float y)
				{
					final PdfTemplate createTemplate = canvas.createTemplate(50,50);
					tocPlaceholder.put(title, createTemplate);

					canvas.addTemplate(createTemplate, urx - 50, y);
				}
			});
		}


	}
	/*function for fetching pageNumber and creating link to navigate to a particular chapter from table of contents  */
	public void createChapter(int count,Chapter chapter,PdfWriter writer) throws DocumentException, IOException
	{

		baseFont=BaseFont.createFont();
		//    	System.out.println(chaptername.get(count));
		Chunk chunk = new Chunk(chaptername.get(count), chapterfont).setLocalDestination(chaptername.get(count));
		Phrase phrase = new Phrase();
		phrase.add(chunk);
		Paragraph para = new Paragraph();
		para.add(phrase);
		para.setAlignment(Element.ALIGN_CENTER);
		chapter.add(para);
		//     	chapterfont.setColor(BaseColor.BLUE);


		// fetching pagenumber for each chapter and filling it in the template
		final PdfTemplate template = tocPlaceholder.get(chaptername.get(count));
		template.beginText();
		template.setFontAndSize(baseFont, 12);
		template.setTextMatrix(50 - baseFont.getWidthPoint(String.valueOf(writer.getPageNumber()), 12), 10);
		template.showText(String.valueOf(writer.getPageNumber()));
		template.endText();

	}
	public  String findnodetype( int nodetype){
		String NodeType="";
		if(nodetype==7){
			NodeType="HUB";

		}
		else if(nodetype==8){
			NodeType="CDC-ROADM";
		}
		else if(nodetype==1){
			NodeType="TE";
		}
		else if(nodetype==2){
			NodeType="ILA";
		}
		else if(nodetype==6){
			NodeType="B & Select ROADM";
		}


		return NodeType;
	}
	public String typeisgne(int isgne){

		String value="";
		if(isgne==0)
			value= "No";
		else if (isgne==1)
			value="yes";
		return value;
	}
	public String getcolour(int color){
		String colour="";
		if(color==1){
			colour="Voilet";
		}
		else if(color==2){
			colour="Indigo";
		}
		else if(color==3){
			colour="Blue";
		}
		else if(color==4){
			colour="Green";
		}
		else if(color==5){
			colour="Yellow"; 
		}
		else if(color==6){
			colour="Orange";
		}
		else if(color==7){
			colour="Red";
		}

		return colour;
	}
	public String getNodeCapacity(String capacity){
		String Capacity="";
		int cap= Integer.parseInt(capacity);
		if(cap==1){
			Capacity="Forty Even";
		}
		else if(cap==2){
			Capacity="Forty Odd";
		}
		else if(cap==3){
			Capacity="Eighty";
		}
		return Capacity;
	}
	public void addDescription(Section section,String description){

		Phrase phrase= new Phrase(description);

		Paragraph descr = new Paragraph("");
		descr.setIndentationLeft(75);
		descr.add(phrase);


		section.add(descr);
	}



}
