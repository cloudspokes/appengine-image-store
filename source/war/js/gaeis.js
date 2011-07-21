var restPrefix = "rest/images";

/**
 * Binds the event handlers and initialize the required data
 */
function bindEventHandlers() {
	resetUploadURL();
	
	$('#submitForm').submit(function() {
		var options = { 
		        beforeSubmit: showUploading,
		        success:    onUploadResponse, 
		        iframe: true,
		        dataType:  	'json',        // 'xml', 'script', or 'json' (expected server response type) 
		    };
	    $(this).ajaxSubmit(options);
	    return false;
	});
	
	$('#submitForm').bind('error', function(data) {
		showErrorMessage("Failed to upload image");
	});

	$("#imagePreview").error(function(data) {
		showErrorMessage("Failed to upload image");
	});
	
	$("#imagePreview").bind('error', function(data) {
		showErrorMessage("Could not load transformed image, verify your parameters");
		$("#imagePreview").attr("src", "./images/not-available.gif");
	});

	$("#imageKey").change(function () {
	    showImage();
	});
	
	$("#restGet").click(function () {
	    showRESTImage({action: null, title: "REST Get"});
	});
	
	$("#servingURLGet").click(function () {
		showImage();
	});
	
	$("#lucky").click(function () {
	    showRESTImage({action: "lucky", title: "Lucky"});
	});
	
	$("#flipH").click(function () {
	    showRESTImage({action: "flipH", title: "Flip horizontally"});
	});
	
	$("#flipV").click(function () {
	    showRESTImage({action: "flipV", title: "Flip vertically"});
	});
	
	$("#rotated").click(function () {
	    showRESTImage({action: "rotated", title: "Rotate",  args: {degrees: $("#degrees").val() }});
	});
	
	$("#resized").click(function () {
	    showRESTImage({action: "resized", title: "Resize", args: {width: $("#width").val(), height: $("#height").val() }});
	});
	
	$("#cropped").click(function () {
	    showRESTImage({action: "cropped", title: "Crop", args: {leftX: $("#leftX").val(), topY: $("#topY").val(), rightX: $("#rightX").val(), bottomY: $("#bottomY").val() }});
	});
	
	/* JQuery styles */
	$("#main-sections").accordion({ autoHeight: false });
	$("#buttons-panel").accordion({ autoHeight: true });
	
	$('input[type=button], button').button();
	$('input[type=submit], button').button();
	$('a.button').button();
	
	/* Image selection dialog */
	$("#showPreview").button();
	$("#showPreview").click(function () {
		if (this.checked != "") {
			var value = $("#imageList").val();
			$("#imageSelector").attr("src", restPrefix+"/"+value+"/resized?width=100&height=100"); 
			$("#imageSelector").show();
		} else {
			$("#imageSelector").hide();
		} 
	});
	
	/* When the Browse button is clicked, we load the images metadata and open the dialog */
	$("#popupImageSelector").click(function() {
		$.getJSON(restPrefix, null, function (result) {
			var selectOptions = "";
			$.each(result, function(index, value) {
				selectOptions += "<option value='"+value.blobKey+"'>"+ value.filename +" / " + value.blobKey + "</option>";
			});
			$("#imageList").html(selectOptions);
			$("#imageDialog").dialog("open");
		}).error(function(response) {
			showErrorMessage("Failed to load images information.");
		});
	});
	
	$("#imageList").change(function() {
		if ($("#showPreview").attr("checked")) {
			$("#imageSelector").attr("src", restPrefix+"/"+this.value+"/resized?width=100&height=100");
		} 
	});
	
	$("#imageDialog").dialog({ minWidth: 400, autoOpen: false, modal: true,  buttons: {"Select & Close": function() { 
			$(this).dialog('close');
			var value = $("#imageList").val();
			$("#imageKey").val(value);
			showImage();
		}},
		
	});

}
/**
 * Resets the upload URL in order to allow another file upload 
 */
function resetUploadURL() {
	$.getJSON("/upload", null, function (result) {
		$('#submitForm').attr("action", result.url);
	}).error(function(response) {
		showErrorMessage("Failed to update upload URL, refresh your browser");
	});;
}

/**
 * Processes the upload response, and shows the uploaded image
 */
function onUploadResponse(response, statusText, xhr, $form)  { 
    hideUploading();
    resetUploadURL();
	if (!response.success) {
		showErrorMessage("Falied to upload image: "+ response.message);
	} else {
		$form.resetForm();
		$("#imageKey").val(response.blobKey);
		$("#main-sections").accordion( "option", "active", 1);
		showImage();
	}
}

/**
 * Shows the image specified with the key contained in the imageKey text field. 
 */
function showImage() {
	clearCurrentImageData();
	var blobKey = $("#imageKey").val();
	var servingURL = restPrefix+"/"+blobKey+"/servingURL";
	$.getJSON(servingURL, null, function(data) {
		showURLImage("servingURL get", "", data.url);
	}).error(function(response) {
		clearCurrentImageData();
		showErrorMessage(response.status == 404 ? "Image not found" : "Server error: "+response.status);
	});
}

/**
 * Shows an error message
 */
function showErrorMessage(message) {
	$("#error").html(message);
	$("#error").show();
	$(this).oneTime(5000, function() {
		$("#error").hide();
	});
}

/**
 * Shows an image using a rest service, the transform contains the kind of service 
 * to call and the args for it
 */
function showRESTImage(transform) {
	$("#imageURL").val("");
	$("#imageURLButton").attr("href","#");
	$("#imagePreview").attr("src", "./images/not-available.gif");
	$("#action").html("");
	$("#args").html("");
	
	var blobKey = $("#imageKey").val();
	var imageURL = restPrefix+"/"+blobKey;
	if (transform && transform.action) {
		imageURL = imageURL + "/"+transform.action;
		if (transform.args && transform.args != "") {
			imageURL = imageURL + "?" + jQuery.param(transform.args);
		}
	}
	showURLImage(transform.title, transform.args, imageURL)
}

/**
 * Updates the image section with given URL, action, and args 
 */
function showURLImage(action, args, URL) {
	$("#action").html(action);
	$("#args").html(jsonToHTML(args));
	$("#imageURL").val(URL);
	$("#imageURLButton").attr("href", URL);
	$("#imagePreview").attr("src", URL);
}

/**
 * Converts a JSON object to HTJML
 */
function jsonToHTML(args) {
	var argsText = "";
	if (args && !$.isEmptyObject(args)) {
		$.each(args, function(key,value) {
			argsText = argsText + " "+key+": '"+value + "', " ;
		});
		return argsText.substring(0, argsText.length - 2)
	}
}

/**
 * Clears image section 
 */
function clearCurrentImageData() {
	$("#imagePreview").attr("src", "./images/not-available.gif");
	$("#imageURL").val("");
	$("#imageURLButton").attr("href","#");
}

function showUploading() {
	$("#uploadingImg").show();
}
function hideUploading() {
	$("#uploadingImg").hide();
}