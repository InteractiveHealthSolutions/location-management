<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="location-management-web/src/main/webapp/WEB-INF/css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css">
        <link rel="stylesheet" href="css/jquery-ui.css" type="text/css">
        <link rel="stylesheet" href="css/zTreeStyle/zTreeStyle.css" type="text/css">
        <link rel="stylesheet" href="css/mystyle.css" type="text/css">

        <script type="text/javascript"> function getVoidedLocations()  { return ${voidedLocationNodes}; } </script>
        <script type="text/javascript"> function getLocations()  { return ${locationNodes}; } </script>
        <script type="text/javascript"> function getLocationTypes() { return ${locationTypeNodes}; } </script>
        <script type="text/javascript"> function getLocationAttributes() { return ${locationAttributeNodes}; } </script>
        <script type="text/javascript"> function getLocationAttributeTypes() { return ${locationAttributeTypeNodes}; } </script>
        <script src="js/jquery/jquery.min.js" type="text/javascript"></script>
        <script src="js/jquery/jquery.csv.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="src/main/webapp/WEB-INF/js/ztree/jquery.ztree.core.js"></script>
        <script type="text/javascript" src="js/ztree/jquery.ztree.excheck.js"></script>
        <script type="text/javascript" src="js/ztree/jquery.ztree.exedit.js"></script>
        <script type="text/javascript" src="js/exportcsv/excellentexport.min.js"></script>
        <script>
	        function setLocationNames() {
	            var name = document.getElementById("addName").value;
	            document.getElementById("addShortName").value = name;
	            document.getElementById("addShortName").innerHTML = name;
	            document.getElementById("addFullName").value = name;
	            document.getElementById("addFullName").innerHTML = name;
	        }
	        function setDisplayName() {
	            var name = document.getElementById("addlocationAttributeTypeName").value;
	            document.getElementById("addlocationAttributeTypeDisplayName").value = name;
	            document.getElementById("addlocationAttributeTypeDisplayName").innerHTML = name;
	        }
        </script>
    </head>
    <body>
        <table id="locationTree" style="background: white;" border=0 align=left >
            <tr>
                <td align="left" valign=top style="border-right: #999999 1px dashed">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="collapseAllBtn" href="#" title="Collapse All" onclick="return false;">Collapse	All</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="expandAllBtn" href="#" title="Expand All" onclick="return false;">Expand All</a>
                	<ul id="treeDemo" class="ztree myTreeClass overflow"></ul>
                	<ul id="voidedLocations" class="ztree myTreeClass overflow"></ul>
				</td>
            </tr>
        </table>
   		<div>
	        <div id="myDiv"></div>
	        
	        <div id="headerBtns" >
			    <div id="groupBtn_1">
			      	<div id="groupBtn1">
				      	<strong>Location</strong>
				      	<div id="group">
							<a id="addLocationBtn" data-toggle="modal" data-target="#myModal" data-backdrop="static" data-keyboard="false" title="Add New Location" onclick="openModals('addLocationBtn');" style="cursor:pointer" class="icon" ><img src='images/plus.png' width='25px' height='25px'></a>
							<!-- <a id="updateLocationBtn" data-toggle="modal" data-target="#myModalUpdate" data-backdrop="static" data-keyboard="false" title="Update Location" onclick="openModals('updateLocationBtn');" style="cursor:pointer" class="icon" ><img src='images/edit-icon.png' width='25px' height='25px'></a> -->
			    	  	</div>
			      	</div>
			      	<div id="groupBtn2">
				      	<strong>Location Attribute</strong>
				      	<div id="group">
							<a id="addLocationAttributeBtn" data-toggle="modal" data-target="#myModalLocationAttribute" data-backdrop="static" data-keyboard="false" title="Add New Location Attribute" onclick="openModals('addLocationAttributeBtn');" style="cursor:pointer" class="icon" ><img src='images/plus.png' width='25px' height='25px'></a>
							<!-- <a id="updateLocationAttributeBtn" data-toggle="modal" data-target="#myModalLocationAttributeUpdate" data-backdrop="static" data-keyboard="false" title="Update Location Attribute" onclick="openModals('updateLocationAttributeBtn');" style="cursor:pointer" class="icon" ><img src='images/edit-icon.png' width='25px' height='25px'></a> -->
			    	  	</div>
			      	</div>
				</div>
	
			    <div id="groupBtn_2">
			      	<div id="groupBtn1">
				      	<strong>Location Type</strong>
				      	<div id="group">
							<a id="addLocationTypeBtn" data-toggle="modal" data-target="#myModalLocationType" data-backdrop="static" data-keyboard="false" title="Add New Location Type" onclick="openModals('addLocationTypeBtn');" style="cursor:pointer" class="icon" ><img src='images/plus.png' width='25px' height='25px'></a>
							<a id="updateLocationTypeBtn" data-toggle="modal" data-target="#myModalLocationTypeUpdate" data-backdrop="static" data-keyboard="false" title="Update Location Type" onclick="openModals('updateLocationTypeBtn');" style="cursor:pointer" class="icon" ><img src='images/edit-icon.png' width='25px' height='25px'></a>
							<a id="viewLocationTypeBtn" data-toggle="modal" data-target="#myModalLocationTypeView" data-backdrop="static" data-keyboard="false" title="View Location Type" onclick="openModals('viewLocationTypeBtn');" style="cursor:pointer" class="icon" ><img src='images/viewDetail.png' width='25px' height='25px'></a>
			    	  	</div>
			      	</div>
			      	<div id="groupBtn2">
				      	<strong>Location Attribute Type</strong>
				      	<div id="group">
							<a id="addLocationAttributeTypeBtn" data-toggle="modal" data-target="#myModalLocationAttributeType" data-backdrop="static" data-keyboard="false" title="Add New Location Attribute Type" onclick="openModals('addLocationAttributeTypeBtn');" style="cursor:pointer" class="icon" ><img src='images/plus.png' width='25px' height='25px'></a>
							<a id="updateLocationAttributeTypeBtn" data-toggle="modal" data-target="#myModalLocationAttributeTypeUpdate" data-backdrop="static" data-keyboard="false" title="Update Location Attribute Type" onclick="openModals('updateLocationAttributeTypeBtn');" style="cursor:pointer" class="icon" ><img src='images/edit-icon.png' width='25px' height='25px'></a>
							<a id="viewLocationAttributeTypeBtn" data-toggle="modal" data-target="#myModalLocationAttributeTypeView" data-backdrop="static" data-keyboard="false" title="View Location Attribute Type" onclick="openModals('viewLocationAttributeTypeBtn');" style="cursor:pointer" class="icon" ><img src='images/viewDetail.png' width='25px' height='25px'></a>
			    	  	</div>
			      	</div>
				</div>
		      	
			    <div id="groupBtn_3">
				    <div id="groupBtn1">
				      	<div class="mydropdown">
							<a class="mydropbtn" title="Upload CSV"><img src='images/csv_upload.png' width='45px' height='45px'></a>
							<div class="mydropdown-content">
				      			<a style="cursor:pointer" class="icon" onclick="document.getElementById('fileUpload').click();" title="Upload CSV">Upload Locations</a>
				      			<a style="cursor:pointer" class="icon" onclick="document.getElementById('fileUploadLocAttrs').click();" title="Upload CSV">Upload Location Attributes</a>
							</div>
						</div>
						<div class="mydropdown">
							<a class="mydropbtn" title="Download CSV"><img src='images/csv_download.png' width='45px' height='45px'></a>
							<div class="mydropdown-content">
								<a style="cursor:pointer" class="icon" onclick="downloadCSV(); return ExcellentExport.csv(this, 'zTreeTable');" title="Download Locations" download="locations.csv" >Download Locations</a>
								<a style="cursor:pointer" class="icon" onclick="downloadCSVTemplate(); return ExcellentExport.csv(this, 'zTreeTable');" title="Download CSV Template" download="locations_template.csv" >Download Location Template</a>
								<a style="cursor:pointer" class="icon" onclick="downloadCSVTemplateForLocAttrs(); return ExcellentExport.csv(this, 'zTreeTable');" title="Download CSV Template For Location Attributes" download="locations_attributes_template.csv" >Download Location Attribute Template</a>
							</div>
						</div>
					</div>
				</div>
			
			</div>
			</div>
	
		  	<hr />
	
			<div id="myAlertDIV"></div>
			
		  	
	  		<div id="errors"></div> 
	        <div id="dvTable"></div>
	        <br/><br/>
	        <div id="nodeDetails"></div>
	        
	      	<div id="mySidenav">
			    <input id="fileUpload" name="fileUpload" type="file" accept=".csv" />
			    <input id="fileUploadLocAttrs" name="fileUploadLocAttrs" type="file" accept=".csv" />
			</div>
			
			<div id="zTreeTableDiv"></div>

		</div>
		
        <!-- Modal Add New Location-->
        <div class="modal fade" id="myModal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
                        <h4 class="modal-title">Add New Location</h4>
                    </div>
                   	<div id="myAlertDIV1_addLocation"></div>
                    <!-- <form id="nodeForm" name="nodeForm"> -->
                    	<div class="modal-body" id="myLocationAdd">
                            <div class="form-group">
                                <label for="addName" class="form-control-label">Name: *</label>
                                <input type="text" class="form-control" id="addName" name="addName" value="" title="Enter Name, Any 3 to 30 Alphanumeric and/or Special Characters (.,-,_)" placeholder="Name" maxlength="30" onfocus="setFocus('addlocation')" oninput="setLocationNames()">
                            </div>
                            <div class="form-group">
                                <label for="addShortName" class="form-control-label">Short Name: *</label>
                                <input type="text" class="form-control" id="addShortName" name="addShortName" value="" title="Enter Short Name, Any 3 to 30 Alphanumeric and/or Special Characters (.,-,_)" placeholder="Short Name" maxlength="30" onfocus="removeFocus('addlocation')" >
                            </div>
                            <div class="form-group">
                                <label for="addFullName" class="form-control-label">Full Name: *</label>
                                <input type="text" class="form-control" id="addFullName" name="addFullName" value="" title="Enter Full Name, Any 3 to 50 Alphanumeric and/or Special Characters (.,-,_)" placeholder="Full Name" maxlength="50" onfocus="removeFocus('addlocation')">
                            </div>
                            <div class="form-group">
                                <!-- <label for="addParent" class="form-control-label">Parent Id:</label> -->
                                <div id="addParentLocationListDiv"></div>
                            </div>
                            <div class="form-group">
                                <label for="addType" class="form-control-label">Location Type: *</label>
                                <div id="addLocationTypeListDiv"></div>
                            </div>

                            <div class="form-group">
                                <label for="addDescription" class="form-control-label">Description:</label>
                                <input type="text" class="form-control" id="addDescription" name="addDescription" value="" title="Enter Description, Any 255 Characters" placeholder="Description" maxlength="255" onfocus="removeFocus('addlocation')">
                            </div>
                            <div class="form-group">
                                <label for="addOtherIdentifier" class="form-control-label">Other Identifier:</label>
                                <input type="text" class="form-control" id="addOtherIdentifier" name="addOtherIdentifier" value="" title="Enter Identifier, Any 50 Positive Integer/Decimal Numbers Only" placeholder="Identifier" maxlength="50" onfocus="removeFocus('addlocation')">
                            </div>
                            <div class="form-group">
                                <label for="addLatitude" class="form-control-label">Latitude:</label>
                                <input type="text" class="form-control" id="addLatitude" name="addLatitude" value="" title="Enter Latitude, Any 255 Characters" placeholder="Latitude" maxlength="255" onfocus="removeFocus('addlocation')">
                            </div>
                            <div class="form-group">
                                <label for="addLongitude" class="form-control-label">Longitude:</label>
                                <input type="text" class="form-control" id="addLongitude" name="addLongitude" value="" title="Enter Longitude, Any 255 Characters" placeholder="Longitude" maxlength="255" onfocus="removeFocus('addlocation')">
                            </div>
	                    </div>
	                    <div class="modal-footer">
	                        <input name="nextBtnLocation" id="nextBtnLocation" type="button" title="Add New Location Type" data-toggle="modal" data-target="#myModalLocationType" data-backdrop="static" data-keyboard="false" data-dismiss="modal" value="Add New Location Type" class="btn btn-default transparent pull-left" >
							<input name="closeBtnLocation" id="closeBtnLocation" type="button" title="Close" data-dismiss="modal" value="Close" class="btn btn-default transparent"/>
	                        <input name="addAllNodes" id="addAllNodes" type="submit" title="Add Location" value="Add Location" class="btn btn-default transparent">
	                    </div>
                	<!-- </form> -->
                </div>
            </div>
        </div>

        <!-- Modal Add New Location Type-->
        <div class="modal fade" id="myModalLocationType" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
                        <h4 class="modal-title">Add New Location Type</h4>
                    </div>
                   	<div id="myAlertDIV1_addLocationType"></div>
                    <!-- <form id="locationTypeForm" name="locationTypeForm" method="post"> -->
                    	<div class="modal-body" id="myLocationTypeAdd">
                            <div class="form-group">
                                <label for="addlocationTypeName" class="form-control-label">Name: *</label>
                                <input type="text" class="form-control" id="addlocationTypeName" name="addlocationTypeName" value="" title="Enter Name, Any 3 to 50 Alphanumeric and/or Special Characters (.,-,_)" placeholder="Name" maxlength="50" onfocus="setFocus('addlocationtype')">
                            </div>
                            <div class="form-group">
                                <label for="addlocationTypeLevel" class="form-control-label">Level:</label>
                                <input type="text" class="form-control" id="addlocationTypeLevel" name="addlocationTypeLevel" value="0" title="Enter Level, Any 10 Positive Integer/Decimal Numbers Only" placeholder="Level" min="0" maxlength="10" onfocus="removeFocus('addlocationtype')">
                            </div>
                            <div class="form-group">
                                <label for="addlocationTypeDescription" class="form-control-label">Description:</label>
                                <input type="text" class="form-control" id="addlocationTypeDescription" name="addlocationTypeDescription" value="" title="Enter Description, Any 255 Characters" placeholder="Description" maxlength="255" onfocus="removeFocus('addlocationtype')">
                            </div>
	                    </div>
	                    <div class="modal-footer">
	                        <input name="nextBtnLocationType" id="nextBtnLocationType" type="button" title="Back" data-toggle="modal" data-target="#myModal" data-backdrop="static" data-keyboard="false" data-dismiss="modal" value="Back" class="btn btn-default transparent pull-left" >
							<input name="closeBtnLocationType" id="closeBtnLocationType" type="button" title="Close" data-dismiss="modal" value="Close" class="btn btn-default transparent"/>
	                        <input name="addAlllocationTypes" id="addAlllocationTypes" type="submit" title="Add Location Type" value="Add Location Type" class="btn btn-default transparent">
	                    </div>
                	<!-- </form> -->
                </div>
            </div>
        </div>

        <!-- Modal Update Location Type-->
        <div class="modal fade" id="myModalLocationTypeUpdate" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
                        <h4 class="modal-title">Update Location Type</h4>
                    </div>
                   	<div id="myAlertDIV1_updateLocationType"></div>
                    <!-- <form id="locationTypeFormUpdate" name="locationTypeFormUpdate"> -->
                    	<div class="modal-body" id="myLocationTypeUpdate">
                            <div class="form-group">
                                <label for="updatelocationTypeName" class="form-control-label">Name: *</label>
                            	<div id="updatelocationTypeDiv"></div>
                            	<!-- <input type="text" class="form-control" id="updatelocationTypeName" name="updatelocationTypeName" value=""> -->
                            </div>
                            <div class="form-group">
                                <label for="updatelocationTypeLevel" class="form-control-label">Level:</label>
                                <input type="text" class="form-control" id="updatelocationTypeLevel" name="updatelocationTypeLevel" value="" title="Enter Level, Any 10 Positive Integer/Decimal Numbers Only" placeholder="Level" min="0" maxlength="10" onfocus="removeFocus('updatelocationtype')">
                            </div>
                            <div class="form-group">
                                <label for="updatelocationTypeDescription" class="form-control-label">Description:</label>
                                <input type="text" class="form-control" id="updatelocationTypeDescription" name="updatelocationTypeDescription" value="" title="Enter Description, Any 255 Characters" placeholder="Description" maxlength="255" onfocus="removeFocus('updatelocationtype')">
                            </div>
	                    </div>
	                    <div class="modal-footer">
							<input name="closeBtnLocationType" id="closeBtnLocationType" type="button" title="Close" data-dismiss="modal" value="Close" class="btn btn-default transparent"/>
	                        <input name="updateAlllocationTypes" id="updateAlllocationTypes" type="submit" title="Update Location Type" value="Update Location Type" class="btn btn-default transparent">
	                    </div>
                	<!-- </form> -->
                </div>
            </div>
        </div>

        <!-- Modal Add New Location Attribute Types-->
        <div class="modal fade" id="myModalLocationAttributeType" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
                        <h4 class="modal-title">Add New Location Attribute Type</h4>
                    </div>
                   	<div id="myAlertDIV1_addLocationAttributeType"></div>
                    <!-- <form id="locationAttributeTypeForm" name="locationAttributeTypeForm"> -->
                    	<div class="modal-body" id="myLocationAttributeTypeAdd">
                            <div class="form-group">
                                <label for="addlocationAttributeTypeName" class="form-control-label">Name: *</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeName" name="addlocationAttributeTypeName" value="" title="Enter Name, Any 3 to 100 Alphanumeric and/or Special Characters (.,-,_)" placeholder="Name" maxlength="100" onfocus="setFocus('addlocationattributetype')" oninput="setDisplayName()">
                            </div>
                            <div class="form-group">
                                <label for="addlocationAttributeTypeDisplayName" class="form-control-label">Display Name: *</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeDisplayName" name="addlocationAttributeTypeDisplayName" value="" title="Enter Name, Any 3 to 100 Alphanumeric and/or Special Characters (.,-,_)" placeholder="Display Name" maxlength="100" onfocus="removeFocus('addlocationattributetype')">
                            </div>
                            <div class="form-group">
                                <label for="addlocationAttributeTypeDescription" class="form-control-label">Description:</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeDescription" name="addlocationAttributeTypeDescription" value="" title="Enter Description, Any 255 Characters" placeholder="Description" maxlength="255" onfocus="removeFocus('addlocationattributetype')">
                            </div>
                            <div class="form-group">
                                <label for="addlocationAttributeTypeCategory" class="form-control-label">Category:</label>
                                <input type="text" class="form-control" id="addlocationAttributeTypeCategory" name="addlocationAttributeTypeCategory" value="" title="Enter Category, Any 50 Characters" placeholder="Category" maxlength="50" onfocus="removeFocus('addlocationattributetype')">
                            </div>
	                    </div>
	                    <div class="modal-footer">
	                        <input name="nextBtnLocationAttributeType" id="nextBtnLocationAttributeType" type="button" title="Back" data-toggle="modal" data-target="#myModalLocationAttribute" data-backdrop="static" data-keyboard="false" data-dismiss="modal" value="Back" class="btn btn-default transparent pull-left" >
							<input name="closeBtnLocationAttributeType" id="closeBtnLocationAttributeType" type="button" title="Close" data-dismiss="modal" value="Close" class="btn btn-default transparent"/>
	                        <input name="addAlllocationAttributeTypes" id="addAlllocationAttributeTypes" type="submit" title="Add Location Attribute Type" value="Add Location Attribute Type" class="btn btn-default transparent">
	                    </div>
                	<!-- </form> -->
                </div>
            </div>
        </div>

        <!-- Modal Update Location Attribute Types-->
        <div class="modal fade" id="myModalLocationAttributeTypeUpdate" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
                        <h4 class="modal-title">Update Location Attribute Type</h4>
                    </div>
                   	<div id="myAlertDIV1_updateLocationAttributeType"></div>
                    <!-- <form id="locationAttributeTypeFormUpdate" name="locationAttributeTypeFormUpdate"> -->
                    	<div class="modal-body" id="myLocationAttributeTypeUpdate">
                            <div class="form-group">
                                <label for="updatelocationAttributeTypeName" class="form-control-label">Name: *</label>
                                <div id="updatelocationAttributeTypeDiv"></div>
                                <!-- <input type="text" class="form-control" id="updatelocationAttributeTypeName" name="updatelocationAttributeTypeName" value="" oninput="setDisplayName()" maxlength="255"> -->
                            </div>
                            <div class="form-group">
                                <label for="updatelocationAttributeTypeDisplayName" class="form-control-label">Display Name:</label>
                                <input type="text" class="form-control" id="updatelocationAttributeTypeDisplayName" name="updatelocationAttributeTypeDisplayName" value="" title="Enter Name, Any 3 to 100 Alphanumeric and/or Special Characters (.,-,_)" placeholder="Display Name" maxlength="100" onfocus="removeFocus('updatelocationattributetype')">
                            </div>
                            <div class="form-group">
                                <label for="updatelocationAttributeTypeDescription" class="form-control-label">Description:</label>
                                <input type="text" class="form-control" id="updatelocationAttributeTypeDescription" name="updatelocationAttributeTypeDescription" value="" title="Enter Description, Any 255 Characters" placeholder="Description" maxlength="255" onfocus="removeFocus('updatelocationattributetype')">
                            </div>
                            <div class="form-group">
                                <label for="updatelocationAttributeTypeCategory" class="form-control-label">Category:</label>
                                <input type="text" class="form-control" id="updatelocationAttributeTypeCategory" name="updatelocationAttributeTypeCategory" value="" title="Enter Category, Any 255 Characters" placeholder="Category" maxlength="255" onfocus="removeFocus('updatelocationattributetype')">
                            </div>
	                    </div>
	                    <div class="modal-footer">
							<input name="closeBtnLocationAttributeType" id="closeBtnLocationAttributeType" type="button" title="Close" data-dismiss="modal" value="Close" class="btn btn-default transparent"/>
	                        <input name="updateAlllocationAttributeTypes" id="updateAlllocationAttributeTypes" type="submit" title="Update Location Attribute Type" value="Update Location Attribute Type" class="btn btn-default transparent">
	                    </div>
                	<!-- </form> -->
                </div>
            </div>
        </div>

        <!-- Modal Add New Location Attribute-->
        <div class="modal fade" id="myModalLocationAttribute" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
                        <h4 class="modal-title">Add New Location Attribute</h4>
                    </div>
                   	<div id="myAlertDIV1_addLocationAttribute"></div>
                    <div id="form_sample"></div>
                </div>
            </div>
        </div>
        
        <!-- Modal -->
		<div class="modal fade" id="alertModal" role="dialog">
		    <div class="modal-dialog">
			    <!-- Modal content-->
			    <div class="modal-content">
			        <div class="modal-header">
			        	<button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
		          		<h4 class="modal-title" id="alertModalHead"><br/></h4>
		        	</div>
			        <div class="modal-body" id="alertModalBody"></div>
			        <div class="modal-footer">
			          	<button type="button" class="btn btn-default transparent" data-dismiss="modal">Close</button>
			        </div>
			    </div>
	        </div>
  		</div>

	    <!-- Modal Void Location Details-->
        <div class="modal fade" id="modalVoidLocationDetail" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" title="Close" >&times;</button>
                        <h4 class="modal-title" id="modalVoidLocationDetailHead"></h4>
                    </div>
                   	<div id="myAlertDIV1_voidLocationDetail"></div>
                    <!-- <form id="locationAttributeTypeFormUpdate" name="locationAttributeTypeFormUpdate"> -->
                    	<div class="modal-body" id="modalVoidLocationDetailBody">
                            <div class="form-group">
                                <label for="voided" class="form-control-label">Void Location:</label>
                                <select class="form-control" id="voided" name="voided" title="Void/Un-Void Location" onfocus="removeFocus('voidLocationDetail')">
                                	<option value="true">True</option>
                                	<option value="false">False</option>
                                </select>
                            </div>
                            <div class="form-group"> 
                                <label for="voidReason" class="form-control-label">Void Reason:</label>
                                <textarea class="form-control" id="voidReason" name="voidReason" title="Enter Reason for Voiding Location" placeholder="Void Reason" maxlength="255"  onfocus="removeFocus('voidLocationDetail')"></textarea>
                            </div>
                            <div class="form-group">
                                <div id="voidLocationDiv"></div>
                            </div>
	                    </div>
	                    <div class="modal-footer">
							<input name="closeBtnVoidLocationDetail" id="closeBtnVoidLocationDetail" type="button" title="Close" data-dismiss="modal" value="Close" class="btn btn-default transparent"/>
	                        <input name="saveBtnVoidLocationDetail" id="saveBtnVoidLocationDetail" type="submit" title="Void Location" value="Void Location" class="btn btn-default transparent">
	                    </div>
                	<!-- </form> -->
                </div>
            </div>
        </div>
	    
	    
	    
	    <script type="text/javascript" src="js/form.js"></script>
        <script type="text/javascript" src="js/myscript.js"></script>
		<script> document.getElementById("mySidenav").style.visibility = "hidden"; </script>
    </body>
</html>