var locationAttributeId = [];
var locationAttributeName = [];
var locationAttributeDisplayName = [];
var locationAttributeCategory = [];
var locationAttributeDescription = [];
var locationAttributeNodes = getLocationAttributeTypes();
if(locationAttributeNodes !== null && typeof locationAttributeNodes !== 'undefined') {
	for(var i = 0; i<locationAttributeNodes.length; i++) {
		locationAttributeId.push(locationAttributeNodes[i].id);
		locationAttributeName.push(locationAttributeNodes[i].name);
		locationAttributeDisplayName.push(locationAttributeNodes[i].displayName);
		locationAttributeDescription.push(locationAttributeNodes[i].description);
		locationAttributeCategory.push(locationAttributeNodes[i].category);
	}
}

var formDIV = document.getElementById("form_sample");

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
var footerDiv = document.createElement("div");
footerDiv.setAttribute("class", "modal-footer");
form.appendChild(footerDiv);

var button = document.createElement('input'); 
button.setAttribute("id", "nextBtnLocationAttribute");
button.setAttribute("name", "nextBtnLocationAttribute");
button.setAttribute("type", "button");
button.setAttribute("data-toggle", "modal");
button.setAttribute("data-target", "#myModalLocationAttributeType");
button.setAttribute("data-backdrop", "static");
button.setAttribute("data-keyboard", "false");
button.setAttribute("data-dismiss", "modal");
button.setAttribute("value", "Add New Location Attribute Type");
button.setAttribute("title", "Add New Location Attribute Type");
button.setAttribute("class", "btn btn-default transparent pull-left");
footerDiv.appendChild(button);

var button = document.createElement('input'); 
button.setAttribute("type", "button");
button.setAttribute("id", "closeBtnLocationAttribute");
button.setAttribute("name", "closeBtnLocationAttribute");
button.setAttribute("data-dismiss", "modal");
button.setAttribute("value", "Close");
button.setAttribute("title", "Close");
button.setAttribute("class", "btn btn-default transparent");
footerDiv.appendChild(button);

var button = document.createElement('input'); 
button.setAttribute("type", "submit");
button.setAttribute("id", "addAlllocationAttributes");
button.setAttribute("name", "addAlllocationAttributes");
//button.setAttribute("data-dismiss", "modal");
button.setAttribute("value", "Add Location Attribute");
button.setAttribute("title", "Add Location Attribute");
button.setAttribute("class", "btn btn-default transparent");
footerDiv.appendChild(button);