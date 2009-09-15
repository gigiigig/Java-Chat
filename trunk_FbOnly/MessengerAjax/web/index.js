/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function activeTab(tab){
    $("#active").attr('id', 'inactive');
    tab.setAttribute('id', 'active');
}
function switchTab(){
    var active = $("#active");
    var inactive = $("#inactive");
    active.attr('id', 'inactive');
    inactive.attr('id', 'active');
}


var options = {
    target:        '#myDiv',   // target element(s) to be updated with server response
    beforeSubmit:  showRequest,  // pre-submit callback
    success:       showResponse  // post-submit callback

// other available options:
//url:       url         // override for form's 'action' attribute
//type:      type        // 'get' or 'post', override for form's 'method' attribute
//dataType:  null        // 'xml', 'script', or 'json' (expected server response type)
//clearForm: true        // clear all form fields after successful submit
//resetForm: true        // reset the form after successful submit

// $.ajax options can be used here too, for example:
//timeout:   3000
};
function ajaxSubmit(formId,validate){
//    var ed = tinyMCE.get('contents');
//    if(ed != undefined || ed != null){
//        $(formId).append("<textarea cols=\"0\" style=\"display: none\" name=\"fckNewText\">"+ed.getContent()+"</textarea>");
//    }
    
    if(validate == null || validate){
        if($(formId).valid()){
            $(formId).ajaxSubmit(options);
        }
    }else{
        $(formId).ajaxSubmit(options);
    }
    return false;
}


// pre-submit callback
function showRequest(formData, jqForm, options) {
    // formData is an array; here we use $.param to convert it to a string to display it
    // but the form plugin does this for you automatically when it submits the data
    var queryString = $.param(formData);

    // jqForm is a jQuery object encapsulating the form element.  To access the
    // DOM element for the form do this:
    // var formElement = jqForm[0];


    // here we could return false to prevent the form from being submitted;
    // returning anything other than false will allow the form submit to continue
    return true;
}

// post-submit callback
function showResponse(responseText, statusText)  {
// for normal html responses, the first argument to the success callback
// is the XMLHttpRequest object's responseText property

// if the ajaxForm method was passed an Options Object with the dataType
// property set to 'xml' then the first argument to the success callback
// is the XMLHttpRequest object's responseXML property

// if the ajaxForm method was passed an Options Object with the dataType
// property set to 'json' then the first argument to the success callback
// is the json data object returned by the server

// alert('status: ' + statusText + '\n\nresponseText: \n' + responseText +
//   '\n\nThe output div should have already been updated with the responseText.');

}

/*funzione per tutti gli <a>*/
function ajaxLoad(url,data,effect,div){
    if(div == null){
        div = '#myDiv';
    }
    if(effect){
        $(div).fadeTo(100,0.001,function(){
            $(div).load(url,data, function() {
                $(div).fadeTo(100,1,function(){
                    //rimette l'antialiasing su explorer '
                    $(div).attr("style","display: block");
                });
            });
        });
       
    }else{
        $(div).load(url,data);
    }
}

/*immagine di Upload del file*/
function showGif(){
    if(isVisible){
        $('#loadingDiv').css("display", "block");
        $('#loadingDiv').css("height", "20px");
    }
}
function hideGif(){
    $('#loadingDiv').css("display", "none");
}

/*Visualizza pagina di upload*/
function showAddObjectPage(field, id, type){
    // $("#myDiv").load("content/addObject.jsp?field="+field+"&id="+id+"&type="+type);
    //$("#uploadFrame").attr('src','loading.gif');
    $("#uploadFrame").attr('src',"content/addObject.jsp?field="+field+"&id="+id+"&type="+type);
    $("#uploadFrame").css("border","1px solid silver");
    $("#uploadFrame").css("height","100px");
    $("#uploadFrame").css("width","250px");
    $("#uploadFrame").css("margin-top","20px");
    $("#uploadFrame").css("margin-bottom","20px");
}

function FCKeditor_OnComplete(editorInstance) {
    window.status = editorInstance.Description;
}



function reloadAttach(idVal){
    $('#attachs',window.parent.document).load("AttachList", {
        id : idVal
    });
    $('#attachs',window.parent.document).css('display', 'block');
    hided = false;
    loaded = true;
}

function deleteAttach(idVal,name){
    if(confirm("Vuoi eliminare l'allegato "+name+"?")){
        jQuery.post("DeleteAttach",{
            id : idVal
        },
        function(data){
            if(data == 'true'){
                $("#attach_"+idVal).html("Allegato eliminato");
                setTimeout(new Function("$('#attach_"+idVal+"').remove()"), 2000);
            }else{
                $("#attach_"+idVal).html("<label style='color: red;font-weight: normal;width: 200px'>Errore nell'eliminazione</label>");
            }
        });
    }
}


