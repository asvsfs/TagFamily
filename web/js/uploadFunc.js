/**
 * Created by asvsfs on 4/21/2015.
 */

$(function () {

    $('#fileupload').fileupload({

        dataType: 'json',

        done: function (e, data) {
            $("tr:has(td)").remove();
            $.each(data.result, function (index, file) {

                $("#uploaded-files").append(
                    $('<tr/>')
                        .append($('<td/>').text(file.fileName))
                        .append($('<td/>').text(file.fileSize))
                        .append($('<td/>').text(file.fileType))
                        //.append($('<td/>').html("<a href='upload?f="+index+"'>Click</a>"))
                        .append($('<td/>').html("<img class='imgholder' src='upload?f="+index+"' >Click</a>"))
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
