/**
 * @author hp
 * @date   2nd Feb, 2017
 * @brief  This JS provides a resizing feature of sidebar to the use for better view.  
 */


/**Module Var*/
let min,max,
togglemaximize=true;

/**Sidenbar resize Events*/
$('#expand-sidebar-btn').mousedown(function (e) {
	e.preventDefault();
	min = $(window).width()/3;
	max = $(window).width()-($(window).width()*.25);
	$(document).mousemove(function (e) {
		$(this).css({'opacity':'0.1'});
		e.preventDefault();
		/* console.log("e.pageX=  "+e.pageX+" e.pageY=  "+e.pageY+" And $('#sidebar').offset().left=  "
        		+$('#sidebar').offset().left+", Right "+ $('#sidebar').offset().right+", And $('#canvasId').offset().left=  "+$('#canvasId').offset().left)*/
//		var x =  e.pageX - $('#canvasIdContainer').offset().left;
//		console.log("X= "+x);
		
		let canvasWidth =  ((e.pageX - $('#canvasIdContainer').offset().left)/$(window).width())*100;
		let sideBarWidth= 100 - canvasWidth - 8.90;
		//console.log("canvasWidth :",canvasWidth);
		//console.log("sideBarWidth :",sideBarWidth);		
		
		if (e.pageX > min && e.pageX <max) {  
			console.log("Resize Condition true ----- min:",min," max:",max);  
			
			$('#sidebar').css("width", sideBarWidth+"%");
			$('#canvasIdContainer').css("width",canvasWidth+"%" );        	
			setSidebarContainerContentHeight();

		}else {
			console.log("Resize Condition false ----- min:",min," max:",max);  
		}
	})
	
	$(this).css({'opacity':'1'});
});


$('#maximize-sidebar-btn').click(function (e) {
	let min = $(window).width()/3,
		max = $(window).width()-($(window).width()*.25);
	let canvasWidth;
	if(togglemaximize)
		canvasWidth =  ((min - $('#canvasIdContainer').offset().left)/$(window).width())*100;
	else canvasWidth =  ((max - $('#canvasIdContainer').offset().left)/$(window).width())*100;

	let sideBarWidth= 100 - canvasWidth - 8.90;

	togglemaximize=!togglemaximize;

	$('#sidebar').css("width", sideBarWidth+"%");
	$('#canvasIdContainer').css("width",canvasWidth+"%" );  
	// console.log($('.side-bar-container__menu').height());      
	setSidebarContainerContentHeight();
})

function setSidebarContainerContentHeight(){
	$('.side-bar-container__content').height($('#sidebar #tabularMenu').height()-$('.side-bar-container__menu').height()-23);
}

$(document).mouseup(function (e) {
	$(document).unbind('mousemove');
});

