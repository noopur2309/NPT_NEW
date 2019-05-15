  $(document).ready(function() {
            $(".geHsplit").click(function() {
              
               /*alert("Exp");  
               $(".geHsplit").attr('style','width: 8px; top: 95px; bottom: 0px; left: 0px;');
               $("#canvasId").attr('style',"right: 240px; left: 10px; top: 95px; bottom: 0px; overflow: auto;")
               e.preventDefault();               
               $(".geHsplit").attr('style','display:none;');
               $("#canvasId").attr('style','display:none;');
               */
            	
               $(".geHsplit").toggleClass("collapseNavBar");               
               $(".geDiagramContainer").toggleClass("geDiagramContainerColEx");
                
            });
        });
  
  
  
  $(document).ready(function() {
      $("#generalTab").click(function() {                  
        $("#geSidebar").toggle();       
      });
  });