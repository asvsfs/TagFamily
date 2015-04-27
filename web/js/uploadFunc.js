/**
 * Created by asvsfs on 4/21/2015.
 */

var searchTag=function(){
    alert("HOY");
};
$(function () {

    //$.get( "/fetchuserimage", function( data ) {
    //    $( ".result" ).html( data );
    //    alert( "Load was performed." );
    //});
    //$.get( "/fetchuserimage", { userid: "John", time: "2pm" } )
    //    .done(function( data ) {
    //        alert( "Data Loaded: " + data );
    //    });

    $("#tagname").autocomplete({
        source: "list.jsp",
        appendTo:"#form_panel"
    });

    $('#fileupload').fileupload({

        dataType: 'json',

        done: function (e, data) {
            //$("tr:has(td)").remove();
           //location.reload();

            $.each(data.result, function (index, file) {
                $("#uploaded-files").append(
                    $('<tr/>')
                        .append($('<td/>').text(file.fileName))
                        .append($('<td/>').text(file.fileSize))
                        .append($('<td/>').text(file.fileType))
                        //.append($('<td/>').html("<a href='upload?f="+index+"'>Click</a>"))
                        .append($('<td/>').html("<a href='/imagetag?f="+file.fileAddress+"><img class='imgholder' src='fetchimage?f="+file.fileAddress+"' >Click</a>"))
                        .append($('<td/>').text("@"+file.twitter))

                )//end $("#uploaded-files").append()
            });
        },

        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css(
                'width',
                progress + '%'
            );
        },

        dropZone: $('#dropzone')
    }).bind('fileuploadsubmit', function (e, data) {
        // The example input, doesn't have to be part of the upload form:
        var twitter = $('#twitter');
        data.formData = {twitter: twitter.val()};
    });

});
