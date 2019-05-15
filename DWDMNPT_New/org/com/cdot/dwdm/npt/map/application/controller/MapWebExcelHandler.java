/**
 * 
 */
package application.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.constants.MapConstants;
import application.model.Equipment;
import application.service.DbService;



/**
 * @author web [imported api :http://howtodoinjava.com/apache-commons/readingwriting-excel-files-in-java-poi-tutorial/]
 *
 * @brief This Handler can read/write/operation perform on the Excel file. Input File: BOM.xlsx
 */
public class MapWebExcelHandler {

	public Equipment[] viewBOMExcelDataRequestReq(DbService dbService) 
	{
		try
		{
            String path=System.getProperty("user.dir");
			//File fileTest = new File(getClass().getResource("/static/BOM_Data/").getFile());
            File fileTest = new File(path+"/bom.xlsx");
					
			//File[] files = fileTest.listFiles();
			//for(int i=0; i<files.length; i++)
			//	System.out.println("viewBOMExcelDataRequestReq File :- "+ i +" -> " + files[i].toString());

			//FileInputStream file = new FileInputStream(new File(files[MapConstants.I_ONE].toString()));	            
			FileInputStream file = new FileInputStream(fileTest);	
			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			int sheetCount=0;
			int rowCountTotal=0;

			StringBuilder equipmentDetailsString=new StringBuilder("");  

			while(sheetCount<2)
			{
				//Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(sheetCount);
				//Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				//rowIterator.next();
				// rowIterator.next();
				while (rowIterator.hasNext()) 
				{
					rowCountTotal++;
					Row row = rowIterator.next();
					//For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) 
					{
						Cell cell = cellIterator.next();
						//Check the cell type and format accordingly
						switch (cell.getCellType()) 
						{
						case Cell.CELL_TYPE_NUMERIC:
						{
//							System.out.print(cell.getNumericCellValue() + " | ");
							if(cellIterator.hasNext())
								equipmentDetailsString.append(cell.getNumericCellValue()+"|");// | for cell change
							else equipmentDetailsString.append(cell.getNumericCellValue());
						}
						break;
						case Cell.CELL_TYPE_STRING:
						{
//							System.out.print(cell.getStringCellValue() + " | ");
							if(cellIterator.hasNext())
								equipmentDetailsString.append(cell.getStringCellValue()+"|");
							else equipmentDetailsString.append(cell.getStringCellValue());
						}
						break;
						}
					}
					equipmentDetailsString.append("^");//$ for row change
					//System.out.println("$");
				}
				equipmentDetailsString.append("?");//& for sheet change
				sheetCount++;
			}
			file.close();
			System.out.println("viewBOMExcelDataRequestReq end");

			System.out.print("rowCountTotal"+rowCountTotal+"\n");
			//System.out.print("equipmentDetailsString"+equipmentDetailsString.toString());

			String equipmentDetailsStringOutput=equipmentDetailsString.toString();
			
			System.out.println("equipmentDetailsStringOutput ::"+equipmentDetailsStringOutput);

			String[] sheetsString=equipmentDetailsStringOutput.split("\\?");
			System.out.println("sheetsString ::"+sheetsString.length);			
			
			int equipmentModelObjectArrayLength=rowCountTotal-sheetCount;
			System.out.println("equipmentModelObjectArrayLength ::"+equipmentModelObjectArrayLength);
			//Create equipment model class objects array for holding info from excel file 
			Equipment[] equipmentModelObjectArray=new Equipment[equipmentModelObjectArrayLength];
			for(int i=0;i<equipmentModelObjectArrayLength;i++)
			{
				equipmentModelObjectArray[i]=new Equipment();
			}
			int equipmentObjectIndex=0,k;
			
			ResourcePlanning rc = new ResourcePlanning(dbService);
			
			for(int i=0;i<sheetsString.length;i++)
			{
				String[] sheetRowstString=sheetsString[i].split("\\^");
//                System.out.println("sheetRowstString ::"+sheetRowstString.length);
                
				for(int j=0;j<sheetRowstString.length;j++)
				{
					String[] sheetCellsString=sheetRowstString[j].split("\\|");
					System.out.println("sheetCellsString ::"+sheetCellsString.length + sheetRowstString[j]);
					equipmentModelObjectArray[equipmentObjectIndex].setEquipmentId(equipmentObjectIndex+1);
                    k=1;
						if(j==0)
							continue;
						else
						{				
//							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setName(sheetCellsString[k++]);
//							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setPartNo(sheetCellsString[k++]);
//							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setPowerConsumption(Integer.parseInt(sheetCellsString[k++].toString().split("\\.")[0]));
//							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setTypicalPower(Integer.parseInt(sheetCellsString[k++].toString().split("\\.")[0]));
							
							
//							k++; //skip column
//							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setDetails(sheetCellsString[k++]);
//							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);							
							equipmentModelObjectArray[equipmentObjectIndex].setSlotSize(Integer.parseInt(sheetCellsString[k++].toString().split("\\.")[0]));
																
							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setPrice(Float.parseFloat(sheetCellsString[k++].toString()));							
							
							k++; //skip column		
							
							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setCategory(Integer.parseInt(sheetCellsString[k++].toString().split("\\.")[0]));
							System.out.println("Value of K ::"+k + " "+ sheetCellsString[k]);
							equipmentModelObjectArray[equipmentObjectIndex].setRevisionCode(sheetCellsString[k++]);
							//System.out.println("Value of K ::"+k);
						}
					equipmentObjectIndex++;					
				}

			}
			
			for(int i=0;i<equipmentModelObjectArrayLength;i++)
			{
				System.out.print("getEquipmentId :: "+equipmentModelObjectArray[i].getEquipmentId());
				System.out.print("  getName :: "+equipmentModelObjectArray[i].getName());
				System.out.print("  getPartNo :: "+equipmentModelObjectArray[i].getPartNo());
				System.out.print("  getPowerConsumption :: "+equipmentModelObjectArray[i].getPowerConsumption());
				System.out.print("  getTypicalPower :: "+equipmentModelObjectArray[i].getTypicalPower());
				System.out.print("  getSlotSize :: "+equipmentModelObjectArray[i].getSlotSize());
				System.out.print("  getPrice :: "+equipmentModelObjectArray[i].getPrice());
				System.out.print("  getDetails :: "+equipmentModelObjectArray[i].getDetails());
				System.out.print("  getCategory :: "+equipmentModelObjectArray[i].getCategory());
				System.out.print("  getRevisionCode :: "+equipmentModelObjectArray[i].getRevisionCode());
				System.out.print("  getEquipmentId :: "+equipmentModelObjectArray[i].getEquipmentId());
				System.out.println("--------------------------------------");
			}		
			
			return equipmentModelObjectArray;

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}


		return null;
	}
}
