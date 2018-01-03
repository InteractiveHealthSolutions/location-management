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
// TODO remove 5 variables below and refactor code to use locationAttributeNodes
var locationAttributeId = [];
var locationAttributeName = [];
var locationAttributeDisplayName = [];
var locationAttributeDescription = [];
var locationAttributeCategory = [];

var globalRowId;
var globalCellId;
//TODO remove and refactor variable below
var nodesToBeSaved = [];
var errorStr = "";
var mySelectData = [];

if(jQuery.isEmptyObject(voidedLocations)) { voidedLocations = []; }
if(jQuery.isEmptyObject(zNodes)) { zNodes = []; errorStr += "Location Database is EMPTY.<br/>"; }
if(jQuery.isEmptyObject(locationTypeNodes)) { locationTypeNodes = []; errorStr += "Location Type Database is EMPTY.<br/>"; }
if(jQuery.isEmptyObject(locationAttributeDataNodes)) { locationAttributeDataNodes = []; }
if(jQuery.isEmptyObject(locationAttributeNodes)) { locationAttributeNodes = []; }
// TODO remove code below. why there is need of 5 arrays 
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
    						url: "${pageContext.request.contextPath}/ws/location/newCsv",
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
    	} else { alertDIV("warning", "ERROR: ", "Please upload a valid CSV file with valid name."); fileUpload.value = null; }
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
					for (var i = 0; i < rowArr.length - 1; i++) {
						csvDataForLocAttrs.push(rowArr[i] .split(","));
					}
					
    				//csvDataForLocAttrs = validateTableForLocAttrs();
    				if(!csvDataForLocAttrs || csvDataForLocAttrs.length < 2 
    						|| (csvDataForLocAttrs[0].indexOf("location")=== -1 
    								&& csvDataForLocAttrs[0].indexOf("LOCATION")=== -1 
    								&& csvDataForLocAttrs[0].indexOf("Location")=== -1 )) {
    					alertDIV("warning", "ERROR: ", "A valid CSV with header columns (including location) and associated data should be provided");
    				}
    				else {
    					$.ajax({
    						url: "${pageContext.request.contextPath}/ws/location/newCsvForLocAttrs",
        					type: "POST",
        					data : { csvData: JSON.stringify(csvDataForLocAttrs) },
        					success : function(result) {
        						var data = JSON.parse(result);
        						var locationAttributeNodesData = eval(data.locationAttributeTypeNodes);
        						var locationAttributeDataNodesData = eval(data.locationAttributeNodes);
        						
        						alertDIV(data.status.toString(), data.message.toString()+"<br>REFRESH PAGE TO RELOAD LOCATIONS WITH LATEST DATA", data.returnStr.toString());
        						
    						}, error: function(jqXHR, textStatus, errorThrown) { alertDIV("warning", "ERROR: ", "EXCEPTION OCCURED"); /*alert("EXCEPTION OCCURED");*/ }
    					});
    				}
    			} 
    			reader.readAsText(fileUpload.files[0]);
    		} else { alertDIV("warning", "ERROR: ", "This browser does not support HTML5."); }
    	} else { alertDIV("warning", "ERROR: ", "Please upload a valid CSV file with valid name."); }
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
		var treeNode = ($.fn.zTree.init($("#treeDemo"), setting, zNodes))
					.getNodeByParam('id', $("#voidLocation").attr('location-id'));
		if(treeNode === null) { 
			myTreeId = "#voidedLocations"; 
			treeNode = ($.fn.zTree.init($("#voidedLocations"), setting, voidedLocations))
					.getNodeByParam('id', $("#voidLocation").attr('location-id')); 
		}
		
		if(treeNode.name === "VOIDED LOCATIONS") { 
			$('#modalVoidLocationDetail').modal('toggle'); 
			alertDIV("warning", "WARNING", "LOCATION '" + treeNode.name + "' CANNOT BE VOIDED/UN-VOIDED."); 
		}
		else if((treeNode.voided).toString() === document.getElementById("voided").value) { 
			$('#modalVoidLocationDetail').modal('toggle'); 
			
			if(myTreeId === "#treeDemo") { 
				alertDIV("info", "INFO", "LOCATION '" + treeNode.name + "' IS ALREADY UN-VOIDED."); 
			} 
			else { 
				alertDIV("info", "INFO", "LOCATION '" + treeNode.name + "' IS ALREADY VOIDED."); 
			}
		}
		else {
			if(document.getElementById("voidReason").length > 255) { 
				alertDIV1("warning", "ERROR: ", "Void Reason must be 255 charachers long", "_voidLocationDetail"); 
			}
			else {
				$.ajax({
					url: "${pageContext.request.contextPath}/ws/location/updatelocation/type",
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
		if (document.getElementById("addShortName").value === "") {
			document.getElementById("addShortName").value = document.getElementById("addName").value;
		}
		if (document.getElementById("addFullName").value === "") {
			document.getElementById("addFullName").value = document.getElementById("addName").value;
		}
		
		var pattern = new RegExp("^([\-a-zA-Z0-9._ ]+)$");
		var pattern2 = new RegExp("\-[0-9]*\.[0-9]*$");
		
		if(document.getElementById("addName").value !== "" 
			&& document.getElementById("addShortName").value !== "" 
				&& document.getElementById("addFullName").value !== "" 
					&& document.getElementById("addType").value !== "") {
			
			if(( document.getElementById("addName").value.length >= 3 
					&& document.getElementById("addName").value.length <= 30 
					&& pattern.test(document.getElementById("addName").value )) 
					&& ( document.getElementById("addShortName").value.length >= 3 
							&& document.getElementById("addShortName").value.length <= 30 
							&& pattern.test(document.getElementById("addShortName").value )) 
							&& (document.getElementById("addFullName").value.length >= 3 
									&& document.getElementById("addFullName").value.length <= 50 
									&& pattern.test(document.getElementById("addFullName").value)) 
									&& (!(pattern2.test(document.getElementById("addOtherIdentifier").value)))) {
								
				var parentLocation = null;
				if(document.getElementById("addParent")){
					parentLocation = document.getElementById("addParent").value;	
				}
				
				var dataNode = {
					name : document.getElementById("addName").value,
					shortName : document.getElementById("addShortName").value,
					fullName : document.getElementById("addFullName").value,
					pName : parentLocation,
					description : document.getElementById("addDescription").value,
					otherIdentifier : document.getElementById("addOtherIdentifier").value,
					typeName : document.getElementById("addType").options[document.getElementById("addType").selectedIndex].text,
					latitude : document.getElementById("addLatitude").value,
					longitude : document.getElementById("addLongitude").value
				};
				
				$.ajax({
					url: "${pageContext.request.contextPath}/ws/location/newlocation",
					type: "POST",
					data : dataNode,
					success : function(result) {
						var data = JSON.parse(result);
						var id = null;
						id = eval(data.id);
						if(data.status.toString() === "info") { alertDIV1(data.status.toString(), data.message.toString(), data.returnStr.toString(), "_addLocation"); }
						else { 
							$('#myModal').modal('toggle');
							// MUST add node before cleaning up model data
							addAll(id);
							document.getElementById("errors").innerHTML = "";
							$("#myModal").find('#myLocationAdd').find("input[id^='add'],select[id^='add']").val('');
							/*document.getElementById("addName").value = "";
					        document.getElementById("addShortName").value = "";
					        document.getElementById("addFullName").value = "";
					        document.getElementById("addParent").value = "";
					        document.getElementById("addDescription").value = "";
					        document.getElementById("addOtherIdentifier").value = "";
					        document.getElementById("addType").value = "";
					        document.getElementById("addLatitude").value = "";
					        document.getElementById("addLongitude").value = "";*/
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
					url: "${pageContext.request.contextPath}/ws/location/newlocationtype",
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
						url: "${pageContext.request.contextPath}/ws/location/updatelocationtype",
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
					url: "${pageContext.request.contextPath}/ws/location/newlocationattributetype",
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
							addToModal(id, node);
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
					url: "${pageContext.request.contextPath}/ws/location/updatelocationattributetype",
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
			var parent = $("#locationParent").attr('location-id');
			for (var i = 0; i < locationAttributeId.length; i++) {
				var myBound = null;
				var radioGroup = document.getElementsByName("bound_" + locationAttributeId[i]);
				for (var j = 0; j < radioGroup.length; j++) {
					if(radioGroup[j].checked) {
						myBound = radioGroup[j];

						var locationAttributeTypeValueTemp;
						var value1Temp;
						var value2Temp;
						
						locationAttributeTypeValueTemp = $("#locationAttribute_"+locationAttributeId[i]).val();

						if(myBound.value === "Text") { 
							value1Temp = null;
							value2Temp = null;
						}
						else if(myBound.value === "Anually") {
							value1Temp = document.getElementById('val_'+myBound.id).value;
							value2Temp = null;
							
							if(locationAttributeTypeValueTemp !== "") {
								if(value1Temp) {
									var date = value1Temp;
									if(date.length < 4) { 
										errorStr += "'" + locationAttributeTypeValueTemp + "': must have valid year .<br/>"; 
									}
								} 
								else {
									errorStr += "'" + locationAttributeTypeValueTemp + "': attribute is required.<br/>";
								}
							}
						}
						else if(myBound.value === "Monthly") {
							value1Temp = document.getElementById('val_'+myBound.id).value;
							value2Temp = null;
							
							if(locationAttributeTypeValueTemp !== "") {
								if(value1Temp) {
									var date = value1Temp;
									if(date.length < 7) { 
										errorStr += "'" + locationAttributeTypeValueTemp + "': must have valid month<br/>"; 
									}
								} 
								else {
									errorStr += "'" + locationAttributeTypeValueTemp + "': attribute is required.<br/>";
								}
							}
						}
						else if(myBound.value === "Daily") {
							value1Temp = document.getElementById('val_'+myBound.id).value;
							value2Temp = null;
							
							if(locationAttributeTypeValueTemp !== "") {
								if(value1Temp) {
									var date = value1Temp;
									if(date.length < 10) { 
										errorStr += "'" + locationAttributeTypeValueTemp + "': must have valid date<br/>"; 
									}
								} 
								else {
									errorStr += "'" + locationAttributeTypeValueTemp + "': attribute is required.<br/>";
								}
							}
						}

						else if(myBound.value === "Time_Bound") {
							value1Temp = document.getElementById('val_'+myBound.id+"_From").value;
							value2Temp = document.getElementById('val_'+myBound.id+"_To").value;
							
							if(locationAttributeTypeValueTemp !== "") {
								if(value1Temp) {
									if(value1Temp.length < 10 || value2Temp.length < 10) { 
										errorStr += "'" + locationAttributeTypeValueTemp + "': must have valid date range<br/>"; 
									}
								} 
								else {
									errorStr += "'" + locationAttributeTypeValueTemp + "': attribute is required.<br/>";
								}
							}
						}
					}
				}
				
				var node = {id: myBound.id, 
						locationAttributeTypeValue: locationAttributeTypeValueTemp,
						value1: value1Temp, 
						value2: value2Temp, 
						type: myBound.value, 
						locationAttributeTypeId: locationAttributeId[i], 
						locationAttributeTypeName: locationAttributeDisplayName[i] }; 
				
				if(locationAttributeTypeValueTemp) { 
					boundArr.push(node); 
				}
			} 
			
			console.log(locationAttributeDataNodes);
			if(errorStr !== "") { 
				alertDIV1("warning", "ERROR: ", errorStr, "_addLocationAttribute"); 
				return;
			}
			else {
				$('#myModalLocationAttribute').modal('toggle');
				$.ajax({
					url: "${pageContext.request.contextPath}/ws/location/newlocationattribute",
					type: "POST",
					data : { parent: parent, array: JSON.stringify(boundArr) },
					success : function(result) {
						var data = JSON.parse(result);
						var idArr = null;
						idArr = eval(data.id);
						
						var attrs = [];
						if(idArr.length === boundArr.length) {
							for (var i = 0; i < boundArr.length; i++) {
								try{
									var myLocAttrNode = { id: idArr[i], 
											typeId: boundArr[i].locationAttributeTypeId, 
											locationId: parent, 
											attributeType: getLocationAttributeType(boundArr[i].locationAttributeTypeId).name,
											value: boundArr[i].locationAttributeTypeValue, 
											typeName: boundArr[i].type, 
											typeValue1: boundArr[i].value1, 
											typeValue2: boundArr[i].value2 };
									
									attrs.push(myLocAttrNode);
								}
								catch(e){console.log(e);}
							}
						} 
						
						var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
						var parentNode = treeObj.getNodeByParam("name", $("#locationParent").val(), null);
						parentNode.attributes = attrs;
						treeObj.updateNode(parentNode);
						
						alertDIV(data.status.toString(), data.message.toString(), data.returnStr.toString());
						 
						for(var i=0; i<locationAttributeId.length; i++) { 
							document.getElementById("locationAttribute_"+locationAttributeId[i]).value = ""; 
						}
						
						$("#addLocationAttributeBody").html('');
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

function getLocationAttributeType(id){
	var attribs = getLocationAttributeTypes();
	for (var i = 0; i < attribs.length; i++) {
		if(attribs[i].id === id){
			return attribs[i];
		}
	}
	return null;
}

var count = 0;
var log, className = "dark";
var setting = {
    view: {
        addHoverDom: addHoverDom,
        removeHoverDom: removeHoverDom,
        selectedMulti: false,
        showIcon: false
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
	showLog("[ "+getTime()+" beforeCollapse ]" + treeNode.name );
	return (treeNode.collapse !== false);
}
function onCollapse(event, treeId, treeNode) {
	showLog("[ "+getTime()+" onCollapse ]" + treeNode.name);
}		
function beforeExpand(treeId, treeNode) {
	className = (className === "dark" ? "":"dark");
	showLog("[ "+getTime()+" beforeExpand ]" + treeNode.name );
	return (treeNode.expand !== false);
}
function onExpand(event, treeId, treeNode) {
	showLog("[ "+getTime()+" onExpand ]" + treeNode.name);
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
	if(!treeNode.isEditable){
		return;
	}
    className = (className === "dark" ? "":"dark");
    showLog("[ "+getTime()+" beforeEditName ] " + treeNode.name);
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
				url: "${pageContext.request.contextPath}/ws/location/updatelocationname",
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
function openLocationTypeEditForm() {
	document.getElementById("updatelocationTypeDiv").innerHTML = '';
	document.getElementById("addlocationTypeLevel").value = '';
	document.getElementById("addlocationTypeDescription").value = '';

	var nameHtml = "<select id='updatelocationTypeName' name='updatelocationTypeName' class='form-control myclass' onchange='updateLocationTypeChange()' title='Choose Location Type' style='width: 94%;'>";
	nameHtml += '<option value="" selected></option>';  

	console.log(locationTypeNodes);
	
	for (var i = 0; i < locationTypeNodes.length; i++) {
		nameHtml += '<option value="' + locationTypeNodes[i].id +'" >' + locationTypeNodes[i].name + '</option>';  
	} 
	nameHtml += "</select>";
	document.getElementById("updatelocationTypeDiv").innerHTML = nameHtml;
	
	$("#myModalLocationTypeUpdate").modal();
}
function openLocationAttributeTypeEditForm() {
	document.getElementById("updatelocationAttributeTypeDiv").innerHTML = '';
	document.getElementById("updatelocationAttributeTypeDisplayName").value = '';
	document.getElementById("updatelocationAttributeTypeDescription").value = '';
	document.getElementById("updatelocationAttributeTypeCategory").value = '';

	var nameHtml = "<select id='updatelocationAttributeTypeName' name='updatelocationAttributeTypeName' class='form-control myclass' onchange='updateLocationAttributeTypeChange()' title='Choose Location Attribute Type' style='width: 94%;'>";
	nameHtml += '<option value="" selected></option>';  

	console.log(locationAttributeNodes);
	
	for (var i = 0; i < locationAttributeNodes.length; i++) {
		nameHtml += '<option value="' + locationAttributeNodes[i].id +'" >' + locationAttributeNodes[i].name + '</option>';  
	} 
	nameHtml += "</select>";
	document.getElementById("updatelocationAttributeTypeDiv").innerHTML = nameHtml;
	
	$("#myModalLocationAttributeTypeUpdate").modal();
}
function addHoverDom(treeId, treeNode) {
	if(!treeNode.isEditable){
		document.getElementById(treeNode.tId + "_edit").style.display = "none";
		document.getElementById(treeNode.tId + "_a").title = treeNode.name + "-[" + treeNode.typeName + "]";

		return;
	}
	if(treeId === "treeDemo") {
		var sObj = $("#" + treeNode.tId + "_a");
	    var id1 = "addlocation";
	    var id2 = "addlocationattribute";
	    var id3 = "voidLocationDetail";
	    if (treeNode.editNameFlag || $("button[id^='toolBtn_"+treeNode.tId+"']").length>0) return;
	    
	    var toolBtnLocation = createTreeToolButton(treeNode.tId,id1,'Add New Location','plus',treeNode)
					    .click(function () {    
					    	$('#addParentLocationListDiv').html(
					    			'<label for="addParent" class="form-control-label">Parent Location: *</label>'+
					    			'<input location-id="'+treeNode.id+'" id="addParent" name="addParent" value="'+treeNode.name+'" readonly="readonly" class="form-control">');
					    	$("#myModal").modal();
					    });
	    var toolBtnLocationAttrib = createTreeToolButton(treeNode.tId,id2,'Add New Location Attribute' ,'plus',treeNode)
	    				.click(function () {    
	    					createLocationAttributeModal();
	    					$('#addParentLocationAttributeListDiv').html(
					    			'<label for="locationParent" class="form-control-label">Location: *</label>'+
					    			'<input location-id="'+treeNode.id+'" id="locationParent" name="locationParent" value="'+treeNode.name+'" readonly="readonly" class="form-control">');
	    					$("#myModalLocationAttribute").modal();
					    });
	    var toolBtnVoid = createTreeToolButton(treeNode.tId,id3,'Void/Un Void Location','void',treeNode)
	    				.click(function () {    
	    					createLocationAttributeModal();
	    					$('#voidLocationDiv').html(
					    			'<label for="voidLocation" class="form-control-label">Location: *</label>'+
					    			'<input location-id="'+treeNode.id+'" id="voidLocation" name="voidLocation" value="'+treeNode.name+'" readonly="readonly" class="form-control">');
	    					$("#modalVoidLocationDetail").modal();
					    });
	    
		document.getElementById(treeNode.tId + "_a").title = treeNode.name + "-[" + treeNode.typeName + "]";
		
		document.getElementById(treeNode.tId + "_edit").title = "Edit Location Name";
		document.getElementById(treeNode.tId + "_edit").style.visibility = "visible";
		// document.getElementById(treeNode.tId + "_edit").style.cssFloat = "right";
		
		sObj.append(toolBtnLocation);
		sObj.append(toolBtnLocationAttrib);
		sObj.append(toolBtnVoid);
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

function createTreeToolButton(itemId,targetId,title,imageName,clickFunction){
    return $("<button class='toolbar-action-button' style='background-image: url(images/"+imageName+".png)' " +
    			"id='toolBtn_"+itemId+"_"+targetId+"' title='"+title+"' ></button>");
}
function removeHoverDom(treeId, treeNode) {
    $("button[id^='toolBtn_"+treeNode.tId+"']").unbind().remove();
};
function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
}
// TODO refactor
function onClick(e, treeId, treeNode) {
	if(treeNode != null) {
		parentName = treeNode.name;
	}
    if(jQuery.isEmptyObject(treeNode.children) !== true) { GenerateTable(treeNode.children, treeNode.name, treeNode.id); }
    else { document.getElementById("dvTable").innerHTML = "<h2>Child Locations of '" + "<a style='cursor:pointer' title='View Details of " + treeNode.name + "' onclick='viewLocationDetails(" + JSON.stringify(treeNode.id) + ");'>" + treeNode.name + "</a>" + "'</h2><br/><br/><p>&emsp;No Child Locations associated with '"+treeNode.name+"'.</p>"; }
}
function addAll(id) {
    var name = document.getElementById("addName").value;
    var shortName = document.getElementById("addShortName").value;
    var fullName = document.getElementById("addFullName").value;
    var parent = !document.getElementById("addParent")?null:document.getElementById("addParent").value;
    var description = document.getElementById("addDescription").value;
    var otherIdentifier = document.getElementById("addOtherIdentifier").value;
    var type = document.getElementById("addType").value;
    var typeName = document.getElementById("addType").options[document.getElementById("addType").selectedIndex].text;
    var latitude = document.getElementById("addLatitude").value;
    var longitude = document.getElementById("addLongitude").value;    
    var node;
    
    console.log(id +"-"+name+"-"+type);
    
    if(id != "" && name != "" && type != "") {
    	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
 	    var parentNode = treeObj.getNodeByParam("name", parent, null);
 	    
    	node = { id: id, name: name, shortName: shortName, fullName: fullName, 
    			pId: parentNode?parentNode.id:null, pName: parent, description: description, otherIdentifier: otherIdentifier, type: type, typeName: typeName, latitude: latitude, longitude: longitude, open:true};
    	
    	zNodes.push(node);
    	addToTable(node);
	    	    
	    newNode = treeObj.addNodes(parentNode, node);
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
	    
		if(myNodes){
			while(myNodes[i] !== undefined) {
				if(myNodes[i].isEditable){
		    	editArray.push("<a name='editBtn' id='editBtn' class='icon' style='cursor:pointer;'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>");
				}
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
	}
    var arr = [editArray.length, idArray.length, nameArray.length, shortNameArray.length, fullNameArray.length, pIdArray.length, pNameArray.length, descriptionArray.length, otherIdentifierArray.length, typeArray.length, typeNameArray.length];
    arr = arr.sort();
    arr = arr.reverse();
    var rowsAfterHeader = arr[0];
    var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "datatable";
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

        var cell = row.insertCell(-1); var id = "row"+i+"_"+count;

        if(parentName === "VOIDED LOCATIONS") { }
        else if(myNodes[i].isEditable){
            cell.innerHTML = "<a name='editBtn' id='editBtn' class='icon' title='Update "+nameArray[i]+"' onclick='editTableRow(\""+id+"\");' style='cursor:pointer;'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";
            cell.id= "row"+i+"_"+count; count++;
        } else { cell.innerHTML = ""; }
        
        var val = null; val = idArray[i];
        
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
function addToTable(node) {
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
	    if(node.pName !== undefined) { cell.innerHTML = node.pName; }
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
//TODO get rid of this method and do appropriate refactoring
function addToModal(id, node) {
	console.log("HI I AM HERE");
	
	var tag1 = "addlocationattribute";
	var bodyDiv = document.getElementById("myLocationAttributeAdd");
	var div = document.createElement("div");
    div.setAttribute("class", "form-group");

    var descriptionLabel = createLabel("locationAttribute_"+id, "form-control-label", 
    		node.displayName + ":");
   
    div.appendChild(descriptionLabel);

    var descriptionValue = document.createElement('input');
    descriptionValue.setAttribute("type", "number");
    descriptionValue.setAttribute("name", "locationAttribute_"+id);
    descriptionValue.setAttribute("id", "locationAttribute_"+id);
    descriptionValue.setAttribute("class", "form-control");
    descriptionValue.setAttribute("value", "");
    descriptionValue.setAttribute("title", "Enter "+node);
    descriptionValue.setAttribute("placeholder", "Enter "+node);
    descriptionValue.setAttribute("maxlength", "255");
    descriptionValue.setAttribute("onfocus", "removeFocus(\'"+tag1+"\');");
    div.appendChild(descriptionValue);

    bodyDiv.appendChild(div);
    
    var radioHTML = createAttributeNatureWidget(locationAttributeId[i]);
    
    bodyDiv.innerHTML += radioHTML;
	
    //TODO Remove lines below after refactor
	document.getElementById("addlocationAttributeTypeName").value = "";
	document.getElementById("addlocationAttributeTypeDisplayName").value = "";
	document.getElementById("addlocationAttributeTypeDescription").value = "";
	document.getElementById("addlocationAttributeTypeCategory").value = "";
}

function createAttributeNatureWidget(attributeId){
	var div = document.getElementById("bound_"+attributeId+"_DIV");
	if(!div){
		div = document.createElement("DIV");
	    div.setAttribute("id", "bound_"+attributeId+"_DIV");
	    div.style.display='inline-block';
	    div.style.cssFloat='right';
	}
	var radioHTML = "";
    radioHTML += '<div id="bound_'+attributeId+'_RADIO" class="form-group">';
    radioHTML += '<input type="radio" id="bound_'+attributeId+'_Text" name="bound_'+attributeId+'" onclick="myFunctionChecks(this)" value="Text" checked="checked"> Text ';
    radioHTML += '<input type="radio" id="bound_'+attributeId+'_Anually" name="bound_'+attributeId+'" onclick="myFunctionChecks(this)" value="Anually"> Anually ';
    radioHTML += '<input type="radio" id="bound_'+attributeId+'_Monthly" name="bound_'+attributeId+'" onclick="myFunctionChecks(this)" value="Monthly"> Monthly '; 
    radioHTML += '<input type="radio" id="bound_'+attributeId+'_Daily" name="bound_'+attributeId+'" onclick="myFunctionChecks(this)" value="Daily"> Daily ';
    radioHTML += '<input type="radio" id="bound_'+attributeId+'_Time_Bound" name="bound_'+attributeId+'" onclick="myFunctionChecks(this)" value="Time_Bound"> Time Bound ';  
    radioHTML += '</div>';
    
    div.innerHTML = radioHTML;
    
    return div;
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
				url: "${pageContext.request.contextPath}/ws/location/updatelocation",
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

function createLabel(forAttribute, classAttribute, text){
	var label = document.createElement("LABEL");
	label.setAttribute("for", forAttribute);
	label.setAttribute("class", classAttribute);
	label.innerHTML = text;
	
	return label;
}

function createLocationAttributeModal(){
	var formDIV = document.getElementById("addLocationAttributeBody");
	formDIV.innerHTML = '';
	
	var form = document.createElement('div');
	form.id = "locatioAttributeForm";
	form.name = "locatioAttributeForm";
	formDIV.appendChild(form);

	var bodyDiv = document.createElement("div");
	//bodyDiv.id="locationAttributeModal"	;
	bodyDiv.setAttribute("class", "modal-body");
	bodyDiv.setAttribute("id", "myLocationAttributeAdd");
	form.appendChild(bodyDiv);

	var div = document.createElement("div");
	div.setAttribute("class", "form-group");

	var descriptionValue = document.createElement('div');
	descriptionValue.setAttribute("id", "addParentLocationAttributeListDiv");
	div.appendChild(descriptionValue);

	bodyDiv.appendChild(div);

	for(var i = 0; i<locationAttributeId.length; i++) {
		if(locationAttributeDisplayName[i] === "" || locationAttributeDisplayName[i] === null) {}
		else {
			var tag1 = "addlocationattribute";
		    var div = document.createElement("div");
		    div.setAttribute("class", "form-group");
		
		    var descriptionLabel = createLabel("locationAttribute_"+locationAttributeId[i], 
		    		"form-control-label", locationAttributeDisplayName[i] + ":");
		    div.appendChild(descriptionLabel);
		    	    
		    var attributeNature = createAttributeNatureWidget(locationAttributeId[i]);
		    
		    div.appendChild(attributeNature);
		    
		    var innerdiv = document.createElement("div");
		    innerdiv.setAttribute("class", "form-group");
		    
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
		    innerdiv.appendChild(descriptionValue);
		    
		    innerdiv.innerHTML += '<div id="bound_'+locationAttributeId[i]+'_tb_div" style="margin-left:20px; display: none"></div>';

		    div.appendChild(innerdiv);
		    
		    bodyDiv.appendChild(div);
		}
	}	
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

//TODO MOVE ALL THESE VALIDATIONS TO SERVER 
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
// TODO DELETE IT
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
//TODO DELETE IT
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
// TODO delete this 
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
	var headerArray = ["location"];
	
	for (var i = 0; i < locationAttributeNodes.length; i++) {
		if(locationAttributeNodes[i].dataType && locationAttributeNodes[i].dataType.toLowerCase().indexOf('text') !== -1)
		headerArray.push(locationAttributeNodes[i].name);
	}
	
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
	    cell.innerHTML = "[Location " + (i+1) + "]";
	    
	    // skip column 1 i.e. location
	    for (var i = 1; i < headerArray.length; i++) {
		    var cell = row.insertCell(i);
		    cell.innerHTML = "value";
		}
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
//TODO rename
function myFunctionChecks(myBound,value1,value2) {
	if(!value1){
		value1 = "";
	}
	
	if(!value2){
		value2 = "";
	}
	
	var currentDate = getCurrentDate();
	var div = document.getElementById(myBound.name+"_DIV");
	//div.innerHTML = "";
	
	var attributeInput = $('#'+myBound.name+"_DIV").parent().find('.form-control');
	var timeboundDiv = $('#'+myBound.name+'_tb_div');
	timeboundDiv.html('');
	timeboundDiv.hide();
	
	var value = myBound.value;
	while (value.includes("_")) { value = value.replace("_", " "); }
	
    if(myBound.value === "Text") {
    	attributeInput.css('width', '100%');
    	attributeInput.css('display','block');
    	
    	timeboundDiv.html('');
    	return;
    }
   
    timeboundDiv.show();
    timeboundDiv.css('display','inline-block');

	attributeInput.css('width', '300px');
	attributeInput.css('display','inline-block');
		
    if(myBound.value === "Anually") {
    	timeboundDiv.append('<input type="number" name="val_'+myBound.id+'" id="val_'+myBound.id+'" '+
    			'min="1990" class="form-control" title="Enter Year" placeholder="Year" value="'+value1+'"/>');	
	}
	else if(myBound.value === "Monthly") {
    	timeboundDiv.append('<input type="month" name="val_'+myBound.id+'" id="val_'+myBound.id+'" '+ 
    			'max="'+currentDate.substring(0, 7)+'" "onkeydown"="return false" class="form-control" '+
    			'title="Enter Month" placeholder="Month" value="'+value1+'"/>');
	}
	else if(myBound.value === "Daily") {
		timeboundDiv.append('<input type="date" name="val_'+myBound.id+'" id="val_'+myBound.id+'" '+ 
    			'max="'+currentDate.substring(0, 7)+'" "onkeydown"="return false" class="form-control" '+
    			'title="Enter Day" placeholder="Day" value="'+value1+'"/>');
	}
    else if(myBound.value === "Time_Bound") {
    	timeboundDiv.append('<input type="date" name="val_'+myBound.id+'_From" id="val_'+myBound.id+'_From"'+ 
    			'max="'+currentDate+'" "onchange"="setDateOfTo(this)" "onkeydown"="return false" class="form-control" '+
    			'title="Enter Date From" placeholder="Date From" value="'+value1+'"/>');
    	
    	timeboundDiv.append('<input type="date" name="val_'+myBound.id+'_To" id="val_'+myBound.id+'_To" '+ 
    			'max="'+currentDate+'" "onkeydown"="return false" class="form-control" '+
    			'title="Enter Date To" placeholder="Date To" value="'+value2+'"/>');
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
	document.getElementById("myAlertDIV").innerHTML = 
		"<div id='alertDIV' class='alert alert-"+messageStatus+" alert-dismissable' role='alert'>" +
			"<button class='close' data-dismiss='alert' aria-label='close'>&times;</button>" +
			"<strong>"+messageHeading.replace(":", "")+"</strong> "+messageText+"" +
		"</div>"; 
		$("#alertDIV").alert();
   // $("#myAlertDIV").fadeTo(10000, 10000).slideUp(2000, function(){ $("#myAlertDIV").slideUp(5000); });   
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

    appendTableRow(table, ["Name: ", nameLocation]);
    appendTableRow(table, ["Short Name: ", shortNameLocation]);
    appendTableRow(table, ["Full Name: ", fullNameLocation]);
    appendTableRow(table, ["Parent Id: ", pIdLocation]);
    appendTableRow(table, ["Parent Name: ", pNameLocation]);
    appendTableRow(table, ["Type: ", typeLocation]);
    appendTableRow(table, ["Type Name: ", typeNameLocation]);
    appendTableRow(table, [ "Description: ", descriptionLocation]);
    appendTableRow(table, ["Identifier: ", otherIdentifierLocation]);
    appendTableRow(table, ["Latitude: ", latitudeLocation]);
    appendTableRow(table, ["Longitude: ", longitudeLocation]);

    if(node.attributes) { 
    	for (var i = 0; i < node.attributes.length; i++) {
    		appendTableRow(table, [node.attributes[i].attributeType, node.attributes[i].value]);
    	} 
    }
	var html = ""; 
	html += "<h4 style='text-align: center;'>LOCATION DETAIL</h4><table id='locationTable' name='locationTable' style=' width: 100%;'>" + table.innerHTML + "</table>"
	
	//TODO ADD OTHER ATTRIBUTES WITH TIME DIM
	//if (myWindow1) { myWindow1.close(); }
	//myWindow1 = window.open("", "myWindow1", "width=500,height=500"); myWindow1.document.write("<html><head><title>LOCATION DETAILS OF '" + node.name.toUpperCase() + "'</title><style>table, th, td { border: 1px solid silver; border-collapse: collapse; }</style></head><body>" + html + "</body></html>");
	$("#alertModalHead").html("LOCATION DETAILS OF '" + node.name.toUpperCase() + "'");
	$("#alertModalBody").html(html);
	$('#alertModal').modal({ backdrop: 'static', keyboard: false }); 
	$("#alertModal").modal('show');
}

function appendTableRow(table, rowItems){
	row = table.insertRow(-1);

	for (var i = 0; i < rowItems.length; i++) {
		row.insertCell(-1).innerHTML = rowItems[i];
	}
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
	$("#alertModalBody").html("<table id='locationTypeDetail' name='locationTypeDetail' style=' width: 100%;'>" + table.innerHTML + "</table>"); 
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
		    var value = row.insertCell(-1); value.innerHTML = '<div class="wordwrap" style="width: 200px">'+name+'</div>';
		    var value = row.insertCell(-1); value.innerHTML = displayName;
		    var value = row.insertCell(-1); value.innerHTML = !description?"":description;
		    var value = row.insertCell(-1); value.innerHTML = !category?"":category;
		}
	}
    if(table.innerHTML === "") {
    	var row = table.insertRow(-1);
		for (var i = 0; i < headerArray.length; i++) {
			var headerCell = document.createElement("TH");
			headerCell.innerHTML = headerArray[i];
			row.appendChild(headerCell);
		}
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
	$("#alertModalBody").html("<table id='locationAttributeTypeDetail' name='locationAttributeTypeDetail' style=' width: 100%;'>" + table.innerHTML + "</table>"); 
	$('#alertModal').modal({ backdrop: 'static', keyboard: false }); 
	$("#alertModal").modal('show');
}
// TODO why these two methods below are needed?????
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

function setFocus(id) {
	try{
		if(id==="addlocation") { document.getElementById("addName").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("addName").setAttribute("autofocus", "autofocus"); }
		else if(id==="addlocationtype") { document.getElementById("addlocationTypeName").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("addlocationTypeName").setAttribute("autofocus", "autofocus"); }
		else if(id==="addlocationattribute") { document.getElementById("locationParent").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("locationParent").setAttribute("autofocus", "autofocus"); }
		else if(id==="addlocationattributetype") { document.getElementById("addlocationAttributeTypeName").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("addlocationAttributeTypeName").setAttribute("autofocus", "autofocus"); }
		else if(id==="addlocationattributebutton") { document.getElementById("nextBtnLocationAttribute").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("nextBtnLocationAttribute").setAttribute("autofocus", "autofocus"); }
		else if(id==="updatelocationtype") { document.getElementById("updatelocationTypeName").setAttribute("style", "width: 94%; outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("updatelocationTypeName").setAttribute("autofocus", "autofocus"); }
		else if(id==="updatelocationattributetype") { document.getElementById("updatelocationAttributeTypeName").setAttribute("style", "width: 94%; outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("updatelocationAttributeTypeName").setAttribute("autofocus", "autofocus"); }
		else if(id==="voidLocationDetail") { document.getElementById("voided").setAttribute("style", "outline: none !important; border-color: #66afe9; box-shadow: 0 0 7px #66afe9;"); document.getElementById("voided").setAttribute("autofocus", "autofocus"); }
	}
	catch(e){console.log(e);}
}
function removeFocus(id) {
	try{
		if(id==="addlocation") { document.getElementById("addName").setAttribute("style", ""); }
		else if(id==="addlocationtype") { document.getElementById("addlocationTypeName").setAttribute("style", ""); }
		else if(id==="addlocationattribute") { document.getElementById("locationParent").setAttribute("style", ""); }
		else if(id==="addlocationattributetype") { document.getElementById("addlocationAttributeTypeName").setAttribute("style", ""); }
		else if(id==="addlocationattributebutton") { document.getElementById("nextBtnLocationAttribute").setAttribute("style", ""); }
		else if(id==="updatelocationtype") { document.getElementById("updatelocationTypeName").setAttribute("style", ""); }
		else if(id==="updatelocationattributetype") { document.getElementById("updatelocationAttributeTypeName").setAttribute("style", ""); }
		else if(id==="voidLocationDetail") { document.getElementById("voided").setAttribute("style", ""); }
	}
	catch(e){console.log(e);}
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
				url: "${pageContext.request.contextPath}/ws/location/updatelocationtypename",
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
				url: "${pageContext.request.contextPath}/ws/location/updatelocationattributetypename",
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