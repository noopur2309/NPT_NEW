/**
 * 
 */
package npt.map.application.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hp
 *
 */
@SpringBootApplication
/*@ComponentScan({"npt.map.web.controller","npt.map.web.model"})*/
public class Map {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("DBG => Main Class Started .. !");
		SpringApplication.run(Map.class, args);

	}

}
