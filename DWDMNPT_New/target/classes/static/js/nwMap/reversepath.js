
$("#xml_reversepath").click(
		function ()  
		{
			
			xml_reversepath();
		
		});

function xml_reversepath(){

	let postData = jsonPostObject();
  
	serverPostMessage("xml_reversepath",postData)
	.then(function (data){
		dummyJson = data;
		initializeReversePathview();
		console.log("Schema updated succesfully :",data);
		
		console.log("Chart Data :",data);
		console.log("into initialize");
		
	})
	.catch(function (e) {
		fShowFailureMessage("Schema updation failed.");
		console.log(e);
	});;
}

var initializeReversePathview = function ()
{

	console.log("Inside finitializeReversePath View");
	var DifferenceListArr = dummyJson;
    console.log(DifferenceListArr);
	$(".modalBodyReversePathContentPanelTable tbody").empty();
	
	var str='';
	console.log("DifferenceListArr.length "+DifferenceListArr.length)
	if(DifferenceListArr.length==0)
	{  str+= `<p> No Changes</p>`;
		
	}
	
	else
		{ 
	
	for(var i=0;i<DifferenceListArr.length;i++)
	{   
	    
	    
		
		var NodeArr = DifferenceListArr[i].NodeArray;
		console.log(NodeArr);
		if(NodeArr!=null)
    {   
	   
	   for(var j=0;j<NodeArr.length;j++)
     {   var jsonobj = NodeArr[j];
            str+=`<tr>
    			<td rowspan = '${jsonobj.length}' >${DifferenceListArr[i].NetworkId}</td>
    			<td rowspan = '${jsonobj.length}'>${DifferenceListArr[i].NodeId}</td>`;
           for(var count=0 ;count<jsonobj.length; count++)
           {   
        	   var FieldName = Object.keys(jsonobj[count]);
        	   var Values =  Object.values(jsonobj[count]);
        	   var OldValues = Values[0];
        	   
        	  var OldValue = OldValues[0];
              var NewValue = OldValues[1];
              
        		   
        	   
        	   
        	   console.log("hereee",OldValue);
        	   str+=`<td class='working'>${FieldName}</td>
        			<td class='working'>${OldValue} </td>
        			<td class='working'>${NewValue}</td>
        			</tr>`;		
        		
        	  
            }
		   
		   
		   
		   }
	  }
  console.log(str);

	}
	
		}
$(".modalBodyReversePathContentPanelTable tbody").append(str);

console.log('optionalParam: ', optionalParam)


}


