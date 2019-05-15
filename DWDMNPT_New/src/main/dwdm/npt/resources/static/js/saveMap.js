 $( "#saveMapId" ).click(function() { saveMap(); });
 
 function saveMap() {
	
	    ///alert("here ...!!")
	 
	    var jsonStr = JSON.stringify(graph);  
	    var data = {"name":"hp", "sname":"patel"}
	    var graphStr = graph.toJSON();
	    console.log("Raw Graph "+graphStr);
	    var jsonGraph = JSON.stringify(graphStr);
	    console.log("Json Str "+jsonGraph);
	    
		///data["name"] = "hiten";
	    
		$.ajax({
	    type: "POST",
	    contentType: "application/json",
	
	    headers: {          
	        Accept : "application/json; charset=utf-8",         
	       "Content-Type": "application/json; charset=utf-8"   
	    } ,    
	 
	    url: "/save",
	    data: JSON.stringify(graphStr),
	    dataType: 'json',
	   
	   timeout: 600000,
	
	   success: function (data) {
		   console.log("success"+data);
		   alert("Plz be here");
	   },
	  
	   error: function (e) {
		   console.log("fail"+e);	    }
		});
	    
 }