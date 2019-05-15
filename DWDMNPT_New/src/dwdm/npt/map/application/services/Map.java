/**
 * 
 */
package map.application.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hp
 *
 */
@SpringBootApplication
public class Map {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("DBG => Main Class Started .. !");
		SpringApplication.run(Map.class, args);
	}

}
