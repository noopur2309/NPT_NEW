/**
 * 
 */
package application;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import application.controller.MapWebCommonAPIs;
import application.controller.MapWebGenerateControlPathConfigFile;
import application.service.DbService;

/**
 * @author hp
 * @date   27th Nov, 2017
 *
 */
public class MySqlThread implements Runnable {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */	
	private Thread t;
	private DbService dbService;
	private String threadName;
		
	public MySqlThread(String threadName, DbService dbService) {
		this.threadName =threadName;
		this.dbService=dbService;
	}
	
	/**
	 * Overwritten Method of Runnable interface
	 */
	@SuppressWarnings("static-access")
	public void start () {
		
		MainMap.logger = MainMap.logger.getLogger(MySqlThread.class.getName());		
		///MainMap.logger.setLevel(Level.DEBUG);
				
		if (t == null && (MapWebCommonAPIs.NestedStaticClass.MySqlThreadLock!=true)) {/**Only If Global Lock is not set yet*/
			MainMap.logger.info("==============  Starting Thread ==> " +  threadName );
			MainMap.logger.info(" Going to create a MySql thread ... ");
			t = new Thread (this, threadName);
			t.start ();				
		}
		else {
			MainMap.logger.info(" ============== MySql Thread is already in Running State ============== "); 
		}
	}
	
	/**
	 * Run thread for each millis
	 */
	public void run() {
		while(true) {		

			MainMap.logger.info(" ==========Thread==========");
		    MainMap.logger.info(" Sql Fetch : "+dbService.getNetworkService().Count());
			long millis = MapWebCommonAPIs.NestedStaticClass.MySqlThreadSleepTimer;/** Every 5 hours*/
			
			try {
				MainMap.logger.info(" ==========Thread : Going To Sleep Now==========");
				MapWebCommonAPIs.NestedStaticClass.MySqlThreadLock=true;/**Set Lock*/		
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
						
	}

}
