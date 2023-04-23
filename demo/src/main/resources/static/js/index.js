console.log("hello");

function timeConverter(UNIX_timestamp){
  var a = new Date(UNIX_timestamp);
  var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  var year = a.getFullYear();
  var month = months[a.getMonth()];
  var date = a.getDate();
  var hour = a.getHours();
  var min = a.getMinutes();
  var sec = a.getSeconds();
  var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec ;
  return time;
}
function isOdd(num) { return num % 2;}

const userAction = async () => {
  const response = await fetch('/bloque');
  const myJson = await response.json(); //extract JSON from the http response
  // do something with myJson
  
  console.log(myJson.bloques);
  var bloques = myJson.bloques; 
var dict = new Object();
	  for(var i = 0; i < bloques.length; i++) {
	    var obj = bloques[i].transacciones;
	
	    for(var i = 0; i < obj.length; i++) {
		    var obj_1 = obj[i];
		
		    console.log(obj_1);
		    var emisor = obj_1.emisor;
		    console.log(emisor);
			if(!(emisor in dict)){
				var toSave = {};
				toSave.counts = 1;
				toSave.hora = obj_1.timestamp;
				dict[emisor] = toSave;
			}else{
				var toSave = dict[emisor];
				toSave.counts = toSave.counts + 1;
				toSave.hora = obj_1.timestamp;
			}
			
		}
	}
	console.log(dict);

	var i = 0
	var old = document.getElementById('myTable').getElementsByTagName('tbody')[0];
	var tbodyRef = document.createElement('tbody');
	for(const [ key, value ] of Object.entries(dict)){
		i = i+1;
		var newRow = tbodyRef.insertRow();
		var newCell = newRow.insertCell();
		var newText = document.createTextNode("Cliente "+ i);
		newCell.appendChild(newText);
		
		newCell = newRow.insertCell();
		var dentro = "Si";
		if(!isOdd(value.counts)){
			dentro = "No"
		}
		newText = document.createTextNode(dentro);
		newCell.appendChild(newText);
		
		newCell = newRow.insertCell();

		newText = document.createTextNode(timeConverter(value.hora));
		newCell.appendChild(newText);
		
		newCell = newRow.insertCell();
		newText = document.createTextNode(key);
		newCell.appendChild(newText);
	}
	old.parentNode.replaceChild(tbodyRef, old)

}

userAction();