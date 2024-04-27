/**
 * 
 */


 window.addEventListener('message', function(event) {
        if (event.data === 'closePopup') {
			
			 var iframe = document.querySelector('#popup iframe');
   			 iframe.src = '';
   			 
            var popup = document.getElementById('popup');
            popup.style.display = 'none';
        }
    });
    

function showNote(note) {
		
	
     // Lấy thẻ iframe
    var iframe = document.getElementById('popup-iframe');
    // Đặt nội dung của tempProposal.note vào thuộc tính src của iframe
    iframe.srcdoc = note;
   	 
   	 var popup = document.getElementById('popup');
     popup.style.display = "block";
}
 

function showPopup( src){ // hiện lên pop up 
	
	 var iframe = document.querySelector('#popup iframe');
   	 iframe.src = src;
   	 
   	 var popup = document.getElementById('popup');
     popup.style.display = "block";
}


function hidePopup(){
	 var iframe = document.querySelector('#popup iframe');
  	 iframe.src = '';
	 var popup = document.getElementById('popup');
      popup.style.display = "none";
     
}
