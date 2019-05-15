package application.constants;

public interface DataConfigConstants {
	
	public static int Upstream = 1;
	public static int Downstream = 0;
	
	public static String ProtLink		= "Link"; 
	public static String ProtChannel	= "Channel";

	public static String ProtClient_BPSR		= "Client_BPSR";
	public static String ProtClient_OSNCP		= "Client_OSNCP";
	
	public static String ProtOneIsToOne		= "1:1";
	public static String ProtOneIsToTwoR 	= "1:2R";

	
	public static int ProtRevertive		= 1;
	public static int ProtNonRevertive	= 2;
	

	public static int ProtEnable	= 0;
	public static int ProtDisable	= 1;
	
	public static int AmpEnable		= 1;
	public static int AmpDisable	= 0;
	
	/**
	 * Voa Config
	 */
	public static int OlpChannelTypeNormal	=0;
	public static int OlpChannelTypeProt	=1;
	public static int AttConfigModeAuto		=0;
	public static int AttConfigModeManual	=1;

}
