/**
 * 
 */
package application;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hp
 * 
 * @brief This is Main class for Map Package
 *
 */
@SpringBootApplication
public class MainMap {  

	/* Get actual class name to be printed on */
	public static Logger logger = Logger.getLogger(MainMap.class.getName());
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("DBG => Org Main Class Started .. !");
		 
		 logger.debug("Here is some DEBUG");
		 logger.info("Here is some INFO ");
		 logger.warn("Here is some WARN");
		 logger.error("Here is some ERROR");
		 logger.fatal("Here is some FATAL");
		 
		SpringApplication.run(MainMap.class, args);

	}

}




