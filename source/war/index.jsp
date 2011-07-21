<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Cloupspokes image service - Sample</title>

    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="./js/jquery.json-2.2.js"></script>
    <script src="./js/jquery-ui.js"></script>
    <script src="./js/jquery.form.js"></script>
    <script src="./js/jquery-timers.js"></script>
    <script src="./js/gaeis.js"></script>

    <link rel="stylesheet" href="./styles/css/ui-lightness/jquery-ui-1.8.12.custom.css" type="text/css"/>
    <link rel="stylesheet" href="./styles/style.css" type="text/css" />
    
    <script>
        $(document).ready(function() {
            bindEventHandlers();
        });
    </script>
    
  </head>

  <body>
    <h1>CloudSpokes-GAE image service</h1>
    <p>This sample interface provides the user the hability to upload images, preview images, browse available images, and transform any image previously uploaded.<br/>    Transformations are obtained using a REST service created for that purpose.</p>

    <!-- Error panel -->
    <div id="error" style="display:none"></div>

    
    <!-- Main sections (Upload and preview) -->
    <div id="main-sections">

        <!-- Upload section -->
        <h3><a href="#">Image upload section</a></h3>
	    <div id="upload-section">
	       <h1>Image upload</h1>
           <p>Select the image to upload</p>
            <form method="post" enctype="multipart/form-data" id="submitForm">
                <input id="image"name="image" type="file"/>
                <input id="submitButton" name="submitButton" type="submit" value="Upload & View">
            </form>
            <div id="uploadingImg" style="display: none">
                <img src="images/uploading.gif"/>
            </div>
        </div>

        <!-- Preview section -->
        <h3><a href="#">Image viewer and Transformations (REST services)</a></h3>
        <div id="image-viewer-section">
            <div id="display-section">
                <p><b>Paste the ImageKey to display or click the "browse" button to select an image from a list</b></p>
                <p>Key: <input type="text" id="imageKey" value=""/> <a id="popupImageSelector" class="button">Browse</a></p>
            </div>    
            <div id="transformation-section">
                <div id="buttons-panel" class="transformation-panel">
                    <h3><a href="#">servingURL get (Non REST)</a></h3>
                    <div class="transformation">
                        <span><input type="button" class="button" id="servingURLGet" value="Execute"/></span>
                    </div>
                    <h3><a href="#">REST get</a></h3>
                    <div class="transformation">
                        <span><input type="button" class="button" id="restGet" value="Execute"/></span>
                    </div>
                    <h3><a href="#">Resize</a></h3>
                    <div class="transformation">
                        <span><label for="width">width</label><input type="text" id="width"/></span>
                        <span><label for="height">height</label><input type="text" id="height"/></span>
                        <span><input type="button" class="button" id="resized" value="Resize"/></span>
                    </div>
                    <h3><a href="#">Crop</a></h3>
                    <div class="transformation">
                        <span><label for="leftX">leftX</label><input type="text" id="leftX"/></span>
                        <span><label for="topY">topY</label><input type="text" id="topY"/></span>
                        <span><label for="rightX">rightX</label><input type="text" id="rightX"/></span>
                        <span><label for="bottomY">bottomY</label><input type="text" id="bottomY"/></span>
                        <span><input type="button" class="button" id="cropped" value="Crop"/></span>
                    </div>
                    <h3><a href="#">Rotate</a></h3>
                    <div class="transformation">
                        <span><label for="degreesX">degrees</label><input type="text" id="degrees"/></span>
                        <span><input type="button" class="button" id="rotated" value="Rotate"/>
                    </div>
                    <h3><a href="#">Flip horizontally</a></h3>
                    <div class="transformation">
                        <span><input type="button" class="button" id="flipH" value="Execute"/></span>
                    </div>
                    <h3><a href="#">Flip vertically</a></h3>
                    <div class="transformation">    
                        <span><input type="button" class="button" id="flipV" value="Execute"/></span>
                    </div>
                    <h3><a href="#">Lucky</a></h3>
                    <div class="transformation">
                        <span><input type="button" class="button" id="lucky"value="Execute"/></span>
                    </div>
                </div>
                <div class="transformation-panel" id="image-panel">
                    <h2>Image</h2>
                    Action: <span id="action" class="value-text"></span><br/>
                    Args: <span id="args" class="value-text"></span><br/>
                    URL: <span class="value-text"><input type="text" id="imageURL"size="60" readonly="readonly"/><a href="#" id="imageURLButton" class="button" target="_blank" title="Open in a new window">Open</a></span><br/>
                    <br/>
                    <img id="imagePreview" src="/images/not-available.gif"/>    
                </div>
            </div>
        </div>
     </div>


   <!-- Image selector dialog --> 
    <div id="imageDialog" title="Select image" style="display: none;">
        <form>
            <select id="imageList" class="ui-widget" size="6" width="380" style="width: 380px">
            </select>
            <br/>
            <input type="checkbox" value="Y" id="showPreview" checked="true"/><label for="showPreview">Show preview</label>
	    </form>
        <img id="imageSelector" src="" width="100" height="100"/>
    </div>

  </body>
</html>
