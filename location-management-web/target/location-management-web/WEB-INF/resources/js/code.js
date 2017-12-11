var zNodes = getLocations();
var locationTypeNodes = getLocationTypes();
var parentName;
var locationAttributeNodes = getLocationAttributeTypes();
var locationAttributeId = [];
var locationAttributeName = [];
var locationAttributeDescription = [];
var locationAttributeCategory = [];
var globalRowId;
var nodesToBeSaved = [];
alert(zNodes);
for(var i = 0; i<locationAttributeNodes.length; i++) {
	locationAttributeId.push(locationAttributeNodes[i].id);
	locationAttributeName.push(locationAttributeNodes[i].name);
	locationAttributeDescription.push(locationAttributeNodes[i].description);
	locationAttributeCategory.push(locationAttributeNodes[i].category);
}

var disabledNodes = [66, 77 ,88 ,99];
$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	
    $("#selectAll").bind("click", selectAll);
    $("#saveBtn").bind("click", saveTableRow);
	for (var i = 0; i < locationTypeNodes.length; i++) {
		var itemval= '<option value="' + locationTypeNodes[i].id +'">' + locationTypeNodes[i].name + '</option>';
		$("#addType").append(itemval);
    }
	$("#expandAllBtn").bind("click", {type:"expandAll"}, expandNode);
	$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);

	
	$("#addAllNodes").bind("click", function() {
		if(document.getElementById("addName").value !== "" && document.getElementById("addType").value !== "" ) {
			$.ajax({
				url: "/LocationDatabaseManagement/data/location/newlocation",
				type: "POST",
				data : $("#nodeForm").serialize(),
				success : function(id) {
					addAll(id);
					alert("New Location Added");
				}, error: function(jqXHR, textStatus, errorThrown) { alert("Location Data already exists!"); }
			});
		}
		else {//VALIDATION
			if(document.getElementById("addName").value === "" && document.getElementById("addType").value === "") { alert("Name and Type is required"); }
			else if(document.getElementById("addName").value === "") { alert("Name is required"); }	
			else if(document.getElementById("addType").value === "") { alert("Type is required"); }	
		 }
	});	
	$("#addAlllocationTypes").bind("click", function() {
		if(document.getElementById("addlocationTypeName").value !== "" ) {
			$.ajax({
				url: "/LocationDatabaseManagement/data/location/newlocationType",
				type: "POST",
				data : $("#locationTypeForm").serialize(),
				success : function(id) {
					var name = document.getElementById("addlocationTypeName").value;
					var level = document.getElementById("addlocationTypeLevel").value;
					var node = { id: id, name: name, level: level };
					var itemval= '<option value="' + node.id +'">' + node.name + '</option>';
					$("#addType").append(itemval);
					locationTypeNodes.push(node);
					alert("New Location Type Added");
				}, error: function(jqXHR, textStatus, errorThrown) { alert("Location Type Data already exists!"); }
			});
		}
		else {//VALIDATION
			if(document.getElementById("addlocationTypeName").value === "") {
				alert("Location Type Name is required");
			}	
		 }
	});	
	$("#addAlllocationAttributeTypes").bind("click", function() {
		if(document.getElementById("addlocationAttributeTypeName").value !== "" ) {
			$.ajax({
				url: "/LocationDatabaseManagement/data/location/newlocationattributetype",
				type: "POST",
				data : $("#locationAttributeTypeForm").serialize(),
				success : function(id) {
					addToModal(id);
					locationAttributeId.push(id)
					alert("New Attribute Type Created");
				}, error: function(jqXHR, textStatus, errorThrown) { alert("Location Attribute Type Data already exists!"); }
			});
		}
		else {
			if(document.getElementById("addlocationAttributeTypeName").value === "" ) {
				alert("Location Attribute Type Name is required");
			}
		}
	});	
	$("#addAlllocationAttributes").bind("click", function() {
		$.ajax({
			url: "/LocationDatabaseManagement/data/location/newlocationattribute",
			type: "POST",
			data : $("#locatioAttributeForm").serialize(),
			success : function(ids) {
				for(var i=0; i<locationAttributeId.length; i++) { document.getElementById("locationAttribute_"+locationAttributeId[i]).value = ""; }
				alert("New Attribute Created");
			}, error: function(jqXHR, textStatus, errorThrown) { alert("Location Attribute Data already exists!"); }
		});
	});	
});
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
        showRenameBtn: false
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeDrag: beforeDrag,
        beforeEditName: beforeEditName,
        beforeRemove: beforeRemove,
        beforeRename: beforeRename,
        onRemove: onRemove,
        onRename: onRename,
        onClick: onClick,
        beforeCollapse: beforeCollapse,
		beforeExpand: beforeExpand,
		onCollapse: onCollapse,
		onExpand: onExpand
    }
};
var count = 0;
var log, className = "dark";
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
function beforeRemove(treeId, treeNode) {
    className = (className === "dark" ? "":"dark");
    showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    return confirm("Confirm delete node '" + treeNode.name + "' it?");
}
function onRemove(e, treeId, treeNode) {
    showLog("[ "+getTime()+" onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
}
function beforeRename(treeId, treeNode, newName, isCancel) {
    className = (className === "dark" ? "":"dark");
    showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
    if (newName.length === 0) {
        setTimeout(function() {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.cancelEditName();
            alert("Node name can not be empty.");
        }, 0);
        return false;
    }
    return true;
}
function onRename(e, treeId, treeNode, isCancel) {
    showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
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
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addNodeBtn_"+treeNode.tId).length>0) return;
    if (treeNode.editNameFlag || $("#addStatBtn_"+treeNode.tId).length>0) return;
    var addStr1 = "<button type='button' id='addNodeBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Add New Location' data-toggle='modal' data-target='#myModal' ><img src='images/plus.png' width='15px' height='15px'></button>";
    var addStr2 = "<button type='button' id='addStatBtn_" + treeNode.tId + "' style='background: transparent; background-color: Transparent; background-repeat:no-repeat; border: none; cursor:pointer; overflow: hidden; outline:none;' title='Add New Lcation Attribute' data-toggle='modal' data-target='#myModalLocationAttribute' ><img src='images/plus.png' width='15px' height='15px'></button>";
    sObj.after(addStr1 + "" + addStr2);
};
function removeHoverDom(treeId, treeNode) {
    $("#addNodeBtn_"+treeNode.tId).unbind().remove();
    $("#addStatBtn_"+treeNode.tId).unbind().remove();
};
function selectAll() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
}
function onClick(e, treeId, treeNode) {
	if(treeNode != null) {
		document.getElementById("locationParent").value = treeNode.id;
		document.getElementById("addParent").value = treeNode.id;
		parentName = treeNode.name;
	}
    if(jQuery.isEmptyObject(treeNode.children) !== true) GenerateTable(treeNode.children, treeNode.name);
    else document.getElementById("dvTable").innerHTML = "";
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
    var node;
    if(id != "" && name != "" && parent != "" && type != "") {
        node = { id: id, name: name, shortName: shortName, fullName: fullName, pId: parent, parentName: parentName, description: description, otherIdentifier: otherIdentifier, type: type, typeName: typeName, open:true};
        document.getElementById("errors").innerHTML = "";
        document.getElementById("addName").value = "";
        document.getElementById("addShortName").value = "";
        document.getElementById("addFullName").value = "";
        document.getElementById("addParent").value = "";
        document.getElementById("addDescription").value = "";
        document.getElementById("addOtherIdentifier").value = "";
        document.getElementById("addType").value = "";
    	zNodes.push(node);
	    addToTree(node);
	    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    }
}
function GenerateTable(myNodes, parentName) {
    var headerArray = ["", "Id", "Name", "Short Name", "Full Name", "Other Identifier", "Type Name", "Description"];
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
    while(myNodes[i] !== undefined) {
    	
    	editArray.push("<a name='editBtn' id='editBtn' class='icon'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>");
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
    var arr = [editArray.length, idArray.length, nameArray.length, shortNameArray.length, fullNameArray.length, pIdArray.length, pNameArray.length, descriptionArray.length, otherIdentifierArray.length, typeArray.length, typeNameArray.length];
    arr = arr.sort();
    arr = arr.reverse();
    var rowsAfterHeader = arr[0];
    var table = document.createElement("TABLE");
    table.border = "1";
    table.id = "datatable";
    var row = table.insertRow(-1);
    for (var i = 0; i < headerArray.length; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerArray[i];
//        headerCell.width = '35px';
        row.appendChild(headerCell);
    }
    var count=0;
    for (var i = 0; i < rowsAfterHeader; i++) {;
        row = table.insertRow(-1);
        row.id = "row"+i;

        var cell = row.insertCell(-1);
        if(editArray[i] !== undefined) { cell.innerHTML = editArray[i]; }
        else { cell.innerHTML = ""; }
        cell.id= "row"+i+"_"+count; count++;
        cell.addEventListener("click", function(){ editTableRow(this.id); }, true);
        //cell.onclick = alert(i+1);

        var cell = row.insertCell(-1);
        if(idArray[i] !== undefined) { cell.innerHTML = idArray[i]; }
        else { cell.innerHTML = ""; }

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
        if(otherIdentifierArray[i] !== undefined) { cell.innerHTML = otherIdentifierArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(typeNameArray[i] !== undefined) { cell.innerHTML = typeNameArray[i]; }
        else { cell.innerHTML = ""; }

        var cell = row.insertCell(-1);
        if(descriptionArray[i] !== undefined) { cell.innerHTML = descriptionArray[i]; }
        else { cell.innerHTML = ""; }

    }
    document.getElementById("dvTable").innerHTML = "<h2>Child Locations of " + parentName + "</h2>";
    document.getElementById("dvTable").appendChild(table);
}
function addToTree(node) {
	var i = document.getElementById("datatable").rows.length;
	var table = document.getElementById("datatable");
	var row = table.insertRow(-1);
    row.id = "row"+i;
	var edit = "<a name='editBtn' id='editBtn' class='icon'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";
	
	var cell = row.insertCell(-1);
    if(edit !== undefined) { cell.innerHTML = edit; }
    else { cell.innerHTML = ""; }
    cell.id= "row"+i+"_"+count; count++;
    cell.addEventListener("click", function(){ editTableRow(this.id, true); }, true);
    
    var cell = row.insertCell(-1);
    if(node.id !== undefined) { cell.innerHTML = node.id; }
    else { cell.innerHTML = ""; }

    var cell = row.insertCell(-1);
    if(node.name !== undefined) { cell.innerHTML = node.name; }
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
function addToModal(id) {
	var bodyDiv = document.getElementById("locationAttributeModal");
	
	var div = document.createElement("div");
    div.setAttribute("class", "form-group");

    var descriptionLabel = document.createElement('label');
    descriptionLabel.innerHTML = document.getElementById("addlocationAttributeTypeName").value;
    descriptionLabel.setAttribute("for", "locationAttribute_"+id);
    descriptionLabel.setAttribute("class", "form-control-label");
    div.appendChild(descriptionLabel);

    var descriptionValue = document.createElement('input');
    descriptionValue.setAttribute("type", "number");  
    descriptionValue.setAttribute("name", "locationAttribute_"+id);
    descriptionValue.setAttribute("id", "locationAttribute_"+id);
    descriptionValue.setAttribute("class", "form-control");
    descriptionValue.setAttribute("value", "");
    div.appendChild(descriptionValue);

    bodyDiv.appendChild(div);
	
	document.getElementById("addlocationAttributeTypeName").value = "";
	document.getElementById("addlocationAttributeTypeDescription").value = "";
	document.getElementById("addlocationAttributeTypeCategory").value = "";
}
function editTableCell(cell) {
	cell.contentEditable = false;
}
function editTableRow(cellId) {
    var rowId = cellId.substr(0, cellId.indexOf("_"));
    for (var i=0; i<document.getElementById(rowId).cells.length; i++) {
	  	if(i===0) {
	  		document.getElementById(rowId).cells[i].innerHTML = "<a name='saveBtn' id='saveBtn'  onclick='saveTableRow();' class='icon'><img alt='edit' src='images/tick.png' class='icon' width='15px' height='15px'></a>";
	  	}
	  	else if(i===1) { }
	  	else {
		  	document.getElementById(rowId).cells[i].contentEditable = true;
		  	document.getElementById(rowId).cells[i].style.backgroundColor = "#FFFF00";
	  	}
	}
    globalRowId = rowId;
}

function saveTableRow() {
	var treeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	var txt;
    var r = confirm("Would you like to save it?");
    if (r === true) {
		var node = treeObj.getNodeByParam('id', document.getElementById(globalRowId).cells[1].innerHTML);
		if(node.id == document.getElementById(globalRowId).cells[1].innerHTML) { 
			node.name = document.getElementById(globalRowId).cells[2].innerHTML;
			node.shortName = document.getElementById(globalRowId).cells[3].innerHTML;
			node.fullName = document.getElementById(globalRowId).cells[4].innerHTML;
			node.otherIdentifier = document.getElementById(globalRowId).cells[5].innerHTML;
			node.typeName = document.getElementById(globalRowId).cells[6].innerHTML;
			node.description = document.getElementById(globalRowId).cells[7].innerHTML;
			document.getElementById(globalRowId).cells[2].contentEditable = false;
			document.getElementById(globalRowId).cells[3].contentEditable = false;
			document.getElementById(globalRowId).cells[4].contentEditable = false;
			document.getElementById(globalRowId).cells[5].contentEditable = false;
			document.getElementById(globalRowId).cells[6].contentEditable = false;
			document.getElementById(globalRowId).cells[7].contentEditable = false;
		  	document.getElementById(globalRowId).cells[2].style.backgroundColor = "";
		  	document.getElementById(globalRowId).cells[3].style.backgroundColor = "";
		  	document.getElementById(globalRowId).cells[4].style.backgroundColor = "";
		  	document.getElementById(globalRowId).cells[5].style.backgroundColor = "";
		  	document.getElementById(globalRowId).cells[6].style.backgroundColor = "";
		  	document.getElementById(globalRowId).cells[7].style.backgroundColor = "";
		}
		treeObj.updateNode(node);
		document.getElementById(globalRowId).cells[0].innerHTML = "<a name='editBtn' id='editBtn' class='icon'><img alt='edit' src='images/edit-icon.png' class='icon' width='15px' height='15px'></a>";
		$.ajax({
			url: "/LocationDatabaseManagement/data/location/newlocationupdate",
			type: "POST",
			data : { id: node.id , name: node.name, shortName: node.shortName, fullName: node.fullName, pId: node.pId, description: node.description, otherIdentifier: node.otherIdentifier, type: node.type, typeName: node.typeName},
			success : function(id) {
				$("#successMsg").html("<strong>Location Updated.</strong>");
				$("#successMsg").delay(5000).fadeOut();
			}
		});
    }
    else {
		var node = treeObj.getNodeByParam('id', document.getElementById(globalRowId).cells[1].innerHTML);
		if(node.id == document.getElementById(globalRowId).cells[1].innerHTML) { 
			document.getElementById(globalRowId).cells[2].innerHTML = node.name;
			document.getElementById(globalRowId).cells[3].innerHTML = node.shortName;
			document.getElementById(globalRowId).cells[4].innerHTML = node.fullName;
			document.getElementById(globalRowId).cells[5].innerHTML = node.otherIdentifier;
			document.getElementById(globalRowId).cells[6].innerHTML = node.typeName;
			document.getElementById(globalRowId).cells[7].innerHTML = node.description;
			document.getElementById(globalRowId).cells[2].contentEditable = true;
			document.getElementById(globalRowId).cells[3].contentEditable = true;
			document.getElementById(globalRowId).cells[4].contentEditable = true;
			document.getElementById(globalRowId).cells[5].contentEditable = true;
			document.getElementById(globalRowId).cells[6].contentEditable = true;
			document.getElementById(globalRowId).cells[7].contentEditable = true;
		}
		treeObj.updateNode(node);
		alert(node.toString());
    }
}
function removeDuplicates(arr){
    var tmp = [];
    for(var i = 0; i < arr.length; i++){ if(tmp.indexOf(arr[i]) == -1){ tmp.push(arr[i]); } }
    return tmp;
}
function remove(array, value) {
	var newArray = [];
    for(var i=0; i<array.length; i++) {
    	if(array[i]==value) {}
		else {
			newArray.push(array[i]);
        }
	}
    return newArray;
}