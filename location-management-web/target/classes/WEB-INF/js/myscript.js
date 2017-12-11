var headerColsForLocAttrs = ["parent", "attribute_name", "value", "type_value"];
var requiredColsForLocAttrs = ["parent", "attribute_name", "value", "type_value"];
var csvDataForLocAttrs = [];
var unknownColsForLocAttrs = [];

var headerCols = ["name", "short_name", "full_name", "parent", "identifier", "latitude", "longitude", "type", "description"];//, "children_population", "women_population"];//
var requiredCols = ["name", "parent", "type"];
var unknownCols = [];
var requiredColsData = [];
var csvData = [];
var voidedLocations = getVoidedLocations();
var zNodes = getLocations();
var locationTypeNodes = getLocationTypes();
var locationAttributeDataNodes = getLocationAttributes();
var locationAttributeNodes = getLocationAttributeTypes();
var parentName;
var locationAttributeId = [];
var locationAttributeName = [];
var locationAttributeDisplayName = [];
var locationAttributeDescription = [];
var locationAttributeCategory = [];
var globalRowId;
var globalCellId;
var nodesToBeSaved = [];
var errorStr = "";
var mySelectData = [];
var myWindow1 = null;
var myWindow2 = null;
var myWindow3 = null;
if(jQuery.isEmptyObject(voidedLocations)) { voidedLocations = []; }
if(jQuery.isEmptyObject(zNodes)) { zNodes = []; errorStr += "Location Database is EMPTY.<br/>"; }
if(jQuery.isEmptyObject(locationTypeNodes)) { locationTypeNodes = []; errorStr += "Location Type Database is EMPTY.<br/>"; }
if(jQuery.isEmptyObject(locationAttributeDataNodes)) { locationAttributeDataNodes = []; }
if(jQuery.isEmptyObject(locationAttributeNodes)) { locationAttributeNodes = []; }
if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
	for(var i = 0; i<locationAttributeNodes.length; i++) {
		locationAttributeId.push(locationAttributeNodes[i].id);
		locationAttributeName.push(locationAttributeNodes[i].name);
		locationAttributeDisplayName.push(locationAttributeNodes[i].displayName);
		locationAttributeDescription.push(locationAttributeNodes[i].description);
		locationAttributeCategory.push(locationAttributeNodes[i].category);
	}
}
$(document).ready(function(){
	console.log(voidedLocations);
	if(errorStr !== "") { alertDIV("info", "INFO: ", errorStr); }
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	$.fn.zTree.init($("#voidedLocations"), setting, voidedLocations);
    $("#selectAll").bind("click", selectAll);
    $("#saveBtn").bind("click", saveTableRow);

    if(!jQuery.isEmptyObject(locationTypeNodes)) { 
    	var tag = "addlocation";
    	var nameHtml = "<select id='addType' name='addType' class='form-control myclass' title='Choose Location Type' onfocus='removeFocus(\""+tag+"\");'><option value=''></option>";
    	var idArray = [];
    	var nameArray = [];
    	var tempArray = [];
    	for (var i = 0; i < locationTypeNodes.length; i++) {
    		idArray.push(locationTypeNodes[i].id);
    		nameArray.push(locationTypeNodes[i].name);
    		tempArray.push(capitalize(locationTypeNodes[i].name));
    	} tempArray.sort();
    	for (var i = 0; i < tempArray.length; i++) { 
    		var name; var id;
    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; } }
    		nameHtml += '<option value="' + id +'">' + name + '</option>'; 
		} nameHtml += "</select>";
		document.getElementById("addLocationTypeListDiv").innerHTML = nameHtml;
	}
	$("#expandAllBtn").bind("click", {type:"expandAll"}, expandNode);
	$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);

	$('#fileUpload').change( function(event) {
    	unknownCols = [];
    	requiredColsData = [];
    	csvData = [];
    	var fileUpload = document.getElementById("fileUpload");
    	var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)");
    	if (regex.test(fileUpload.value.toLowerCase())) {
//    		alert(fileUpload.value.toLowerCase());
    		if (typeof (FileReader) != "undefined") {
    			var reader = new FileReader();
    			reader.onload = function (e) {
    				var rowArr = e.target.result.split("\n");
    				for(var i=0; i<rowArr.length-1; i++) { csvData.push(rowArr[i].split(",")); }
    				csvData = validateTable();
    				if(csvData === null) {}
    				else {
    					$.ajax({
    						url: "/LocationDatabaseManagement/data/location/newCsv",
        					type: "POST",
        					data : { csvData: JSON.stringify(csvData) },
        					success : function(result) {
        						var data = JSON.parse(result);
        						var voidedLocationNodesData = null;
        						var zNodesData = null;
        					    var locationTypeNodesData = null;
        						var locationAttributeNodesData = null;
        						var locationAttributeDataNodesData = null;
        						voidedLocationNodesData = eval(data.voidedLocationNodes);
        						zNodesData = eval(data.locationNodes);
        						locationTypeNodesData = eval(data.locationTypeNodes);
        						locationAttributeNodesData = eval(data.locationAttributeTypeNodes);
        						locationAttributeDataNodesData = eval(data.locationAttributeNodes);
        						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
        						if(voidedLocationNodesData != null && zNodesData != null && locationTypeNodesData != null && locationAttributeDataNodesData != null && locationAttributeNodesData != null) {
        							voidedLocations = voidedLocationNodesData;
        							zNodes = zNodesData;
            						locationTypeNodes = locationTypeNodesData;
            						locationAttributeNodes = locationAttributeNodesData;
            						locationAttributeDataNodes = locationAttributeDataNodesData;
            						$.fn.zTree.init($("#treeDemo"), setting, zNodes);
            						$.fn.zTree.init($("#voidedLocations"), setting, voidedLocations);
            					    $("#selectAll").bind("click", selectAll);
            					    $("#saveBtn").bind("click", saveTableRow);

            					    var nameHtml = "<select id='addType' name='addType' class='form-control myclass' title='Choose Location Type' ><option value=''></option>";
            				    	var idArray = [];
            				    	var nameArray = [];
            				    	var tempArray = [];
            				    	for (var i = 0; i < locationTypeNodes.length; i++) {
            				    		idArray.push(locationTypeNodes[i].id);
            				    		nameArray.push(locationTypeNodes[i].name);
            				    		tempArray.push(capitalize(locationTypeNodes[i].name));
            				    	} tempArray.sort();
            				    	for (var i = 0; i < tempArray.length; i++) { 
            				    		var name; var id;
            				    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; } }
            				    		nameHtml += '<option value="' + id +'">' + name + '</option>'; 
            						} nameHtml += "</select>";
            						document.getElementById("addLocationTypeListDiv").innerHTML = nameHtml;
            						
            						$("#expandAllBtn").bind("click", {type:"expandAll"}, expandNode);
            						$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);
        						}
        					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "EXCEPTION OCCURED"); }
    					});
    				}
    			} 
    			reader.readAsText(fileUpload.files[0]);
    		} else { alertDIV("warning", "ERROR: ", "This browser does not support HTML5."); }
    	} else { alertDIV("warning", "ERROR: ", "Please upload a valid CSV file."); fileUpload.value = null; }
	});
	
	$('#fileUploadLocAttrs').change( function(event) {
    	unknownColsForLocAttrs = [];
    	requiredColsDataForLocAttrs = [];
		csvDataForLocAttrs = [];
		var fileUpload = document.getElementById("fileUploadLocAttrs");
    	var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)");
    	if (regex.test(fileUpload.value.toLowerCase())) {
//    		alert(fileUpload.value.toLowerCase());
    		if (typeof (FileReader) != "undefined") {
    			var reader = new FileReader();
    			reader.onload = function (e) {
    				var rowArr = e.target.result.split("\n");
    				for(var i=0; i<rowArr.length-1; i++) { csvDataForLocAttrs.push(rowArr[i].split(",")); }
    				csvDataForLocAttrs = validateTableForLocAttrs();
    				if(csvDataForLocAttrs === null) {}
    				else {
    					$.ajax({
    						url: "/LocationDatabaseManagement/data/location/newCsvForLocAttrs",
        					type: "POST",
        					data : { csvData: JSON.stringify(csvDataForLocAttrs) },
        					success : function(result) {
        						var data = JSON.parse(result);
        						var locationAttributeNodesData = null;
        						var locationAttributeDataNodesData = null;
        						locationAttributeNodesData = eval(data.locationAttributeTypeNodes);
        						locationAttributeDataNodesData = eval(data.locationAttributeNodes);
        						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
        						if(locationAttributeDataNodesData != null && locationAttributeNodesData != null) {
        							locationAttributeNodes = locationAttributeNodesData;
        							locationAttributeDataNodes = locationAttributeDataNodesData;
        							if(locationAttributeNodes !== null && typeof locationAttributeNodes !== 'undefined') {
        								for(var i = 0; i<locationAttributeNodes.length; i++) {
        									locationAttributeId.push(locationAttributeNodes[i].id);
        									locationAttributeName.push(locationAttributeNodes[i].name);
        									locationAttributeDisplayName.push(locationAttributeNodes[i].displayName);
        									locationAttributeDescription.push(locationAttributeNodes[i].description);
        									locationAttributeCategory.push(locationAttributeNodes[i].category);
        								}
        							}
        							var bodyDiv = document.getElementById("myLocationAttributeAdd");
        							for(var i = 0; i<locationAttributeId.length; i++) {
        								if(locationAttributeDisplayName[i] === "" || locationAttributeDisplayName[i] === null) {}
        								else {
        									var tag1 = "addlocationattribute";
        								    var div = document.createElement("div");
        								    div.setAttribute("class", "form-group");
        								
        								    var descriptionLabel = document.createElement('label');
        								    descriptionLabel.innerHTML = locationAttributeDisplayName[i] + ":";
        								    descriptionLabel.setAttribute("for", "locationAttribute_"+locationAttributeId[i]);
        								    descriptionLabel.setAttribute("class", "form-control-label");
        								    div.appendChild(descriptionLabel);
        								
        								    var descriptionValue = document.createElement('input');
        								    descriptionValue.setAttribute("type", "text");
        								    descriptionValue.setAttribute("name", "locationAttribute_"+locationAttributeId[i]);
        								    descriptionValue.setAttribute("id", "locationAttribute_"+locationAttributeId[i]);
        								    descriptionValue.setAttribute("class", "form-control");
        								    descriptionValue.setAttribute("value", "");
        								    descriptionValue.setAttribute("title", "Enter "+locationAttributeDisplayName[i]+", Any 255 Characters");
        								    descriptionValue.setAttribute("placeholder", locationAttributeDisplayName[i]);
        								    descriptionValue.setAttribute("maxlength", "255");
        								    descriptionValue.setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
        								    div.appendChild(descriptionValue);
        								    bodyDiv.appendChild(div);
        								    var radioHTML = "";
        								    radioHTML += '<div id="bound_'+locationAttributeId[i]+'_RADIO" class="form-group">';
        								    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="None" checked>&nbsp;None&nbsp;';//+locationAttributeId[i]+
        								    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Anually">&nbsp;Anually&nbsp;';
        								    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
        								    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
        								    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
        								    radioHTML += '</div>'
        								    radioHTML += '<div id="bound_'+locationAttributeId[i]+'_DIV"></div>';
        								    bodyDiv.innerHTML += radioHTML;
        								}
        							}
        						}
    						}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "EXCEPTION OCCURED"); /*alert("EXCEPTION OCCURED");*/ }
    					});
    				}
    			} 
    			reader.readAsText(fileUpload.files[0]);
    		} else { alertDIV("warning", "ERROR: ", "This browser does not support HTML5."); }
    	} else { alertDIV("warning", "ERROR: ", "Please upload a valid CSV file."); }
	});

	$("#addAll").bind("click", function() {
		var pattern = new RegExp("^[0-9]*$");
		if(document.getElementById("myModalHorizontalName").value !== "" ) {
			if(document.getElementById("myModalHorizontalName").value.length >= 3 && document.getElementById("myModalHorizontalName").value.length <= 50 && pattern.test(document.getElementById("myModalHorizontalLevel").value )) { $('#myModalHorizontal').modal('toggle'); }
			else {//VALIDATION
				var errorStr = "";
		        if(!(document.getElementById("myModalHorizontalName").value.length >= 3)) { errorStr += "Location Type Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("myModalHorizontalName").value.length <= 50)) { errorStr += "Location Type Name must have atmost 50 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("myModalHorizontalLevel").value ))) { errorStr += "Location Type Level must have numbers only.<br/>"; }
			    alertDIV1("warning", "ERROR: ", errorStr, "_modalll");
			}
		}
		else {//VALIDATION
			var errorStr= "";
			if(document.getElementById("myModalHorizontalName").value === "") { errorStr += "Location Type Name is required."; } 
			alertDIV1("warning", "ERROR: ", errorStr, "_modalll"); 
		}
	});	

	$("#saveBtnVoidLocationDetail").bind("click", function() {
		var myTreeId = "#treeDemo";
		var treeNode = ($.fn.zTree.init($("#treeDemo"), setting, zNodes)).getNodeByParam('id', document.getElementById("voidLocation").value);
		if(treeNode === null) { myTreeId = "#voidedLocations"; treeNode = ($.fn.zTree.init($("#voidedLocations"), setting, voidedLocations)).getNodeByParam('id', document.getElementById("voidLocation").value); }
		if(treeNode.name === "VOIDED LOCATIONS") { $('#modalVoidLocationDetail').modal('toggle'); alertDIV("warning", "WARNING", "LOCATION '" + treeNode.name + "' CANNOT BE VOIDED/UN-VOIDED."); }
		else if((treeNode.voided).toString() === document.getElementById("voided").value) { $('#modalVoidLocationDetail').modal('toggle'); if(myTreeId === "#treeDemo") { alertDIV("info", "INFO", "LOCATION '" + treeNode.name + "' IS ALREADY UN-VOIDED."); } else { alertDIV("info", "INFO", "LOCATION '" + treeNode.name + "' IS ALREADY VOIDED."); } }
		else {
			if(document.getElementById("voidReason").length > 255) { alertDIV1("warning", "ERROR: ", "Void Reason must be 255 charachers long", "_voidLocationDetail"); }
			else {
				$.ajax({
					url: "/LocationDatabaseManagement/data/location/updatelocation/type",
					type: "POST",
					data : { id: treeNode.id, voided: document.getElementById("voided").value, reason: document.getElementById("voidReason").value },
					success : function(result) {
						$('#modalVoidLocationDetail').modal('toggle');
						var data = JSON.parse(result);
						var zNodesData = null;
						var voidedLocationNodesData = null;
						voidedLocationNodesData = eval(data.voidedLocationNodes);
						zNodesData = eval(data.locationNodes);
						if(voidedLocationNodesData != null && zNodesData != null) { zNodes = zNodesData; voidedLocations = voidedLocationNodesData; $.fn.zTree.init($("#treeDemo"), setting, zNodes); $.fn.zTree.init($("#voidedLocations"), setting, voidedLocations); }
						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
						if(document.getElementById("dvTable").innerHTML === "") { }
						else { 
							var treeNodeParent = null; 
							if(myTreeId === "#treeDemo") { treeNodeParent = ($.fn.zTree.init($("#treeDemo"), setting, zNodes)).getNodeByParam('id', treeNode.pId); }
							else { treeNodeParent = ($.fn.zTree.init($("#voidedLocations"), setting, voidedLocations)).getNodeByParam('id', treeNode.pId); }
							GenerateTable(treeNodeParent.children, treeNodeParent.name, treeNodeParent.id); 
						}
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING LOCATION"); $("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); $("#alertModal").modal('show'); }
				});
			}
		}
	});	
	$("#addAllNodes").bind("click", function() {
		if(document.getElementById("addShortName").value === "") { document.getElementById("addShortName").value = document.getElementById("addName").value; }
		if(document.getElementById("addFullName").value === "") { document.getElementById("addFullName").value = document.getElementById("addName").value; }
		var pattern = new RegExp("^([\-a-zA-Z0-9._ ]+)$");
		var pattern2 = new RegExp("\-[0-9]*\.[0-9]*$");
		if(document.getElementById("addName").value !== "" && document.getElementById("addShortName").value !== "" && document.getElementById("addFullName").value !== "" && document.getElementById("addType").value !== "") {
			if(( document.getElementById("addName").value.length >= 3 && document.getElementById("addName").value.length <= 30 && pattern.test(document.getElementById("addName").value )) && ( document.getElementById("addShortName").value.length >= 3 && document.getElementById("addShortName").value.length <= 30 && pattern.test(document.getElementById("addShortName").value )) && (document.getElementById("addFullName").value.length >= 3 && document.getElementById("addFullName").value.length <= 50 && pattern.test(document.getElementById("addFullName").value)) && (!(pattern2.test(document.getElementById("addOtherIdentifier").value)))) {
				var node = $.fn.zTree.init($("#treeDemo"), setting, zNodes).getNodeByParam('id', document.getElementById("addParent").value);
				var nodeName = null;
				if(node === null) { nodeName = ""; } else { nodeName = node.name; }
				var dataNode = { name: document.getElementById("addName").value, shortName: document.getElementById("addShortName").value, fullName: document.getElementById("addFullName").value, pName: nodeName, description: document.getElementById("addDescription").value, otherIdentifier: document.getElementById("addOtherIdentifier").value, typeName: document.getElementById("addType").options[document.getElementById("addType").selectedIndex].text, latitude: document.getElementById("addLatitude").value, longitude: document.getElementById("addLongitude").value };
				$.ajax({
					url: "/LocationDatabaseManagement/data/location/newlocation",
					type: "POST",
					data : dataNode,
					success : function(result) {
						var data = JSON.parse(result);
						var id = null;
						id = eval(data.id);
						if(data.status.toString() === "info") { alertDIV1(data.status.toString(), data.message.toString(), data.returnStr.toString(), "_addLocation"); }
						else { 
							$('#myModal').modal('toggle');
							addAll(id);
							document.getElementById("errors").innerHTML = "";
					        document.getElementById("addName").value = "";
					        document.getElementById("addShortName").value = "";
					        document.getElementById("addFullName").value = "";
					        document.getElementById("addParent").value = "";
					        document.getElementById("addDescription").value = "";
					        document.getElementById("addOtherIdentifier").value = "";
					        document.getElementById("addType").value = "";
					        document.getElementById("addLatitude").value = "";
					        document.getElementById("addLongitude").value = "";
							alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
						}
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE CREATING NEW LOCATION"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION"); $("#alertModal").modal('show');*/ }
				});
			}
			else {//VALIDATION
				var errorStr = "";
				if(!(document.getElementById("addName").value.length >= 3)) { errorStr += "Location Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addName").value.length <= 30)) { errorStr += "Location Name must have atmost 30 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addName").value))) { errorStr += "Location Name must have alphabets only.<br/>"; }
				if(!(document.getElementById("addShortName").value.length >= 3)) { errorStr += "Location Short Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addShortName").value.length <= 30)) { errorStr += "Location Short Name must have atmost 30 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addShortName").value))) { errorStr += "Location Short Name must have alphabets only.<br/>"; }
				if(!(document.getElementById("addFullName").value.length >= 3)) { errorStr += "Location Full Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addFullName").value.length <= 50)) { errorStr += "Location Full Name must have atmost 30 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addFullName").value))) { errorStr += "Location Full Name must have alphabets only.<br/>"; }
			    if(pattern2.test(document.getElementById("addOtherIdentifier").value)) { errorStr += "Other Identifier must have alphabets or positive numbers only.<br/>"; }
				alertDIV1("warning", "ERROR: ", errorStr, "_addLocation");
			}
		}
		else {//VALIDATION-CHECK-NULL
			var errorStr = "";
			if(document.getElementById("addName").value === "") { errorStr += "Location Name is required.<br/>"; }
			if(document.getElementById("addShortName").value === "") { errorStr += "Location Short Name is required.<br/>"; }
			if(document.getElementById("addFullName").value === "") { errorStr += "Location Full Name is required.<br/>"; }
			//if(document.getElementById("addParent").value === "") { errorStr += "Location Parent is required.<br/>"; }	
			if(document.getElementById("addType").value === "") { errorStr += "Location Type is required."; }	
			alertDIV1("warning", "ERROR: ", errorStr, "_addLocation");
		 }
	});	
	$("#addAlllocationTypes").bind("click", function() {
		if(document.getElementById("addlocationTypeLevel").value === "") { document.getElementById("addlocationTypeLevel").value = "0"; document.getElementById("addlocationTypeLevel").innerHTML = "0"; }
		var pattern = new RegExp("^[0-9]*.[0-9]*$");
		var pattern2 = new RegExp("^([\-a-zA-Z0-9._ ]+)$");
		if(document.getElementById("addlocationTypeName").value !== "" ) {
			if(document.getElementById("addlocationTypeName").value.length >= 3 && document.getElementById("addlocationTypeName").value.length <= 50 && pattern2.test(document.getElementById("addlocationTypeName").value) && pattern.test(document.getElementById("addlocationTypeLevel").value )) {
				var dataNode = { name: document.getElementById("addlocationTypeName").value, level: document.getElementById("addlocationTypeLevel").value, description: document.getElementById("addlocationTypeDescription").value };
				$.ajax({
					url: "/LocationDatabaseManagement/data/location/newlocationtype",
					type: "POST",
					data: dataNode,
					success : function(result) {
						var data = JSON.parse(result);
						var id = null;
						id = eval(data.id);
						var name = document.getElementById("addlocationTypeName").value;
						var level = document.getElementById("addlocationTypeLevel").value;
						var description = document.getElementById("addlocationTypeDescription").value;
						var node = { id: id, name: name, level: level, description: description };
						var itemval= '<option value="' + node.id +'">' + node.name + '</option>';
						if(data.status.toString() === "info") { alertDIV1(data.status.toString(), data.message.toString(), data.returnStr.toString(), "_addLocationType"); }
						else { 
					        $('#myModalLocationType').modal('toggle');
							locationTypeNodes.push(node);
					        var nameHtml = "<select id='addType' name='addType' class='form-control myclass' title='Choose Location Type' ><option value=''></option>";
					    	var idArray = [];
					    	var nameArray = [];
					    	var tempArray = [];
					    	for (var i = 0; i < locationTypeNodes.length; i++) {
					    		idArray.push(locationTypeNodes[i].id);
					    		nameArray.push(locationTypeNodes[i].name);
					    		tempArray.push(capitalize(locationTypeNodes[i].name));
					    	} tempArray.sort();
					    	for (var i = 0; i < tempArray.length; i++) { 
					    		var name; var id;
					    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; } }
					    		nameHtml += '<option value="' + id +'">' + name + '</option>'; 
				    		} nameHtml += "</select>";
							document.getElementById("addLocationTypeListDiv").innerHTML = nameHtml;
							alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
							document.getElementById("addlocationTypeName").value = "";
							document.getElementById("addlocationTypeLevel").value = "";
							document.getElementById("addlocationTypeDescription").value = "";
						}
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); }
				});
			}
			else {//VALIDATION
				var errorStr = "";
		        if(!(document.getElementById("addlocationTypeName").value.length >= 3)) { errorStr += "Location Type Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addlocationTypeName").value.length <= 50)) { errorStr += "Location Type Name must have atmost 50 characters.<br/>"; }
			    if(!(pattern2.test(document.getElementById("addlocationTypeName").value ))) { errorStr += "Location Type Name must have alphanumeric or special characters (.-_) only.<br/>"; }
			    if(!(pattern.test(document.getElementById("addlocationTypeLevel").value ))) { errorStr += "Location Type Level must have numbers only.<br/>"; }
			    alertDIV1("warning", "ERROR: ", errorStr, "_addLocationType");
			}
		}
		else {//VALIDATION
			var errorStr= "";
			if(document.getElementById("addlocationTypeName").value === "") { errorStr += "Location Type Name is required."; }
		    alertDIV1("warning", "ERROR: ", errorStr, "_addLocationType");
		 }
	});	
	$("#updateAlllocationTypes").bind("click", function() {
		if(document.getElementById("updatelocationTypeLevel").value === "") { document.getElementById("addlocationTypeLevel").value = "0"; document.getElementById("addlocationTypeLevel").innerHTML = "0"; }
		if(document.getElementById("updatelocationTypeName") != null) {
			var pattern = new RegExp("^[0-9.]*$");
			var id = document.getElementById("updatelocationTypeName").value;
			var name = document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text;
			var level = document.getElementById("updatelocationTypeLevel").value;
			var description = document.getElementById("updatelocationTypeDescription").value;
			if(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text !== "" ) {
				if(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text.length >= 3 && document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text.length <= 50 && pattern.test(document.getElementById("updatelocationTypeLevel").value )) {
					$.ajax({
						url: "/LocationDatabaseManagement/data/location/updatelocationtype",
						type: "POST",
						data : { name: name, level: level, description: description },
						success : function(result) {
					        $('#myModalLocationTypeUpdate').modal('toggle');
							var data = JSON.parse(result);
							var node = { id: id, name: name, level: level, description: description };
							for (var i = 0; i < locationTypeNodes.length; i++) {
								if(locationTypeNodes[i].id.toString() === node.id) {
									locationTypeNodes[i].id = node.id;
									locationTypeNodes[i].name = node.name;
									locationTypeNodes[i].level = node.level;
									locationTypeNodes[i].description = node.description;
								}
							}
							document.getElementById("updatelocationTypeName").value = id;
							document.getElementById("updatelocationTypeLevel").value = level;
							document.getElementById("updatelocationTypeDescription").value = description;
							alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
						}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING NEW LOCATION TYPE"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); $("#alertModal").modal('show');*/ }
					});
				}
				else {//VALIDATION
					var errorStr = "";
				    if(!(pattern.test(document.getElementById("updatelocationTypeLevel").value ))) { errorStr += "Location Type Level must have numbers only.<br/>"; }
				    alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationType");
				}
			}
			else {//VALIDATION
				var errorStr= "";
				if(document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text === "") { errorStr += "Location Type Name is required."; }
			    alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationType");
			 }
		}
		else {//VALIDATION
		    alertDIV1("warning", "ERROR: ", "Please Save or Cancel Name Changes First", "_updateLocationType");
		 }
	});	
	$("#addAlllocationAttributeTypes").bind("click", function() {
		if(document.getElementById("addlocationAttributeTypeDisplayName").value === "") { document.getElementById("addlocationAttributeTypeDisplayName").value = document.getElementById("addlocationAttributeTypeName").value; }
		var pattern = new RegExp("^([\-a-zA-Z0-9._ ]+)$");
		if(document.getElementById("addlocationAttributeTypeName").value !== "") {
			if(( document.getElementById("addlocationAttributeTypeName").value.length >= 3 && document.getElementById("addlocationAttributeTypeName").value.length <= 100 && pattern.test(document.getElementById("addlocationAttributeTypeName").value)) && pattern.test(document.getElementById("addlocationAttributeTypeDisplayName").value) ) {
				var dataNode = { name: document.getElementById("addlocationAttributeTypeName").value, displayName: document.getElementById("addlocationAttributeTypeDisplayName").value, description: document.getElementById("addlocationAttributeTypeDescription").value, category: document.getElementById("addlocationAttributeTypeCategory").value };
				$.ajax({
					url: "/LocationDatabaseManagement/data/location/newlocationattributetype",
					type: "POST",
					data : dataNode,
					success : function(result) {
						var data = JSON.parse(result);
						var id = null;
						id = eval(data.id);
						var name = document.getElementById("addlocationAttributeTypeName").value;
						var displayName = document.getElementById("addlocationAttributeTypeDisplayName").value;
						var description = document.getElementById("addlocationAttributeTypeDescription").value;
						var category = document.getElementById("addlocationAttributeTypeCategory").value;
						var node = { id: id, name: name, displayName: displayName, description: description, category: category };
						if(data.status.toString() === "info") { alertDIV1(data.status.toString(), data.message.toString(), data.returnStr.toString(), "_addLocationAttributeType"); }
						else { 
					        $('#myModalLocationAttributeType').modal('toggle');
							locationAttributeNodes.push(node);
							locationAttributeId.push(id);
							locationAttributeName.push(name);
							locationAttributeDisplayName.push(displayName);
							locationAttributeDescription.push(description);
							locationAttributeCategory.push(category);
							addToModal(id);
							alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString()); 
						}
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE CREATING NEW LOCATION ATTRIBUTE TYPE"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION ATTRIBUTE TYPE"); $("#alertModal").modal('show');*/ }
				});
			}
			else {// VALIDATION
				var errorStr = "";
		        if(!(document.getElementById("addlocationAttributeTypeName").value.length >= 3)) { errorStr += "Location Attribute Type Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addlocationAttributeTypeName").value.length <= 100)) { errorStr += "Location Attribute Type Name must have atmost 50 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addlocationAttributeTypeName").value ))) { errorStr += "Location Attribute Type Name must have alphanumeric or special characters (.-_) only.<br/>"; }
		        if(!(document.getElementById("addlocationAttributeTypeDisplayName").value.length >= 3)) { errorStr += "Location Attribute Type Display Name must have atleast 3 characters.<br/>"; }
			    if(!(document.getElementById("addlocationAttributeTypeDisplayName").value.length <= 100)) { errorStr += "Location Attribute Type Display Name must have atmost 50 characters.<br/>"; }
			    if(!(pattern.test(document.getElementById("addlocationAttributeTypeDisplayName").value ))) { errorStr += "Location Attribute Type Display Name must have alphanumeric or special characters (.-_) only.<br/>"; }
				alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttributeType");
			}
		}
		else {// VALIDATION
			var errorStr = "";
			if(document.getElementById("addlocationAttributeTypeName").value === "" ) { errorStr += "Location Attribute Type Name is required.<br/>"; }
			//if(document.getElementById("addlocationAttributeTypeDisplayName").value === "" ) { errorStr += "Location Attribute Type Display Name is required.<br/>"; }
			alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttributeType");
		}
	});	
	$("#updateAlllocationAttributeTypes").bind("click", function() {
		if(document.getElementById("updatelocationAttributeTypeName") != null) {
			if(document.getElementById("updatelocationAttributeTypeName").value !== "" ) {
				var id = document.getElementById("updatelocationAttributeTypeName").value;
				var name = document.getElementById("updatelocationAttributeTypeName").options[document.getElementById("updatelocationAttributeTypeName").selectedIndex].text;
				var displayName = document.getElementById("updatelocationAttributeTypeDisplayName").value;
				var description = document.getElementById("updatelocationAttributeTypeDescription").value;
				var category = document.getElementById("updatelocationAttributeTypeCategory").value;
		        $('#myModalLocationAttributeTypeUpdate').modal('toggle');
				$.ajax({
					url: "/LocationDatabaseManagement/data/location/updatelocationattributetype",
					type: "POST",
					data : { name: name, displayName: displayName, description: description, category: category },
					success : function(result) {
						var data = JSON.parse(result);
						var node = { id: id, name: name, displayName: displayName, description: description, category: category };
						for (var i = 0; i < locationAttributeNodes.length; i++) {
							if(locationAttributeNodes[i].name === name) {
								locationAttributeNodes[i].displayName = displayName;
								locationAttributeNodes[i].description = description;
								locationAttributeNodes[i].category = category;
							}
						}
						for (var i = 0; i < locationAttributeName.length; i++) {
							if(locationAttributeName[i].toString() === name.toString()) {
								locationAttributeId[i] = id.toString();
								locationAttributeName[i] = name.toString();
								locationAttributeDisplayName[i] = displayName.toString();
								locationAttributeDescription[i] = description.toString();
								locationAttributeCategory[i] = category.toString();
							}
						}
						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString()); 
					}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING NEW LOCATION ATTRIBUTE TYPE"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION ATTRIBUTE TYPE"); $("#alertModal").modal('show');*/ }
				});
			}
			else {// VALIDATION
				var errorStr = "";
				if(document.getElementById("updatelocationAttributeTypeName").value === "" ) { errorStr += "Location Attribute Type Name is required."; }
				alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationAttributeType");
			}
		}
		else {// VALIDATION
		    alertDIV1("warning", "ERROR: ", "Please Save or Cancel Name Changes First", "_updateLocationAttributeType");
		}
	});	

	$("#addAlllocationAttributes").bind("click", function() {
		if(document.getElementById("locationParent").value !== "" ) {
			var boundArr = [];
			var errBoundArr = [];
			var errorStr= "";
			var parent = document.getElementById("locationParent").value;
			for (var i = 0; i < locationAttributeId.length; i++) {
				var myBound = null;
				var radioGroup = document.getElementsByName("bound_" + locationAttributeId[i]);
				for (var j = 0; j < radioGroup.length; j++) {
					if(radioGroup[j].checked) {
						myBound = radioGroup[j];
						if(myBound.value === "None") { 
							var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i]).value, value1: null, value2: null, type: myBound.value, locationAttributeTypeId: locationAttributeId[i], locationAttributeTypeName: locationAttributeDisplayName[i] }; 
							if(node.locationAttributeTypeValue !== "") {
								if(node.value1 !== "" && node.value2 !== "") { boundArr.push(node); }
								else { if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + node.type + "' attribute is required.<br/>"; } }
							}
						}
						else if(myBound.value === "Anually") {
							if(document.getElementById("locationAttribute_"+locationAttributeId[i]).value !== "") {
							var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i]).value, value1: document.getElementById(myBound.id+"_BOUND").value, value2: null, type: myBound.value, locationAttributeTypeId: locationAttributeId[i], locationAttributeTypeName: locationAttributeDisplayName[i] }; 
								if(node.locationAttributeTypeValue !== "") {
									if(node.value1 !== "" && node.value2 !== "") {
										var currentDate = getCurrentDate();
										var date = node.value1;
										if(date.length > (currentDate.substring(0, 4)).length) { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + node.type + "' attribute must have atleast " + (currentDate.substring(0, 4)).length + " numbers to make .<br/>"; }
										else { 
											if(currentDate.substring(0, 4) >= date.substring(0, 4)) { boundArr.push(node); } 
											else { errorStr += "PLEASE CHOOSE A YEAR LESS THAN OR EQUAL TO '"+(currentDate.substring(0, 4))+"'.<br/>"; }
										}
									} else { if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + node.type + "' attribute is required.<br/>"; } }
								} else { errorStr += node.locationAttributeTypeName + " is required.<br/>"; }
							} else { errorStr += locationAttributeName[i] + " is required.<br/>"; }
						}
						else if(myBound.value === "Monthly") {
							if(document.getElementById("locationAttribute_"+locationAttributeId[i]).value !== "") {
							var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i]).value, value1: document.getElementById(myBound.id+"_BOUND").value, value2: null, type: myBound.value, locationAttributeTypeId: locationAttributeId[i], locationAttributeTypeName: locationAttributeDisplayName[i] }; 
								if(node.locationAttributeTypeValue !== "") {
									if(node.value1 !== "" && node.value2 !== "") { 
										var currentDate = getCurrentDate();
										var date = node.value1;
										if(currentDate.substring(0, 4) >= date.substring(0, 4)) {
											if(currentDate.substring(5, 7) >= date.substring(5, 7)) {
												boundArr.push(node); 
											} else { errorStr += "PLEASE CHOOSE A MONTH LESS THAN OR EQUAL TO '"+(currentDate.substring(5, 7))+"'.<br/>"; }
										} else { errorStr += "PLEASE CHOOSE A YEAR LESS THAN OR EQUAL TO '"+(currentDate.substring(0, 4))+"'.<br/>"; }
									} else { if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + node.type + "' attribute is required.<br/>"; } }
								} else { errorStr += node.locationAttributeTypeName + " is required.<br/>"; }
							} else { errorStr += locationAttributeName[i] + " is required.<br/>"; }
						}
						else if(myBound.value === "Daily") {
							if(document.getElementById("locationAttribute_"+locationAttributeId[i]).value !== "") {
							var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i]).value, value1: document.getElementById(myBound.id+"_BOUND").value, value2: null, type: myBound.value, locationAttributeTypeId: locationAttributeId[i], locationAttributeTypeName: locationAttributeDisplayName[i] }; 
								if(node.locationAttributeTypeValue !== "") {
									if(node.value1 !== "" && node.value2 !== "") { 
										var currentDate = getCurrentDate();
										var date = node.value1;
										if(currentDate.substring(0, 4) >= date.substring(0, 4)) {
											if(currentDate.substring(5, 7) >= date.substring(5, 7)) {
												if((currentDate.substring(8) >= date.substring(8)) || (currentDate.substring(5, 7) > date.substring(5, 7) && currentDate.substring(8) < date.substring(8))) { boundArr.push(node); }
												else { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"'.<br/>"; }
											} else { 
												errorStr += "PLEASE CHOOSE A MONTH LESS THAN OR EQUAL TO '"+(currentDate.substring(5, 7))+"'.<br/>"; 
												if(!(currentDate.substring(8) >= date.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"'.<br/>"; } 
											}
										} else { 
											errorStr += "PLEASE CHOOSE A YEAR LESS THAN OR EQUAL TO '"+(currentDate.substring(0, 4))+"'.<br/>"; 
											if(currentDate.substring(5, 7) >= date.substring(5, 7)) { if(!(currentDate.substring(8) >= date.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"'.<br/>"; } } 
											else { errorStr += "PLEASE CHOOSE A MONTH LESS THAN OR EQUAL TO '"+(currentDate.substring(5, 7))+"'.<br/>"; if(!(currentDate.substring(8) >= date.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"'.<br/>"; } }
										}
									} else { if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + node.type + "' attribute is required.<br/>"; } }
								} else { errorStr += node.locationAttributeTypeName + " is required.<br/>"; }
							} else { errorStr += locationAttributeName[i] + " is required.<br/>"; }
						}

						else if(myBound.value === "Time_Bound") {
							if(document.getElementById("locationAttribute_"+locationAttributeId[i]).value !== "") {
								var node = {id: myBound.id, locationAttributeTypeValue: document.getElementById("locationAttribute_"+locationAttributeId[i]).value, value1: document.getElementById(myBound.id+"_BOUND_From").value, value2: document.getElementById(myBound.id+"_BOUND_To").value, type: myBound.value, locationAttributeTypeId: locationAttributeId[i], locationAttributeTypeName: locationAttributeDisplayName[i] }; 
								var type = node.type;
								while(type.includes("_")) { type = type.replace("_", " "); }
								if(node.locationAttributeTypeValue !== "") {
									if(node.value1 !== "" && node.value2 !== "") {  
										var currentDate = getCurrentDate();
										var date1 = node.value1;
										var date2 = node.value2;
										var date1Check = false;
										var date2Check = false;
										if(currentDate.substring(0, 4) >= date1.substring(0, 4)) { 
											if(currentDate.substring(5, 7) >= date1.substring(5, 7)) { 
												if((currentDate.substring(8) >= date1.substring(8)) || (currentDate.substring(5, 7) > date1.substring(5, 7) && currentDate.substring(8) < date1.substring(8))) { date1Check = true; }
												else { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE FROM'.<br/>"; }
											} else { 
												errorStr += "PLEASE CHOOSE A MONTH LESS THAN OR EQUAL TO '"+(currentDate.substring(5, 7))+"' FOR 'DATE FROM'.<br/>"; 
												if(!(currentDate.substring(8) >= date1.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE FROM'.<br/>"; } 
											}
										} else { 
											errorStr += "PLEASE CHOOSE A YEAR LESS THAN OR EQUAL TO '"+(currentDate.substring(0, 4))+"' FOR 'DATE FROM'.<br/>"; 
											if(currentDate.substring(5, 7) >= date1.substring(5, 7)) { if(!(currentDate.substring(8) >= date1.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE FROM'.<br/>"; } } 
											else { errorStr += "PLEASE CHOOSE A MONTH LESS THAN OR EQUAL TO '"+(currentDate.substring(5, 7))+"' FOR 'DATE FROM'.<br/>"; if(!(currentDate.substring(8) >= date1.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE FROM'.<br/>"; } }
										}
										if(currentDate.substring(0, 4) >= date2.substring(0, 4)) { 
											if(currentDate.substring(5, 7) >= date2.substring(5, 7)) { 
												if((currentDate.substring(8) >= date2.substring(8)) || (currentDate.substring(5, 7) > date2.substring(5, 7) && currentDate.substring(8) < date2.substring(8))) { console.log("d2-if-d"); date2Check = true; }
												else { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE TO'.<br/>"; }
											} else { 
												errorStr += "PLEASE CHOOSE A MONTH LESS THAN OR EQUAL TO '"+(currentDate.substring(5, 7))+"' FOR 'DATE TO'.<br/>"; 
												if(!(currentDate.substring(8) >= date2.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE TO'.<br/>"; } 
											}
										} else { 
											errorStr += "PLEASE CHOOSE A YEAR LESS THAN OR EQUAL TO '"+(currentDate.substring(0, 4))+"' FOR 'DATE TO'.<br/>"; 
											if(currentDate.substring(5, 7) >= date2.substring(5, 7)) { if(!(currentDate.substring(8) >= date2.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE TO'.<br/>"; } } 
											else { errorStr += "PLEASE CHOOSE A MONTH LESS THAN OR EQUAL TO '"+(currentDate.substring(5, 7))+"' FOR 'DATE TO'.<br/>"; if(!(currentDate.substring(8) >= date2.substring(8))) { errorStr += "PLEASE CHOOSE A DAY LESS THAN OR EQUAL TO '"+(currentDate.substring(8))+"' FOR 'DATE TO'.<br/>"; } }
										}
										if(date1Check && date2Check) { boundArr.push(node); }
									}
									else {
										if(node.value1 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + type + " From' attribute is required.<br/>"; }
										if(node.value2 === "") { errorStr += "Value for '" + node.locationAttributeTypeName + "-" + type + " To' attribute is required.<br/>"; }
									}
								} else { errorStr += node.locationAttributeTypeName + " is required.<br/>"; }
							} else { errorStr += locationAttributeName[i] + " is required.<br/>"; }
						} else { }
					}
				}
			} 
			console.log(locationAttributeDataNodes);
			if(errorStr !== "") { alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttribute"); }
			else {
				$('#myModalLocationAttribute').modal('toggle');
				$.ajax({
					url: "/LocationDatabaseManagement/data/location/newlocationattribute",
					type: "POST",
					data : { parent: parent, array: JSON.stringify(boundArr) },
					success : function(result) {
						var data = JSON.parse(result);
						var idArr = null;
						idArr = eval(data.id);
						if(idArr.length === boundArr.length) {
							for (var i = 0; i < boundArr.length; i++) {
								var myLocAttrNode = { id: idArr[i], typeId: boundArr[i].locationAttributeTypeId, locationId: parent, value: boundArr[i].locationAttributeTypeValue, typeName: boundArr[i].type, typeValue1: boundArr[i].value1, typeValue2: boundArr[i].value2 };
								locationAttributeDataNodes.push(myLocAttrNode);
							}
						} alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
						for (var i = 0; i < boundArr.length; i++) {
							var node = boundArr[i];
							if(node.type === "None") { 
								if(node.value1 !== "" && node.value2 !== "") {
									document.getElementById("locationAttribute_"+node.locationAttributeTypeId).value = "";
									document.getElementById(node.id+"_DIV").innerHTML = "";
									var radioGroup = document.getElementsByName("bound_" + node.locationAttributeTypeId);
									for (var j = 0; j < radioGroup.length; j++) { if(radioGroup[j].value === "None") { radioGroup[j].checked = true; } }
								}
							}
							else if(node.type === "Time_Bound") {
								if(node.value1 !== "" && node.value2 !== "") {
									document.getElementById("locationAttribute_"+node.locationAttributeTypeId).value = "";
									document.getElementById(node.id+"_DIV").innerHTML = "";
									var radioGroup = document.getElementsByName("bound_" + node.locationAttributeTypeId);
									for (var j = 0; j < radioGroup.length; j++) { if(radioGroup[j].value === "None") { radioGroup[j].checked = true; } }
								}
							}
							else { 
								if(node.value1 !== "" && node.value2 !== "") {
									document.getElementById("locationAttribute_"+node.locationAttributeTypeId).value = "";
									document.getElementById(node.id+"_DIV").innerHTML = "";
									var radioGroup = document.getElementsByName("bound_" + node.locationAttributeTypeId);
									for (var j = 0; j < radioGroup.length; j++) { if(radioGroup[j].value === "None") { radioGroup[j].checked = true; } }
								}
							}
						}
						for(var i=0; i<locationAttributeId.length; i++) { document.getElementById("locationAttribute_"+locationAttributeId[i]).value = ""; }
					}
				});
			}
		}
		else {// VALIDATION
			var errorStr = "";
			if(document.getElementById("locationParent").value === "" ) { errorStr += "Parent is required."; }
			alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttribute");
		}
	});	
});
var count = 0;
var log, className = "dark";
var setting = {
    view: {
        addHoverDom: addHoverDom,
        removeHoverDom: removeHoverDom,
        selectedMulti: false
    },
    edit: {
        enable: true,
        editNameSelectAll: true,
        showRemoveBtn: false,
        showRenameBtn: true
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeDrag: beforeDrag,
        beforeRename: beforeRename,
        onClick: onClick,
        beforeCollapse: beforeCollapse,
		beforeExpand: beforeExpand,
		onCollapse: onCollapse,
		onExpand: onExpand
    }
};
function beforeCollapse(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" beforeCollapse ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name );
	return (treeNode.collapse !== false);
}
function onCollapse(event, treeId, treeNode) {
	showLog("[ "+getTime()+" onCollapse ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
}		
function beforeExpand(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" beforeExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name );
	return (treeNode.expand !== false);
}
function onExpand(event, treeId, treeNode) {
	showLog("[ "+getTime()+" onExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
}
function showLog(str) {
	if (!log) log = $("#log");
	log.append("<li class='"+className+"'>"+str+"</li>");
	if(log.children("li").length > 8) {
		log.get(0).removeChild(log.children("li")[0]);
	}
}
function getTime() {
	var now= new Date(),
	h=now.getHours(),
	m=now.getMinutes(),
	s=now.getSeconds(),
	ms=now.getMilliseconds();
	return (h+":"+m+":"+s+ " " +ms);
}
function expandNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	type = e.data.type,
	nodes = zTree.getSelectedNodes();
	if (type == "expandAll") {
		zTree.expandAll(true);
	} else if (type == "collapseAll") {
		zTree.expandAll(false);
	}
}
function beforeDrag(treeId, treeNodes) {
    return false;
}
function beforeEditName(treeId, treeNode) {
    className = (className === "dark" ? "":"dark");
    showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    setTimeout(function() {
    if (confirm("Start node '" + treeNode.name + "' editorial status?")) {
        setTimeout(function() {
            zTree.editName(treeNode);
        }, 0);
    }
    }, 0);
    return false;
}
function beforeRename(treeId, treeNode, newName, isCancel) {
    if (newName.length === 0) { alertDIV("warning", "ERROR:", "Location Name must have atleast 3 characters."); return false; }
    else {
        var pattern = new RegExp("^([\-a-zA-Z0-9._ ]+)$");
		if(( newName.length >= 3 && newName.length <= 30 && pattern.test(newName) )) {
			$.ajax({
				url: "/LocationDatabaseManagement/data/location/updatelocationname",
				type: "POST",
				data : { id: treeNode.id, name: newName },
				success : function(result) {
					var data = JSON.parse(result);
					var zNodesData = null;
					var voidedLocationNodesData = null;
					zNodesData = eval(data.locationNodes);
					voidedLocationNodesData = eval(data.voidedLocationNodes);
					if(voidedLocationNodesData != null && zNodesData != null) { zNodes = zNodesData; voidedLocations = voidedLocationNodesData; $.fn.zTree.init($("#treeDemo"), setting, zNodes); $.fn.zTree.init($("#voidedLocations"), setting, voidedLocations); }
					alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
					GenerateTable(treeNode.children, newName, treeNode.id);
					return true;
				}, error: function(jqXHR, textStatus, errorThrown) { zTree.cancelEditName(); alertDIV("warning", "ERROR:", "ERROR OCCURED WHILE UPDATING LOCATION"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); $("#alertModal").modal('show');*/  return false; }
			});
		} else {
			var errorStr = "";
			if(!(newName.length >= 3)) { errorStr += "Location Name must have atleast 3 characters.<br/>"; }
		    if(!(newName.length <= 30)) { errorStr += "Location Name must have atmost 30 characters.<br/>"; }
		    if(!(pattern.test(newName))) { errorStr += "Location Name must have alphabets only.<br/>"; }
			alertDIV("warning", "ERROR:", errorStr);
			return false;
		}
    }
}
function onRename(e, treeId, treeNode, isCancel) {
	console.log("onRename");
}
function showLog(str) {
    if (!log) log = $("#log");
    log.append("<li class='"+className+"'>"+str+"</li>");
    if(log.children("li").length > 8) { log.get(0).removeChild(log.children("li")[0]); }
}
function getTime() {
    var now= new Date(),
    h=now.getHours(),
    m=now.getMinutes(),
    s=now.getSeconds(),
    ms=now.getMilliseconds();
    return (h+":"+m+":"+s+ " " +ms);
}
function addHoverDom(treeId, treeNode) {
	if(treeId === "treeDemo") {
		var sObj = $("#" + treeNode.tId + "_span");
	    var id1 = "addlocation";
	    var id2 = "addlocationattribute";
	    var id3 = "voidLocationDetail";
	    if (treeNode.editNameFlag || $("#addNodeBtn_"+treeNode.tId).length>0) return;
	    if (treeNode.editNameFlag || $("#addStatBtn_"+treeNode.tId).length>0) return;
	    if (treeNode.editNameFlag || $("#voidLocationBtn_"+treeNode.tId).length>0) return;
	    var addStr1 = "<button type='button' id='addNodeBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Add New Location' data-toggle='modal' data-target='#myModal' data-backdrop='static' data-keyboard='false' onclick='setFocus(\""+id1+"\");' ><img src='images/plus.png' width='15px' height='15px'></button>";
	    var addStr2 = "<button type='button' id='addStatBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Add New Lcation Attribute' data-toggle='modal' data-target='#myModalLocationAttribute' data-backdrop='static' data-keyboard='false' onclick='setFocus(\""+id2+"\");' ><img src='images/plus.png' width='15px' height='15px'></button>";
	    var addStr3 = "<button type='button' id='voidLocationBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Void Location' data-toggle='modal' data-target='#modalVoidLocationDetail' data-backdrop='static' data-keyboard='false' onclick='setFocus(\""+id3+"\");' ><img src='images/void.png' width='15px' height='15px'></button>";
	    
	    var tId = null; var id = null; var name = null;
		tId = treeNode.tId;
		name = treeNode.name;
		typeName = treeNode.typeName;
		document.getElementById(tId + "_a").title = name + "-[" + typeName + "]";
		document.getElementById(treeNode.tId + "_edit").title = "Edit Location Name";
		document.getElementById(treeNode.tId + "_edit").style.visibility = "visible";
		sObj.after(addStr1 + " " + addStr2 + " " + addStr3);
		
		document.getElementById("nextBtnLocation").setAttribute("type", "button");
	    document.getElementById("nextBtnLocationType").setAttribute("type", "button");
	    document.getElementById("nextBtnLocationAttributeType").setAttribute("type", "button");
	    document.getElementById("nextBtnLocationAttribute").setAttribute("type", "button");
	    
		document.getElementById("nextBtnLocation").style.visibility = "visible";
		document.getElementById("nextBtnLocationType").style.visibility = "visible";
		document.getElementById("nextBtnLocationAttributeType").style.visibility = "visible";
		document.getElementById("nextBtnLocationAttribute").style.visibility = "visible";
	}
	else {
		var sObj = $("#" + treeNode.tId + "_span");
	    var id3 = "voidLocationDetail";
	    if (treeNode.editNameFlag || $("#voidLocationBtn_"+treeNode.tId).length>0) return;
	    var addStr3 = "<button type='button' id='voidLocationBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Void Location' data-toggle='modal' data-target='#modalVoidLocationDetail' data-backdrop='static' data-keyboard='false' onclick='setFocus(\""+id3+"\");' ><img src='images/void.png' width='15px' height='15px'></button>";
	    
	    var tId = null; var id = null; var name = null;
		tId = treeNode.tId;
		name = treeNode.name;
		typeName = treeNode.typeName;
		document.getElementById(tId + "_a").title = name + "-[" + typeName + "]";
		document.getElementById(treeNode.tId + "_edit").title = "Edit Location Name";
		document.getElementById(treeNode.tId + "_edit").style.visibility = "hidden";
		sObj.after(addStr3);
	}
};
function removeHoverDom(treeId, treeNode) {
    $("#addNodeBtn_"+treeNode.tId).unbind().remove();
    $("#addStatBtn_"+treeNode.tId).unbind().remove();
    $("#voidLocationBtn_"+treeNode.tId).unbind().remove();
};
function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
}
function onClick(e, treeId, treeNode) {
	if(treeNode != null) {
		var tag1 = "addlocation";
		var parentDiv = document.getElementById("addParentLocationListDiv");
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "addParent");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Parent Name:"
		parentDiv.appendChild(label);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "hidden");  
		parentInput.setAttribute("name", "addParent");
		parentInput.setAttribute("id", "addParent");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Choose Parent Location");
		parentInput.setAttribute("placeholder", "Parent Location");
		parentInput.setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
		parentDiv.appendChild(parentInput);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "text");  
		parentInput.setAttribute("name", "addParentName");
		parentInput.setAttribute("id", "addParentName");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Choose Parent Location");
		parentInput.setAttribute("placeholder", "Parent Location");
		parentInput.setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
		parentDiv.appendChild(parentInput);

		var nameHtml = "<select id='addType' name='addType' class='form-control myclass' title='Choose Location Type' onfocus='removeFocus(\""+tag1+"\");'><option value=''></option>";
    	var idArray = [];
    	var nameArray = [];
    	var tempArray = [];
    	for (var i = 0; i < locationTypeNodes.length; i++) {
    		idArray.push(locationTypeNodes[i].id);
    		nameArray.push(locationTypeNodes[i].name);
    		tempArray.push(capitalize(locationTypeNodes[i].name));
    	} tempArray.sort();
    	for (var i = 0; i < tempArray.length; i++) {
    		var name; var id;
    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; } }
    		nameHtml += '<option value="' + id +'">' + name + '</option>'; 
    	} nameHtml += "</select>";
		document.getElementById("addLocationTypeListDiv").innerHTML = nameHtml;

		
		var tag2 = "addlocationattribute";
		var parentDiv = document.getElementById("addParentLocationAttributeListDiv");
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "addParent");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Parent Name:"
		parentDiv.appendChild(label);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "hidden");  
		parentInput.setAttribute("name", "locationParent");
		parentInput.setAttribute("id", "locationParent");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Choose Parent Location");
		parentInput.setAttribute("placeholder", "Parent Location");
		parentInput.setAttribute("onfocus", "removeFocus(\'"+tag2+"\');");
		parentDiv.appendChild(parentInput);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "text");  
		parentInput.setAttribute("name", "locationParentName");
		parentInput.setAttribute("id", "locationParentName");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Choose Parent Location");
		parentInput.setAttribute("placeholder", "Parent Location");
		parentInput.setAttribute("onfocus", "removeFocus(\'"+tag2+"\');");
		parentDiv.appendChild(parentInput);

		document.getElementById("locationParent").value = treeNode.id;
		document.getElementById("addParent").value = treeNode.id;
		parentName = treeNode.name;
		
		
		var tag3 = "voidLocationDetail";
		var parentDiv = document.getElementById("voidLocationDiv");
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "voidLocation");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Location Name:"
		parentDiv.appendChild(label);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "hidden");  
		parentInput.setAttribute("name", "voidLocation");
		parentInput.setAttribute("id", "voidLocation");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Void Location");
		parentInput.setAttribute("placeholder", "Void Location");
		parentInput.setAttribute("onfocus", "setFocus(\'"+tag3+"\');");
		parentDiv.appendChild(parentInput);
		var parentInput = document.createElement('input');
		parentInput.setAttribute("type", "text");  
		parentInput.setAttribute("name", "voidLocationName");
		parentInput.setAttribute("id", "voidLocationName");
		parentInput.setAttribute("readonly", "readonly");
		parentInput.setAttribute("class", "form-control");
		parentInput.setAttribute("value", "");
		parentInput.setAttribute("title", "Void Location");
		parentInput.setAttribute("placeholder", "Void Location");
		parentInput.setAttribute("onfocus", "setFocus(\'"+tag3+"\');");
		parentDiv.appendChild(parentInput);

		document.getElementById("addParent").value = treeNode.id;
		document.getElementById("locationParent").value = treeNode.id;
		document.getElementById("voidLocation").value = treeNode.id;
		
		document.getElementById("addParentName").value = treeNode.name;
		document.getElementById("locationParentName").value = treeNode.name;
		document.getElementById("voidLocationName").value = treeNode.name;
		document.getElementById("voidReason").value = treeNode.voidReason;
		
		document.getElementById("voided").value = treeNode.voided;
		document.getElementById("modalVoidLocationDetailHead").innerHTML = "Void Location";

		parentName = treeNode.name;
	}
    if(jQuery.isEmptyObject(treeNode.children) !== true) { GenerateTable(treeNode.children, treeNode.name, treeNode.id); }
    else { document.getElementById("dvTable").innerHTML = "<h2>Child Locations of '" + "<a style='cursor:pointer' title='View Details of " + treeNode.name + "' onclick='viewLocationDetails(" + JSON.stringify(treeNode.id) + ");'>" + treeNode.name + "</a>" + "'</h2><br/><br/><p>&emsp;No Child Locations associated with '"+treeNode.name+"'.</p>"; }
}
function addAll(id) {
    var name = document.getElementById("addName").value;
    var shortName = document.getElementById("addShortName").value;
    var fullName = document.getElementById("addFullName").value;
    var parent = document.getElementById("addParent").value;
    var description = document.getElementById("addDescription").value;
    var otherIdentifier = document.getElementById("addOtherIdentifier").value;
    var type = document.getElementById("addType").value;
    var typeName = document.getElementById("addType").options[document.getElementById("addType").selectedIndex].text;
    var latitude = document.getElementById("addLatitude").value;
    var longitude = document.getElementById("addLongitude").value;    
    var node;
    if(id != "" && name != "" && type != "") {
        node = { id: id, name: name, shortName: shortName, fullName: fullName, pId: parent, pName: parentName, description: description, otherIdentifier: otherIdentifier, type: type, typeName: typeName, latitude: latitude, longitude: longitude, open:true};
        document.getElementById("errors").innerHTML = "";
        document.getElementById("addName").value = "";
        document.getElementById("addShortName").value = "";
        document.getElementById("addFullName").value = "";
        document.getElementById("addParent").value = "";
        document.getElementById("addDescription").value = "";
        document.getElementById("addOtherIdentifier").value = "";
        document.getElementById("addType").value = "";
        document.getElementById("addLatitude").value = "";
        document.getElementById("addLongitude").value = "";
    	zNodes.push(node);
	    addToTree(node);
	    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    }
}
function GenerateTable(myNodes, parentName, parentId) {
	var headerArray = [];
    var editArray = [];
    var idArray = [];
    var nameArray = [];
    var shortNameArray = [];
    var fullNameArray = [];
    var pIdArray = [];
    var pNameArray = [];
    var descriptionArray = [];
    var otherIdentifierArray = [];
    var typeArray = [];
    var typeNameArray = [];
    
    var i=0;
	if(parentName === "VOIDED LOCATIONS") { 
		headerArray = ["Name", "Short Name", "Full Name", "Parent Name", "Other Identifier", "Location Type", "Description"]; 
	    while(myNodes[i] !== undefined) {
	        idArray.push(myNodes[i].id);
	        nameArray.push(myNodes[i].name);
	        shortNameArray.push(myNodes[i].shortName);
	        fullNameArray.push(myNodes[i].fullName);
	        pIdArray.push(myNodes[i].pId);
	        pNameArray.push(parentName);
	        descriptionArray.push(myNodes[i].description);
	        otherIdentifierArray.push(myNodes[i].otherIdentifier);
	        typeArray.push(myNodes[i].type);
	        typeNameArray.push(myNodes[i].typeName);
	        i++;
	    }
	} 
	else { 
		headerArray = ["", "Name", "Short Name", "Full Name", "Parent Name", "Other Identifier", "Location Type", "Description"]; 
	    while(myNodes[i] !== undefined) {
	    	editArray.push("<a name='editBtn' id='editBtn' class='icon' style='cursor:pointer;'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>");
	        idArray.push(myNodes[i].id);
	        nameArray.push(myNodes[i].name);
	        shortNameArray.push(myNodes[i].shortName);
	        fullNameArray.push(myNodes[i].fullName);
	        pIdArray.push(myNodes[i].pId);
	        pNameArray.push(parentName);
	        descriptionArray.push(myNodes[i].description);
	        otherIdentifierArray.push(myNodes[i].otherIdentifier);
	        typeArray.push(myNodes[i].type);
	        typeNameArray.push(myNodes[i].typeName);
	        i++;
	    }
	}
    var arr = [editArray.length, idArray.length, nameArray.length, shortNameArray.length, fullNameArray.length, pIdArray.length, pNameArray.length, descriptionArray.length, otherIdentifierArray.length, typeArray.length, typeNameArray.length];
    arr = arr.sort();
    arr = arr.reverse();
    var rowsAfterHeader = arr[0];
    var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "datatable";
//    table.width = '80%';
    var row = table.insertRow(-1);
    row.id = "row_h";
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i];
        row.appendChild(headerCell);
    }
    var count=0;
    for (var i = 0; i < rowsAfterHeader; i++) {
    	row = table.insertRow(-1);
        row.id = "row"+i;

        if(parentName === "VOIDED LOCATIONS") { }
        else {
            var cell = row.insertCell(-1); var id = "row"+i+"_"+count;
            cell.innerHTML = "<a name='editBtn' id='editBtn' class='icon' title='Update "+nameArray[i]+"' onclick='editTableRow(\""+id+"\");' style='cursor:pointer;'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";
            cell.id= "row"+i+"_"+count; count++;
        } var val = null; val = idArray[i];
        
        var cell = row.insertCell(-1);
        if(nameArray[i] !== undefined) { cell.innerHTML = "<a id='row"+i+"_link' name='row"+i+"_link' style='cursor:pointer' title='View Details of "+nameArray[i]+"' onclick='viewLocationDetails("+JSON.stringify(val)+");'>" + nameArray[i] + "</a> "; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(shortNameArray[i] !== undefined) { cell.innerHTML = shortNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(fullNameArray[i] !== undefined) { cell.innerHTML = fullNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(pNameArray[i] !== undefined) { cell.innerHTML = pNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(otherIdentifierArray[i] !== undefined) { cell.innerHTML = otherIdentifierArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(typeNameArray[i] !== undefined) { cell.innerHTML = typeNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(descriptionArray[i] !== undefined) { cell.innerHTML = descriptionArray[i]; }
        else { cell.innerHTML = ""; }
    }
    
	document.getElementById("dvTable").innerHTML = "<h2>Child Locations of '" + "<a style='cursor:pointer' title='View Details of " + parentName + "' onclick='viewLocationDetails(" + JSON.stringify(parentId) + ");'>" + parentName + "</a>" + "'</h2>"; 
    document.getElementById("dvTable").appendChild(table);
}
function addToTree(node) {
	if(document.getElementById("datatable") == null) {}
	else {
		var i = document.getElementById("datatable").rows.length;
		var table = document.getElementById("datatable");
		var row = table.insertRow(-1);
	    row.id = "row"+i;
        var id = "row"+i+"_"+count;
		var edit = "<a name='editBtn' id='editBtn' class='icon' title='Update "+node.name+"' onclick='editTableRow(\""+id+"\");' style='cursor:pointer;'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";

		var cell = row.insertCell(-1);
	    cell.id= "row"+i+"_"+count; count++;
	    cell.innerHTML = edit;

	    var html = "<a id='row"+i+"_link' name='row"+i+"_link' style='cursor:pointer' title='Update "+node.name+"'onclick='viewLocationDetails("+JSON.stringify(node.id)+");'>" + node.name + "</a>"; 

	    var cell = row.insertCell(-1);
	    if(node.name !== undefined) { cell.innerHTML = html; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.shortName !== undefined) { cell.innerHTML = node.shortName; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.fullName !== undefined) { cell.innerHTML = node.fullName; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.otherIdentifier !== undefined) { cell.innerHTML = node.otherIdentifier; }
	    else { cell.innerHTML = ""; }
	    
	    var cell = row.insertCell(-1);
	    if(node.typeName !== undefined) { cell.innerHTML = node.typeName; }
	    else { cell.innerHTML = ""; }

	    var cell = row.insertCell(-1);
	    if(node.description !== undefined) { cell.innerHTML = node.description; }
	    else { cell.innerHTML = ""; }
	}
}
function addToModal(id) {
	var tag1 = "addlocationattribute";
	var bodyDiv = document.getElementById("myLocationAttributeAdd");
	var div = document.createElement("div");
    div.setAttribute("class", "form-group");

    var descriptionLabel = document.createElement('label');
    descriptionLabel.innerHTML = document.getElementById("addlocationAttributeTypeDisplayName").value + ":";
    descriptionLabel.setAttribute("for", "locationAttribute_"+id);
    descriptionLabel.setAttribute("class", "form-control-label");
    div.appendChild(descriptionLabel);

    var descriptionValue = document.createElement('input');
    descriptionValue.setAttribute("type", "number");
    descriptionValue.setAttribute("name", "locationAttribute_"+id);
    descriptionValue.setAttribute("id", "locationAttribute_"+id);
    descriptionValue.setAttribute("class", "form-control");
    descriptionValue.setAttribute("value", "");
    descriptionValue.setAttribute("title", "Enter "+document.getElementById("addlocationAttributeTypeDisplayName").value+", Any 255 Characters");
    descriptionValue.setAttribute("placeholder", document.getElementById("addlocationAttributeTypeDisplayName").value);
    descriptionValue.setAttribute("maxlength", "255");
    descriptionValue.setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
    div.appendChild(descriptionValue);

    bodyDiv.appendChild(div);
    
    var radioHTML = "";
    radioHTML += '<div id="bound_'+locationAttributeId[i]+'_RADIO" class="form-group">';
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="None" checked>&nbsp;None&nbsp;';//+locationAttributeId[i]+
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Anually">&nbsp;Anually&nbsp;';
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
    radioHTML += '<input type="radio" id="bound_'+locationAttributeId[i]+'" name="bound_'+locationAttributeId[i]+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
    radioHTML += '</div>'
    radioHTML += '<div id="bound_'+locationAttributeId[i]+'_DIV"></div>';
    
    bodyDiv.innerHTML += radioHTML;
	
	document.getElementById("addlocationAttributeTypeName").value = "";
	document.getElementById("addlocationAttributeTypeDisplayName").value = "";
	document.getElementById("addlocationAttributeTypeDescription").value = "";
	document.getElementById("addlocationAttributeTypeCategory").value = "";
}
function editTableCell(cell) { cell.contentEditable = false; }
function editTableRow(cellId) {
    var rowId = cellId.substr(0, cellId.indexOf("_"));
    document.getElementById(cellId).innerHTML = "<a name='saveBtn' id='saveBtn' title='Save' onclick='saveTableRow();' class='icon' style='cursor:pointer;'><img alt='edit' src='images/tick.png' class='icon' width='15px' height='15px' ></a>";
    for (var i=0; i<document.getElementById(rowId).cells.length; i++) {
	  	if(i===0) { }
	  	else if(i===1) { }
	  	else if(i===4) { 
	  		var html = "<select id='mySelectParent_"+rowId+"' name='mySelectParent_"+rowId+"' class='form-control myclass' title='Choose Parent Location' >";
	  		var idArray = [];
	  		var nameArray = [];
	  		var tempArray = [];	  		
	  		if(!jQuery.isEmptyObject(zNodes)) { 
		    	for (var j = 0; j < zNodes.length; j++) {
		    		idArray.push(zNodes[j].id);
		    		nameArray.push(zNodes[j].name);
		    		tempArray.push(capitalize(zNodes[j].name));
		    	} tempArray.sort(); tempArray = removeDuplicates(tempArray); tempArray.sort(); 
	  			for (var j = 0; j < tempArray.length; j++) {
	  				var name; var id;
	  				for (var k = 0; k < nameArray.length; k++) { if(capitalize(nameArray[k])===tempArray[j]) { id = idArray[k]; name = nameArray[k]; } }
	  				if(name === document.getElementById(rowId).cells[i].innerHTML) { html += '<option value="' + id +'" selected>' + name + '</option>'; } 
	  				else { html += '<option value="' + id +'">' + name + '</option>'; }
  				}
			} else { html += '<option value=""></option>'; } html += "</select>";
			document.getElementById(rowId).cells[i].innerHTML = html;
	  		//document.getElementById(rowId).cells[i].contentEditable = true;
	  		//document.getElementById(rowId).cells[i].style.padding = "10px";
	  		document.getElementById(rowId).cells[i].style.backgroundColor = "#f8fbff"; 
	  		document.getElementById(rowId).cells[i].style.border = "2px solid #428bca";
	  	} 
	  	else if(i===6) { 
	  		var html = "<select id='mySelect_"+rowId+"' name='mySelect_"+rowId+"' class='form-control myclass' title='Choose Location Type' >";
	  		var idArray = [];
	  		var nameArray = [];
	  		var tempArray = [];
	  		if(!jQuery.isEmptyObject(locationTypeNodes)) { 
		  		for (var j = 0; j < locationTypeNodes.length; j++) {
		  			idArray.push(locationTypeNodes[j].id);
		  			nameArray.push(locationTypeNodes[j].name);
		  			tempArray.push(capitalize(locationTypeNodes[j].name));
	  			} tempArray.sort();
	  			for (var j = 0; j < tempArray.length; j++) {
	  				var name; var id;
	  				for (var k = 0; k < nameArray.length; k++) { if(capitalize(nameArray[k])===tempArray[j]) { id = idArray[k]; name = nameArray[k]; } }
					if(name === document.getElementById(rowId).cells[i].innerHTML) { html += '<option value="' + id +'" selected>' + name + '</option>'; } 
		  			else { html += '<option value="' + id +'">' + name + '</option>'; } 
  				}
			} else { html += '<option value=""></option>'; } html += "</select>";
			document.getElementById(rowId).cells[i].innerHTML = html;
	  		//document.getElementById(rowId).cells[i].contentEditable = true;
	  		//document.getElementById(rowId).cells[i].style.padding = "10px";
	  		document.getElementById(rowId).cells[i].style.backgroundColor = "#f8fbff"; 
	  		document.getElementById(rowId).cells[i].style.border = "2px solid #428bca";
	  	} 
	  	else {
	  		document.getElementById(rowId).cells[i].contentEditable = true;
	  		document.getElementById(rowId).cells[i].style.backgroundColor = "#f8fbff"; 
	  		document.getElementById(rowId).cells[i].style.padding = "10px";
	  		document.getElementById(rowId).cells[i].style.border = "2px solid #428bca";
  		}
	} globalRowId = rowId; globalCellId = cellId;
}
function saveTableRow() {
	var headerArray = ["", "Name", "Short Name", "Full Name", "Parent Name", "Other Identifier", "Location Type", "Description"];
	var txt;
    var r = confirm("Would you like to save it?");
    if (r === true) {
    	var name = document.getElementById(globalRowId+"_link").innerHTML;
    	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		var node = treeObj.getNodeByParam('name', name);
		var nodeName = null;
		nodeName = node.name;
		if(nodeName === name) { 
			for (var i = 0; i < headerArray.length; i++) {
			  	if(i === 0) { }
			  	else if(i === 1) { }
			  	else if(i === 2) { node.shortName = document.getElementById(globalRowId).cells[i].innerHTML; }
			  	else if(i === 3) { node.fullName = document.getElementById(globalRowId).cells[i].innerHTML; }
			  	else if(i === 4) { 
			  		node.pId = document.getElementById("mySelectParent_"+globalRowId).value; 
			  		node.pName = document.getElementById("mySelectParent_"+globalRowId).options[document.getElementById("mySelectParent_"+globalRowId).selectedIndex].text;
			  	}
			  	else if(i === 5) { node.otherIdentifier = document.getElementById(globalRowId).cells[i].innerHTML; }
			  	else if(i === 6) { 
			  		node.type = document.getElementById("mySelect_"+globalRowId).value; 
			  		node.typeName = document.getElementById("mySelect_"+globalRowId).options[document.getElementById("mySelect_"+globalRowId).selectedIndex].text;
			  	}
			  	else if(i === 7) { node.description = document.getElementById(globalRowId).cells[i].innerHTML;}
			  	else { }
		  	}
		} treeObj.updateNode(node);
		var id = null;
		var name = null;
		var shortName = null;
		var fullName = null;
		var pId = null;
		var pName = null;
		var description = null;
		var otherIdentifier = null;
		var type = null;
		var typeName = null;
		var latitude = null;
		var longitude = null;
		
		id = node.id;
		name = node.name;
		shortName = node.shortName;
		fullName = node.fullName;
		pId = node.pId;
		pName = node.pName;
		description = node.description;
		otherIdentifier = node.otherIdentifier;
		type = node.type;
		typeName = node.typeName;
		latitude = node.latitude;
		longitude = node.longitude;
		
		var pattern = new RegExp("^[a-zA-Z0-9.-_ ]*$");
		var pattern2 = new RegExp("-[0-9]*.[0-9]*$");
		var errorStr = "";
		if(name === "") { errorStr += "Location Name is required.<br/>"; }
		if(shortName === "") { errorStr += "Location Short Name is required.<br/>"; }
		if(fullName === "") { errorStr += "Location Full Name is required.<br/>"; }
		//if(pId === "") { errorStr += "Location Parent is required.<br/>"; }	
		if(type === "") { errorStr += "Location Type is required."; }	
		if(!(name.length >= 3)) { errorStr += "Location Name must have atleast 3 characters.<br/>"; }
		if(!(name.length <= 30)) { errorStr += "Location Name must have atmost 30 characters.<br/>"; }
		if(!(pattern.test(name))) { errorStr += "Location Name must have alphabets only.<br/>"; }
		if(!(shortName.length >= 3)) { errorStr += "Location Short Name must have atleast 3 characters.<br/>"; }
		if(!(shortName.length <= 30)) { errorStr += "Location Short Name must have atmost 30 characters.<br/>"; }
		if(!(pattern.test(shortName))) { errorStr += "Location Short Name must have alphabets only.<br/>"; }
		if(!(fullName.length >= 3)) { errorStr += "Location Full Name must have atleast 3 characters.<br/>"; }
		if(!(fullName.length <= 50)) { errorStr += "Location Full Name must have atmost 30 characters.<br/>"; }
		if(!(pattern.test(fullName))) { errorStr += "Location Full Name must have alphabets only.<br/>"; }
		if(pattern2.test(otherIdentifier)) { errorStr += "Other Identifier must have alphabets or positive numbers only.<br/>"; }
		if(!(description.length <= 255)) { errorStr += "Location Description must have atmost 255 characters.<br/>"; }
		if(!(otherIdentifier.length <= 50)) { errorStr += "Location Other Identifier must have atmost 255 characters.<br/>"; }
		if(!(latitude.length <= 255)) { errorStr += "Location Latitude must have atmost 255 characters.<br/>"; }
		if(!(longitude.length <= 255)) { errorStr += "Location Longitude must have atmost 255 characters.<br/>"; }

		if(errorStr !== "") { alertDIV("warning", "ERROR: ", errorStr); }
		else {
			document.getElementById(globalRowId).cells[0].innerHTML = "<a name='editBtn' id='editBtn' class='icon'  title='Update "+name+"' onclick='editTableRow(\""+globalCellId+"\");' style='cursor:pointer;'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";
			for (var i = 0; i < headerArray.length; i++) {
				if(i === 6) { document.getElementById(globalRowId).cells[i].innerHTML = document.getElementById("mySelect_"+globalRowId).options[document.getElementById("mySelect_"+globalRowId).selectedIndex].text; }
				document.getElementById(globalRowId).cells[i].contentEditable = false;
			  	document.getElementById(globalRowId).cells[i].style.backgroundColor = "";
			  	document.getElementById(globalRowId).cells[i].style.border = "";
			  	document.getElementById(globalRowId).cells[i].style.padding = "";
			}
			$.ajax({
				url: "/LocationDatabaseManagement/data/location/updatelocation",
				type: "POST",
				data : { name: name, shortName: shortName, fullName: fullName, pName: pName, description: description, otherIdentifier: otherIdentifier, typeName: typeName, latitude: latitude, longitude: longitude },
				success : function(result) {
					var data = JSON.parse(result);
					var zNodesData = eval(data.locationNodes);
					zNodes = zNodesData;
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
					var treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
					var node = treeObj.getNodeByParam('id', (eval(data.lastPid)));
					if(jQuery.isEmptyObject(node.children) !== true) { GenerateTable(node.children, node.name, node.id); }
				    else { document.getElementById("dvTable").innerHTML = "<h2>Child Locations of '" + "<a style='cursor:pointer' title='View Details of " + treeNode.name + "' onclick='viewLocationDetails(" + JSON.stringify(treeNode.id) + ");'>" + treeNode.name + "</a>" + "'</h2><br/><br/><p>&emsp;No Child Locations associated with '"+treeNode.name+"'.</p>"; }
					alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
				}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING CHILD LOCATIONS"); /*$("#alertModalBody").html("ERROR OCCURED WHILE UPDATING CHILD LOCATIONS"); $("#alertModal").modal('show');*/ }
			});
		}
    } else { }
}
function removeDuplicates(arr){
    var tmp = [];
    for(var i = 0; i < arr.length; i++){ if(tmp.indexOf(arr[i]) == -1) { tmp.push(arr[i]); } }
    return tmp;
}
function remove(array, value) {
	var newArray = [];
    for(var i=0; i<array.length; i++) { if(array[i]==value) {} else { newArray.push(array[i]); } }
    return newArray;
}
function openModals(id) {
	if(id === 'addLocationBtn') { 
		document.getElementById("nextBtnLocation").setAttribute("type", "hidden"); 
		var parentDiv = document.getElementById("addParentLocationListDiv");
		var tag1 = "addlocation";
		parentDiv.innerHTML = ""; 
		var label = document.createElement("LABEL");
		label.setAttribute("for", "addParent");
		label.setAttribute("class", "form-control-label");
		label.innerHTML = "Parent Name:"
		parentDiv.appendChild(label);
		var parentSelect = document.createElement("SELECT");
		parentSelect.setAttribute("id", "addParent");
		parentSelect.setAttribute("name", "addParent");
		parentSelect.setAttribute("class", "form-control myclass");
		parentSelect.setAttribute("title", "Choose Parent");
		parentSelect.setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");

		parentDiv.appendChild(parentSelect);
	    var parentOption = document.createElement("option");
	    parentOption.setAttribute("value", "");
	    document.getElementById("addParent").appendChild(parentOption);
	    if(!jQuery.isEmptyObject(zNodes)) { 
	    	var idArray = [];
	    	var nameArray = [];
	    	var tempArray = [];
	    	for (var i = 0; i < zNodes.length; i++) {
	    		idArray.push(zNodes[i].id);
	    		nameArray.push(zNodes[i].name);
	    		tempArray.push(capitalize(zNodes[i].name));
	    	} tempArray.sort();
	    	for (var i = 0; i < tempArray.length; i++) {
	    		var name; var id;
	    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; } }
	    		var parentOption = document.createElement("option");
			    parentOption.setAttribute("value", id);
			    var parentOptionText = document.createTextNode(name);
			    parentOption.appendChild(parentOptionText);
			    document.getElementById("addParent").appendChild(parentOption);
			}
	    }
        var nameHtml = "<select id='addType' name='addType' class='form-control myclass' title='Choose Location Type' onfocus='removeFocus(\""+tag1+"\");' ><option value=''></option>";
	    if(!jQuery.isEmptyObject(locationTypeNodes)) { 
	    	var idArray = [];
	    	var nameArray = [];
	    	var tempArray = [];
	    	for (var i = 0; i < locationTypeNodes.length; i++) {
	    		idArray.push(locationTypeNodes[i].id);
	    		nameArray.push(locationTypeNodes[i].name);
	    		tempArray.push(capitalize(locationTypeNodes[i].name));
	    	} tempArray.sort();
	    	for (var i = 0; i < tempArray.length; i++) {
	    		var name; var id;
	    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; } }
	    		nameHtml += '<option value="' + id +'">' + name + '</option>'; 
	    	} nameHtml += "</select>";
			document.getElementById("addLocationTypeListDiv").innerHTML = nameHtml;
		} setFocus('addlocation');
	}
	else if(id === 'addLocationAttributeBtn') { 
	    if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
			document.getElementById("nextBtnLocationAttribute").setAttribute("type", "hidden"); 
			document.getElementById("myLocationAttributeAdd").style.height = "420px"; 
			var tag1 = "addlocationattribute";
			var parentDiv = document.getElementById("addParentLocationAttributeListDiv");
			parentDiv.innerHTML = ""; 
			var label = document.createElement("LABEL");
			label.setAttribute("for", "addParent");
			label.setAttribute("class", "form-control-label");
			label.innerHTML = "Parent Name: *"
			parentDiv.appendChild(label);
			var parentSelect = document.createElement("SELECT");
			parentSelect.setAttribute("id", "locationParent");
			parentSelect.setAttribute("name", "locationParent");
			parentSelect.setAttribute("class", "form-control myclass");
			parentSelect.setAttribute("onchange", "locationAttributeParentChange()");
			parentSelect.setAttribute("onfocus", "setFocus(\'"+tag1+"\');");
			parentSelect.setAttribute("title", "Choose Parent");		
			parentDiv.appendChild(parentSelect);
			var parentOption = document.createElement("option");
		    parentOption.setAttribute("value", "");
		    document.getElementById("locationParent").appendChild(parentOption);
		    if(!jQuery.isEmptyObject(zNodes)) { 
		    	var idArray = [];
		    	var nameArray = [];
		    	var tempArray = [];
		    	for (var i = 0; i < zNodes.length; i++) {
		    		idArray.push(zNodes[i].id);
		    		nameArray.push(zNodes[i].name);
		    		tempArray.push(capitalize(zNodes[i].name));
		    	} tempArray.sort();
		    	for (var i = 0; i < tempArray.length; i++) {
		    		var name; var id;
		    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; } }
		    		var parentOption = document.createElement("option");
				    parentOption.setAttribute("value", id);
				    var parentOptionText = document.createTextNode(name);
				    parentOption.appendChild(parentOptionText);
				    document.getElementById("locationParent").appendChild(parentOption);
		    	}
		    } setFocus('addlocationattribute');
	    }
		else { 
	    	document.getElementById("myLocationAttributeAdd").style.height = "105px"; 
			document.getElementById("myAlertDIV1_addLocationAttribute").innerHTML = "";
			document.getElementById("myAlertDIV1_addLocationAttribute").innerHTML = "<div class='alert alert-"+"info"+" alert-dismissable' style='margin-bottom: 0px; padding: 5px;'><a href='#' class='close' data-dismiss='alert' aria-label='close' style=' right: 0; top: 0;' title='Close' >&times;</a><strong>"+"INFO: "+"</strong>"+"PLEASE CREATE ATTRIBUTES FIRST."+"</div>"; 
			var tag1 = "addlocationattributebutton";
			var parentDiv = document.getElementById("addParentLocationAttributeListDiv");
			parentDiv.innerHTML = ""; 
			var label = document.createElement("LABEL");
			label.setAttribute("for", "addParent");
			label.setAttribute("class", "form-control-label");
			label.innerHTML = "Parent Name: *"
			parentDiv.appendChild(label);
			var parentSelect = document.createElement("SELECT");
			parentSelect.setAttribute("id", "locationParent");
			parentSelect.setAttribute("name", "locationParent");
			parentSelect.setAttribute("class", "form-control myclass");
			parentSelect.setAttribute("onchange", "locationAttributeParentChange()");
			parentSelect.setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
			parentSelect.setAttribute("title", "Choose Parent");		
			parentDiv.appendChild(parentSelect);
			var parentOption = document.createElement("option");
		    parentOption.setAttribute("value", "");
		    document.getElementById("locationParent").appendChild(parentOption);
		    if(!jQuery.isEmptyObject(zNodes)) { 
		    	var idArray = [];
		    	var nameArray = [];
		    	var tempArray = [];
		    	for (var i = 0; i < zNodes.length; i++) {
		    		idArray.push(zNodes[i].id);
		    		nameArray.push(zNodes[i].name);
		    		tempArray.push(zNodes[i].name);
		    	} tempArray.sort();
		    	for (var i = 0; i < tempArray.length; i++) {
		    		var name = tempArray[i];
		    		var id = idArray[nameArray.indexOf(tempArray[i])];
		    		var parentOption = document.createElement("option");
				    parentOption.setAttribute("value", id);
				    var parentOptionText = document.createTextNode(name);
				    parentOption.appendChild(parentOptionText);
				    document.getElementById("locationParent").appendChild(parentOption);
		    	}
		    } setFocus('addlocationattributebutton');
			document.getElementById("closeBtnLocationAttribute").setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
			document.getElementById("addAlllocationAttributes").setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
			document.getElementById("myAlertDIV1_addLocationAttribute").setAttribute("onclick", "removeFocus(\'"+tag1+"\');");
		}
	}
	else if(id === 'addLocationTypeBtn') { document.getElementById("nextBtnLocationType").setAttribute("type", "hidden"); setFocus('addlocationtype'); }
	else if(id === 'addLocationAttributeTypeBtn') { document.getElementById("nextBtnLocationAttributeType").setAttribute("type", "hidden"); setFocus('addlocationattributetype'); }
	else if(id === 'viewLocationTypeBtn') { viewLocationTypeDetails(); setFocus('viewlocationtype'); }
	else if(id === 'viewLocationAttributeTypeBtn') { viewLocationAttributeTypeDetails(); setFocus('viewlocationattributetype'); }
	else if(id === 'updateLocationTypeBtn') { 
		if(!jQuery.isEmptyObject(locationTypeNodes)) { 
			var tag1 = "updatelocationtype";
			var nameHtml = "<select id='updatelocationTypeName' name='updatelocationTypeName' class='form-control myclass' onchange='updateLocationTypeChange()' onfocus='setFocus(\""+tag1+"\");' title='Choose Location Type'>";
	    	var idArray = [];
	    	var nameArray = [];
	    	var levelArray = [];
	    	var descriptionArray = [];
	    	var tempArray = [];
	    	for (var i = 0; i < locationTypeNodes.length; i++) {
	    		idArray.push(locationTypeNodes[i].id);
	    		nameArray.push(locationTypeNodes[i].name);
	    		tempArray.push(capitalize(locationTypeNodes[i].name));
	    		levelArray.push(locationTypeNodes[i].level);
	    		descriptionArray.push(locationTypeNodes[i].description);
	    	} tempArray.sort();
	    	for (var i = 0; i < tempArray.length; i++) {
	    		var name; var id; var level; var description;
	    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; level = levelArray[j]; description = descriptionArray[j]; } }
				if(id === 1) {
					nameHtml += '<option value="' + id +'" selected>' + name + '</option>'; 
					document.getElementById("updatelocationTypeLevel").value = level; document.getElementById("updatelocationTypeLevel").innerHTML = level;
					document.getElementById("updatelocationTypeDescription").value = description; document.getElementById("updatelocationTypeDescription").innerHTML = description;
				} else { nameHtml += '<option value="' + id +'">' + name + '</option>'; }
	    	} nameHtml += "</select>";
	    	nameHtml += "<a name='renameBtn' id='renameBtn' class='icon' title='Edit Name' style='cursor:pointer;' onclick='updateName(\""+tag1+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' style='width: 30px; float: right; position: relative; top: -30px;'></a>";
	    	document.getElementById("updatelocationTypeDiv").innerHTML = nameHtml;
		}
		else {
			var nameHtml = "<select id='updatelocationTypeName' name='updatelocationTypeName' class='form-control myclass' onchange='updateLocationTypeChange()' onfocus='setFocus(\""+tag1+"\");' title='Choose Location Type' ><option value=''></option></select>";
			document.getElementById("updatelocationTypeDiv").innerHTML = nameHtml; 
		} setFocus('updatelocationtype');
	}
	else if(id === 'updateLocationAttributeTypeBtn') { 
		if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
			var tag1 = "updatelocationattributetype";
			var nameHtml = "<select id='updatelocationAttributeTypeName' name='updatelocationAttributeTypeName' class='form-control myclass' onchange='updateLocationAttributeTypeChange()' onfocus='setFocus(\""+tag1+"\");' title='Choose Location Attribute Type' >";
			var idArray = [];
	    	var nameArray = [];
	    	var displayNameArray = [];
	    	var descriptionArray = [];
	    	var categoryArray = [];
	    	var tempArray = [];
	    	for (var i = 0; i < locationAttributeNodes.length; i++) {
	    		idArray.push(locationAttributeNodes[i].id);
	    		nameArray.push(locationAttributeNodes[i].name);
	    		tempArray.push(capitalize(locationAttributeNodes[i].name));
	    		displayNameArray.push(locationAttributeNodes[i].displayName);
	    		descriptionArray.push(locationAttributeNodes[i].description);
	    		categoryArray.push(locationAttributeNodes[i].category);
	    	} tempArray.sort();
	    	for (var i = 0; i < tempArray.length; i++) {
	    		var name; var id; var displayName; var description; var category;
	    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { id = idArray[j]; name = nameArray[j]; displayName = displayNameArray[j]; description = descriptionArray[j]; category = categoryArray[j]; } }
				if(id === 1) {
					nameHtml += '<option value="' + id +'" selected>' + name + '</option>'; 
					document.getElementById("updatelocationAttributeTypeDisplayName").value = displayName; document.getElementById("updatelocationAttributeTypeDisplayName").innerHTML = displayName;
					document.getElementById("updatelocationAttributeTypeDescription").value = description; document.getElementById("updatelocationAttributeTypeDescription").innerHTML = description;
					document.getElementById("updatelocationAttributeTypeCategory").value = category; document.getElementById("updatelocationAttributeTypeCategory").innerHTML = category;
				}
				else { nameHtml += '<option value="' + id +'">' + name + '</option>'; }
			} nameHtml += "</select>";
	    	nameHtml += "<a name='renameBtn' id='renameBtn' class='icon' title='Edit Name' style='cursor:pointer;' onclick='updateName(\""+tag1+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' style='width: 30px; float: right; position: relative; top: -30px;'></a>";
			document.getElementById("updatelocationAttributeTypeDiv").innerHTML = nameHtml;
		}
		else {
			var nameHtml = "<select id='updatelocationAttributeTypeName' name='updatelocationAttributeTypeName' class='form-control myclass' onchange='updateLocationAttributeTypeChange()' onfocus='setFocus(\""+tag1+"\");' title='Choose Location Attribute Type' ><option value=''></option></select>";
			document.getElementById("updatelocationAttributeTypeDiv").innerHTML = nameHtml; 
		} setFocus('updatelocationattributetype');
	}
	else { }
}
function validateUnknownCols(csvData) {
	for(var i=0; i<csvData[0].length; i++) {
		if(headerCols.includes(csvData[0][i].trim().toLowerCase())) { }
		else if(i=== csvData[0].length-1) {
			if(headerCols.includes(csvData[0][i].trim().toLowerCase())) { }
			else { unknownCols.push(csvData[0][i].trim().toUpperCase()); }
		} else { unknownCols.push(csvData[0][i].trim().toUpperCase()); }
	}
	var str = "";
	for (var i = 0; i < unknownCols.length; i++) { str += unknownCols[i]; if(i !== unknownCols.length-1) { str += "<br/>"; } }
	if(unknownCols.length>0) { return (unknownCols.length + " UNKNOWN COLUMNS FOUND, I.E:-<br/>" + str); }	
	else { return ""; }
}
function validateRequiredNull(csvData) {
	var str = "";
	for(var i=0; i<csvData[0].length; i++) {
		if((requiredCols.includes(csvData[0][i].trim().toLowerCase()))  && ((csvData[0][i].trim().toLowerCase()) != "parent")) {//
			for(var j=1; j<csvData.length; j++) {
				if(csvData[j][i] === "") { csvData[j][i] = "NULL"; str += "EMPTY CELLS FOUND AT '" + csvData[0][i] + "' COLUMN"; }
			}
		}
	}
	return str;
}
function validateNameParent(csvData) {
	var nameArr = [];
	var parentArr = [];
	for(var i=0; i<csvData[0].length; i++) {
		if((requiredCols.includes(csvData[0][i])) && ((csvData[0][i]) === "name")) { for(var j=1; j<csvData.length; j++) { nameArr.push(csvData[j][i]); } }
		else if((requiredCols.includes(csvData[0][i])) && ((csvData[0][i]) === "parent")) { for(var j=1; j<csvData.length; j++) { parentArr.push(csvData[j][i]); } }
	}
	requiredColsData.push(nameArr);
	requiredColsData.push(parentArr);
	var str = "";
	if (typeof parentArr[0] === 'undefined') { return ""; }
	else {
		str += "NO LOCATION INFO FOUND FOR:-<br/>";
		for(var i=1; i<parentArr.length; i++) {
			if(nameArr.includes(parentArr[i])) { }
			else if(parentArr[i] === "NULL") { }
			else {  
				str += parentArr[i]; 
				if(i!= parentArr.length-1) { str += "<br/>"; } 
			}
		}
		return "";
	}
}
function validateTable() {
	var x = validateUnknownCols(csvData);
	var y = validateRequiredNull(csvData);
	var z = validateNameParent(csvData);
	if(x === "" && y === "" && z === "") { return csvData; }
	else {
		var str = "";
		str += "" + x + "<br/>";
		str += "<br/>" + y + "<br/>";
		str += "<br/>" + z + "";
		alertDIV("warning", "ERROR: ", str);
		return null;
	}
}
function validateTableForLocAttrs() {
	var x = validateUnknownColsForLocAttrs(csvDataForLocAttrs);
	var y = validateRequiredNullForLocAttrs(csvDataForLocAttrs);
//	var z = validateNameParent(csvData);
//	if(x === "" && y === "" && z === "") { return csvData; }
	if(x === "" && y === "" ) { return csvDataForLocAttrs; }
	else {
		var str = "";
		str += "" + x + "<br/>";
		str += "<br/>" + y + "<br/>";
//		str += "<br/>" + z + "";
//		$("#alertModalBody").html(str);
//		$("#alertModal").modal('show');
		alertDIV("warning", "ERROR: ", str);
		return null;
	}
}
function validateUnknownColsForLocAttrs(csvDataForLocAttrs) {
	for(var i=0; i<csvDataForLocAttrs[0].length; i++) {
		if(headerColsForLocAttrs.includes(csvDataForLocAttrs[0][i].trim().toLowerCase())) { }
		else if(i=== csvDataForLocAttrs[0].length-1) {
			if(headerColsForLocAttrs.includes(csvDataForLocAttrs[0][i].trim().toLowerCase())) { }
			else { unknownColsForLocAttrs.push(csvDataForLocAttrs[0][i].trim().toUpperCase()); }
		}
		else { unknownColsForLocAttrs.push(csvDataForLocAttrs[0][i].trim().toUpperCase()); }
	}
	var str = "";
	for (var i = 0; i < unknownColsForLocAttrs.length; i++) {
		str += unknownColsForLocAttrs[i];
		if(i !== unknownColsForLocAttrs.length-1) { str += "<br/>"; }
	}
	if(unknownColsForLocAttrs.length>0) { return (unknownColsForLocAttrs.length + " UNKNOWN COLUMNS FOUND, I.E:-<br/>" + str); }	
	else { return ""; }
}
function validateRequiredNullForLocAttrs(csvDataForLocAttrs) {
	var str = "";
	for(var i=0; i<csvDataForLocAttrs[0].length; i++) {
		if((requiredColsForLocAttrs.includes(csvDataForLocAttrs[0][i].trim().toLowerCase()))) {//
			for(var j=1; j<csvDataForLocAttrs.length; j++) {
				if(csvDataForLocAttrs[j][i] === "") {
					csvDataForLocAttrs[j][i] = "NULL";
					str += "EMPTY CELLS FOUND AT '" + csvDataForLocAttrs[0][i] + "' COLUMN<br/>";
				}
			}
		}
	}
	return str;
}
function openNav() {
	if(document.getElementById("mySidenav").style.width === "") { document.getElementById("mySidenav").style.width = "81%"; document.getElementById("mySidenav").style.visibility = "visible"; }
    else if(document.getElementById("mySidenav").style.width === "81%") { document.getElementById("mySidenav").style.width = ""; document.getElementById("mySidenav").style.visibility = "hidden"; }
}
function downloadCSV() {
	var myNodes = zNodes;
	var headerArray = ["name", "short_name", "full_name", "parent", "identifier", "latitude", "longitude", "type", "description"];
    var nameArray = [];
    var shortNameArray = [];
    var fullNameArray = [];
    var pIdArray = [];
    var pNameArray = [];
    var descriptionArray = [];
    var otherIdentifierArray = [];
    var typeArray = [];
    var typeNameArray = [];
    var latitudeArray = [];
    var longitudeArray = [];
    
    var i=0;
    while(myNodes[i] !== undefined) {
        nameArray.push(myNodes[i].name);
        shortNameArray.push(myNodes[i].shortName);
        fullNameArray.push(myNodes[i].fullName);
        pIdArray.push(myNodes[i].pId);
        pNameArray.push(myNodes[i].pName);
        descriptionArray.push(myNodes[i].description);
        otherIdentifierArray.push(myNodes[i].otherIdentifier);
        typeArray.push(myNodes[i].type);
        typeNameArray.push(myNodes[i].typeName);
        latitudeArray.push(myNodes[i].latitude);
        longitudeArray.push(myNodes[i].longitude);
        i++;
    }
    var arr = [nameArray.length, shortNameArray.length, fullNameArray.length, pIdArray.length, pNameArray.length, descriptionArray.length, otherIdentifierArray.length, typeArray.length, typeNameArray.length, latitudeArray.length, longitudeArray.length];
    arr = arr.sort();
    arr = arr.reverse();
    var rowsAfterHeader = arr[0];
    var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "zTreeTable";
//	    table.width = '100%';
    var row = table.insertRow(-1);
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i].toUpperCase();
        row.appendChild(headerCell);
    }
    var count=0;
    for (var i = 0; i < rowsAfterHeader; i++) {;
        row = table.insertRow(-1);
        row.id = "row"+i;

        var cell = row.insertCell(-1);
        if(nameArray[i] !== undefined) { cell.innerHTML = nameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(shortNameArray[i] !== undefined) { cell.innerHTML = shortNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(fullNameArray[i] !== undefined) { cell.innerHTML = fullNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(otherIdentifierArray[i] !== undefined) { cell.innerHTML = pNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(otherIdentifierArray[i] !== undefined) { cell.innerHTML = otherIdentifierArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(latitudeArray[i] !== undefined) { cell.innerHTML = latitudeArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(longitudeArray[i] !== undefined) { cell.innerHTML = longitudeArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(typeNameArray[i] !== undefined) { cell.innerHTML = typeNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(descriptionArray[i] !== undefined) { cell.innerHTML = descriptionArray[i]; }
        else { cell.innerHTML = ""; }
    }
    document.getElementById("zTreeTableDiv").innerHTML = "";
    document.getElementById("zTreeTableDiv").appendChild(table);
    document.getElementById("zTreeTableDiv").style.visibility = "hidden";
}
function downloadCSVTemplate() { 
	var headerArray = ["name", "short_name", "full_name", "parent", "identifier", "latitude", "longitude", "type", "description"];
	var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "zTreeTable";
//	table.width = '80%';
    var row = table.insertRow(-1);
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i].toUpperCase();
        row.appendChild(headerCell);
    }	
    var rowsAfterHeader = 5;
    for (var i = 0; i < rowsAfterHeader; i++) {;
	    row = table.insertRow(-1);
	    row.id = "row"+i;

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "[Location " + (i+1) + " Name]";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = ""; }
	    else { cell.innerHTML = "[Location " + i + " Name]"; }

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";

	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";
	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = "COUNTRY"; }
	    else if(i===1) { cell.innerHTML = "CITY"; }
	    else if(i===2) { cell.innerHTML = "DISTRICT"; }
	    else if(i===3) { cell.innerHTML = "TOWN"; }
	    else if(i===4) { cell.innerHTML = "UC"; }
	    var cell = row.insertCell(-1);
	    cell.innerHTML = "";
    }
    document.getElementById("zTreeTableDiv").innerHTML = "";
    document.getElementById("zTreeTableDiv").appendChild(table);
    document.getElementById("zTreeTableDiv").style.visibility = "hidden";
}
function downloadCSVTemplateForLocAttrs() { 
	var headerArray = ["parent", "attribute_name", "value", "type_value"];
	var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "zTreeTable";
//	table.width = '80%';
    var row = table.insertRow(-1);
    for (var i = 0; i < headerArray.length; i++) { var headerCell = document.createElement("TH"); headerCell.innerHTML = headerArray[i].toUpperCase(); row.appendChild(headerCell); }	
    var rowsAfterHeader = 5;
    for (var i = 0; i < rowsAfterHeader; i++) {;
	    row = table.insertRow(-1);
	    row.id = "row"+i;
	    var cell = row.insertCell(-1);
	    cell.innerHTML = "[Location Attribute " + (i+1) + "]";
	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = "[Attribute " + (i+1) +" Name]"; }
	    else if(i===1) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_ANUALLY"; }
	    else if(i===2) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_MONTHLY"; }
	    else if(i===3) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_DAILY"; }
	    else if(i===4) { cell.innerHTML = "[Attribute "+ (i+1) + " Name]_TIME_BOUND"; }
	    var cell = row.insertCell(-1);
	    cell.innerHTML = "[Location Attribute Name " + (i+1) + " Value]";
	    var cell = row.insertCell(-1);
	    if(i===0) { cell.innerHTML = ""; }
	    else if(i===1) { cell.innerHTML = "2017"; }
	    else if(i===2) { cell.innerHTML = "2017-03"; }
	    else if(i===3) { cell.innerHTML = "2017-02-03"; }
	    else if(i===4) { cell.innerHTML = "2017-02-10:2017-02-11"; }
    }
    document.getElementById("zTreeTableDiv").innerHTML = "";
    document.getElementById("zTreeTableDiv").appendChild(table);
    document.getElementById("zTreeTableDiv").style.visibility = "hidden";
}
function compareArr(array1, array2) {
	var count = 0; 
	if(array1.length !== array2.length) {return false;}
	else {
		if(array1.length === array2.length) { for(var i=0; i<array1.length; i++) { if(array1[i] === array2[i]) { count ++; } else { } } }
		if(count === array1.length-1 && count === array2.length-1) { return true; }
		else { return false;}
	}
}
function myFunctionChecks(myBound) {
	var currentDate = getCurrentDate();
	var div = document.getElementById(myBound.id+"_DIV");
	div.innerHTML = "";
	var value = myBound.value;
	while (value.includes("_")) { value = value.replace("_", " "); }
    if(myBound.value === "None") { div.innerHTML = ""; }
    else if(myBound.value === "Anually") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "number");
	    boundValue.setAttribute("name", myBound.id+"_BOUND");
	    boundValue.setAttribute("id", myBound.id+"_BOUND");
	    boundValue.setAttribute("min", "1990");
	    boundValue.setAttribute("max", currentDate.substring(0, 4));
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Year, 1990 to Present Year");
	    boundValue.setAttribute("placeholder", "Year");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(myBound.value === "Monthly") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "month");
	    boundValue.setAttribute("name", myBound.id+"_BOUND");
	    boundValue.setAttribute("id", myBound.id+"_BOUND");
	    boundValue.setAttribute("max", currentDate.substring(0, 7));
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Month");
	    boundValue.setAttribute("placeholder", "Month");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(myBound.value === "Daily") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", myBound.id+"_BOUND");
	    boundValue.setAttribute("id", myBound.id+"_BOUND");
	    boundValue.setAttribute("max", currentDate);
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Day");
	    boundValue.setAttribute("placeholder", "Day");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
    else if(myBound.value === "Time_Bound") {
    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " From:";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND_From");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", myBound.id+"_BOUND_From");
	    boundValue.setAttribute("id", myBound.id+"_BOUND_From");
	    boundValue.setAttribute("max", currentDate);
	    boundValue.setAttribute("onchange", "setDateOfTo(this)");
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Date From");
	    boundValue.setAttribute("placeholder", "Date From");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);

    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " To:";
    	boundLabel.setAttribute("for", myBound.id+"_BOUND_To");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", myBound.id+"_BOUND_To");
	    boundValue.setAttribute("id", myBound.id+"_BOUND_To");
	    boundValue.setAttribute("max", currentDate);
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", "");
	    boundValue.setAttribute("title", "Enter Date To");
	    boundValue.setAttribute("placeholder", "Date To");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);
    }
    else { div.innerHTML = ""; }
}
function myFunctionSetChecks(id, valuee, val1, val2) {
	/*if(Array.isArray(valuee)) {
		console.log(valuee);
	}
	else {
		console.log(valuee);
	}*/
	var currentDate = getCurrentDate();
	var div = document.getElementById(id+"_DIV"); div.innerHTML = "";
	var value = valuee;
	while (value.includes("_")) { value = value.replace("_", " "); }
    if(valuee === "None") { div.innerHTML = ""; }
    else if(valuee === "Anually") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "number");
	    boundValue.setAttribute("name", id+"_BOUND");
	    boundValue.setAttribute("id", id+"_BOUND");
	    boundValue.setAttribute("min", "1990");
	    boundValue.setAttribute("max", currentDate.substring(0, 4));
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Year, 1990 to Present Year");
	    boundValue.setAttribute("placeholder", "Year");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(valuee === "Monthly") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "month");
	    boundValue.setAttribute("name", id+"_BOUND");
	    boundValue.setAttribute("id", id+"_BOUND");
	    boundValue.setAttribute("max", currentDate.substring(0, 7));
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Month");
	    boundValue.setAttribute("placeholder", "Month");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
	else if(valuee === "Daily") {
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + ":";
    	boundLabel.setAttribute("for", id+"_BOUND");
    	boundLabel.setAttribute("class", "form-control-label");
        div.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", id+"_BOUND");
	    boundValue.setAttribute("id", id+"_BOUND");
	    boundValue.setAttribute("max", currentDate);
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Day");
	    boundValue.setAttribute("placeholder", "Day");
	    div.appendChild(boundValue);	
	    div.setAttribute("class", "form-group");
	}
    else if(valuee === "Time_Bound") {
    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " From:";
    	boundLabel.setAttribute("for", id+"_BOUND_From");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", id+"_BOUND_From");
	    boundValue.setAttribute("id", id+"_BOUND_From");
	    boundValue.setAttribute("max", currentDate);
	    boundValue.setAttribute("onchange", "setDateOfTo(this)");
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val1);
	    boundValue.setAttribute("title", "Enter Date From");
	    boundValue.setAttribute("placeholder", "Date From");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);

    	var div1 = document.createElement('div');
    	var boundLabel = document.createElement('label');
    	boundLabel.innerHTML = value + " To:";
    	boundLabel.setAttribute("for", id+"_BOUND_To");
    	boundLabel.setAttribute("class", "form-control-label");
        div1.appendChild(boundLabel);
        var boundValue = document.createElement('input');
	    boundValue.setAttribute("type", "date");
	    boundValue.setAttribute("name", id+"_BOUND_To");
	    boundValue.setAttribute("id", id+"_BOUND_To");
	    boundValue.setAttribute("max", currentDate);
	    boundValue.setAttribute("onkeydown", "return false");
	    boundValue.setAttribute("class", "form-control");
	    boundValue.setAttribute("value", val2);
	    boundValue.setAttribute("title", "Enter Date To");
	    boundValue.setAttribute("placeholder", "Date To");
	    div1.appendChild(boundValue);	
	    div1.setAttribute("class", "form-group");
	    div.appendChild(div1);
    }
    else { div.innerHTML = ""; }
}
function setDateOfTo(fromDateObj) {
	var id = fromDateObj.id;
	var value = fromDateObj.value;
	var newId = fromDateObj.id.replace("_From", "_To");
	document.getElementById(newId).setAttribute("min", document.getElementById(id).value);
}
function alertDIV(messageStatus, messageHeading, messageText) { 
	document.getElementById("myAlertDIV").innerHTML = "";
	document.getElementById("myAlertDIV").innerHTML = "<div class='alert alert-"+messageStatus+" alert-dismissable' style='margin: 0 auto; padding: 3px; text-align: center;'><a href='#' class='close' data-dismiss='alert' aria-label='close' style=' right: 0; top: 0;' title='Close' >&times;</a><strong>"+messageHeading.replace(":", "")+"</strong> "+messageText+"</div>"; 
    $("#myAlertDIV").alert();
    $("#myAlertDIV").fadeTo(10000, 10000).slideUp(2000, function(){ $("#myAlertDIV").slideUp(5000); });   
}
function alertDIV1(messageStatus, messageHeading, messageText, id) {
	document.getElementById("myAlertDIV1"+id).innerHTML = "";
	document.getElementById("myAlertDIV1"+id).innerHTML = "<div class='alert alert-"+messageStatus+" alert-dismissable' style='margin-bottom: 0px; padding: 3px; text-align: center;'><a href='#' class='close' data-dismiss='alert' aria-label='close' style=' right: 0; top: 0;' title='Close' >&times;</a><strong>"+messageHeading.replace(":", "")+"</strong><br/>"+messageText+"</div>"; 
    $("#myAlertDIV1"+id).alert();
    $("#myAlertDIV1"+id).fadeTo(10000, 10000).slideUp(2000, function(){ $("#myAlertDIV1"+id).slideUp(5000); });   
}
function viewLocationDetails(nodeName) {
	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	var node = treeObj.getNodeByParam('id', nodeName);
	if(jQuery.isEmptyObject(node)) { treeObj = $.fn.zTree.init($("#voidedLocations"), setting, voidedLocations); node = treeObj.getNodeByParam('id', nodeName); }
	console.log(node);

	var headerArray = ["KEY", "VALUE"];
    var table = document.createElement("TABLE");
    var headerArray1 = ["Attribute Id", "Attribute Type Id", "Location Attribute Value"];
    var headerArray2 = ["Attribute Id", "Attribute Type Id", "Type Name", "Location Attribute Value", "Type Value From", "Type Value To"];
    var idLocation = null;
    var nameLocation = null;
    var shortNameLocation = null;
    var fullNameLocation = null;
    var pIdLocation = null;
    var pNameLocation = null;
    var typeLocation = null;
    var typeNameLocation = null;
    var descriptionLocation = null;
    var otherIdentifierLocation = null;
    var latitudeLocation = null;
    var longitudeLocation = null;
    idLocation = node.id;
    nameLocation = node.name;
    shortNameLocation = node.shortName;
    fullNameLocation = node.fullName;
    if(node.pId === null) { pIdLocation = 0; pNameLocation = ""; } else { pIdLocation = node.pId; pNameLocation = node.pName; }
    typeLocation = node.type;
    typeNameLocation = node.typeName;
    descriptionLocation = node.description;
    otherIdentifierLocation = node.otherIdentifier;
    latitudeLocation = node.latitude;
    longitudeLocation = node.longitude;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Name: "; 
    var value = row.insertCell(1); value.innerHTML = nameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Short Name: "; 
    var value = row.insertCell(1); value.innerHTML = shortNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Full Name: "; 
    var value = row.insertCell(1); value.innerHTML = fullNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Parent Id: "; 
    var value = row.insertCell(1); value.innerHTML = pIdLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Parent Name: "; 
    var value = row.insertCell(1); value.innerHTML = pNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Type: "; 
    var value = row.insertCell(1); value.innerHTML = typeLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Type Name: "; 
    var value = row.insertCell(1); value.innerHTML = typeNameLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Description: "; 
    var value = row.insertCell(1); value.innerHTML = descriptionLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Identifier: "; 
    var value = row.insertCell(1); value.innerHTML = otherIdentifierLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Latitude: "; 
    var value = row.insertCell(1); value.innerHTML = latitudeLocation;

    var row = table.insertRow(-1);
    var key = row.insertCell(0); key.innerHTML = "Longitude: "; 
    var value = row.insertCell(1); value.innerHTML = longitudeLocation;

    var table2 = null;
    var table3 = null;
    if(!jQuery.isEmptyObject(locationAttributeDataNodes)) { 
    	table2 = document.createElement("TABLE");
        var row = table2.insertRow(-1);
        for (var i = 0; i < headerArray1.length; i++) {
        	if(i===0) {}
        	else if(i===1) {}
        	else { var headerCell = document.createElement("TH"); headerCell.innerHTML = headerArray1[i]; row.appendChild(headerCell); }
        }
    	for (var i = 0; i < locationAttributeDataNodes.length; i++) {
    		var locationIdForLocationAttribute = null;
    		var idForLocationAttribute = null;
    		var typeIdForLocationAttribute = null; 
    		var valueForLocationAttribute = null;
    		var typeNameForLocationAttribute = null;
    		var typeValue1ForLocationAttribute = null;
    		var typeValue2ForLocationAttribute = null;
    		
    		locationIdForLocationAttribute = locationAttributeDataNodes[i].locationId;
    		idForLocationAttribute = locationAttributeDataNodes[i].id;
    		typeIdForLocationAttribute = locationAttributeDataNodes[i].typeId;
    		valueForLocationAttribute = locationAttributeDataNodes[i].value;
    		typeNameForLocationAttribute = locationAttributeDataNodes[i].typeName;
    		typeValue1ForLocationAttribute = locationAttributeDataNodes[i].typeValue1;
    		typeValue2ForLocationAttribute = locationAttributeDataNodes[i].typeValue2;

    		if(idLocation === locationIdForLocationAttribute) { if("None" === typeNameForLocationAttribute ) { row = table2.insertRow(-1); var value = row.insertCell(-1); value.innerHTML = valueForLocationAttribute; } }
    	} table3 = document.createElement("TABLE");
        var row = table3.insertRow(-1);
        for (var i = 0; i < headerArray2.length; i++) {
        	if(i===0) {}
        	else if(i===1) {}
        	else { var headerCell = document.createElement("TH"); headerCell.innerHTML = headerArray2[i]; row.appendChild(headerCell); }
        }
		for (var i = 0; i < locationAttributeDataNodes.length; i++) {
    		var locationIdForLocationAttribute = null;
    		var idForLocationAttribute = null;
    		var typeIdForLocationAttribute = null; 
    		var valueForLocationAttribute = null;
    		var typeNameForLocationAttribute = null;
    		var typeValue1ForLocationAttribute = null;
    		var typeValue2ForLocationAttribute = null;
    		
    		locationIdForLocationAttribute = locationAttributeDataNodes[i].locationId; 
    		idForLocationAttribute = locationAttributeDataNodes[i].id;
    		typeIdForLocationAttribute = locationAttributeDataNodes[i].typeId;
    		valueForLocationAttribute = locationAttributeDataNodes[i].value;
    		typeNameForLocationAttribute = locationAttributeDataNodes[i].typeName;
    		typeValue1ForLocationAttribute = locationAttributeDataNodes[i].typeValue1;
    		typeValue2ForLocationAttribute = locationAttributeDataNodes[i].typeValue2;

    		if(idLocation === locationIdForLocationAttribute) {
    			if("None" === typeNameForLocationAttribute ) {}
    			else {
    				row = table3.insertRow(-1);
    				var value = row.insertCell(-1); value.innerHTML = typeNameForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = valueForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = typeValue1ForLocationAttribute;
    				var value = row.insertCell(-1); value.innerHTML = typeValue2ForLocationAttribute;
    			}
    		}
    	}    	
    }
	var html = ""; 
	html += "<h4 style='text-align: center;'>LOCATION DETAIL</h4><table id='locationTable' name='locationTable' style=' width: 100%;'>" + table.innerHTML + "</table>"
	if(table2 === null) {}
	else {
		if("<tbody><tr><th>Location Attribute Value</th></tr></tbody>" === table2.innerHTML) {}
	    else { html += "<br/><h4 style='text-align: center;'>LOCATION ATTRIBUTE DETAILS</h4><table id='locationAttributeTable' name='locationAttributeTable' style=' width: 100%;'>" + table2.innerHTML + "</table>" }
	}
	if(table3 === null) {}
	else {
		if("<tbody><tr><th>Type Name</th><th>Location Attribute Value</th><th>Type Value From</th><th>Type Value To</th></tr></tbody>" === table3.innerHTML) {}
	    else { html += "<br/><h4 style='text-align: center;'>LOCATION ATTRIBUTE DETAILS-TIME BOUND</h4><table id='locationAttributeTimeTable' name='locationAttributeTimeTable' style=' width: 100%;'>" + table3.innerHTML + "</table>" }
	} 
	//if (myWindow1) { myWindow1.close(); }
	//myWindow1 = window.open("", "myWindow1", "width=500,height=500"); myWindow1.document.write("<html><head><title>LOCATION DETAILS OF '" + node.name.toUpperCase() + "'</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body>" + html + "</body></html>");
	$("#alertModalHead").html("LOCATION DETAILS OF '" + node.name.toUpperCase() + "'");
	$("#alertModalBody").html(html);
	$('#alertModal').modal({ backdrop: 'static', keyboard: false }); 
	$("#alertModal").modal('show');
}
function viewLocationTypeDetails() {
	var headerArray = ["Name", "Level", "Description"];
	var table = document.createElement("TABLE");
    if(!jQuery.isEmptyObject(locationTypeNodes)) { 
        var row = table.insertRow(-1);
        for (var i = 0; i < headerArray.length; i++) { var headerCell = document.createElement("TH"); headerCell.innerHTML = headerArray[i]; row.appendChild(headerCell); }
		for (var i = 0; i < locationTypeNodes.length; i++) {
			var id = null;
			var name = null;
			var level = null;
			var description = null;
			id = locationTypeNodes[i].id;
			name = locationTypeNodes[i].name;
			level = locationTypeNodes[i].level;
			description = locationTypeNodes[i].description;
	    	row = table.insertRow(-1);
		    var value = row.insertCell(-1); value.innerHTML = name;
			if(level === "") { var value = row.insertCell(-1); value.innerHTML = "0" ; }
			else { var value = row.insertCell(-1); value.innerHTML = level; }
		    var value = row.insertCell(-1); value.innerHTML = description;
		}
	}
    if(table.innerHTML === "") {
    	var row = table.insertRow(-1);
        for (var i = 0; i < headerArray.length; i++) {
            var headerCell = document.createElement("TH");
            headerCell.innerHTML = headerArray[i];
            row.appendChild(headerCell);
        } row = table.insertRow(-1);
	    var value = row.insertCell(-1); value.innerHTML = "";
	    var value = row.insertCell(-1); value.innerHTML = "";
	    var value = row.insertCell(-1); value.innerHTML = "";
    }
    else {}
    //if (myWindow2) { myWindow2.close(); }
    //myWindow2 = window.open("", "myWindow2", "width=500,height=500"); myWindow2.document.write("<html><head><title>LOCATION TYPE DETAILS</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body><h4 style='text-align: center;'>LOCATION TYPE DETAILS</h4><table style=' width: 100%;'>" + table.innerHTML + "</table></body></html>");
	$("#alertModalHead").html("LOCATION TYPE DETAILS"); 
	$("#alertModalBody").html("<h4 style='text-align: center;'>LOCATION TYPE DETAILS</h4><table id='locationTypeDetail' name='locationTypeDetail' style=' width: 100%;'>" + table.innerHTML + "</table>"); 
	$('#alertModal').modal({ backdrop: 'static', keyboard: false }); 
	$("#alertModal").modal('show');

}
function viewLocationAttributeTypeDetails() {
	var headerArray = ["Name", "Display Name", "Description", "Category"];
    var table = document.createElement("TABLE");
    if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
    	var row = table.insertRow(-1);
        for (var i = 0; i < headerArray.length; i++) { var headerCell = document.createElement("TH"); headerCell.innerHTML = headerArray[i]; row.appendChild(headerCell); }
        for (var i = 0; i < locationAttributeNodes.length; i++) {
			var id = null;
			var name = null;
			var displayName = null;
			var description = null;
			var category = null;
			id = locationAttributeNodes[i].id;
			name = locationAttributeNodes[i].name;
			displayName = locationAttributeNodes[i].displayName;
			description = locationAttributeNodes[i].description;
			category = locationAttributeNodes[i].category;
			row = table.insertRow(-1);
		    var value = row.insertCell(-1); value.innerHTML = name;
		    var value = row.insertCell(-1); value.innerHTML = displayName;
		    var value = row.insertCell(-1); value.innerHTML = description;
		    var value = row.insertCell(-1); value.innerHTML = category;
		}
	}
    if(table.innerHTML === "") {
    	var row = table.insertRow(-1);
        for (var i = 0; i < headerArray.length; i++) { var headerCell = document.createElement("TH"); headerCell.innerHTML = headerArray[i]; row.appendChild(headerCell); }
		row = table.insertRow(-1);
	    var value = row.insertCell(-1); value.innerHTML = "";
	    var value = row.insertCell(-1); value.innerHTML = "";
	    var value = row.insertCell(-1); value.innerHTML = "";
	    var value = row.insertCell(-1); value.innerHTML = "";
    }
    else {}
    //if (myWindow3) { myWindow3.close(); }
    //myWindow3 = window.open("", "myWindow3", "width=500,height=500"); myWindow3.document.write("<html><head><title>LOCATION ATTRIBUTE TYPE DETAILS</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body><h4 style='text-align: center;'>LOCATION ATTRIBUTE TYPE DETAILS</h4><table style=' width: 100%;'>" + table.innerHTML + "</table></body></html>");
	$("#alertModalHead").html("LOCATION ATTRIBUTE TYPE DETAILS"); 
	$("#alertModalBody").html("<h4 style='text-align: center;'>LOCATION ATTRIBUTE TYPE DETAILS</h4><table id='locationAttributeTypeDetail' name='locationAttributeTypeDetail' style=' width: 100%;'>" + table.innerHTML + "</table>"); 
	$('#alertModal').modal({ backdrop: 'static', keyboard: false }); 
	$("#alertModal").modal('show');
}
function mySelectChange(mySelect) {
	var selectIndex = document.getElementById(mySelect.id).selectedIndex;
	var selectOption = document.getElementById(mySelect.id).options;
	var index = selectOption[selectIndex].index;
	var value = selectOption[selectIndex].value;
	var text = selectOption[selectIndex].text;
	var rowId = mySelect.id.replace("mySelect_", "");
	for (var i = 0; i < mySelectData.length; i++) {
		if(mySelectData[i].rowId === rowId) {
			mySelectData[i].index = index;
			mySelectData[i].text = text;
			mySelectData[i].value = value;
		}
	}
}
function updateLocationTypeChange() {
	var id = null;
	if(!jQuery.isEmptyObject(locationTypeNodes)) { 
		for (var i = 0; i < locationTypeNodes.length; i++) {
			id = locationTypeNodes[i].id;
			if(id.toString() === document.getElementById("updatelocationTypeName").value) {
				document.getElementById("updatelocationTypeLevel").value = locationTypeNodes[i].level;
				document.getElementById("updatelocationTypeDescription").value = locationTypeNodes[i].description;
			}
		}
	}
}
function updateLocationAttributeTypeChange() {
	var id = null;
	if(!jQuery.isEmptyObject(locationAttributeNodes)) { 
		for (var i = 0; i < locationAttributeNodes.length; i++) {
			id = locationAttributeNodes[i].id;
			if(id.toString() === document.getElementById("updatelocationAttributeTypeName").value) {
				document.getElementById("updatelocationAttributeTypeDisplayName").value = locationAttributeNodes[i].displayName;
				document.getElementById("updatelocationAttributeTypeDescription").value = locationAttributeNodes[i].description;
				document.getElementById("updatelocationAttributeTypeCategory").value = locationAttributeNodes[i].category;
			}
		}
	}
}
function locationAttributeParentChange() {
	var id = null;
	var locationId = null;
	var typeId = null;
	var typeName = null;
	var typeValue1 = null;
	var typeValue2 = null;
	var value = null;
	var locationAttributeNodesForParent = [];
	for (var i = 0; i < locationAttributeDataNodes.length; i++) { 
		var idX = null; 
		id = locationAttributeDataNodes[i].id;
		locationId = locationAttributeDataNodes[i].locationId;
		typeId = locationAttributeDataNodes[i].typeId;
		typeName = locationAttributeDataNodes[i].typeName;
		typeValue1 = locationAttributeDataNodes[i].typeValue1;
		typeValue2 = locationAttributeDataNodes[i].typeValue2;
		value = locationAttributeDataNodes[i].value;

		document.getElementById("locationAttribute_"+typeId).value = "";
		document.getElementById('bound_'+typeId+'_DIV').innerHTML = "";

		if(locationAttributeDataNodes[i].locationId.toString() === document.getElementById("locationParent").value) { 
			document.getElementById("locationAttribute_"+typeId).value = value;
			if(typeName === "None") {
				var radioHTML = "";
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None" checked>&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" >&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
				document.getElementById('bound_'+typeId+'_RADIO').innerHTML = radioHTML; 
		    }
			else if(typeName === "Anually") {
				var radioHTML = "";
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None">&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" checked>&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
				document.getElementById('bound_'+typeId+'_RADIO').innerHTML = radioHTML; 
		    }
			else if(typeName === "Monthly") {
				var radioHTML = "";
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None">&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually">&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly" checked>&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
				document.getElementById('bound_'+typeId+'_RADIO').innerHTML = radioHTML; 
		    }
			else if(typeName === "Daily") {
				var radioHTML = "";
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None">&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually">&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily" checked>&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
				document.getElementById('bound_'+typeId+'_RADIO').innerHTML = radioHTML; 
		    }
			else if(typeName === "Time_Bound") {
				var radioHTML = "";
		    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None">&nbsp;None&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually" checked>&nbsp;Anually&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
			    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound" checked>&nbsp;Time Bound&nbsp;';  
				document.getElementById('bound_'+typeId+'_RADIO').innerHTML = radioHTML; 
		    } myFunctionSetChecks("bound_"+typeId, typeName, typeValue1, typeValue2);
		}
		else {
			var radioHTML = "";
	    	radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="None" checked>&nbsp;None&nbsp;'; 
		    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Anually">&nbsp;Anually&nbsp;';
		    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Monthly">&nbsp;Monthly&nbsp;'; 
		    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Daily">&nbsp;Daily&nbsp;';
		    radioHTML += '<input type="radio" id="bound_'+typeId+'" name="bound_'+typeId+'" onclick="myFunctionChecks(this)" value="Time_Bound">&nbsp;Time Bound&nbsp;';  
			document.getElementById('bound_'+typeId+'_RADIO').innerHTML = radioHTML; 
		}
	}
}
function setFocus(id) {
	if(id==="addlocation") { document.getElementById("addName").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("addName").setAttribute("autofocus", "autofocus"); }
	else if(id==="addlocationtype") { document.getElementById("addlocationTypeName").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("addlocationTypeName").setAttribute("autofocus", "autofocus"); }
	else if(id==="addlocationattribute") { document.getElementById("locationParent").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("locationParent").setAttribute("autofocus", "autofocus"); }
	else if(id==="addlocationattributetype") { document.getElementById("addlocationAttributeTypeName").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("addlocationAttributeTypeName").setAttribute("autofocus", "autofocus"); }
	else if(id==="addlocationattributebutton") { document.getElementById("nextBtnLocationAttribute").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("nextBtnLocationAttribute").setAttribute("autofocus", "autofocus"); }
	else if(id==="updatelocationtype") { document.getElementById("updatelocationTypeName").setAttribute("style", "width: 94%; outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("updatelocationTypeName").setAttribute("autofocus", "autofocus"); }
	else if(id==="updatelocationattributetype") { document.getElementById("updatelocationAttributeTypeName").setAttribute("style", "width: 94%; outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("updatelocationAttributeTypeName").setAttribute("autofocus", "autofocus"); }
	else if(id==="voidLocationDetail") { document.getElementById("voided").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("voided").setAttribute("autofocus", "autofocus"); }
	else { /*console.log(id);*/ }
//	console.log(document.activeElement.type);
}
function removeFocus(id) {
	if(id==="addlocation") { document.getElementById("addName").setAttribute("style", ""); }
	else if(id==="addlocationtype") { document.getElementById("addlocationTypeName").setAttribute("style", ""); }
	else if(id==="addlocationattribute") { document.getElementById("locationParent").setAttribute("style", ""); }
	else if(id==="addlocationattributetype") { document.getElementById("addlocationAttributeTypeName").setAttribute("style", ""); }
	else if(id==="addlocationattributebutton") { document.getElementById("nextBtnLocationAttribute").setAttribute("style", ""); }
	else if(id==="updatelocationtype") { document.getElementById("updatelocationTypeName").setAttribute("style", ""); }
	else if(id==="updatelocationattributetype") { document.getElementById("updatelocationAttributeTypeName").setAttribute("style", ""); }
	else if(id==="voidLocationDetail") { document.getElementById("voided").setAttribute("style", ""); }
	else { /*console.log(id);*/ }
}
function updateName(id) {
	if(id==="updatelocationtype") { 
		var saveId = document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].value;		
		var saveName = document.getElementById("updatelocationTypeName").options[document.getElementById("updatelocationTypeName").selectedIndex].text;		
		var nameHtml = "<input type='text' class='form-control' id='renamelocationTypeName' name='renamelocationTypeName' value='"+saveName+"' title='Enter Name, Any 3 to 50 Alphanumeric and/or Special Characters (.,-,_)' placeholder='Name' maxlength='50' style='width: 94%; outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;' autofocus='autofocus'>";
    	nameHtml += "<div style='float: right; position: relative; top: -30px;'>";
    	nameHtml += "<a name='saveNameBtn' id='saveNameBtn' class='icon' title='Save Name'style='cursor:pointer;' onclick='saveUpdatedName(\""+id+"\");'><img alt='edit' src='images/tick.png' class='icon' style='width: 15px; '></a>";
    	nameHtml += "<a name='cancelNameBtn' id='cancelNameBtn' class='icon' title='Cancel Name' style='cursor:pointer;' onclick='cancelUpdatedName(\""+id+"\");'><img alt='edit' src='images/undo.png' class='icon' style='width: 15px; '></a>";
    	nameHtml += "</div>";
		nameHtml += "<input type='hidden' id='renamelocationTypeId' name='renamelocationTypeId' value='"+saveId+"'>";
		nameHtml += "<input type='hidden' id='renamelocationTypeName' name='renamelocationTypeName' value='"+saveName+"'>";
    	document.getElementById("updatelocationTypeDiv").innerHTML = nameHtml;
	}
	else if(id==="updatelocationattributetype") { 
		var saveId = document.getElementById("updatelocationAttributeTypeName").options[document.getElementById("updatelocationAttributeTypeName").selectedIndex].value;		
		var saveName = document.getElementById("updatelocationAttributeTypeName").options[document.getElementById("updatelocationAttributeTypeName").selectedIndex].text;		
		var nameHtml = "<input type='text' class='form-control' id='renamelocationAttributeTypeName' name='renamelocationAttributeTypeName' value='"+saveName+"' title='Enter Name, Any 3 to 50 Alphanumeric and/or Special Characters (.,-,_)' placeholder='Name' maxlength='50' style='width: 94%; outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;' autofocus='autofocus'>";
		nameHtml += "<div style='float: right; position: relative; top: -30px;'>";
    	nameHtml += "<a name='saveNameBtn' id='saveNameBtn' title='Save Name' class='icon' style='cursor:pointer;' onclick='saveUpdatedName(\""+id+"\");'><img alt='edit' src='images/tick.png' class='icon' style='width: 15px; '></a>";
    	nameHtml += "<a name='cancelNameBtn' id='cancelNameBtn' title='Cancel Name' class='icon' style='cursor:pointer;' onclick='cancelUpdatedName(\""+id+"\");'><img alt='edit' src='images/undo.png' class='icon' style='width: 15px; '></a>";
    	nameHtml += "</div>";
		nameHtml += "<input type='hidden' id='renamelocationAttributeTypeId' name='renamelocationAttributeTypeId' value='"+saveId+"'>";
		nameHtml += "<input type='hidden' id='renamelocationAttributeTypeName' name='renamelocationAttributeTypeName' value='"+saveName+"'>";
    	document.getElementById("updatelocationAttributeTypeDiv").innerHTML = nameHtml;
	}
	else { /*console.log(id);*/ }
}
function cancelUpdatedName(id) {
	if(id==="updatelocationtype") { 
		var saveId = document.getElementById("renamelocationTypeId").value;		
		var saveName = document.getElementById("renamelocationTypeName").value;
		var nameHtml = "<select id='updatelocationTypeName' name='updatelocationTypeName' class='form-control myclass' onchange='updateLocationTypeChange()' onfocus='setFocus(\""+id+"\");' title='Choose Location Type' style='width: 94%;'>";
		var idArray = []; var nameArray = []; var tempArray = []; var levelArray = []; var descriptionArray = [];
		for (var i = 0; i < locationTypeNodes.length; i++) {
			idArray.push(locationTypeNodes[i].id);
			nameArray.push(locationTypeNodes[i].name);
			tempArray.push(capitalize(locationTypeNodes[i].name));
			levelArray.push(locationTypeNodes[i].level);
			descriptionArray.push(locationTypeNodes[i].description);
		} tempArray.sort();
		for (var i = 0; i < tempArray.length; i++) {
			var tempName; var tempId; var tempLevel; var tempDescription;
			for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { tempId = idArray[j]; tempName = nameArray[j]; tempLevel = levelArray[j]; tempDescription = descriptionArray[j]; } }
			if(tempId.toString() === saveId.toString()) { nameHtml += '<option value="' + tempId +'" selected>' + tempName + '</option>';  }
			else { nameHtml += '<option value="' + tempId +'">' + tempName + '</option>'; }
		} nameHtml += "</select>";
		nameHtml += "<a name='renameBtn' id='renameBtn' class='icon' title='Edit Name' style='cursor:pointer;' onclick='updateName(\""+id+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' style='width: 30px; float: right; position: relative; top: -30px;'></a>";
		document.getElementById("updatelocationTypeDiv").innerHTML = nameHtml;
		setFocus(id);
	}
	else if(id==="updatelocationattributetype") { 
		var saveId = document.getElementById("renamelocationAttributeTypeId").value;		
		var saveName = document.getElementById("renamelocationAttributeTypeName").value;
		var nameHtml = "<select id='updatelocationAttributeTypeName' name='updatelocationAttributeTypeName' class='form-control myclass' onchange='updateLocationAttributeTypeChange()' onfocus='setFocus(\""+id+"\");' title='Choose Location Attribute Type' style='width: 94%;'>";
		var idArray = []; var nameArray = []; var displayNameArray = []; var descriptionArray = []; var categoryArray = []; var tempArray = [];
    	for (var i = 0; i < locationAttributeNodes.length; i++) {
    		idArray.push(locationAttributeNodes[i].id);
    		nameArray.push(locationAttributeNodes[i].name);
    		tempArray.push(capitalize(locationAttributeNodes[i].name));
    		displayNameArray.push(locationAttributeNodes[i].displayName);
    		descriptionArray.push(locationAttributeNodes[i].description);
    		categoryArray.push(locationAttributeNodes[i].category);
    	} tempArray.sort();
    	for (var i = 0; i < tempArray.length; i++) {
    		var tempName; var tempId; var tempDisplayName; var tempDescription; var tempCategory;
    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { tempId = idArray[j]; tempName = nameArray[j]; tempDisplayName = displayNameArray[j]; tempDescription = descriptionArray[j]; tempCategory = categoryArray[j]; } }
    		if(tempId.toString() === saveId.toString()) { nameHtml += '<option value="' + tempId +'" selected>' + tempName + '</option>';  }
			else { nameHtml += '<option value="' + tempId +'">' + tempName + '</option>'; }
		} nameHtml += "</select>";
    	nameHtml += "<a name='renameBtn' id='renameBtn' class='icon' title='Edit Name' style='cursor:pointer;' onclick='updateName(\""+id+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' style='width: 30px; float: right; position: relative; top: -30px;'></a>";
		document.getElementById("updatelocationAttributeTypeDiv").innerHTML = nameHtml;
		setFocus(id);
	}
	else { /*console.log(id);*/ }
}
function saveUpdatedName(id) {
	if(id==="updatelocationtype") { 
		var saveId = document.getElementById("renamelocationTypeId").value;		
		var saveName = document.getElementById("renamelocationTypeName").value;
		var pattern = new RegExp("^([\-a-zA-Z0-9._ ]+)$");
		if(pattern.test(saveName)) {
			$.ajax({
				url: "/LocationDatabaseManagement/data/location/updatelocationtypename",
				type: "POST",
				data : { id: saveId, name: saveName },
				success : function(result) {
					var data = JSON.parse(result);
			        var node = { id: saveId, name: saveName };
			        for (var i = 0; i < locationTypeNodes.length; i++) { if(locationTypeNodes[i].id.toString() === node.id) { locationTypeNodes[i].name = node.name; } }
					var nameHtml = "<select id='updatelocationTypeName' name='updatelocationTypeName' class='form-control myclass' onchange='updateLocationTypeChange()' onfocus='setFocus(\""+id+"\");' title='Choose Location Type' style='width: 94%;'>";
			    	var idArray = []; var nameArray = []; var tempArray = []; var levelArray = []; var descriptionArray = [];
			    	for (var i = 0; i < locationTypeNodes.length; i++) {
			    		idArray.push(locationTypeNodes[i].id);
			    		nameArray.push(locationTypeNodes[i].name);
			    		tempArray.push(capitalize(locationTypeNodes[i].name));
			    		levelArray.push(locationTypeNodes[i].level);
			    		descriptionArray.push(locationTypeNodes[i].description);
					} tempArray.sort();
			    	for (var i = 0; i < tempArray.length; i++) {
			    		var tempName; var tempId; var tempLevel; var tempDescription;
			    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { tempId = idArray[j]; tempName = nameArray[j]; tempLevel = levelArray[j]; tempDescription = descriptionArray[j]; } }
			    		if(tempId.toString() === saveId.toString()) { nameHtml += '<option value="' + tempId +'" selected>' + tempName + '</option>';  }
						else { nameHtml += '<option value="' + tempId +'">' + tempName + '</option>'; }
			    	} nameHtml += "</select>";
			    	nameHtml += "<a name='renameBtn' id='renameBtn' class='icon' title='Edit Name' style='cursor:pointer;' onclick='updateName(\""+id+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' style='width: 30px; float: right; position: relative; top: -30px;'></a>";
			    	document.getElementById("updatelocationTypeDiv").innerHTML = nameHtml;
			    	setFocus(id);
					alertDIV1(data.status.toString(), data.message.toString(), data.returnStr.toString(), "_updateLocationType");
				}, error: function(jqXHR, textStatus, errorThrown) { alertDIV1("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING LOCATION TYPE", "_updateLocationType"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); $("#alertModal").modal('show');*/ }
			});
		}
		else {
			var errorStr = "";
	        if(!(pattern.test(saveName))) { errorStr += "Location Type Name must have alphanumeric or special characters (.-_) only.<br/>"; }
			alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationType");
		}
	}
	else if(id==="updatelocationattributetype") { 
		var saveId = document.getElementById("renamelocationAttributeTypeId").value;		
		var saveName = document.getElementById("renamelocationAttributeTypeName").value;
		var pattern = new RegExp("^([\-a-zA-Z0-9._ ]+)$");
		if(pattern.test(saveName)) {
			$.ajax({
				url: "/LocationDatabaseManagement/data/location/updatelocationattributetypename",
				type: "POST",
				data : { id: saveId, name: saveName },
				success : function(result) {
					var data = JSON.parse(result);
			        var node = { id: saveId, name: saveName };
					for (var i = 0; i < locationAttributeNodes.length; i++) { if(locationAttributeNodes[i].id.toString() === node.id.toString()) { locationAttributeNodes[i].name = node.name; } }
					var nameHtml = "<select id='updatelocationAttributeTypeName' name='updatelocationAttributeTypeName' class='form-control myclass' onchange='updateLocationAttributeTypeChange()' onfocus='setFocus(\""+id+"\");' title='Choose Location Attribute Type' style='width: 94%;'>";
					var idArray = []; var nameArray = []; var displayNameArray = []; var descriptionArray = []; var categoryArray = []; var tempArray = [];
			    	for (var i = 0; i < locationAttributeNodes.length; i++) {
			    		idArray.push(locationAttributeNodes[i].id);
			    		nameArray.push(locationAttributeNodes[i].name);
			    		tempArray.push(capitalize(locationAttributeNodes[i].name));
			    		displayNameArray.push(locationAttributeNodes[i].displayName);
			    		descriptionArray.push(locationAttributeNodes[i].description);
			    		categoryArray.push(locationAttributeNodes[i].category);
			    	} tempArray.sort();
			    	for (var i = 0; i < tempArray.length; i++) {
			    		var tempName; var tempId; var tempDisplayName; var tempDescription; var tempCategory;
			    		for (var j = 0; j < nameArray.length; j++) { if(capitalize(nameArray[j])===tempArray[i]) { tempId = idArray[j]; tempName = nameArray[j]; tempDisplayName = displayNameArray[j]; tempDescription = descriptionArray[j]; tempCategory = categoryArray[j]; } }
			    		if(tempId.toString() === saveId.toString()) { nameHtml += '<option value="' + tempId +'" selected>' + tempName + '</option>';  }
						else { nameHtml += '<option value="' + tempId +'">' + tempName + '</option>'; }
					} nameHtml += "</select>";
			    	nameHtml += "<a name='renameBtn' id='renameBtn' class='icon' title='Edit Name' style='cursor:pointer;' onclick='updateName(\""+id+"\");'><img alt='edit' src='images/edit-icon.png' class='icon' style='width: 30px; float: right; position: relative; top: -30px;'></a>";
					document.getElementById("updatelocationAttributeTypeDiv").innerHTML = nameHtml;
					setFocus(id);
					alertDIV1(data.status.toString(), data.message.toString(), data.returnStr.toString(), "_updateLocationAttributeType");
				}, error: function(jqXHR, textStatus, errorThrown) { alertDIV1("warning", "ERROR: ", "ERROR OCCURED WHILE UPDATING LOCATION ATTRIBUTE TYPE", "_updateLocationAttributeType"); /*$("#alertModalBody").html("ERROR OCCURED WHILE CREATING NEW LOCATION TYPE"); $("#alertModal").modal('show');*/ }
			});
		}
		else {
			var errorStr = "";
	        if(!(pattern.test(saveName))) { errorStr += "Location Attribute Type Name must have alphanumeric or special characters (.-_) only.<br/>"; }
			alertDIV1("warning", "ERROR: ", errorStr, "_updateLocationAttributeType");
		}
	}
	else { /*console.log(id);*/ }
}
function getDate(date) {
	var today = new Date(date);
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10) { dd='0'+dd; } 
	if(mm<10) { mm='0'+mm; } 
	today = yyyy+'-'+mm+'-'+dd;
	return today.toString();
}
function getCurrentDate() {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	if(dd<10) { dd='0'+dd; } 
	if(mm<10) { mm='0'+mm; } 
	today = yyyy+'-'+mm+'-'+dd;
	return today.toString();
}
function capitalize(string) { 
	return string.charAt(0).toUpperCase() + string.slice(1); 
}
