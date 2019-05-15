package npt.map.application.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@Controller
@EnableAutoConfiguration(exclude={JndiConnectionFactoryAutoConfiguration.class,DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,JpaRepositoriesAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScan({"netgloo.models"})
public class NetworkController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new user and save it in the database.
   * 
   * @param email User's email
   * @param name User's name
   * @return A string describing if the user is succesfully created or not.
   */
  @RequestMapping("/create1")
  @ResponseBody
  public String create(String name) {
    Network network = null;
    try {
      network = new Network(name);
      networkDao.save(network);
    }
    catch (Exception ex) {
      return "Error creating the user: " + ex.toString();
    }
    return "User succesfully created! (id = " + network.getId() + ")";
  }
  
  /**
   * /delete  --> Delete the user having the passed id.
   * 
   * @param id The id of the user to delete
   * @return A string describing if the user is succesfully deleted or not.
   */
  @RequestMapping("/delete1")
  @ResponseBody
  public String delete(long id) {
    try {
      Network user = new Network(id);
      networkDao.delete(user);
    }
    catch (Exception ex) {
      return "Error deleting the user: " + ex.toString();
    }
    return "User succesfully deleted!";
  }
  
  /**
   * /get-by-email  --> Return the id for the user having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The user id or a message error if the user is not found.
   */
 /* @RequestMapping("/get-by-networkname")
  @ResponseBody
  public String getByEmail(String name) {
    String networkId;
    try {
      Network network = networkDao.findByNetworkName(name);
      networkId = String.valueOf(network.getId());
    }
    catch (Exception ex) {
      return "User not found";
    }
    return "The user id is: " + networkId;
  }*/
  
 /**
  * /get-by-id  --> Return the id for the user having the passed email.
  * 
  * @param email The email to search in the database.
  * @return The user id or a message error if the user is not found.
  */
 /*@RequestMapping("/get-by-name")
 @ResponseBody
 public String getById(String name) {
   String userId;
   try {
	 Network user = networkDao.findByNetworkName(name);     
     userId = String.valueOf(user.getId());
   }
   catch (Exception ex) {
     return "User not found";
   }
   return "The user id is: " + userId;
 }*/
  
  /**
   * /update  --> Update the email and the name for the user in the database 
   * having the passed id.
   * 
   * @param id The id for the user to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the user is succesfully updated or not.
   */
  @RequestMapping("/update1")
  @ResponseBody
  public String updateUser(long id, String name) {
    try {
    	Network user = networkDao.findOne(id);      
    	user.setNetworkName(name);
    	networkDao.save(user);
    }
    catch (Exception ex) {
      return "Error updating the user: " + ex.toString();
    }
    return "User succesfully updated!";
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private NetworkDao networkDao;
  
} // class UserController
