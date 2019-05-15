/**
 * 
 */
package application.constants;

/**
 * @author hp
 *
 */
public interface MapConstants {
	
	/**Return Types*/
	public static int SUCCESS = 1;
	public static int FAILURE = 0;
	public static int ZERO_DEMAND_RWA = 2;
	public static int DUPLICATE_ENTRY = 3;
	
	/**Device Types*/

	public static int hub	=	7;
	public static int roadm	=	8;
	public static int te	=	1;
	public static int ila	=	2;
	public static int twoBselectRoadm	=	6;
	public static int cdRoadm	=	9;
	public static int dRoadm	=	11;
	public static int potp	=	10;
	
	/* Device Subtypes */
	public static int AmpRamanDraSubtype		=	ResourcePlanConstants.TEN;
    public static int AmpRamanHybridSubtype		=	ResourcePlanConstants.EIGHT;
	
	/**Number of IP*/
	public static int IPPERNODE = 6;
    public static int IPPERLINK = 2;
    
    /**String Constants*/
    public static String S_ZERO   = "0";
    public static String S_ONE    = "1";
    public static String S_TWO    = "2";
    public static String S_THREE  = "3";
    
    public static String EAST  	= "east";
    public static String WEST  	= "west";
    public static String NORTH  = "north";
    public static String SOUTH  = "south";
    public static String NE  	= "ne";
    public static String NW  	= "nw";
    public static String SE 	= "se";
    public static String SW  	= "sw";
    
    /**String Constants for NetworkInfo*/
    public static String NetworkInfo  	= "NetworkInfo";
    public static String NetworkName  	= "NetworkName";
    public static String UserName  		= "UserName";
    public static String NetworkType	= "NetworkType";
    public static String NetworkId		= "NetworkId";
    public static String GreenField		= "GreenField";
    public static String BrownField		= "BrownField";    
    public static String GreenFieldId  	= "GreenFieldId";
    public static String BrownFieldId  	= "BrownFieldId";
    
    
    public static int _EAST  	=1;
    public static int _WEST  	= 2;
    public static int _NORTH  = 3;
    public static int _SOUTH  = 4;
    public static int _NE  	= 5;
    public static int _NW  	= 6;
    public static int _SE 	= 7;
    public static int _SW  	= 8;
    
    
    

    /**Networking Constants for various private block*/
    public static String block1NodeStartAddress = "10.5.";
    public static String block1LinkStartAddress = "10.10.";
    
    public static String block2NodeStartAddress = "172.16.";
    public static String block2LinkStartAddress = "172.21.";
    
    public static String block3NodeStartAddress = "192.168.";
    public static String block3LinkStartAddress = "192.173.";
    
    public static String subnetNodeMask = "/29";
    public static String subnetLinkMask = "/30";
    
    
    /***
     * BrownField Map Entry Keys
     */
    public static String LastGeneratedNodeIpSchemeObj = "lastGeneratedNodeIpSchemeObj";
    public static String LastGeneratedLinkIpSchemeObj = "lastGeneratedLinkIpSchemeObj";
    public static String NetworkFlag = "NetworkFlag";
    
    /**Integer Constants*/
    public static int I_MINUS_ONE  = -1;
    public static int I_ZERO  = 0;
    public static int I_ONE	  = 1;
    public static int I_TWO	  = 2;
    public static int I_THREE = 3;
    public static int I_FOUR  = 4;
    public static int I_FIVE  = 5;
    public static int I_SIX   = 6;
    public static int I_SEVEN = 7;
    public static int I_EIGHT = 8;   
    public static int I_NINE  = 9;  
    public static int I_TEN   = 10; 
    public static int I_ELEVEN = 11;  
    public static int I_TWELVE = 12;  
    public static int I_THIRTEEN = 13;  
    public static int I_FOURTEEN = 14;
    public static int I_HUNDREAD = 100;
    public static int I_TWOHUNDREAD = 200;
    public static int I_TWOFIVEFIVE = 255;    
    public static int I_THOUSAND    = 1000;
    
    /*Traffic type*/
    
    public static int G1	=1;
    public static int G10	=10;
    public static int G100	=100;
    
    /*Line Rates*/
    public static String Line200	="200";
    public static String Line100	="100";
    public static String Line10	="10";
    
     /*Client Protection Types*/
    public static String YCableProtection ="Y-Cable";
    public static String OLPProtection ="OLP";
    public static String CardTypeYCable ="YCable1x2Unit";
    public static String CardTypeOPX ="OPX";
  

    /**File Name*/
    public static String ControlPathFile = "ControlPathSetup.xml";
    
    public static int MaxRackPerChassis  = 1;
    public static int MaxSubRackPerRack  = 3;

    public static int MaxYCablePerPerRack = 14; 
    public static int MaxSlotPerPerSubRack = 14;
    public static int yCableFlag = 0;
    
    public String Line100G					="100G";
    public String Line10G					="10G";
    public String Line200G					="200G";
    
    public static String Capacity					="80";
    
    public static String cardTypeMpn= "MPN";
    public static String cardTypeWss= "WSS";
    
    public String Yes = "Yes";
    public String No = "No";
    
    public String OnePlusOnePlusRPtc = SMConstants.OnePlusOnePlusRPtcStr;
    public String OneIsToOnePtcType = SMConstants.OneIsToOnePtcTypeStr;
    public String OnePlusOnePlusTwoRPtcType = SMConstants.OnePlusOnePlusTwoRPtcTypeStr;
    public String OneIsToTwoRPtcTypeStr = SMConstants.OneIsToTwoRPtcTypeStr;
    public String channelPtcType = SMConstants.channelPtcTypeStr;
    
    public int GreenFieldBfId=0;
    public int BrownFieldBfId=-1;
    
    public String MapDataNull="null";
    
    public String NewState="New";
    public String OldState="Old";
    public String DeleteState="Deleted";
    public String ModifiedState="Modified";
    public String GreenFieldState="GreenField";
    
    public String GraphColor_Coral = "rgba(255,127,80,1)";
    public String GraphColor_Blue = "rgba(0,0,255,0.3)";
    public String GraphColor_aquamarine1 = "#7fffd4";
    public String GraphColor_BlueViolet = "#8a2be2";
    public String GraphColor_CadetBlue3 = "#7ac5cd";
    public String GraphColor_thistle2 = "#eed2ee";
    public String GraphColor_VioletRed1 = "#ff3e96";
    public String GraphColor_CornflowerBlue = "#6495ed";
    public String GraphColor_DarkOliveGreen1 = "#caff70";
    public String GraphColor_violet = "#ee82ee"; 
    public String GraphColor_DeepPink2 = "#ee1289";
    public String GraphColor_gold1 = "#ffd700";
    public String GraphColor_GreenYellow = "#ffd700";
    public String GraphColor_IndianRed2 = "#ee6363";
    public String GraphColor_LavenderBlush1 = "#fff0f5";
    public String GraphColor_LightSalmon2 = "#ee9572";
    public String GraphColor_LightSlateBlue = "#8470ff";
    public String GraphColor_pink = "#ffc0cb";

    /**10G Agg Constants*/
    public String Capacity0GAgg= "10(Agg)";
    public int    LineRate10GAgg  = 10;
    public String Traffic10GAgg   = "10";



    public String [] GraphColor_array = {
        GraphColor_Coral,
        GraphColor_Blue,
        GraphColor_aquamarine1,
        GraphColor_BlueViolet,
        GraphColor_CadetBlue3,
        GraphColor_thistle2,
        GraphColor_VioletRed1,
        GraphColor_CornflowerBlue,
        GraphColor_DarkOliveGreen1,
        GraphColor_violet,
        GraphColor_DeepPink2,
        GraphColor_gold1,
        GraphColor_GreenYellow,
        GraphColor_IndianRed2,
        GraphColor_LavenderBlush1,
        GraphColor_LightSalmon2,
        GraphColor_LightSlateBlue        
    };
    
    
    /*Category mapping*/
    public static String Category0="Power-Supply";
    public static String Category1="TE/ILA/CDC-ROADM/FOADM Main Rack";
    public static String Category2="MuxPonder Rack";
    public static String Category3="Subrack";
    public static String Category4="CSCC";
    public static String Category5="All Cards";
    public static String Category6="Regenerator Card";
    public static String Category7="Passive unit";
    public static String Category8="SFP";
    public static String Category9="Optical Patchcord";
    public static String Category10="Ethernet cable";
    public static String Category11="Power Supplycable";
    public static String Category12="Filler plate";
	public static Object WavelengthNotApplicable ="n.a.";
	
	
	
	/**ConfigFile Path Strings*/
	public static String ConfigDirectory        =  System.getProperty("user.home")+"/Downloads/ConfigPathFiles";
	public static String ControlConfigDirectory = ConfigDirectory+"/ControlPath";
	public static String DataConfigDirectory    = ConfigDirectory+"/DataPath";
	
    
	
	//Function mapping of Direction value to Direction string
	public static String fGetDirectionStr(int val)
		{
			System.out.println("Fetching dir string for :"+val);
			String dirStr="";
			switch(val)
			{
			case MapConstants._EAST:
			dirStr=MapConstants.EAST;
			break;
			case MapConstants._WEST:
				dirStr=MapConstants.WEST;
				break;
			case MapConstants._NORTH:
				dirStr=MapConstants.NORTH;
				break;
			case MapConstants._SOUTH:
				dirStr=MapConstants.SOUTH;
				break;
			case MapConstants._NE:
				dirStr=MapConstants.NE;
				break;
			case MapConstants._NW:
				dirStr=MapConstants.NW;
				break;
			case MapConstants._SE:
				dirStr=MapConstants.SE;
				break;
			case MapConstants._SW:
				dirStr=MapConstants.SW;
				break;
			default:
				dirStr="Unkown Value for direction";
				break;
			}
			return dirStr;
		}
	
	
	//Function mapping of Direction value to Direction string
		public static int fGetDirectionStrVal(String dir)
			{
				System.out.println("Fetching val for dir string  :"+dir);
				int  val;
				switch(dir)
				{
				case MapConstants.EAST:
					val=MapConstants._EAST;
				break;
				case MapConstants.WEST:
					val=MapConstants._WEST;
					break;
				case MapConstants.NORTH:
					val=MapConstants._NORTH;
					break;
				case MapConstants.SOUTH:
					val=MapConstants._SOUTH;
					break;
				case MapConstants.NE:
					val=MapConstants._NE;
					break;
				case MapConstants.NW:
					val=MapConstants._NW;
					break;
				case MapConstants.SE:
					val=MapConstants._SE;
					break;
				case MapConstants.SW:
					val=MapConstants._SW;
					break;
				default:
					val=0;
					break;
				}
				return val;
			}
		    
    
    
}
