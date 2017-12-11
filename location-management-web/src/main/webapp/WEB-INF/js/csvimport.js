var headerCols = ["name", "short_name", "full_name", "parent", "identifier", "latitude", "longitude", "type", "description"];//, "children_population", "women_population"];//
var requiredCols = ["name", "parent", "type"];
var unknownCols = [];
var requiredColsData = [];
var csvData = [];
function remove(array, value) { var newArray = [];
	for(var i=0; i<array.length; i++) {
		if(array[i]==value) {}
		else { newArray.push(array[i]); }
	} return newArray;
}
function GenerateTable() {
	var table = document.createElement("TABLE");
	table.border = "1";
	table.id = "datatableImport";
	var columnCount = csvData[0].length;
	var row = table.insertRow(-1);
	var k=0;
	for (var i = 0; i < csvData[0].length; i++) {
		var headerCell = document.createElement("TH");
		headerCell.innerHTML = csvData[0][k];
		k++;
		row.appendChild(headerCell);
	}
	for (var i = 1; i < csvData.length; i++) {
		row = table.insertRow(-1);
		for (var j = 0; j < csvData[i].length; j++) {
			var cell = row.insertCell(-1);
			if(csvData[i][j] === "NULL") {}
			else  cell.innerHTML = csvData[i][j];
		}
	}		
	
	var dvaTable = document.getElementById("dvaTable");
	dvaTable.innerHTML = "";
	dvaTable.appendChild(table);
}
function ValidateUnknownCols(csvData) {
	for(var i=0; i<csvData[0].length; i++) {
		if(headerCols.includes(csvData[0][i].trim().toLowerCase())) { }
		else if(i=== csvData[0].length-1) {
			if(headerCols.includes(csvData[0][i].trim().toLowerCase())) { }
			else { unknownCols.push(csvData[0][i].trim().toUpperCase()); }
		}
		else { unknownCols.push(csvData[0][i].trim().toUpperCase()); }
	}
	var str = "";
	for (var i = 0; i < unknownCols.length; i++) {
		str += unknownCols[i] + "<br/>"
	}
	if(unknownCols.length>0) { return (unknownCols.length + " UNKNOWN COLUMNS FOUND, I.E:-<br/>" + str); }	
	else { return ""; }
}
function ValidateRequiredNull(csvData) {
	for(var i=0; i<csvData.length; i++) {
		if((requiredCols.includes(csvData[0][i]))) {// && ((csvData[0][i]) != "location_parent")) {//
			for(var j=1; j<csvData.length; j++) {
				if(csvData[j][i] === "") {
					//alert("EMPTY CELLS FOUND!");
					csvData[j][i] = "NULL";
					return "EMPTY CELLS FOUND!";
				}
			}
		}
	}
	return "";
}
function ValidateNameParent(csvData) {
	var nameArr = [];
	var parentArr = [];
	for(var i=0; i<csvData.length; i++) {
		if((requiredCols.includes(csvData[0][i])) && ((csvData[0][i]) === "name")) {
			nameArr.push(csvData[0][i]);
			for(var j=1; j<csvData.length; j++) { nameArr.push(csvData[j][i]); }
		}
		else if((requiredCols.includes(csvData[0][i])) && ((csvData[0][i]) === "parent")) {
			parentArr.push(csvData[0][i]);
			for(var j=1; j<csvData.length; j++) { parentArr.push(csvData[j][i]); }
		}
	}
	requiredColsData.push(nameArr);
	requiredColsData.push(parentArr);
	
	if (typeof parentArr[0] === 'undefined') { return ""; }
	else {
		var str = "NO LOCATION INFO FOUND FOR:-<br/>";
		for(var i=1; i<parentArr.length; i++) {
			if(nameArr.includes(parentArr[i])) { }
			else if(parentArr[i] === "NULL") { }
			else {  
				str += parentArr[i].toUpperCase(); 
				if(i!= parentArr.length-1) { str += "<br/>"; } 
			}
		}
		return str;
	}
}
function ValidateTable() {
	var x = ValidateUnknownCols(csvData);
	var y = ValidateRequiredNull(csvData);
	var z = ValidateNameParent(csvData);
	if(x === "" && y === "" && z === "") {
		//GenerateTable(csvData);
		return csvData;
	}
	else {
		var str = "";
		str += "" + x + "<br/>";
		str += "<br/>" + y + "<br/>";
		str += "<br/>" + z + "";
		$("#alertModalBody").html(str);
		$("#alertModal").modal('show');
		return null;
	}
}
$(document).ready(function(){
    $('#fileUpload').change( function(event) {
    	unknownCols = [];
    	requiredColsData = [];
    	csvData = [];
    	var fileUpload = document.getElementById("fileUpload");
    	var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt|)$/;
    	if (regex.test(fileUpload.value.toLowerCase())) {
    		//alert(fileUpload.value.toLowerCase());
    		if (typeof (FileReader) != "undefined") {
    			var reader = new FileReader();
    			reader.onload = function (e) {
    				var rowArr = e.target.result.split("\n");
    				for(var i=0; i<rowArr.length-1; i++) {
    					csvData.push(rowArr[i].split(","));
    				}

    				document.getElementById("dvaTable").innerHTML = "";
    				
    				csvData = ValidateTable();
    				if(csvData === null) {}
    				else {
        		    	//console.log(csvData);
        		    	$.ajax({
        					url: "/LocationDatabaseManagement/data/location/newCsv",
        					type: "POST",
        					data : { csvData: JSON.stringify(csvData) },
        					success : function(result) {
        						var data = JSON.parse(result);
        						console.log(data);
        						alert(data.locationAttributeTypeNodes.toString());
        						$("#alertModalBody").html(data.returnStr.toString());
        						$("#alertModal").modal('show');

        					}//}, error: function(jqXHR, textStatus, errorThrown) { alert(jqXHR); alert(textStatus); alert(errorThrown); }

        				});
    				}
    			}
    			reader.readAsText(fileUpload.files[0]);
    		} else {
    			alert("This browser does not support HTML5.");
    			document.getElementById("dvaTable").innerHTML = "";
    		}
    	} else {
    		alert("Please upload a valid CSV file.");
    		document.getElementById("dvaTable").innerHTML = "";
    	}
   	});
});