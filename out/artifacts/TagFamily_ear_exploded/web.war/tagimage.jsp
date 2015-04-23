<%--
  Created by IntelliJ IDEA.
  User: asvsfs
  Date: 4/23/2015
  Time: 9:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Image Tagging with jQuery and PHP</title>
  <link href="${pageContext.request.contextPath}/css/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css"/>
  <%--<script type="text/javascript" src="//code.jquery.com/jquery-1.11.2.min.js"></script>--%>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>

  <style type="text/css" >

    body{
      font-size:13px;
      font-family:"Arial";
    }
    #mapper{
      border:5px solid #EEE;
      width:100px;
      height:100px;
      min-width:30px;
      min-height:30px;
      z-index:1000;
      position:absolute;
      top:0;
      display:none;
    }

    #planetmap div{

      display:block;
      position:absolute;
    }




    #main_panel{
      margin: auto;
      padding: 10px;
      width: 1000px;
    }
    #url_panel{

    }
    #form_panel{
      float: left;
      background:#00000000;
      border:5px solid #FFF;
      outline:1px solid #eee;
      left: 100px;
      padding: 5px;
      position: absolute;
      top: 40px;
      width: 310px;
      display:none;
      z-index:2000;
    }

    #form_panel input,textarea{
      padding:3px;
      background:#FFF;
      border:1px solid #CFCFCF;
      color:#000;
    }

    #image_panel{
      float:left;
      width:600px;
      position:relative;
    }
    #image_panel img{
      left:0;top:-102px;
      max-width: 600px;
      overflow: hidden;

    }


    #form_panel .label{
      float:left;
      width:80px;
      padding:5px;
    }

    #form_panel .field{
      float:left;
      width:200px;
      padding:5px;
    }

    #form_panel .row{
      clear:both;
    }

    .tagged_title{
      background: none repeat scroll 0 0 #538DD3;
      border: 2px solid;
      color: #FFFFFF;
      font-size: 12px;
      font-weight: bold;
      padding: 3px;
      margin-top:5px;
    }


    #info_panel{
      padding:10px;
      margin:20px 0;
      background:#eee;
    }


    input[type='button']{
      background: none repeat scroll 0 0 #2769C4;
      border: 1px solid #CFCFCF;
      color: #FFFFFF;
      font-weight: bold;
      height: 30px;
      padding: 5px;
    }


  </style>
  <script>
    $(document).ready(function() {

      $("#imageMap").click(function(e){


        var image_left = $(this).offset().left;
        var click_left = e.pageX;
        var left_distance = click_left - image_left;

        var image_top = $(this).offset().top;
        var click_top = e.pageY;
        var top_distance = click_top - image_top;

        var mapper_width = $('#mapper').width();
        var imagemap_width = $('#imageMap').width();

        var mapper_height = $('#mapper').height();
        var imagemap_height = $('#imageMap').height();


        if((top_distance + mapper_height > imagemap_height) && (left_distance + mapper_width > imagemap_width)){
          $('#mapper').css("left", (click_left - mapper_width - image_left  ))
                  .css("top",(click_top - mapper_height - image_top  ))
                  .css("width","100px")
                  .css("height","100px")
                  .show();
        }
        else if(left_distance + mapper_width > imagemap_width){


          $('#mapper').css("left", (click_left - mapper_width - image_left  ))
                  .css("top",top_distance)
                  .css("width","100px")
                  .css("height","100px")
                  .show();

        }
        else if(top_distance + mapper_height > imagemap_height){
          $('#mapper').css("left", left_distance)
                  .css("top",(click_top - mapper_height - image_top  ))
                  .css("width","100px")
                  .css("height","100px")
                  .show();
        }
        else{


          $('#mapper').css("left",left_distance)
                  .css("top",top_distance)
                  .css("width","100px")
                  .css("height","100px")
                  .show();
        }


        $("#mapper").resizable({ containment: "parent" });
        $("#mapper").draggable({ containment: "parent" });

      });

      $("#title").autocomplete({
        source: "list.jsp",
        appendTo:"#form_panel"
      });
    });

    $(".tagged").on("mouseover",function(){
      if($(this).find(".openDialog").length == 0){
        $(this).find(".tagged_box").css("display","block");
        $(this).css("border","3px solid #EEE");

        $(this).find(".tagged_title").css("display","block");
      }
    });

    $(".tagged").on("mouseout",function(){
      if($(this).find(".openDialog").length == 0){
        $(this).find(".tagged_box").css("display","none");
        $(this).css("border","none");
        $(this).find(".tagged_title").css("display","none");
      }
    });

    $(".tagged").on("click",function(){
      $(this).find(".tagged_box").html("<img src='del.png' class='openDialog' value='Delete' onclick='deleteTag(this)' />\n\
        <img src='save.png' onclick='editTag(this);' value='Save' />");

      var img_scope_top = $("#imageMap").offset().top + $("#imageMap").height() - $(this).find(".tagged_box").height();
      var img_scope_left = $("#imageMap").offset().left + $("#imageMap").width() - $(this).find(".tagged_box").width();

      $(this).draggable({ containment:[$("#imageMap").offset().left,$("#imageMap").offset().top,img_scope_left,img_scope_top]  });

    });

    var addTag = function(){
      var position = $('#mapper').position();


      var pos_x = position.left;
      var pos_y = position.top;
      var pos_width = $('#mapper').width();
      var pos_height = $('#mapper').height();

      $('#planetmap').append('<div class="tagged"  style="width:'+pos_width+';height:'+
      pos_height+';left:'+pos_x+';top:'+pos_y+';" ><div   class="tagged_box" style="width:'+pos_width+';height:'+
      pos_height+';display:none;" ></div><div class="tagged_title" style="top:'+(pos_height+5)+';display:none;" >'+
      $("#title").val()+'</div></div>');

      $("#mapper").hide();
      $("#title").val('');
      $("#form_panel").hide();


    };
    var cancelTag= function(){
      $("#mapper").hide();
      $("#form_panel").hide();
    };

    var openDialog = function(){
      $("#form_panel").fadeIn("slow");
    };

    var showTags = function(){
      $(".tagged_box").css("display","block");
      $(".tagged").css("border","5px solid #EEE");
      $(".tagged_title").css("display","block");
    };

    var hideTags = function(){
      $(".tagged_box").css("display","none");
      $(".tagged").css("border","none");
      $(".tagged_title").css("display","none");
    };

    var editTag = function(obj){

      $(obj).parent().parent().draggable( 'disable' );
      $(obj).parent().parent().removeAttr( 'class' );
      $(obj).parent().parent().addClass( 'tagged' );
      $(obj).parent().parent().css("border","none");
      $(obj).parent().css("display","none");
      $(obj).parent().parent().find(".tagged_title").css("display","none");
      $(obj).parent().html('');

    }

    var deleteTag = function(obj){
      $(obj).parent().parent().remove();
    };



  </script>
</head>
<body>
<div id='main_panel'>

  <div style='margin: auto; width: 600px;'>
    <div id='image_panel' >
      <img src='/fetchimage?f=<%= request.getParameter("f")%>' id='imageMap'  />
      <div id='mapper' ><img src='save.png' onclick='openDialog()' /></div>

      <div id="planetmap">

      </div>
      <div id='form_panel'>
        <div class='row'>
          <div class='label'>Title</div><div class='field'><input type='text' id='title' name="username"/></div>
        </div>
        <div class='row'>
          <div class='label'></div>
          <div class='field'>
            <input type='button' value='Add Tag' onclick='addTag()' />
            <input type='button' value='Cancel Tag' onclick='cancelTag()' />
          </div>
        </div>

      </div>
    </div>

  </div>
  <div style="background: none repeat scroll 0 0 #C7C7C8;
                 border: 1px solid #AEAEAE;
                 clear: both;
                 margin: 20px auto;
                 padding: 20px 0;
                 text-align: center;
                 width: 600px;">
    <input type="button" value="Show Tags" onclick="showTags()" />
    <input type="button" value="Hide Tags" onclick="hideTags()" />
  </div>
</div>
</body>
</html>




