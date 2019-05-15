/************************************************************************
 * Contextmenu
 ************************************************************************/

var cellLocal = -1; /**Local Variable to Update every time with the current cellview which can be used in other event*/
var Direction = [
  "N",
  "E",
  "W",
  "S",
  "NE",
  "NW",
  "SE",
  "SW"
]; /**8 Different Direction for port */
var DirectionParam = [
  "north",
  "east",
  "west",
  "south",
  "ne",
  "nw",
  "se",
  "sw"
]; /**8 Different Direction for port */
var DirectionFlag = [
  0,
  0,
  0,
  0,
  0,
  0,
  0,
  0
]; /** Flag to check if port is set or not **/
//var chassisViewCallCount=0;

/**
 * DummyJSon for ChassisView
 */
var dummyCVJson = {
  chassisViewDbData: [
    { Rack: "1", Subrack: "2", Card: "CSCC", CardType: "1" },
    { Rack: "1", Subrack: "1", Card: "TPC", CardType: "1" },
    { Rack: "1", Subrack: "3", Card: "TPC", CardType: "1" }
  ]
};

/**
 * Context menu containing : 1) Add Port    -> 8 Direction
 * 							  2) Remove Port -> 8 Direction
 * 							  3) Delete Node
 */

var contextmenu = [
  //        {
  //     	    name: 'Add Port',
  //     	    img: 'images/General/Ethernet On-104.png',
  //     	    title: 'Add Port',
  //     	    subMenu: [{
  //     	        name: 'All..',
  //     	        title: 'Add All Direction',
  //     	        img:'images/Direction/Add/Check All Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will add all portn now')
  //     	            addPortToDirection("all");
  //     	        },

  //     	    },

  //     	    {
  //     	        name: 'North',
  //     	        title: 'North Direction',
  //     	        img:'images/Direction/Add/North Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	            addPortToDirection("north");
  //     	        },

  //     	    },
  //     	    {
  //     	        name: 'East',
  //     	        title: 'East Direction',
  //     	        img:'images/Direction/Add/East Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("east");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'West',
  //     	        title: 'West Direction',
  //     	        img:'images/Direction/Add/West Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("west");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South',
  //     	        title: 'South Direction',
  //     	        img:'images/Direction/Add/South Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("south");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-East',
  //     	        title: 'North-East Direction',
  //     	        img:'images/Direction/Add/North East Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("ne");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-West',
  //     	        title: 'North-West Direction',
  //     	        img:'images/Direction/Add/North West Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("nw");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-East',
  //     	        title: 'South-East Direction',
  //     	        img:'images/Direction/Add/South East Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("se");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-West',
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Add/South West Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("sw");
  //     	        }
  //     	    },
  //     	    ]
  //     	},

  //   {

  //     	    name: 'Remove Port',
  //     	    img: 'images/General/Ethernet Off-104.png',
  //     	    title: 'Remove Port',
  //     	    subMenu: [{
  //     	        name: 'All..',
  //     	        title: 'Remove All Direction',
  //     	        img:'images/Direction/Remove/Uncheck All Filled-100.png',
  //     	        fun: function () {
  //     	            removePortToDirection("all");
  //     	        }
  //     	    },
  //     	    {
  //     	        name: 'North',
  //     	        title: 'North Direction',
  //     	        img:'images/Direction/Remove/North Filled-100.png',
  //     	        fun: function () {
  //     	            removePortToDirection("north");
  //     	        }
  //     	    },
  //     	    {
  //     	        name: 'East',
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Remove/East Filled-100.png',
  //     	        fun: function () {
  //     	            removePortToDirection("east");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'West',
  //     	        title: 'West Direction',
  //     	        img:'images/Direction/Remove/West Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("west");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South',
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Remove/South Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("south");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-East',
  //     	        title: 'North-East Direction',
  //     	        img:'images/Direction/Remove/North East Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("ne");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-West',
  //     	        title: 'North-West Direction',
  //     	        img:'images/Direction/Remove/North West Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("nw");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-East',
  //     	        title: 'South-East Direction',
  //     	        img:'images/Direction/Remove/South East Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("se");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-West',
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Remove/South West Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("sw");
  //     	        }
  //     	    },
  //     	    ]

  //   },

  {
    name: "Delete Node",
    img: "images/General/Delete-96.png",
    title: "Delete Element",
    fun: function() {
      DeleteNode();
    }
  },
  {
    name: "Fault Node",
    img: "images/General/Error-26 (1).png",
    title: "Fault Node",
    fun: function() {
      createFaultNode();
    }
  },
  {
    name: "Replace Node",
    img: "images/General/Error-26 (1).png",
    title: "Replace Node",
    subMenu: [
      {
        name: nptGlobals.NodeTypeTEDisplayName,
        title: "Replace with TE",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTE);
        }
      },
      {
        name: nptGlobals.NodeTypeCDROADMDisplayName,
        title: "Replace with CD Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeCDROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeILADisplayName,
        title: "Replace with ILA",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeILA);
        }
      },
      {
        name: nptGlobals.NodeTypeTwoDegreeRoadmDisplayName,
        title: "Replace with Two Degree Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTwoDegreeRoadm);
        }
      }
    ]
  }
  /*DBG => Commented since no longer needed, may be use for future reference
              * {
                 name: 'Chassis View',
                 img: 'images/General/Glasses Filled-50.png',
                 title: 'Chassis View',
                 fun: function () {
                 	createChassisView();
                 }
              }*/
];

var contextmenuTE = [
  //        {
  //     	    name: 'Add Port',
  //     	    img: 'images/General/Ethernet On-104.png',
  //     	    title: 'Add Port',
  //     	    subMenu: [{
  //     	        name: 'All..',
  //     	        disable:true,
  //     	        title: 'Add All Direction',
  //     	        img:'images/Direction/Add/Check All Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will add all portn now')
  //     	            addPortToDirection("all");
  //     	        },

  //     	    },

  //     	    {
  //     	        name: 'North',
  //     	        disable:true,
  //     	        title: 'North Direction',
  //     	        img:'images/Direction/Add/North Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	            addPortToDirection("north");
  //     	        },

  //     	    },
  //     	    {
  //     	        name: 'East',
  //     	        title: 'East Direction',
  //     	        img:'images/Direction/Add/East Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("east");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'West',
  //     	        title: 'West Direction',
  //     	        img:'images/Direction/Add/West Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("west");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South',
  //     	        disable:true,
  //     	        title: 'South Direction',
  //     	        img:'images/Direction/Add/South Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("south");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-East',
  //     	        disable:true,
  //     	        title: 'North-East Direction',
  //     	        img:'images/Direction/Add/North East Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("ne");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-West',
  //     	        disable:true,
  //     	        title: 'North-West Direction',
  //     	        img:'images/Direction/Add/North West Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("nw");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-East',
  //     	        disable:true,
  //     	        title: 'South-East Direction',
  //     	        img:'images/Direction/Add/South East Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("se");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-West',
  //     	        disable:true,
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Add/South West Filled-100.png',
  //     	        fun: function () {
  //     	            ///alert('It will merge row')
  //     	        	addPortToDirection("sw");
  //     	        }
  //     	    },
  //     	    ]
  //     	},

  //   {

  //     	    name: 'Remove Port',
  //     	    img: 'images/General/Ethernet Off-104.png',
  //     	    title: 'Remove Port',
  //     	    subMenu: [{
  //     	        name: 'All..',
  //     	        disable:true,
  //     	        title: 'Remove All Direction',
  //     	        img:'images/Direction/Remove/Uncheck All Filled-100.png',
  //     	        fun: function () {
  //     	            removePortToDirection("all");
  //     	        }
  //     	    },
  //     	    {
  //     	        name: 'North',
  //     	        disable:true,
  //     	        title: 'North Direction',
  //     	        img:'images/Direction/Remove/North Filled-100.png',
  //     	        fun: function () {
  //     	            removePortToDirection("north");
  //     	        }
  //     	    },
  //     	    {
  //     	        name: 'East',
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Remove/East Filled-100.png',
  //     	        fun: function () {
  //     	            removePortToDirection("east");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'West',
  //     	        title: 'West Direction',
  //     	        img:'images/Direction/Remove/West Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("west");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South',
  //     	        disable:true,
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Remove/South Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("south");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-East',
  //     	        disable:true,
  //     	        title: 'North-East Direction',
  //     	        img:'images/Direction/Remove/North East Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("ne");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'North-West',
  //     	        disable:true,
  //     	        title: 'North-West Direction',
  //     	        img:'images/Direction/Remove/North West Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("nw");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-East',
  //     	        disable:true,
  //     	        title: 'South-East Direction',
  //     	        img:'images/Direction/Remove/South East Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("se");
  //     	        }
  //     	    },

  //     	    {
  //     	        name: 'South-West',
  //     	        disable:true,
  //     	        title: 'South-West Direction',
  //     	        img:'images/Direction/Remove/South West Filled-100.png',
  //     	        fun: function () {
  //     	        	removePortToDirection("sw");
  //     	        }
  //     	    },
  //     	    ]

  //   },

  {
    name: "Delete Node",
    img: "images/General/Delete-96.png",
    title: "Delete Element",
    fun: function() {
      DeleteNode();
    }
  },
  {
    name: "Fault Node",
    img: "images/General/Error-26 (1).png",
    title: "Fault Node",
    fun: function() {
      createFaultNode();
    }
  },
  {
    name: "Replace Node",
    img: "images/General/Error-26 (1).png",
    title: "Replace Node",
    subMenu: [
      {
        name: nptGlobals.NodeTypeTEDisplayName,
        title: "Replace with TE",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTE);
        }
      },
      {
        name: nptGlobals.NodeTypeROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeCDROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeCDROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeILADisplayName,
        title: "Replace with ILA",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeILA);
        }
      },
      {
        name: nptGlobals.NodeTypeTwoDegreeRoadmDisplayName,
        title: "Replace with Two Degree Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTwoDegreeRoadm);
        }
      }
    ]
  }

  /*DBG => Commented since no longer needed, may be use for future reference
              * {
                 name: 'Chassis View',
                 img: 'images/General/Glasses Filled-50.png',
                 title: 'Chassis View',
                 fun: function () {
                 	createChassisView();
                 }
              }*/
];

var contextmenuILA = [
  {
    name: "Delete Node",
    img: "images/General/Delete-96.png",
    title: "Delete Element",
    fun: function() {
      DeleteNode();
    }
  },
  {
    name: "Fault Node",
    img: "images/General/Error-26 (1).png",
    title: "Fault Node",
    fun: function() {
      createFaultNodeIla();
    }
  },
  {
    name: "Replace Node",
    img: "images/General/Error-26 (1).png",
    title: "Replace Node",
    subMenu: [
      {
        name: nptGlobals.NodeTypeTEDisplayName,
        title: "Replace with TE",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTE);
        }
      },
      {
        name: nptGlobals.NodeTypeROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeCDROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeCDROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeILADisplayName,
        title: "Replace with ILA",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeILA);
        }
      },
      {
        name: nptGlobals.NodeTypeTwoDegreeRoadmDisplayName,
        title: "Replace with Two Degree Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTwoDegreeRoadm);
        }
      }
    ]
  }

  /*DBG => Commented since no longer needed, may be use for future reference
                * {
                   name: 'Chassis View',
                   img: 'images/General/Glasses Filled-50.png',
                   title: 'Chassis View',
                   fun: function () {
                   	createChassisView();
                   }
                }*/
];

var contextmenuTwoDegreeRoadm = [
  //     {
  //  	    name: 'Add Port',
  //  	    img: 'images/General/Ethernet On-104.png',
  //  	    title: 'Add Port',
  //  	    subMenu: [{
  //  	        name: 'All..',
  //  	        disable:true,
  //  	        title: 'Add All Direction',
  //  	        img:'images/Direction/Add/Check All Filled-100.png',
  //  	        fun: function () {
  //  	            ///alert('It will add all portn now')
  //  	            addPortToDirection("all");
  //  	        },

  //  	    },
  //  	    {
  //  	        name: 'East',
  //  	        title: 'East Direction',
  //  	        img:'images/Direction/Add/East Filled-100.png',
  //  	        fun: function () {
  //  	            ///alert('It will merge row')
  //  	        	addPortToDirection("east");
  //  	        }
  //  	    },

  //  	    {
  //  	        name: 'West',
  //  	        title: 'West Direction',
  //  	        img:'images/Direction/Add/West Filled-100.png',
  //  	        fun: function () {
  //  	            ///alert('It will merge row')
  //  	        	addPortToDirection("west");
  //  	        }
  //  	    },
  //  	    ]
  //  	},

  // {

  //  	    name: 'Remove Port',
  //  	    img: 'images/General/Ethernet Off-104.png',
  //  	    title: 'Remove Port',
  //  	    subMenu: [{
  //  	        name: 'All..',
  //  	        disable:true,
  //  	        title: 'Remove All Direction',
  //  	        img:'images/Direction/Remove/Uncheck All Filled-100.png',
  //  	        fun: function () {
  //  	            removePortToDirection("all");
  //  	        }
  //  	    },
  //  	    {
  //  	        name: 'East',
  //  	        title: 'South-West Direction',
  //  	        img:'images/Direction/Remove/East Filled-100.png',
  //  	        fun: function () {
  //  	            removePortToDirection("east");
  //  	        }
  //  	    },

  //  	    {
  //  	        name: 'West',
  //  	        title: 'West Direction',
  //  	        img:'images/Direction/Remove/West Filled-100.png',
  //  	        fun: function () {
  //  	        	removePortToDirection("west");
  //  	        }
  //  	    },
  //  	    ]

  // },

  {
    name: "Delete Node",
    img: "images/General/Delete-96.png",
    title: "Delete Element",
    fun: function() {
      DeleteNode();
    }
  },
  {
    name: "Fault Node",
    img: "images/General/Error-26 (1).png",
    title: "Fault Node",
    fun: function() {
      createFaultNode();
    }
  },
  {
    name: "Replace Node",
    img: "images/General/Error-26 (1).png",
    title: "Replace Node",
    subMenu: [
      {
        name: nptGlobals.NodeTypeTEDisplayName,
        title: "Replace with TE",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTE);
        }
      },
      {
        name: nptGlobals.NodeTypeROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeCDROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeCDROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeILADisplayName,
        title: "Replace with ILA",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeILA);
        }
      },
      {
        name: nptGlobals.NodeTypeTwoDegreeRoadmDisplayName,
        title: "Replace with Two Degree Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTwoDegreeRoadm);
        }
      }
    ]
  }
];

var contextmenuHub = [
  //     {
  //  	    name: 'Add Port',
  //  	    img: 'images/General/Ethernet On-104.png',
  //  	    title: 'Add Port',
  //  	    subMenu: [{
  //  	        name: 'All..',
  //  	        disable:true,
  //  	        title: 'Add All Direction',
  //  	        img:'images/Direction/Add/Check All Filled-100.png',
  //  	        fun: function () {
  //  	            ///alert('It will add all portn now')
  //  	            addPortToDirection("all");
  //  	        },

  //  	    },
  //  	    {
  //  	        name: 'East',
  //  	        title: 'East Direction',
  //  	        img:'images/Direction/Add/East Filled-100.png',
  //  	        fun: function () {
  //  	            ///alert('It will merge row')
  //  	        	addPortToDirection("east");
  //  	        }
  //  	    },

  //  	    {
  //  	        name: 'West',
  //  	        title: 'West Direction',
  //  	        img:'images/Direction/Add/West Filled-100.png',
  //  	        fun: function () {
  //  	            ///alert('It will merge row')
  //  	        	addPortToDirection("west");
  //  	        }
  //  	    },
  //  	    ]
  //  	},

  // {

  //  	    name: 'Remove Port',
  //  	    img: 'images/General/Ethernet Off-104.png',
  //  	    title: 'Remove Port',
  //  	    subMenu: [{
  //  	        name: 'All..',
  //  	        disable:true,
  //  	        title: 'Remove All Direction',
  //  	        img:'images/Direction/Remove/Uncheck All Filled-100.png',
  //  	        fun: function () {
  //  	            removePortToDirection("all");
  //  	        }
  //  	    },
  //  	    {
  //  	        name: 'East',
  //  	        title: 'South-West Direction',
  //  	        img:'images/Direction/Remove/East Filled-100.png',
  //  	        fun: function () {
  //  	            removePortToDirection("east");
  //  	        }
  //  	    },

  //  	    {
  //  	        name: 'West',
  //  	        title: 'West Direction',
  //  	        img:'images/Direction/Remove/West Filled-100.png',
  //  	        fun: function () {
  //  	        	removePortToDirection("west");
  //  	        }
  //  	    },
  //  	    ]

  // },

  {
    name: "Delete Node",
    img: "images/General/Delete-96.png",
    title: "Delete Element",
    fun: function() {
      DeleteNode();
    }
  },
  {
    name: "Fault Node",
    img: "images/General/Error-26 (1).png",
    title: "Fault Node",
    fun: function() {
      createFaultNode();
    }
  },
  {
    name: "Replace Node",
    img: "images/General/Error-26 (1).png",
    title: "Replace Node",
    subMenu: [
      {
        name: nptGlobals.NodeTypeTEDisplayName,
        title: "Replace with TE",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTE);
        }
      },
      {
        name: nptGlobals.NodeTypeROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeCDROADMDisplayName,
        title: "Replace with Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeCDROADM);
        }
      },
      {
        name: nptGlobals.NodeTypeILADisplayName,
        title: "Replace with ILA",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeILA);
        }
      },
      {
        name: nptGlobals.NodeTypeTwoDegreeRoadmDisplayName,
        title: "Replace with Two Degree Roadm",
        img: "images/Direction/Remove/Uncheck All Filled-100.png",
        fun: function() {
          replaceNode(nptGlobals.NodeTypeTwoDegreeRoadm);
        }
      }
    ]
  }
];

var contextmenuLink = [
  {
    name: "Delete Link",
    img: "images/General/Delete-96.png",
    title: "Delete Link",
    fun: function() {
      DeleteNode();
    }
  },
  {
    name: "Fault Link",
    img: "images/General/Error-26 (1).png",
    title: "Fault Link",
    fun: function() {
      createFaultNode();
    }
  },
  {
    name: "Insert ILA",
    img: "images/General/Error-26 (1).png",
    title: "Insert Ila",
    fun: function() {
      insertIlaOnLink();
    }
  }
];
/**
 * Function to Show Tabular as well as Graphical View of the Selected Node : DBG => Commented since no longer needed, may be use for future reference
 */
/*var createChassisView = function(){
	
	//window.chassisViewCallCount++;
	//$("#canvasIdContainer .tab-content").append(fGetChassisViewDivTab(window.chassisViewCallCount));
	//$("#menuDivInsideCanvas .nav-tabs").append(fGetChassisViewListTag(window.chassisViewCallCount));
	
	//drawOnCanvasForFun();
	
	//$('#chassisViewModal').modal('show');
	//console.log("chassisViewModal Loaded");
	
	//initializeChassisView();//Function Call
	
	//ajax call to enable later for the actual data from db
	$.ajax({
		   
		type: "GET",
	    contentType: "application/json",

	    headers: {          
	        Accept : "application/json; charset=utf-8",         
	       "Content-Type": "application/json; charset=utf-8"   
	    } ,    
	 
	   
	   url: "/getChassisViewjsonDataRequest",        
	   
	   timeout: 600000,

	   success: function (data) {
		   console.log("chassis view data fetched successfully");
		   console.log(data);		
		   ctx.fillText("Look for chassis json data in console", canvas.width/2, canvas.height/2); 	
	   },
	  
	   error: function (e) {
		   console.log("fail");
		   console.log(e);  }
		});
	
	
}
*/
/*var drawOnCanvasForFun=function()
{
	var canvas = document.getElementById("chassisViewCanvas");
	var ctx = canvas.getContext("2d");
	ctx.font = "12px Comic Sans MS";
	ctx.fillStyle = "red";
	ctx.textAlign = "center";
	ctx.fillText("Yo!! Chassis View is Here !!", canvas.width/2, canvas.height/2); 	
}

var fGetChassisViewListTag=function(id)
{
	var liElementChassis=`<li><a data-toggle="tab" href="#menu${id}">Chassis View<i class="fa fa-times tabCloseBtn" aria-hidden="true"></i></a></li>`;
	return liElementChassis;
}

var fGetChassisViewDivTab=function(id)
{
	var divElementChassis=`<div id="menu${id}" class="tab-pane fade col-md-12 col-sm-12">
		<canvas id="chassisview" style="width:500px;height:400px;border:2px solid black;"><canvas>
		</div>`;
	return divElementChassis;
}*/

$("body").delegate(".tabCloseBtn", "click", function(evt) {
  console.log("a element ::" + $(this).parent());
  var tabId = $(this)
    .parent()
    .attr("href");
  console.log("tab Id :: " + tabId);
  $(tabId).remove();
  $(this)
    .parent()
    .remove();
  window.chassisViewCallCount--;
});

var initializeChassisView = function() {
  /**Get Data for Chassis*/
  var chassisViewListArr = dummyCVJson.chassisViewDbData;
  console.log("chassisViewListArr " + chassisViewListArr);

  $(".modalBodyCvContentPanelChassisTabulaView tbody").empty();

  var str = "";

  for (var i = 0; i < chassisViewListArr.length; i++) {
    str += `<tr>
		<td>${chassisViewListArr[i].Rack}</td>
		<td>${chassisViewListArr[i].Subrack}</td>
		<td>${chassisViewListArr[i].Card}</td>
		<td>${chassisViewListArr[i].CardType}</td>		
		</tr>`;
    //console.log(str);
  }
  $(".modalBodyCvContentPanelChassisTabulaView tbody").append(str);
};

var generateLinkFault = function(cellView, evt) {
  //Do nothing
  $("#toolTip").remove();
  $("#linkToolTip").remove();

  //set global variable to this node
  nptGlobals.contextElSelected = cellView;

  //add tools for tooltip
  var removeFaultAnalysis = `<a class="removeFaultAnalysisToolTip" href="#" data-toggle="tooltip" title="Remove Link for Fault Analysis"><i class="fa fa-times-circle-o" aria-hidden="true"></i></a>`;
  //create div as tooltip around the node
  var div = $('<div id="linkToolTip">').css({
    position: "absolute",
    left: evt.clientX,
    top: evt.clientY
  });

  //console.log(cellView.model.get('position'));

  //append tools to tooltip
  // console.log(nptGlobals.isFaultAnalysisReady);
  //if(nptGlobals.isFaultAnalysisReady)
  div.append(removeFaultAnalysis);

  //console.log(div);

  //append tooltip to body,hence to node
  $(document.body).append(div);
};

/**
 * Delete A Node From the Network for Fault Analysis
 */
function createFaultNode() {
  //console.log(nptGlobals.contextElSelected);
  var cell = cellLocal.model;
  console.log(
    "createFaultNode() ---- isFaultAnalysisReady::",
    nptGlobals.isFaultAnalysisReady,
    " faultNode::",
    cell.get("nodeProperties").faultNode
  );
  if (nptGlobals.getRwaExecutedStatus()) {
    if (cell.get("nodeProperties").faultNode == 0) {
      if (nptGlobals.faultElementsCount < 3) {
        cell.get("nodeProperties").faultNode = 1;
        //nptGlobals.faultNodesArr.push(cell.get('nodeProperties').nodeId);
        cell.attr(attrs.elementFault);
        nptGlobals.faultElementsCount++;
        executeFaultAnalysis();
      } else {
        bootBoxAlert("Total Fault Nodes/Links count cannot be more than 3");
      }
    } else {
      //If element was set as fault cell , then remove from fault analysis if clicked again on fault remove tool
      cell.get("nodeProperties").faultNode = 0;
      //nptGlobals.faultNodesArr.pop();
      cell.attr(attrs.elementDefault);
      cell.attr({ ".label": { text: cell.get("nodeProperties").nodeId } });
      nptGlobals.faultElementsCount--;
      executeFaultAnalysis();
    }
  } else {
    bootBoxAlert(
      "First run RWA algorithm on the circuit,then do Fault Analysis"
    );
  }
  $("#linkToolTip").remove();
  $("#toolTip").remove();
}

/**
 * Delete A Node From the Network
 */
function DeleteNode() {
  cellLocal.model.remove();
}

/**********remove link using tooltip******************
 * **************************************** */
$("body").delegate("#removeLinkToolTip", "click", function(evt) {
  console.log("Inside removeLinkToolTip");
  //var cell=evt.target;
  //console.log(nptGlobals.contextElSelected);
  var link = nptGlobals.contextElSelected.model;

  /*var src, dest;
    var dir1 = link.attributes.source.port,
        dir2 = link.attributes.target.port;
    console.log(dir1 + dir2);

    src = graph.getCell(link.attributes.source.id);
    dest = graph.getCell(link.attributes.target.id);
    
    //var srcdir=src.attributes.nodeProperties.direction,destdir=dest.attributes.nodeProperties.direction;

    src.attributes.nodeProperties.directions[dir1] = "0";
    dest.attributes.nodeProperties.directions[dir2] = "0";*/

  link.remove();

  $("#linkToolTip").remove();
  $("#toolTip").remove();
});

/**********remove node using tooltip******************
 * **************************************** */
$("body").delegate("#removeNodeToolTip", "click", function(evt) {
  console.log("Inside removeNodeToolTip");
  //var cell=evt.target;
  //console.log(nptGlobals.contextElSelected);
  var node = nptGlobals.contextElSelected.model;

  node.remove();

  $("#linkToolTip").remove();
  $("#toolTip").remove();
});

/**********remove node using link-tooltip******************
 * **************************************** */
$("body").delegate("#addIlaBetweenNodes", "click", function(evt) {
  console.log("Inside addIlaBetweenNodes ");

  let cellViewModel = nptGlobals.contextElSelected.model;
  let LinkProperties = cellViewModel.get("linkProperties");

  linkUserInput = {
    linkId: cellViewModel.id,
    cid: cellViewModel.cid,
    fiberType: LinkProperties.fiberType,
    cdCoefficient: LinkProperties.cdCoefficient,
    pmdCoefficient: LinkProperties.pmdCoefficient,
    cd: LinkProperties.cd,
    pmd: LinkProperties.pmd,
    spanLength: LinkProperties.spanLength,
    splice: LinkProperties.splice,
    lossPerSplice: LinkProperties.lossPerSplice,
    connector: LinkProperties.connector,
    lossPerConnector: LinkProperties.lossPerConnector,
    coefficient: LinkProperties.coefficient,
    calculatedSpanLoss: LinkProperties.calculatedSpanLoss,
    adjustableSpanLoss: LinkProperties.adjustableSpanLoss,
    costMetric: LinkProperties.costmetric,
    opticalParameter: LinkProperties.opticalparameter || "",
    color: LinkProperties.color,
    srlg: LinkProperties.srlg,
    linkType: LinkProperties.linkType
  };

  IlaModule.validateIlaInsertion(cellViewModel, linkUserInput);
  $("#linkToolTip").remove();
  $("#toolTip").remove();
});

/*****************************************
 * *********remove link/node for fault analysis******************
 * **************************************** */
$("body").delegate(".removeFaultAnalysisToolTip", "click", function(evt) {
  console.log("Inside removeFaultAnalysisToolTip");
  var cell = nptGlobals.contextElSelected.model;
  //nptGlobals.isFaultAnalysisReady=true;
  if (nptGlobals.getRwaExecutedStatus()) {
    if (cell.isLink()) {
      console.log(nptGlobals.contextElSelected);
      //console.log("isFaultAnalysisReady::"+nptGlobals.isFaultAnalysisReady+" faultLink::"+cell.get('linkProperties').linkId)
      if (cell.get("linkProperties").faultLink == 0) {
        if (nptGlobals.faultElementsCount < 3) {
          cell.get("linkProperties").faultLink = 1;
          //var linkSrcAndDest=cell.get('source').NodeId+","+cell.get('target').NodeId;
          //nptGlobals.faultLinksArr.push(linkSrcAndDest);

          // cell.attr(attrs.linkFaultAnalysis);
          // cell.label(0, {attrs: { text: { text: 'X' } } });
          // cell.prop(['labels',0, 'attrs','text','text'], 'label text');

          console.log("Link Fault Add", cell.label);
          if (cell.get("linkProperties").isIlaLink == "0")
            cell.label(0, { attrs: { text: { text: "X" } } });
          else
            cell.label(0, {
              position: 0.5,
              attrs: {
                text: {
                  text: "X",
                  stroke: "#d9534f",
                  fill: "#d9534f",
                  "font-size": 30
                }
              },
              rect: {
                fill: "rgba(0,0,0,0)",
                stroke: "#7c68fc",
                "stroke-width": 0,
                rx: 5,
                ry: 5
              }
            });

          nptGlobals.faultElementsCount++;
          console.log(
            "nptGlobals.faultElementsCount ::" + nptGlobals.faultElementsCount
          );
          executeFaultAnalysis();
        } else {
          bootBoxAlert("Total Fault Nodes/Links count cannot be more than 3");
        }
      } else {
        //If element was set as fault cell , then remove from fault analysis if clicked again on fault remove tool
        cell.get("linkProperties").faultLink = 0;
        executeFaultAnalysis();
        //nptGlobals.faultLinksArr.pop();
        var isLineProtected = cell.get("linkProperties").lineProtection;

        // if(isLineProtected==1)
        // 	cell.attr(attrs.linkDefaultLinePtc);
        // else cell.attr(attrs.linkDefault);

        // cell.label(0, { attrs: { text: { text: '' } } });
        // console.log("Link Fault Remove",cell.label);

        if (cell.get("linkProperties").isIlaLink == "0")
          cell.label(0, { attrs: { text: { text: "" } } });
        else cell.label(0, { position: 0.5, attrs: { text: { text: "" } } });

        nptGlobals.faultElementsCount--;
        console.log(
          "nptGlobals.faultElementsCount ::" + nptGlobals.faultElementsCount
        );
      }
      //			/$("#linkToolTip").remove();
    }
  } else {
    bootBoxAlert(
      "First run RWA algorithm on the circuit,then do Fault Analysis"
    );
  }

  $("#linkToolTip").remove();
  $("#toolTip").remove();
});

/*****************************************
 * *********remove/Add link OMS protection ******************
 * **************************************** */
$("body").delegate("#lineProtection", "click", function(evt) {
  console.log("Inside lineProtection");
  var cell = nptGlobals.contextElSelected.model;
  fSetLinkOmsProtection(cell);

  $("#linkToolTip").remove();
  $("#toolTip").remove();
});

function fSetLinkOmsProtection(link) {
  //nptGlobals.isFaultAnalysisReady=true;
  console.log("Link");
  console.log(link);
  var isLineProtected = link.get("linkProperties").lineProtection;

  if (isLineProtected == 0) {
    link.get("linkProperties").lineProtection = 1;
    link.attr(attrs.linkDefaultLinePtc);
  } else {
    link.get("linkProperties").lineProtection = 0;
    link.attr(attrs.linkDefault);
  }
}

/*****************************************
 * *********remove Ila node for fault analysis******************
 * **************************************** */
function createFaultNodeIla() {
  console.log("Inside removeFaultAnalysisToolTip");
  var cell = cellLocal.model;
  //nptGlobals.isFaultAnalysisReady=true;
  if (nptGlobals.getRwaExecutedStatus()) {
    //console.log(nptGlobals.contextElSelected);
    console.log("Ila case for removal from faulty nodes");
    //console.log("isFaultAnalysisReady::"+nptGlobals.isFaultAnalysisReady+" faultNode::"+cell.get('nodeProperties').faultNode)
    if (cell.get("nodeProperties").faultNode == 0) {
      if (nptGlobals.faultElementsCount < 3) {
        cell.get("nodeProperties").faultNode = 1;
        //nptGlobals.faultNodesArr.push(cell.get('nodeProperties').nodeId);
        // if(cell.get('type')==nptGlobals.NodeTypeILA)
        // 	cell.attr(attrs.elementFault);
        // else cell.attr(attrs.elementFault);
        cell.attr(attrs.elementFault);
        nptGlobals.faultElementsCount++;
        console.log(
          "nptGlobals.faultElementsCount ::" + nptGlobals.faultElementsCount
        );
        executeFaultAnalysis();
      } else {
        bootBoxAlert("Total Fault Nodes/Links count cannot be more than 3");
      }
    } else {
      //If element was set as fault cell , then remove from fault analysis if clicked again on fault remove tool
      cell.get("nodeProperties").faultNode = 0;
      //nptGlobals.faultNodesArr.pop();
      //nptGlobals.faultNodesArr.pop();
      if (cell.get("type") == nptGlobals.NodeTypeILA)
        cell.attr(attrs.IlaDefault);
      else cell.attr(attrs.elementDefault);
      cell.attr({ ".label": { text: cell.get("nodeProperties").nodeId } });
      nptGlobals.faultElementsCount--;
      console.log(
        "nptGlobals.faultElementsCount ::" + nptGlobals.faultElementsCount
      );
      executeFaultAnalysis();
    }
  } else {
    bootBoxAlert(
      "First run RWA algorithm on the circuit,then do Fault Analysis"
    );
  }
}

function executeFaultAnalysis() {
  console.log("Inside executeFaultAnalysis");
  nptGlobals.faultNodesArr = [];
  nptGlobals.faultLinksArr = [];
  _.each(graph.getElements(), function(el) {
    if (el.get("nodeProperties").faultNode == 1) {
      nptGlobals.faultNodesArr.push(el.attributes.nodeProperties.nodeId);
    }
  });

  _.each(graph.getLinks(), function(el) {
    if (el.get("linkProperties").faultLink == 1) {
      nptGlobals.faultLinksArr.push(
        el.get("source").NodeId + "," + el.get("target").NodeId
      );
    }
  });

  console.log("faultNodes Array");
  console.log(nptGlobals.faultNodesArr);

  for (var i = 0; i < nptGlobals.faultLinksArr.length; i++) {
    console.log("Links for Fault :", i, nptGlobals.faultLinksArr[i]);
  }

  for (var i = 0; i < nptGlobals.faultNodesArr.length; i++) {
    console.log("Nodes for Fault :", i, nptGlobals.faultNodesArr[i]);
  }

  if (
    nptGlobals.faultNodesArr.length > 0 ||
    nptGlobals.faultLinksArr.length > 0
  ) {
    getDemandMatrixFaultAnalysis();
    updateDemandMatrixTabFaultAnalysis();
  } else {
    console.log("Nodes Fault Array is empty");
    console.log("Links Fault Array is empty");
    //console.log(nptGlobals.DemandMatrixViewData.view1);
    showFaultAnalysisData(nptGlobals.DemandMatrixViewData.view1);
  }
}

function updateDemandMatrixTabFaultAnalysis() {
  /*if(nptGlobals.DemandMatrixViewData!=null)
	    var demandMatrixDataArr=nptGlobals.DemandMatrixViewData.view1;*/
  console.log("Inside updateDemandMatrixTabFaultAnalysis");
  if (nptGlobals.faultElementsCount > 0) {
    $("#DemandMatrixTable tbody")
      .find("td#pathColumn")
      .filter(function() {
        return !$(this).hasClass("rejectedWithError");
      })
      .each(function() {
        //get all rows in table

        //console.log($(this));
        var isIncludedinPath = 0;
        var classname = $(this)[0].className;
        var classnameArr = $(this)[0].className.split(" ");
        //console.log(classname);
        var demandPathStr = $(this).html();
        var demandPathArr = $(this)
          .html()
          .split(",");
        var $tr = $(this).closest("tr");

        //node fault analysis
        for (var path = 0; path < demandPathArr.length; path++) {
          for (var j = 0; j < nptGlobals.faultNodesArr.length; j++) {
            if (nptGlobals.faultNodesArr[j] == demandPathArr[path]) {
              $tr.find("td." + classnameArr[0]).addClass("fault");
              isIncludedinPath = 1;
              break;
            }
          }

          if (isIncludedinPath == 1) break;
        }

        //link fault Analysis
        if (!isIncludedinPath) {
          for (var l = 0; l < nptGlobals.faultLinksArr.length; l++) {
            var reversedPathStr = nptGlobals.faultLinksArr[l]
              .split(",")
              .reverse()
              .join(",");
            console.log("reversed Path string :: " + reversedPathStr);
            if (
              demandPathStr.includes(nptGlobals.faultLinksArr[l]) ||
              demandPathStr.includes(reversedPathStr)
            ) {
              $tr.find("td." + classnameArr[0]).addClass("fault");
              isIncludedinPath = 1;
              break;
            }
          }
        }

        //remove fault if this does not include fault node but was previously a fault node
        if (isIncludedinPath == 0 && classname.includes("fault")) {
          console.log(
            "isIncludedinPath::" +
              isIncludedinPath +
              "  classname[1]::" +
              classnameArr[1]
          );
          $tr.find("td." + classnameArr[0]).removeClass("fault");
        }
      });
  } else {
    console.log("Nodes Fault Array is empty");
    console.log($("#DemandMatrixTable td.fault"));
    showFaultAnalysisData(nptGlobals.DemandMatrixViewData.view1);
    //$("#DemandMatrixTable td.fault").removeClass('fault');
  }
}

var getDemandMatrixFaultAnalysis = function() {
  var template = "";
  var jsonArr = nptGlobals.DemandMatrixViewData.view1;
  var temp = 0,
    rowSpanValue = 0;
  console.log(jsonArr);
  var demandFinalOutputobj = [];
  for (var i = 0; i < jsonArr.length; i++) {
    //do something with obj[i]
    var tempObj = [];
    var isPathFaulty = 0;

    //console.log("Value of i :"+i);
    //console.log(jsonArr[i]);

    var errorState = jsonArr[i].RoutePriority,
      errorString = "";
    //errorState: 0=no paths ,-1=less paths ,5=paths with error
    if (errorState == nptGlobals.NoPathRwaErrorState) {
      errorString = jsonArr[i].error;
      //console.log("Error String for 0 case :"+errorString);
    } else if (errorState == nptGlobals.LessPathRwaErrorState) {
      errorString = jsonArr[i].error;
      //console.log("Error String for -1 case :"+errorString);
      errorState = jsonArr[i].RoutePriority;
      i++;
    }

    //var ptcType=jsonArr[i].ProtectionType;
    var demandId = jsonArr[i].DemandId;
    //console.log("Demand id value :"+ demandId);
    tempObj[0] = jsonArr[i];
    console.log("tempObj ::");
    console.log(tempObj);

    isPathFaulty = checkPathForFaultNodeInclusion(jsonArr[i]);

    temp = 0;
    var count = i + 1;
    while (count < jsonArr.length) {
      if (jsonArr[count].DemandId == demandId) {
        if (jsonArr[count].RoutePriority != nptGlobals.LessPathRwaErrorState) {
          tempObj[++temp] = jsonArr[count];

          if (
            !isPathFaulty &&
            jsonArr[count].RoutePriority < nptGlobals.RejectedPathRwaErrorState
          )
            // Don't include faults for rejected paths
            isPathFaulty = checkPathForFaultNodeInclusion(jsonArr[count]);
        }

        count++;
        //temp++;
      } else break;
    }

    if (isPathFaulty) {
      console.log("tempObj ::");
      console.log(tempObj);

      for (var d = 0; d < tempObj.length; d++)
        demandFinalOutputobj.push(tempObj[d]);

      console.log("demandFinalOutputobj ::");
      console.log(demandFinalOutputobj);

      //showFaultAnalysisData(demandFinalOutputobj);
    }
  }

  showFaultAnalysisData(demandFinalOutputobj);
};

var checkPathForFaultNodeInclusion = function(demandViewObj) {
  var isPathFaulty = 0;
  var demandPathStr = demandViewObj.Path;
  var demandPathArr = demandViewObj.Path.split(",");
  //node fault analysis
  for (var path = 0; path < demandPathArr.length; path++) {
    for (var j = 0; j < nptGlobals.faultNodesArr.length; j++) {
      if (nptGlobals.faultNodesArr[j] == demandPathArr[path]) {
        isPathFaulty = 1;
        break;
      }
    }

    if (isPathFaulty == 1) break;
  }

  //link fault Analysis
  if (!isPathFaulty) {
    for (var l = 0; l < nptGlobals.faultLinksArr.length; l++) {
      var reversedPathStr = nptGlobals.faultLinksArr[l]
        .split(",")
        .reverse()
        .join(",");
      console.log("reversed Path string :: " + reversedPathStr);
      if (
        demandPathStr.includes(nptGlobals.faultLinksArr[l]) ||
        demandPathStr.includes(reversedPathStr)
      ) {
        //$tr.find('td.'+classnameArr[0]).addClass('fault');
        isPathFaulty = 1;
        break;
      }
    }
  }

  return isPathFaulty;
};

var showFaultAnalysisData = function(data) {
  console.log("showFaultAnalysisData()------ Data:", data);
  var template = "";
  var jsonArr = data;
  var temp = 0,
    rowSpanValue = 0;
  console.log(jsonArr);
  if (data.length > 0) {
    for (var i = 0; i < jsonArr.length; i++) {
      var errorState = jsonArr[i].RoutePriority,
        errorString = "";
      //errorState: 0=no paths ,-1=less paths ,5=paths with error
      if (errorState == 0) {
        errorString = jsonArr[i].error;
        console.log("Error String for 0 case :" + errorString);
      } else if (errorState == -1) {
        errorString = jsonArr[i].error;
        console.log("Error String for -1 case :" + errorString);
        errorState = jsonArr[i].RoutePriority;
        i++;
      }

      // do something with obj[i]
      console.log("Value of i :" + i);
      console.log(jsonArr[i]);
      // var ptcType=jsonArr[i].ProtectionType;
      var demandId = jsonArr[i].DemandId;
      console.log("Demand id value :" + demandId);

      temp = 0;
      var count = i + 1;
      while (count < jsonArr.length) {
        if (jsonArr[count].DemandId == demandId) {
          count++;
          temp++;
        } else break;
      }

      console.log("Value of temp :" + temp);
      rowSpanValue = temp + 1;
      var state = jsonArr[i].State + "RowClass";
      if (errorState == nptGlobals.NoPathRwaErrorState) {
        template += `<tr><td rowspan='${rowSpanValue}' class='commonRow'>`;
        template += getLabelTagsFromState(state);
        template += `</td><td rowspan='${rowSpanValue}' class='commonRow demandId'>${
          jsonArr[i].DemandId
        }</td>`;
        template += `<td rowspan='${rowSpanValue}' class='commonRow srcNodeClass'>${
          jsonArr[i].SrcNodeId
        }</td>`;
        template += `<td rowspan='${rowSpanValue}' class='commonRow destNodeClass'>${
          jsonArr[i].DestNodeId
        }</td>`;

        if (errorState == nptGlobals.LessPathRwaErrorState)
          template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
            jsonArr[i].ProtectionType
          }<p class=".bg-warning text-muted smallText text-center ">(${errorString})</p></td>`;
        else
          template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
            jsonArr[i].ProtectionType
          }</td>`;

        template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
          jsonArr[i].Traffic
        }</td>`;
        template += `<td class='working noPathFoundClass' id='pathColumn'>-</td>`;
        template += `<td class='working noPathFoundClass text-center' id='rwaError'>${
          jsonArr[i].error
        }</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
        template += `<td class='working noPathFoundClass'>-</td>`;
      } else {
        template += `<tr><td rowspan='${rowSpanValue}' class='commonRow'>`;
        template += getLabelTagsFromState(state);
        template += `</td><td rowspan='${rowSpanValue}' class='commonRow demandId'>${
          jsonArr[i].DemandId
        }</td>`;
        template += `<td rowspan='${rowSpanValue}' class='commonRow srcNodeClass'>${
          jsonArr[i].SrcNodeId
        }</td>`;
        template += `<td rowspan='${rowSpanValue}' class='commonRow destNodeClass'>${
          jsonArr[i].DestNodeId
        }</td>`;

        if (errorState == nptGlobals.LessPathRwaErrorState)
          template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
            jsonArr[i].ProtectionType
          }<p class=".bg-warning text-muted smallText text-center">(${errorString})</p></td>`;
        else
          template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
            jsonArr[i].ProtectionType
          }</td>`;

        template += `<td rowspan='${rowSpanValue}' class='commonRow '>${
          jsonArr[i].Traffic
        }</td>`;
        template += `<td class='working' id='pathColumn'>${
          jsonArr[i].Path
        }</td>`;
        template += `<td class='working text-center' id='rwaError'>${
          jsonArr[i].error
        }</td>`;
        template += `<td class='working text-center' id='regenLoc'>${
          jsonArr[i].regeneratorLoc
        }</td>`;
        template += `<td class='working' id='pathType'>${
          jsonArr[i].PathType
        }</td>`;
        template += `<td class='working'>${jsonArr[i].WavelengthNo}</td>`;
        template += `<td class='working'>${jsonArr[i].Osnr}</td>`;
        template += `<td class='working'>${jsonArr[i].lineRate}</td>`;
        template += `<td class='working'>${jsonArr[i].Spanlength}</td>`;
        template += `<td class='working'>${jsonArr[i].Cost}</td>`;
        template += `<td class='working'>${jsonArr[i].cd.toFixed(2)}</td>`;
        template += `<td class='working'>${jsonArr[i].pmd.toFixed(
          2
        )}</td></tr>`;
      }

      // count=i;
      while (temp) {
        // /console.log(jsonArr[i].Routepriority+ "Value of I :"+i);
        i++;
        if (jsonArr[i].RoutePriority == 2) {
          console.log(jsonArr[i].RoutePriority + "Value of I :" + i);
          template += `<tr><td class='protection' id='pathColumn'>${
            jsonArr[i].Path
          }</td>`;
          //if(errorState!=-1)
          template += `<td class='protection text-center' id='rwaError'>${
            jsonArr[i].error
          }</td>`;
          template += `<td class='protection text-center' id='regenLoc'>${
            jsonArr[i].regeneratorLoc
          }</td>`;
          template += `<td class='protection' id='pathType'>${
            jsonArr[i].PathType
          }</td>`;
          template += `<td class='protection'>${jsonArr[i].WavelengthNo}</td>`;
          template += `<td class='protection'>${jsonArr[i].Osnr}</td>`;
          template += `<td class='protection'>${jsonArr[i].lineRate}</td>`;
          template += `<td class='protection'>${jsonArr[i].Spanlength}</td>`;
          template += `<td class='protection'>${jsonArr[i].Cost}</td><`;
          template += `<td class='protection'>${jsonArr[i].cd.toFixed(2)}</td>`;
          template += `<td class='protection'>${jsonArr[i].pmd.toFixed(
            2
          )}</td></tr>`;
        } else if (
          jsonArr[i].RoutePriority >= nptGlobals.RejectedPathRwaErrorState
        ) {
          //case when path returned with issues
          console.log(jsonArr[i].RoutePriority + "Value of I :" + i);
          template += `<tr><td class='rejectedWithError' id='pathColumn'>${
            jsonArr[i].Path
          }</td>`;
          //if(errorState!=-1)
          template += `<td class='rejectedWithError text-center' id='rwaError'>${
            jsonArr[i].error
          }</td>`;
          template += `<td class='rejectedWithError text-center' id='regenLoc'>${
            jsonArr[i].regeneratorLoc
          }</td>`;
          template += `<td class='rejectedWithError' id='pathType'>${
            jsonArr[i].PathType
          }</td>`;
          template += `<td class='rejectedWithError'>${
            jsonArr[i].WavelengthNo
          }</td>`;
          template += `<td class='rejectedWithError'>${jsonArr[i].Osnr}</td>`;
          template += `<td class='rejectedWithError'>${
            jsonArr[i].lineRate
          }</td>`;
          template += `<td class='rejectedWithError'>${
            jsonArr[i].Spanlength
          }</td>`;
          template += `<td class='rejectedWithError'>${jsonArr[i].Cost}</td><`;
          template += `<td class='rejectedWithError'>${jsonArr[i].cd.toFixed(
            2
          )}</td>`;
          template += `<td class='rejectedWithError'>${jsonArr[i].pmd.toFixed(
            2
          )}</td></tr>`;
        } //Restoration for 3 and 4 Values
        else {
          template += `<tr><td class='restoration' id='pathColumn'>${
            jsonArr[i].Path
          }</td>`;
          //if(errorState!=-1 )
          template += `<td class='restoration text-center' id='rwaError'>${
            jsonArr[i].error
          }</td>`;
          template += `<td class='restoration text-center' id='regenLoc'>${
            jsonArr[i].regeneratorLoc
          }</td>`;
          template += `<td class='restoration' id='pathType'>${
            jsonArr[i].PathType
          }</td>`;
          template += `<td class='restoration'>${jsonArr[i].WavelengthNo}</td>`;
          template += `<td class='restoration'>${jsonArr[i].Osnr}</td>`;
          template += `<td class='restoration'>${jsonArr[i].lineRate}</td>`;
          template += `<td class='restoration'>${jsonArr[i].Spanlength}</td>`;
          template += `<td class='restoration'>${jsonArr[i].Cost}</td>`;
          template += `<td class='restoration'>${jsonArr[i].cd.toFixed(
            2
          )}</td>`;
          template += `<td class='restoration'>${jsonArr[i].pmd.toFixed(
            2
          )}</td></tr>`;
        }

        temp--;
      }

      // i=count;
      // console.log("Value of i at end:"+i);
    }
  } else {
    template += `No path will get affected by fault nodes/links.`;
  }

  console.log("Value of template :" + template);
  $("#demands-view tbody")
    .empty()
    .append(template);

  console.log("Demand Matrix Table filling from fault Analysis Data.....");
};

var DeleteLink = function(cellView) {
  cellView.model.remove();
};

/**
 * Context menu for link.
 * Implementation for node will be done when map is loaded from DB or when new map is created.
 * Reference Function for Node : customContextMenuInitialize
 */
paper.on("cell:contextmenu", function(cellView, evt, x, y) {
  fRemovePaperContextmenu();
  // Avoid the real one
  //evt.preventDefault();
  cellLocal = cellView;

  //Calling context menu
  if (cellView.model.isLink()) {
    //properties(cellView.model);
    //initializeLinkProperties(cellView.model);
    console.log("cellView.model for context menu :: ");
    console.log(cellView.model);
    //Do nothing
    $("#toolTip").remove();
    $("#linkToolTip").remove();

    //set global variable to this node
    nptGlobals.contextElSelected = cellView;

    var title = `<li class="paper-context-menu__ul--title text-center">Link ${
      cellView.model.attributes.linkProperties.linkId
    }</li>`;
    var removeFaultAnalysis = `<li class="paper-context-menu__ul--list-item"><a class="removeFaultAnalysisToolTip" href="#" data-toggle="tooltip" title="Remove Link for Fault Analysis"><i class="fa fa-times-circle-o" aria-hidden="true"></i> Fault Link</a></li>`;
    var removeLink = `<li class="paper-context-menu__ul--list-item"><a id="removeLinkToolTip" href="#" data-toggle="tooltip" title="Remove Link"><i class="fa fa-times " aria-hidden="true"></i> Remove Link</a></li>`;
    var addIlaBetweenNodes = `<li class="paper-context-menu__ul--list-item"><a id="addIlaBetweenNodes" href="#" data-toggle="tooltip" title="Add Ila b/w nodes"><i class='fa fa-plus ' aria-hidden='true'></i> Add ILA</a></li>`;
    var lineProtection = `<li class="paper-context-menu__ul--list-item"><a id="lineProtection" href="#" data-toggle="tooltip" title="OMS Protected Link"><i class="fa fa-arrows-h" aria-hidden="true"></i> OMS Protection</a></li>`;
    //create div as tooltip around the node
    var div = $('<div id="linkToolTip" class="paper-context-menu">').css({
      left: evt.clientX,
      top: evt.clientY
    });

    var ulList = $('<ul class="paper-context-menu__ul text-center" >');

    //console.log(cellView.model.get('position'));

    //append tools to tooltip
    // console.log(nptGlobals.isFaultAnalysisReady);
    ulList.append(title);
    ulList.append(removeLink);
    ulList.append(addIlaBetweenNodes);

    if (nptGlobals.getRwaExecutedStatus()) ulList.append(removeFaultAnalysis);

    if (nptGlobals.NetworkTopology == nptGlobals.TopologyLinearStr)
      ulList.append(lineProtection);

    div.append(ulList);

    //console.log(div);

    //append tooltip to body,hence to node
    $(document.body).append(div);
  }
  //  else
  //  	{
  //          cellLocal = cellView;
  //          console.log(cellLocal);
  //          var x = cellView.el.innerHTML;
  //          console.log("X :",x.split("id=")[1]);
  //          var y;
  //          if(cellLocal.model.get('type')==nptGlobals.NodeTypeILA || cellLocal.model.get('type')==nptGlobals.NodeTypeTE || cellLocal.model.get('type')==nptGlobals.NodeTypeROADM || cellLocal.model.get('type')==nptGlobals.NodeTypeCDROADM || cellLocal.model.get('type')==nptGlobals.NodeTypeTwoDegreeRoadm || cellLocal.model.get('type')==nptGlobals.NodeTypeHub|| cellLocal.model.isLink())
  //          	y=x.split("id=")[1];
  //          //else y = x.split("image id=")[1]; /**DBG => Not working for Mozilla since properties mismatch with Chrome*/
  //          console.log(y);
  //          var z = y.split('"')[1];
  //          console.log(z);
  //          var hash = "#";
  //          var final = hash.concat(z);
  //          console.log("Final"+final);
  //          if(cellView.model.get('type')==nptGlobals.NodeTypeROADM || cellView.model.get('type')==nptGlobals.NodeTypeCDROADM)
  //               $(final).contextMenu(contextmenu,{mouseClick:"right",triggerOn:"click"});
  //          else if(cellView.model.get('type')==nptGlobals.NodeTypeTE)
  //          	 $(final).contextMenu(contextmenuTE,{mouseClick:"right",triggerOn:"click"});
  //          else if(cellView.model.get('type')==nptGlobals.NodeTypeTwoDegreeRoadm)
  //         	 $(final).contextMenu(contextmenuTwoDegreeRoadm,{mouseClick:"right",triggerOn:"click"});
  //          else if(cellView.model.get('type')==nptGlobals.NodeTypeILA)
  //          	$(final).contextMenu(contextmenuILA,{mouseClick:"right",triggerOn:"click"});
  //          else if(cellView.model.get('type')==nptGlobals.NodeTypeHub)
  //          	$(final).contextMenu(contextmenuHub,{mouseClick:"right",triggerOn:"click"});
  //          else if(cellView.model.isLink())
  //          	{
  //          	console.log("Case of Link")
  //          	$(final).contextMenu(contextmenuLink,{mouseClick:"right",triggerOn:"click"});
  //          	}
  //     }
});

/**
 * Initialize/Add context menu on each node
 */
function customContextMenuInitialize() {
  console.log("customContextMenu graph.getElements() ::", graph.getElements());
  _.each(graph.getElements(), function(el) {
    addContextMenuToNode(el);
  });
}

/**
 * Helper Function for adding custom context menu on node
 * @param cellView
 */
function addContextMenuToNode(cellModel) {
  const cellView = paper.findViewByModel(cellModel);
  console.log("Context menu Right Click :", cellView);
  var x = cellView.el.innerHTML;
  // console.log("X :"+x);
  var y;
  if (
    cellView.model.get("type") == nptGlobals.NodeTypeILA ||
    cellView.model.get("type") == nptGlobals.NodeTypeTE ||
    cellView.model.get("type") == nptGlobals.NodeTypeROADM ||
    cellView.model.get("type") == nptGlobals.NodeTypeCDROADM ||
    cellView.model.get("type") == nptGlobals.NodeTypeTwoDegreeRoadm ||
    cellView.model.get("type") == nptGlobals.NodeTypeHub ||
    cellView.model.isLink() ||
    cellView.model.get("type") == nptGlobals.NodeTypePotp
  )
    y = x.split("id=")[1];
  //else y = x.split("image id=")[1]; /**DBG => Not working for Mozilla since properties mismatch with Chrome*/

  // console.log(y);
  var z = y.split('"')[1];
  // console.log(z);

  var hash = "#";
  var final = hash.concat(z);
  // console.log("Final"+final);

  if (
    cellView.model.get("type") == nptGlobals.NodeTypeROADM ||
    cellView.model.get("type") == nptGlobals.NodeTypeCDROADM ||
    cellView.model.get("type") == nptGlobals.NodeTypePotp
  )
    $(final).contextMenu(contextmenu, {
      mouseClick: "right",
      triggerOn: "click"
    });
  else if (cellView.model.get("type") == nptGlobals.NodeTypeTE)
    $(final).contextMenu(contextmenuTE, {
      mouseClick: "right",
      triggerOn: "click"
    });
  else if (cellView.model.get("type") == nptGlobals.NodeTypeTwoDegreeRoadm)
    $(final).contextMenu(contextmenuTwoDegreeRoadm, {
      mouseClick: "right",
      triggerOn: "click"
    });
  else if (cellView.model.get("type") == nptGlobals.NodeTypeILA)
    $(final).contextMenu(contextmenuILA, {
      mouseClick: "right",
      triggerOn: "click"
    });
  else if (cellView.model.get("type") == nptGlobals.NodeTypeHub)
    $(final).contextMenu(contextmenuHub, {
      mouseClick: "right",
      triggerOn: "click"
    });
}

/**
 * If the document is clicked somewhere
 */
$(document).bind("mousedown", function(e) {
  // If the clicked element is not the menu
  if (!$(e.target).parents(".custom-menu").length > 0) {
    // Hide it
    $(".custom-menu").hide(100);
  }
});

/**
 * Remove Port from : 1) Particular Direction or
 *                    2) All Direction
 *
 *  @param     param To check the port existence on particular direction
 */
function removePortToDirection(param) {
  ///alert('hasPort '+cellLocal.model.hasPort("northId"));

  if (cellLocal.model.hasPort(param) == 1) {
    cellLocal.model.removePort(param);
  } else if (cellLocal.model.hasPort(param) == 0) {
    alert(param + " Doesnt Exist !!");
  }

  if (param == "all") {
    ///cellLocal.model.hasPort(param) == 0){
    ///alert("Here to remove all port .. ")
    for (iC = 0; iC < 8; iC++) {
      cellLocal.model.removePort(DirectionParam[iC]);
    }
  }
}

/**
 * Add Port To      : 1) Particular Direction or
 *                    2) All Direction
 *
 *  @param     param To check the port existence on particular direction
 */
function addPortToDirection(param) {
  // console.log("CellLocal :");
  // console.log(cellLocal.model);

  var dirId = -1;

  if (param == "all") dirId = 8;
  else if (param == "north") dirId = 0;
  else if (param == "east") dirId = 1;
  else if (param == "west") dirId = 2;
  else if (param == "south") dirId = 3;
  else if (param == "ne") dirId = 4;
  else if (param == "nw") dirId = 5;
  else if (param == "se") dirId = 6;
  else if (param == "sw") dirId = 7;

  ///alert(dirId)
  ///alert('hasPort '+cellLocal.model.hasPort("northId"));

  if (dirId == 8) {
    ///cellLocal.model.hasPort(param) == 0){
    for (iC = 0; iC < 8; iC++) {
      ///alert("Here to add all port .. ")
      if (cellLocal.model.hasPort(DirectionParam[iC]) == 0) {
        cellLocal.model.addPort({
          z: "auto",
          id: DirectionParam[iC],
          group: DirectionParam[iC],
          attrs: { text: { text: Direction[iC] } },
          label: {
            position: {
              name: "outside"
            }
          }
        });
      }
    }
  } else if (cellLocal.model.hasPort(param) == 0) {
    if (cellLocal.model.get("type") == nptGlobals.NodeTypeTE) {
      if (param == "east" && cellLocal.model.hasPort("west") != 0)
        bootBoxAlert("You can only add one port in TE");
      else if (param == "west" && cellLocal.model.hasPort("east") != 0)
        bootBoxAlert("You can only add one port in TE");
      else {
        cellLocal.model.addPort({
          z: "auto",
          id: param,
          group: param,
          attrs: { text: { text: Direction[dirId] } },
          label: {
            position: {
              name: "outside"
            }
          }
        });
      }
    } else {
      cellLocal.model.addPort({
        z: "auto",
        id: param,
        group: param,
        attrs: { text: { text: Direction[dirId] } },
        label: {
          position: {
            name: "outside"
          }
        }
      });
    }
  } else if (cellLocal.model.hasPort(param) == 1) {
    alert(param + " already exist !!");
  } else {
    alert("nothing to follow");
  }

  //		if(dirId == 8){///cellLocal.model.hasPort(param) == 0){
  //			for(iC=0; iC<8; iC++){
  //				///alert("Here to add all port .. ")
  //				cellLocal.model.addPort({
  //					z:'auto',
  //					id:DirectionParam[iC],
  //					group: DirectionParam[iC],
  //		            attrs: { text: {text: Direction[iC]}},
  //		            label: {
  //	                    position: {
  //	                        name: 'outside',
  //	                    }
  //	                }
  //		        });
  //			}
  //		}
}

/**
 * Replace Node
 */
function replaceNode(replaceWithNodeType) {
  var currNode = cellLocal.model;
  var currNodeType = cellLocal.model.get("type");
  var currNodeId = cellLocal.model.get("nodeProperties").nodeId;
  // var currDefaultId = currNode.id;
  var degree = getNodeDegree(currNodeId);
  console.log(
    "currNodeType :" +
      currNodeType +
      " currNodeId :" +
      currNodeId +
      " degree :" +
      degree
  );

  //replacing with same node wont do anything
  if (currNodeType == replaceWithNodeType) {
    bootBoxAlert("You are replacing with same node .", 2000);
    return;
  }

  if (
    currNodeType == nptGlobals.NodeTypeTE &&
    replaceWithNodeType == nptGlobals.NodeTypeILA
  ) {
    bootBoxAlert("ILA cannot be a terminal element.", 2000);
    return;
  }
  fUpdateLinksOnNodeReplacement(currNode, replaceWithNodeType);
  // replaceNodeLinkInsertion(currNode, replaceWithNodeType);
}

/**
 * Swap node with new node
 */
function fUpdateLinksOnNodeReplacement(currNode, replaceWithNodeType) {
  let currNodeProperties = JSON.parse(
      JSON.stringify(currNode.get("nodeProperties"))
    ),
    position = { x: "", y: "" },
    newNode;

  // Use same position
  position.x = currNode.get("position").x;
  position.y = currNode.get("position").y;

  // For assigning same id on node insertion
  nptGlobals.nodeIdForReplacingNode = currNodeProperties.nodeId;

  // Insert new node
  if (replaceWithNodeType == nptGlobals.NodeTypeTE) {
    newNode = insertTeNode(position);
  } else if (replaceWithNodeType == nptGlobals.NodeTypeILA) {
    newNode = insertIlaNode(position);
  } else if (replaceWithNodeType == nptGlobals.NodeTypeROADM) {
    newNode = insertRoadmNode(position);
  } else if (replaceWithNodeType == nptGlobals.NodeTypeCDROADM) {
    newNode = insertCdRoadmNode(position);
  } else if (replaceWithNodeType == nptGlobals.NodeTypeTwoDegreeRoadm) {
    newNode = insertTwoDegreeRoadmNode(position);
  }

  // Assign same node properties to new node
  newNode.attributes.nodeProperties = currNodeProperties;

  // Sort links by dir
  let connectedLinks = _.clone(graph.getConnectedLinks(currNode)).sort(
    (link1, link2) => {
      let link1Dir, link2Dir;
      if (link1.source().id == currNode.id) {
        link1Dir = link1.source().dir;
      } else if (link1.target().id == currNode.id) {
        link1Dir = link1.target().dir;
      }

      if (link2.source().id == currNode.id) {
        link2Dir = link2.source().dir;
      } else if (link2.target().id == currNode.id) {
        link2Dir = link2.target().dir;
      }

      if (link1Dir > link2Dir) return 1;
      else if (link1Dir < link2Dir) return -1;
      else return 0;
    }
  );

  let alertContent = `Replacing a node with lower degree support will lead to deletion of extra links (if any). Are you sure you want to continue ? `;

  // Add links upto the nodes degree limit

  switch (replaceWithNodeType) {
    case nptGlobals.NodeTypeTE:
      {
        connectedLinks = connectedLinks.slice(0, 1);
        openDialog(
          nptGlobals.getDialogTypes().alert,
          alertContent,
          fChangeLinkTargetOnNodeReplace,
          connectedLinks,
          currNode,
          newNode,
          currNodeProperties
        );
      }
      break;
    case nptGlobals.NodeTypeTwoDegreeRoadm:
    case nptGlobals.NodeTypeILA:
      {
        connectedLinks = connectedLinks.slice(0, 2);
        openDialog(
          nptGlobals.getDialogTypes().alert,
          alertContent,
          fChangeLinkTargetOnNodeReplace,
          connectedLinks,
          currNode,
          newNode,
          currNodeProperties
        );
      }
      break;
    case nptGlobals.NodeTypeCDROADM:
    case nptGlobals.NodeTypeROADM:
      {
        // Max degree nodes
        fChangeLinkTargetOnNodeReplace(
          connectedLinks,
          currNode,
          newNode,
          currNodeProperties
        );
      }
      break;
  }
}

/** Change source/target links connected to curr node */
function fChangeLinkTargetOnNodeReplace(
  connectedLinks,
  currNode,
  newNode,
  currNodeProperties
) {
  console.log("*********** fChangeLinkTargetOnNodeReplace *******************");
  // Change source/target links connected to curr node
  _.each(connectedLinks, link => {
    if (link.source().id == currNode.id) {
      link.source({
        id: newNode.id,
        NodeId: currNodeProperties.nodeId,
        dir: link.source().dir
      });
    } else if (link.target().id == currNode.id) {
      link.target({
        id: newNode.id,
        NodeId: currNodeProperties.nodeId,
        dir: link.target().dir
      });
    }
  });
  // Remove node being replaced
  currNode.remove();
}

// function replaceNodeLinkInsertion(currNode, replaceWithNodeType) {
//   let currNodeId,
//     currDefaultId,
//     currNodeProperties = JSON.parse(
//       JSON.stringify(currNode.get("nodeProperties"))
//     ),
//     position = { x: "", y: "" },
//     degreeLimit;

//   position.x = currNode.get("position").x + 20;
//   position.y = currNode.get("position").y + 30;

//   //Set to nodeid of previous node
//   nptGlobals.nodeIdForReplacingNode = currNodeProperties.nodeId;
//   let nodeConnectionsPrevNode = JSON.parse(
//     JSON.stringify(currNodeProperties.nodeConnections)
//   );
//   let nodeConnectionsValues = _.values(nodeConnectionsPrevNode);

//   console.log(
//     "replaceNodeLinkInsertion : nodeConnectionsValues:",
//     nodeConnectionsValues
//   );
//   let nodeConnectionsOfPrevConnectedNodesArr = [];
//   nodeConnectionsValues.map(function(val, index) {
//     if (val != 0) {
//       let targetNode = graph.getCell(getNodeById(val));
//       nodeConnectionsOfPrevConnectedNodesArr[val] = JSON.parse(
//         JSON.stringify(targetNode.get("nodeProperties").nodeConnections)
//       );
//       console.log(
//         "val:",
//         val,
//         " nodeConnectionsOfPrevConnectedNodesArr[val]",
//         nodeConnectionsOfPrevConnectedNodesArr[val]
//       );
//     }
//   });

//   currNode.remove();

//   // let nodeConnectionsOfPrevConnectedNodesArr=removeNode(currNode);

//   if (replaceWithNodeType == nptGlobals.NodeTypeTE) {
//     newCurrNode = insertTeNode(position);
//     degreeLimit = 1;
//   } else if (replaceWithNodeType == nptGlobals.NodeTypeILA) {
//     newCurrNode = insertIlaNode(position);
//     degreeLimit = 2;
//   } else if (replaceWithNodeType == nptGlobals.NodeTypeROADM) {
//     newCurrNode = insertRoadmNode(position);
//     degreeLimit = 8;
//   } else if (replaceWithNodeType == nptGlobals.NodeTypeCDROADM) {
//     newCurrNode = insertCdRoadmNode(position);
//     degreeLimit = 8;
//   } else if (replaceWithNodeType == nptGlobals.NodeTypeTwoDegreeRoadm) {
//     newCurrNode = insertTwoDegreeRoadmNode(position);
//     degreeLimit = 2;
//   }

//   currNodeId = newCurrNode.get("nodeProperties").nodeId;
//   newCurrNode.attributes.nodeProperties = currNodeProperties;
//   console.log(
//     "--------------newCurrNode-------------------:",
//     newCurrNode.get("nodeProperties")
//   );
//   currDefaultId = newCurrNode.get("id");

//   nodeConnectionsValues.map(function(val, index) {
//     console.log("val:", val, " index:", index, " degreeLimit:", degreeLimit);
//     if (index < degreeLimit && val != 0) {
//       let targetNode = graph.getCell(getNodeById(val));
//       let srcInitialDir =
//         Number(
//           _.indexOf(
//             nodeConnectionsValues,
//             targetNode.get("nodeProperties").nodeId
//           )
//         ) + 1;
//       // let destInitialDir = Number(_.indexOf(nodeConnectionsValues,targetNode.get("nodeProperties").nodeId))+1;

//       let newLink = insertLink(currDefaultId, targetNode.get("id"));

//       newCurrNode.attributes.nodeProperties.degree = graph.getNeighbors(
//         newCurrNode
//       ).length;
//       newCurrNode.attributes.nodeProperties.nodeConnections[srcInitialDir] =
//         targetNode.attributes.nodeProperties.nodeId;

//       targetNode.get("nodeProperties").nodeConnections =
//         nodeConnectionsOfPrevConnectedNodesArr[val];

//       fUpdateLinkConnectionProperties(newLink);
//     }
//   });

//   //Set to 0 again
//   nptGlobals.nodeIdForReplacingNode = 0;
// }

// function replaceNodeLinkInsertion(currNode,replaceWithNodeType)
// {
// 	var destNodeEast,destNodeWest,portDirection,newCurrNode,
// 	linkBtwCurrAndDestNodeEast,linkBtwCurrAndDestNodeWest,
// 	newLinkEast,newLinkWest,
// 	destPort,
// 	position={x:'',y:''},
// 	eastPortNode=currNode.get('nodeProperties').directions[nptGlobals.EastDirection],
// 	westPortNode=currNode.get('nodeProperties').directions[nptGlobals.WestDirection];

//     console.log("eastPortNode"+eastPortNode);
//     console.log("westPortNode"+westPortNode);

// 	var currNodeId=currNode.get('nodeProperties').nodeId;
// 	var currDefaultId=currNode.get('id');
// 	/*destNodeId=currNodePortInfo['east'];*/

// 	/*destNode=graph.getCell(getNodeById(destNodeId));
// 	linkBtwCurrAndDestNode=getLinkBetweenSrcandDestNodes(getNodeById(destNodeId),currDefaultId);
// 	destPort=linkBtwCurrAndDestNode.get('source').NodeId==currNodeId?linkBtwCurrAndDestNode.get('target').port:linkBtwCurrAndDestNode.get('source').port;
// 	linkBtwCurrAndDestNode.remove();*/

// 	/*for (var key in currNodePortInfo)
// 	{
// 		if (currNodePortInfo.hasOwnProperty(key) && currNodePortInfo[key]!=0) {
// 			portDirection=key;
// 			destNode=graph.getCell(getNodeById(currNodePortInfo[key]));
// 			linkBtwCurrAndDestNode=getLinkBetweenSrcandDestNodes(getNodeById(currNodePortInfo[key]),currDefaultId);
// 			destPort=linkBtwCurrAndDestNodeEast.get('source').NodeId==currNodeId?linkBtwCurrAndDestNodeEast.get('target').port:linkBtwCurrAndDestNodeEast.get('source').port;
// 			console.log(key + " -> " + currNodePortInfo[key]);
// 			position.x=currNode.get('position').x;
// 			position.y=currNode.get('position').y;
// 			// same node Id for new node as the one of the node its replacing
// 			nptGlobals.nodeIdForReplacingNode=currNode.get('nodeProperties').nodeId;
// 			currNode.remove();
// 		}
// 	}
// */
// 	if(eastPortNode!=0)
// 		{
// 		destNodeEast=graph.getCell(getNodeById(eastPortNode));
// 		linkBtwCurrAndDestNodeEast=getLinkBetweenSrcandDestNodes(getNodeById(eastPortNode),currDefaultId);
// 		destPortEast=linkBtwCurrAndDestNodeEast.get('source').NodeId==currNodeId?linkBtwCurrAndDestNodeEast.get('target').port:linkBtwCurrAndDestNodeEast.get('source').port;
// 		}

// 	//linkBtwCurrAndDestNodeEast.remove();

// 	if(westPortNode!=0)
// 		{
// 		destNodeWest=graph.getCell(getNodeById(westPortNode));
// 		linkBtwCurrAndDestNodeWest=getLinkBetweenSrcandDestNodes(getNodeById(westPortNode),currDefaultId);
// 		destPortWest=linkBtwCurrAndDestNodeWest.get('source').NodeId==currNodeId?linkBtwCurrAndDestNodeWest.get('target').port:linkBtwCurrAndDestNodeWest.get('source').port;
// 		}

// 	//linkBtwCurrAndDestNodeWest.remove();

// 	/*console.log("destPortEast"+destPortEast+" destPortWest :"+ destPortWest);*/

// 	position.x=currNode.get('position').x;
// 	position.y=currNode.get('position').y;

// 	// same node Id for new node as the one of the node its replacing
// 	let currNodeProperties=currNode.get('nodeProperties');
// 	nptGlobals.nodeIdForReplacingNode=currNodeProperties.nodeId;
// 	currNode.remove();

// 	if (replaceWithNodeType==nptGlobals.NodeTypeTE)
// 		newCurrNode=insertTeNode(position);
// 	else if(replaceWithNodeType==nptGlobals.NodeTypeILA)
// 		newCurrNode=insertIlaNode(position);
// 	else if (replaceWithNodeType==nptGlobals.NodeTypeROADM)
// 		newCurrNode=insertRoadmNode(position);
// 	else if (replaceWithNodeType==nptGlobals.NodeTypeCDROADM)
// 		newCurrNode=insertCdRoadmNode(position);
// 	 else if (replaceWithNodeType==nptGlobals.NodeTypeTwoDegreeRoadm)
//          newCurrNode=insertTwoDegreeRoadmNode(position);

// 	currNodeId=newCurrNode.get('nodeProperties').nodeId;
// 	currDefaultId=newCurrNode.get('id');

// 	console.log(newCurrNode.attributes);
// 	newCurrNode.attributes.nodeProperties=currNodeProperties;

// 	if (replaceWithNodeType==nptGlobals.NodeTypeTE)
// 		{
// 		window.cellLocal=paper.findViewByModel(currDefaultId);
// 		if(westPortNode!='0')
// 			addPortToDirection(nptGlobals.WestDirection);
// 		else addPortToDirection(nptGlobals.EastDirection); //For all direction in case of CDC Roadm

// 		}
// 	else if (replaceWithNodeType==nptGlobals.NodeTypeROADM || replaceWithNodeType==nptGlobals.NodeTypeCDROADM)
// 		{
// 		window.cellLocal=paper.findViewByModel(currDefaultId);
// 		addPortToDirection("all");
// 		}
//         else if (replaceWithNodeType==nptGlobals.NodeTypeTwoDegreeRoadm)
//         {
//          window.cellLocal=paper.findViewByModel(newCurrNode);
//          //removePortToDirection("all");
//          addPortToDirection(nptGlobals.WestDirection);
//          addPortToDirection(nptGlobals.EastDirection);
//         }

//     /*console.log("currNodePortInfo['east'] :"+currNodePortInfo[nptGlobals.EastDirection]);
//     console.log("currNodePortInfo['west'] :"+currNodePortInfo[nptGlobals.WestDirection])*/

//     //If node was connected in east direction
// 	if(eastPortNode!=0)
// 		{
// 		newLinkEast=insertLink(currDefaultId,destNodeEast.get('id'));
// 		//update link properties regarding port for source and target node
// 		newLinkEast.get('source').port="east";
// 		newLinkEast.get('target').port=destPortEast;

// 		//Get direction of source and target node and Update direction properties in Model
// 		dir1 = newLinkEast.attributes.source.port,
// 		dir2 = newLinkEast.attributes.target.port;
// 		newCurrNode.attributes.nodeProperties.directions[dir1] = destNodeEast.attributes.nodeProperties.nodeId;
// 		destNodeEast.attributes.nodeProperties.directions[dir2] = newCurrNode.attributes.nodeProperties.nodeId;

// 		newCurrNode.attributes.nodeProperties.degree+=1;
// 		newCurrNode.attributes.nodeProperties.numDirections[dir1] = newCurrNode.attributes.nodeProperties.degree;

//     	destNodeEast.attributes.nodeProperties.degree+=1;
//     	destNodeEast.attributes.nodeProperties.numDirections[dir2] = destNodeEast.attributes.nodeProperties.degree;

// 		//update link properties regarding Source and Target custom Id
// 		newLinkEast.attributes.source.NodeId = newCurrNode.attributes.nodeProperties.nodeId;
// 		newLinkEast.attributes.target.NodeId = destNodeEast.attributes.nodeProperties.nodeId;

// 		//Config file source and destination directions for link
// //		newLinkEast.get('linkProperties').SrcNodeDirection=dir1;
// //		newLinkEast.get('linkProperties').DestNodeDirection=dir2;

// 		//Direction Change - String to Number
// 		newLinkEast.get('linkProperties').SrcNodeDirection=fGetDirectionStr(newCurrNode.attributes.nodeProperties.numDirections[dir1]);;
// 		newLinkEast.get('linkProperties').DestNodeDirection=fGetDirectionStr(destNodeEast.attributes.nodeProperties.numDirections[dir2]);;

// 		}

// 	//If node was connected in west direction
// 	if(westPortNode!=0)
// 		{
// 		newLinkWest=insertLink(currDefaultId,destNodeWest.get('id'));
// 		//update link properties regarding port for source and target node
// 		newLinkWest.get('source').port="west";
// 		newLinkWest.get('target').port=destPortWest;

// 		//Get direction of source and target node and Update direction properties in Model
// 		dir1 = newLinkWest.attributes.source.port,
// 		dir2 = newLinkWest.attributes.target.port;
// 		newCurrNode.attributes.nodeProperties.directions[dir1] = destNodeWest.attributes.nodeProperties.nodeId;
// 		destNodeWest.attributes.nodeProperties.directions[dir2] = newCurrNode.attributes.nodeProperties.nodeId;

// 		//Direction Change - String to Number
// 		newCurrNode.attributes.nodeProperties.degree+=1;
// 		newCurrNode.attributes.nodeProperties.numDirections[dir1] = newCurrNode.attributes.nodeProperties.degree;

// 		destNodeWest.attributes.nodeProperties.degree+=1;
// 		destNodeWest.attributes.nodeProperties.numDirections[dir2] = destNodeWest.attributes.nodeProperties.degree;

// 		//update link properties regarding Source and Target custom Id
// 		newLinkWest.attributes.source.NodeId = newCurrNode.attributes.nodeProperties.nodeId;
// 		newLinkWest.attributes.target.NodeId = destNodeWest.attributes.nodeProperties.nodeId;

// 		//Config file source and destination directions for link
// //		newLinkWest.get('linkProperties').SrcNodeDirection=dir1;
// //		newLinkWest.get('linkProperties').DestNodeDirection=dir2;

// 		//Direction Change - String to Number
// 		newLinkWest.get('linkProperties').SrcNodeDirection=fGetDirectionStr(newCurrNode.attributes.nodeProperties.numDirections[dir1]);;
// 		newLinkWest.get('linkProperties').DestNodeDirection=fGetDirectionStr(destNodeWest.attributes.nodeProperties.numDirections[dir2]);;

// 		}

// 	//For attatching links to the ports in Map Canvas (GUI issue)
// 	//  setallNodeAttributes();

// }
