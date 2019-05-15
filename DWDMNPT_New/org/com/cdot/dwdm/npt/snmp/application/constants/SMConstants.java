package application.constants;

public interface SMConstants {
	
	//Snmp Manager General Configuration
		public String destIpAddress     =/*"192.168.115.187";*/"127.0.0.1";
		public String sourceIpAddress    ="192.168.115.187";/// ="127.0.0.1";

		public String port         ="2004";
		
		//OID values
		public static String  NetrworkTopologyOid  = ".1.3.6.1.4.1.5380.3.2.6.1.1.0";// ends with 0 for scalar object
		public static String  LinkMatrixOid        = ".1.3.6.1.4.1.5380.3.2.6.1.2.1.2.";// ends with 0 for scalar object
		public static String  IncludeSetOid        = ".1.3.6.1.4.1.5380.3.2.6.1.3.1.2.";// ends with 0 for scalar object
		public static String  IncludeNodeSetOid    = ".1.3.6.1.4.1.5380.3.2.6.1.3.1.3.";// ends with 0 for scalar object
		public static String  ExcludeSetOid        = ".1.3.6.1.4.1.5380.3.2.6.1.4.1.2.";// ends with 0 for scalar object
		public static String  DemandSetOid         = ".1.3.6.1.4.1.5380.3.2.6.1.5.0";// ends with 0 for scalar object
		public static String  GetDemandOutputOid   = ".1.3.6.1.4.1.5380.3.2.6.3.1.1.3.";
		public static String  GetDemandOutputOidStr   = ".1.3.6.1.4.1.5380.3.2.6.3.1";
		public static String  GetDemandOutputStatus   = ".1.3.6.1.4.1.5380.3.2.6.1.6.1.2.1";
		public static String  GetOldDemandSetInputOid   = ".1.3.6.1.4.1.5380.3.2.6.4.1.1.2."; /**Demands represents the old RWA o/p for Brown field*/
		
		
		//Set Values
//		public static String  NetrworkTopologyValue  = "3#7#2-2-6-3-0-0-0-0-0#4-1-6-4-0-0-0-0-0#3-1-6-7-0-0-0-0-0#2-2-6-5-0-0-0-0-0#4-4-6-7-0-0-0-0-0#1-1-2-3-4-5-7-0-0#2-3-6-5-0-0-0-0-0";
//		public static String  LinkMatrixValue        = "24#1-2-10-1-2-11#2-1-10-1-2-11#1-3-12-2-3-11#3-1-12-2-3-11#1-6-11-1-4-11#6-1-11-1-4-11#2-6-13-1-5-11#6-2-13-1-5-11#3-6-14-1-6-11#6-3-14-1-6-11#2-4-15-3-7-11#4-2-15-3-7-11#4-6-16-3-8-11#6-4-16-3-8-11#4-5-18-3-9-11#5-4-18-3-9-11#6-5-17-2-10-11#5-6-17-2-10-11"
//		                                               +"#3-7-15-1-3-11#7-3-15-1-3-11#6-7-17-2-3-11#7-6-17-2-3-11#5-7-16-2-1-11#7-5-16-2-1-11";
//		public static String  IncludeSetValue        = "3#1-2-3";
//		public static String  DemandSetValue         = "3#0#4";
		
		
		public static int  setSetStringCount  = 7;/***/
		//Community Constants
		public static String  communityPublic  = "public";
		public static String  communityPrivate  = "private";
		
		public static String  PtcTypeNoneStr  = "UnProtected";
		//public static String  PtcTypeNoneStr  = "None";
		public static String  OnePlusOnePtcStr  = "1+1";
		public static String  OnePlusOnePlusRPtcStr  = "1+1+R";
		public static String  OneIsToOnePtcTypeStr  = "1:1";
		public static String  OnePlusOnePlusTwoRPtcTypeStr  = "1+1+2R";
		public static String  OneIsToTwoRPtcTypeStr  = "1:2R";
		public static String  channelPtcTypeStr  = "Channel";
		public static String  opxPtcTypeStr  = "OPX";
		public static String  yCablePtcTypeStr  = "Y-Cable";
		
		public static int  PtcTypeNone  = 1;
		public static int  OnePlusOnePlusRPtcType = 3;
		public static int  OneIsToTwoRPtcType = 3;
		public static int  OneIsToOnePtcType  = 2;
		public static int  OnePlusOnePtcType  = 2;
		public static int  OnePlusOnePlusTwoRPtcType  = 4;
		public static int  channelPtcType  = 5;
		
		public static int ChannelProtection=1;
		public static int ClientProtection=2;
		
		public static String  YesStr  = "Yes";
		public static String  NoStr   = "No";
		
		public static int  Disjoint  = 0;
		public static int  NonDisjoint  = 1;
		
		public static String  DisjointStr  = "Disjoint";
		public static String  NonDisjointStr  = "Non Disjoint";
		
		public int Violet=1;
		public int Indigo=2;
		public int Blue=3;
		public int Green=4;
		public int Yellow=5;
		public int Orange=6;
		public int Red=7;
		
		// Link Type : Normal/Raman Hybrid/Raman DRA
		public static String  defaultLinkStr  = ResourcePlanConstants.PaBaLink;
		public int defaultLink=0;
		
		public static String  hybridRamanLinkStr  = ResourcePlanConstants.RamanHybridLink;
		public int hybridRamanLink=1;
		
		public static String  draRamanLinkStr  = ResourcePlanConstants.RamanDraLink;
		public int draRamanLink=2;
		
		//Error states from RWA
		public static final int ACCEPTED=0; 
		public static final int LINKFAILURE=1; 
		public static final int NODEFAILURE=2; 
		public static final int NOPATHFOUND=3; //[No path found for demand] 
		public static final int LESSPATHCOUNT=4; //[Total paths are less than required] 
		public static final int INCLUDESETNODEFAILURE_ALL=5; // Error in all case
		
		//Reason(NodeFailure)
		public static final int NONNODEDISJOINT=1; //[Path is not node disjoint] 
		public static final int ODDTOEVEN=6;     //  [Node subtype conflict ] 
		public static final int EVENTOODD=7;      //[Node subtype conflict] 
		public static final int OSNROUTOFRANG=3; // [OSNR is less than its threshold] 
		public static final int HUBNODEINPATH=9;//PassThrough Hub Node
		public static final int INCLUDESETNODEFAILURE_ANY=10; // 	Error in any case
		
		
		public static final String NONNODEDISJOINTSTR="Path is not node disjoint";
		public static final String ODDTOEVENSTR="Node subtype conflict"; 
		public static final String EVENTOODDSTR="Node subtype conflict"; 
		public static final String OSNROUTOFRANGSTR="OSNR is less than its threshold"; 
		public static final String HUBNODEINPATHSTR="Hub Node in path"; 

		//Reason(LinkFailure)
		public static final int NONSRLGDISJOINT=2; //[Path is not SRLG Disjoint ] 
		public static final int NOLEMBDAAVAILABLE=4; //[No Lembda available] 
		public static final int LOADHIGH=5;     //    [Load at link is high] 
		public static final int NOCHANNLEPROTECTIONLEMBDAAVAILABLE=8; //[channel protection Lembda not found ]
		
		public static final String NONSRLGDISJOINTSTR="Path is not SRLG Disjoint";
		public static final String NOLEMBDAAVAILABLESTR="No lambda(λ) available"; 
		public static final String LOADHIGHSTR="Load is high"; 
		public static final String NOCHANNLEPROTECTIONLEMBDAAVAILABLESTR="Channel protection lambda(λ) not found";
}
