/**
 * @brief     : This Modal Separated out of the BOM window in order to View Equipment any point 
 *                    of time and to make the existing flow intact
 * @date      : 28/07/2017                       
 * @author : hp  
 */

var dummyJson={};




/**
 *   On Page Load/Refresh Generate Equipment if not already 
 */
(function(){

	let postJsonData = jsonPostObject();
	
	serverPostMessage("generateEquipmentDBRequest", postJsonData)
		.then(function (data) {
			dummyJson=data;
			console.log(" generateEquipmentDBRequest success",data);
		})
		.catch(function (e) {
			console.log("generateEquipmentDBRequest fail" ,e);
		});

}());


$("#equipmentModalTrigger").on('click',function(){	
	console.log("equipmentModalTrigger Loaded");
	$('#equipmentModal').modal({ backdrop: 'static', keyboard: false }); 
	euipmetnDbAjaxCall();
});


/**
 * Ajax call to Just Fetch Equipment Data from DB
 */
var euipmetnDbAjaxCall=function()
{
	serverGetMessage("getEquipmentJsonDataRequest")
	.then(function (data) {
		dummyJson=data;
		console.log("success Data:",data);
		finitializeEquipmentView();		 
	})
	.catch(function (e) {
		console.log("fail", e);
	});
}

/**
 * finitializeEquipmentView : To Insert Equipment DB data into Table
 */
var finitializeEquipmentView=function()
		{
	
			console.log("Inside finitializeEquipment View");
			var equipmentListArr=dummyJson.equipmentDbData;

			$(".modalBodyEquipmentContentPanelEquipmentTable tbody").empty();
			
			var str='';
			for(var i=0;i<equipmentListArr.length;i++)
			{
			str+=`<tr>
				<td>${equipmentListArr[i].Name}</td>
				<td>${equipmentListArr[i].PartNo}</td>
				<td>${equipmentListArr[i].PowerConsumption}</td>
				<td>${equipmentListArr[i].SlotSize}</td>
				<td>${equipmentListArr[i].Details}</td>
				<td>${equipmentListArr[i].Price}</td>
				<td>${equipmentListArr[i].RevisionCode}</td>
				<td>${equipmentListArr[i].Category}</td>
				</tr>`;		
			}
			
			$(".modalBodyEquipmentContentPanelEquipmentTable tbody").append(str);			
}