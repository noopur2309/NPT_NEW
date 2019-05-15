package application.service;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @brief :this API Converted .xml File to  tar.gz file and Many tar.gz file to Single Compressed tar.gz File 
 * @date:23-03-2018
 * @author Saransh
 *
 */
public class TAR {
	public static String PATH1;
	public TAR(String PATH) {
		PATH1=PATH;
    }
	public static Logger DataPathLogger = Logger.getLogger(TAR.class.getName());/**Logger for the Class*/
    static String name;
    public static  void compress(String name1, File... files) throws IOException {       
    	name=PATH1 + name1;
    	System.out.println(name);
    	try (TarArchiveOutputStream out = getTarArchiveOutputStream(name)){
        	for (File file : files){
                addToArchiveCompression(out, file,null);
            }
        }
    }
    
    /**
     * @brief Add Some Configuration in TAR file
     * @param name
     * @return
     * @throws IOException
     */
    private static  TarArchiveOutputStream getTarArchiveOutputStream(String name) throws IOException {
        TarArchiveOutputStream taos = new TarArchiveOutputStream(new GzipCompressorOutputStream(new FileOutputStream(name)));
        // TAR has an 8 gig file limit by default, this gets around that
        taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
        // TAR originally didn't support long file names, so enable the support for it
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        taos.setAddPaxHeadersForNonAsciiNames(true);
        return taos;
    }
    
    /**
     * @brief Generated tar.gz file in Directory 
     * @param out
     * @param file
     * @param dir
     * @throws IOException
     */
    private static  void addToArchiveCompression(TarArchiveOutputStream out, File file, String dir) throws IOException 
    {
        String entry =File.separator + file.getName();
        if (file.isFile()){
            out.putArchiveEntry(new TarArchiveEntry(file, entry));
            try (FileInputStream in = new FileInputStream(file)){
                IOUtils.copy(in, out);
            }
            out.closeArchiveEntry();
        } else {
            System.out.println(file.getName() + " is not supported");
        }
    }
   
    /**
     * @brief Convert  tar.gz file to Compressed tar.gz file
     * @param RESOURCES_NAME
     * @param OUTPUT_DIRECTOR
     */
    public static void convertTartoTar(String RESOURCES_NAME, String OUTPUT_DIRECTOR) {
	    final String JAR_SUFFIX = ".tar.gz";
	    ///System.out.println(" Resource Name "+ RESOURCES_NAME);
	    final String RESOURCES_PATH =RESOURCES_NAME+JAR_SUFFIX;
//	    System.out.println(" Path "+ RESOURCES_PATH);
//	    System.out.println(" OUTPUT_DIRECTOR "+OUTPUT_DIRECTOR);
	 	try {
	 	File directory = new File(OUTPUT_DIRECTOR);
        File[] fList1 = directory.listFiles();
        
//        for (File file : fList1) {
//			System.out.println("file "+file);
//		}
        
        DataPathLogger.info("Compressed tar.gz File Name :- "+RESOURCES_PATH);
        compress(RESOURCES_PATH,fList1);
        DataPathLogger.info("Successful Converted Compress tar.gz File ");
        }
        catch (IOException e) {
			e.printStackTrace();
        }
	}

}
