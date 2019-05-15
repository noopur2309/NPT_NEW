package application.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import application.MainMap;


public class UnTarFile {

    /**
     * @author surya
     * @param Networkname
     * @return Path
     */
	public String  UnTarfile(String Networkname,String Directory) 


	{   
		
		
		
		// Path to input file, which is a 
		// tar file compressed to create gzip file
		String home = System.getProperty("user.home");  
		
		String finalNetworkName = Networkname.substring(0, Networkname.length()-7);
		String HOMEfolder = home+"/Downloads/ConfigPathFiles/ReversePath/"+Directory+"/"+ Networkname;
		String TAR_Source = home+"/Downloads/ConfigPathFiles/ReversePath/"+Directory;
		String DESTINATION_TarFOLDER = home+"/Downloads/ConfigPathFiles/ReversePath/"+Directory+"/TAR/";

		UnTarFile unTarDemoforTAR = new UnTarFile();



		// Path for untar node Tar.gz  inside NetworkFolder

		String INPUT_FILE = null;
		String HOME = home+"/Downloads/ConfigPathFiles/ReversePath/"+Directory+"/TAR/"+finalNetworkName+"/";
		// This folder should exist, that's where
		// .tar file will go
		String TAR_FOLDER = home+"/Downloads/ConfigPathFiles/ReversePath/"+Directory+"/TAR";
		// After untar files will go to this folder
		String DESTINATION_FOLDER = home+"/Downloads/ConfigPathFiles/ReversePath/"+Directory+"/XML"+"/";

		UnTarFile unTarDemo = new UnTarFile();


		try {  

			File InputFile = new File(HOMEfolder);
			String OutputFileforTAR = getFileName(InputFile, TAR_Source);
			
			File TarFile = new File(OutputFileforTAR);
			TarFile = unTarDemoforTAR.deCompressGZipFile(InputFile, TarFile);
			File destFolder = new File(DESTINATION_TarFOLDER);
			unTarDemoforTAR.unTarFile(TarFile, destFolder);


			File folder = new File(HOME);
			File[] listOfFiles = folder.listFiles();
			for(int i=0; i< listOfFiles.length;i++)
			{
				
				INPUT_FILE = HOME + listOfFiles[i].getName();
				File inputFile = new File(INPUT_FILE);
				String outputFile = getFileName(inputFile, TAR_FOLDER);
				MainMap.logger.info("outputFile " + outputFile);
				File tarFile = new File(outputFile);
				// Calling method to decompress file
				tarFile = unTarDemo.deCompressGZipFile(inputFile, tarFile);
				File destFile = new File(DESTINATION_FOLDER);

				// Calling method to untar file
				unTarDemo.unTarFile(tarFile, destFile);
			}     

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DESTINATION_FOLDER;
	}

	/**
	 * 
	 * @param tarFile
	 * @param destFile
	 * @throws IOException
	 */
	private void unTarFile(File tarFile, File destFile) throws IOException{
		FileInputStream fis = new FileInputStream(tarFile);
		TarArchiveInputStream tis = new TarArchiveInputStream(fis);
		TarArchiveEntry tarEntry = null;
		
		// tarIn is a TarArchiveInputStream
		

		while ((tarEntry = tis.getNextTarEntry()) != null) {
			//System.out.println( "************"+File.separator+"************");
			File outputFile = new File(destFile + File.separator + tarEntry.getName());

			if(tarEntry.isDirectory()){

				MainMap.logger.info("outputFile Directory ---- " 
						+ outputFile.getAbsolutePath());
				if(!outputFile.exists()){
					outputFile.mkdirs();
				}
			}else{
				//File outputFile = new File(destFile + File.separator + tarEntry.getName());
				MainMap.logger.info("outputFile File ---- " + outputFile.getAbsolutePath());
				outputFile.getParentFile().mkdirs();
				outputFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(outputFile); 
				IOUtils.copy(tis, fos);
				fos.close();
			}
		}
		tis.close();

		
	}

	/**
	 * Method to decompress a gzip file
	 * @param gZippedFile
	 * @param newFile
	 * @throws IOException
	 */
	private File deCompressGZipFile(File gZippedFile, File tarFile) throws IOException{
		FileInputStream fis = new FileInputStream(gZippedFile);
		GZIPInputStream gZIPInputStream = new GZIPInputStream(fis);

		FileOutputStream fos = new FileOutputStream(tarFile);
		byte[] buffer = new byte[1024];
		int len;
		while((len = gZIPInputStream.read(buffer)) > 0){
			fos.write(buffer, 0, len);
		}

		fos.close();
		gZIPInputStream.close();
		return tarFile;

	}

	/**
	 * This method is used to get the tar file name from the gz file
	 * by removing the .gz part from the input file
	 * @param inputFile
	 * @param outputFolder
	 * @return
	 */
	private static String getFileName(File inputFile, String outputFolder){
		return outputFolder + File.separator + 
				inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
	}
}





