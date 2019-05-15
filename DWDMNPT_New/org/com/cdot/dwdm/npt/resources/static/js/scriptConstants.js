
/**Spanlength Related varant*/
var spanLengthLimit=640;
var spanLengthInitial=40;
var spanLengthIla=80;

/* ******** global Variables for NPT state *************** */
var _SET_VALUE=true,
	_UNSET_VALUE=false; 	


var isMapSavedStr="isMapSaved",
	isCircuitSavedStr="isCircuitSaved",
	isRwaExecutedStr="isRwaExecuted",
	isInventoryGeneratedStr="isInventoryGenerated";


/* ********Checks for green to brownfield conversion*****************    */
var NptCurrentStateArr=[{"isMapSaved":0},{"isCircuitSaved":0},{"isRwaExecuted":0},{"isInventoryGenerated":0}];


var routePriority={"1":"Working","2":"Protection","3":"Restoration 1","4":"Restoration 2"};
