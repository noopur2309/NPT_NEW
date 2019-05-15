Chart.defaults.global.defaultFontColor = 'black';
Chart.defaults.global.defaultFontFamily ='"Lucida Console", Monaco, monospace';
Chart.defaults.global.defaultFontSize = 14;
Chart.defaults.global.title.fontStyle='normal';
Chart.defaults.global.title.fontSize=20;

var ctx={};
var dummyJson={"Link_Ip":{"data":{"datasets":[{"backgroundColor":"rgba(255,127,80,1)","data":["1","2","3","4"],"label":"Link Ip"}],
	"labels":["10.10.0.0\/30","10.10.0.4\/30","10.10.0.8\/30","10.10.0.12\/30"]},"options":{"scales":{"yAxes":[{"scaleLabel":{"labelString":"Link Ip","display":"true"},"display":"true"}],"xAxes":[{"ticks":{"minRotation":"10"},"scaleLabel":{"labelString":"Link Id","display":"true"},"display":"true"}]},"title":{"display":"true","text":"Link Id v\/s Link Ip"}},"type":"bar"},
	"NodeId_Wavelength":{"data":{"datasets":[{"backgroundColor":"rgba(153,255,51,0.6)","data":["","","","",""],"label":""},
		{"backgroundColor":"rgba(255,127,80,1)","data":["1","5","1","1","4"],"label":""},{"backgroundColor":"rgba(38,198,218,1)",
			"data":["5","3","4","4","2"],"label":""},{"backgroundColor":"rgba(255,0,255,0.3)",
				"data":["","","1","",""],"label":""}],"labels":["1","2","3","4","5"]},"type":"bar"},"Power":{"data":{"datasets":[{"backgroundColor":"rgba(255,0,255,0.3)",
					"data":["3529","4041","3008","3426","3133","526","1082","551","526","526","526"],"label":"Typical Power"},{"backgroundColor":"rgba(38,198,218,1)",
						"data":["6830","8100","6259","7473","6414","1349","2731","1349","1349","1349","1349"],"label":"Power Consumption"}],
						"labels":["1","2","3","4","5","6","7","8","9","10","11"]},"options":{"scales":{"yAxes":[{"stacked":true,
							"scaleLabel":{"labelString":"Node Id","display":"true"},"display":"true"}],"xAxes":[{"stacked":true,"scaleLabel":{"labelString":"Power","display":"true"},"display":"true"}]},"title":{"display":"true","text":"Node Id v\/s Power"}},"type":"horizontalBar"},
							"NodeId_Traffic":{"data":{"datasets":[{"backgroundColor":("pink"),"data":["10","20","30","40","70"],"label":"east"},{"backgroundColor":("yellow"),"data":["10","20","60","70"],"label":"west"},{"backgroundColor":("red"),"data":["10","20","30","40","50","60","70"],"label":"south"}],
								"labels":["1","2","3","4","5","6","7"]},"options":{"title":{"display":"true","text":"Link Id v\/s Traffic"}},"type":"bar"}}

var linkJson={"Traffic":{"data":{"datasets":[{"backgroundColor":["#FF6384","#FFC0CB","#84FF63","#8463FF","#D8BFD8","#87CEFA"],"data":["10","20","30","40","50","60","70"]}],
	"labels":["1","2","3","4","5","6","7"]},"options":{"title":{"display":"true","text":"Link Id v\/s Traffic"}},"type":"polarArea"},"Wavelength":{"data":{"datasets":[{"backgroundColor":"rgba(153,255,51,0.6)","data":["5","5","4","4","3"],"label":"east"},
		{"backgroundColor":"rgba(255,127,80,1)","data":["1","5","1","1","4"],"label":"west"},{"backgroundColor":"rgba(38,198,218,1)",
			"data":["5","3","4","4","2"],"label":"north"},{"backgroundColor":"rgba(255,0,255,0.3)",
				"data":["","","1","",""],"label":"south"}],"labels":["1","2","3","4","5"]},
				"options":{"scales":{"yAxes":[{"stacked":true,"scaleLabel":{"labelString":"Wavelength","display":"true"},"display":"true"}],"xAxes":[{"stacked":true,"scaleLabel":{"labelString":"LinkId","display":"true"},"display":"true"}]},
					"title":{"display":"true","text":"Link Id v\/s Wavelength"}},"type":"bar"}}








$(document).ready(function(){
	$('#Modal_nodeid_wavelengthTrafficChart').modal();
	$('#Modal_nodeid_wavelength').modal();
	$('#Modal_nodeid_price').modal();
	$('#Modal_nodeid_power').modal();
	$('#Modal_nodeid_ip').modal();
	$('#Modal_link_wavelength').modal();
	$('#Modal_link_traffic').modal();
	$('#Modal_link_ip').modal();  
	console.log("my function");
	myfunction();
});

/**
 * This function receives all the JSON data and plot the graphs accordingly. 
 * @returns
 */
function myfunction()
{
	console.log("entered");
	console.log("Session NetworkName :"+sessionStorage.getItem("NetworkName"));

	var generatePostData=jsonPostObject();
	generatePostData.NetworkName=sessionStorage.getItem("NetworkName");
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: "/graphJsonFetch",
		data: JSON.stringify(generatePostData),
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function (data){
			console.log(data);

			var ctx = document.getElementById("chart1_wavelengthTraffiChart").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_WavelengthTraffic);
			var ctx = document.getElementById("chart2_wavelengthTrafficChart").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_WavelengthTraffic);


			var ctx = document.getElementById("chart1_price").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_Price);
			var ctx = document.getElementById("chart2_price").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_Price);
			var ctx = document.getElementById("chart1_wavelength").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_Wavelength);
			var ctx = document.getElementById("chart2_wavelength").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_Wavelength);
			var ctx = document.getElementById("chart1_power").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_Power);
			var ctx = document.getElementById("chart2_power").getContext('2d');
			var myChart = new Chart(ctx,data.NodeId_Power);
			var ctx = document.getElementById("chart1_Node_Ip").getContext('2d');
			var myChart = new Chart(ctx,data.Node_Ip);
			var ctx = document.getElementById("chart2_Node_Ip").getContext('2d');
			var myChart = new Chart(ctx,data.Node_Ip);

//			var ctx = document.getElementById("chart1_traffic").getContext('2d');
//			var myChart = new Chart(ctx,data.NodeId_Traffic);
//			var ctx = document.getElementById("chart2_traffic").getContext('2d');
//			var myChart = new Chart(ctx,data.NodeId_Traffic);



			var ctx = document.getElementById("chart1_link_traffic").getContext('2d');
			var myChart = new Chart(ctx,data.Link_Traffic);
			var ctx = document.getElementById("chart2_link_traffic").getContext('2d');
			var myChart = new Chart(ctx,data.Link_Traffic);
			var ctx = document.getElementById("chart1_link_wavelength").getContext('2d');
			var myChart = new Chart(ctx,data.Link_Wavelength);
			var ctx = document.getElementById("chart2_link_wavelength").getContext('2d');
			var myChart = new Chart(ctx,data.Link_Wavelength);
			var ctx = document.getElementById("chart1_Link_Ip").getContext('2d');
			var myChart = new Chart(ctx,data.Link_Ip);
			var ctx = document.getElementById("chart2_Link_Ip").getContext('2d');
			var myChart = new Chart(ctx,data.Link_Ip);

		},

		error:function(e){
			console.log("Error"+e)
		}
	});

}

function remove(complete_data)
{
	var duplicate=$.extend(true,{},complete_data);

	console.log(duplicate);
	console.log("remove");
	for(i=0;i<duplicate.data.datasets.length;i++)
		duplicate.data.datasets[i].label="";
	//duplicate.options.scales.yAxes[0].scaleLabel="";
	//duplicate.options.scales.xAxes[0].scaleLabel="";
	duplicate.options.title="null";
	duplicate.options.legend.display=false;
	console.log('duplicate ' + duplicate.options.legend);
	console.log('complete_data ' +complete_data.options.legend);
	return duplicate;

}

