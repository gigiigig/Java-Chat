<%-- 
    Document   : index
    Created on : 8-lug-2009, 14.22.38
    Author     : Luigi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="jQuery/js/jquery-1.3.2.min.js" type="text/javascript"> </script>
        <script src="jQuery/js/form.js" type="text/javascript"> </script>
        <script src="index.js" type="text/javascript"> </script>
        <style type="text/css">
            body{
                width: 400px;
            }
            h1{
                font-size: 14px;
                color: gray;
            }
            #clientList{
                border: 1px solid silver;
                margin-bottom: 4px;
            }
            #clientList p{
                margin: 0px;
                padding: 0px;
            }
            .chatText{
                border: 1px solid silver;
                margin-bottom: 4px;

            }
            .inputText{
                border: 1px solid silver;
                margin-bottom: 4px;

            }
            .nick{
                cursor: pointer;
                color: blue;
            }
            .nick:hover{
                text-decoration: underline;
            }
            #chat .chatWindow{
                clear: both;
                display: none;
                border: 1px solid silver;
            }
            #chat .activeChat{
                display: block;
            }
            #activeClientList{
                padding-left: 6px;
            }
            #activeClientList a{
                float: left;
                border: 1px solid  silver;
                border-bottom: 0px ;
                margin-left: 3px;
                margin-bottom: -1px;
                padding: 2px;
                background-color: white;
                display: block;
            }

        </style>
    </head>
    <body>
        <h1>Hello World!</h1>
        <p>
            <label>Nick</label><input type="text" id="nick"/>
        </p>
        <input type="button" value="Start" onclick="startPolling()"/>
        <div id="messages">

        </div>
        <div id="clientList">
            <h1>Utenti connessi</h1>

        </div>
        <div id="activeClientList">


        </div>
        <div id="chat"></div>

    </body>
    <script type="text/javascript">
        
        window.onbeforeunload=function(){
            alert("esco dalla chat");
            $.post("Disconnect");
        }
        function createChatWith(nick,text){
            //$("#chat").load("chat.jsp",{'nick':nick},callback);
            if($("#chat_"+nick).val() == null){
                $.post("chat.jsp",{'nick':nick},function(data){
                    $("#chat").append((data));
                    activeChat(nick);
                    activeClient(nick);
                    if(text != null && text != undefined){
                        var newHtml = text;
                        $("#chat_"+nick+" .chatText").append(nick+" scrive : "+newHtml+"<br>");
                    }
                });
            }else{
                //c'è già la chat con questo utete la mentto in evidenza
                activeChat(nick);
            }
        }

        function activeChat(nick){
            $("#chat .activeChat").attr("class","chatWindow");
            $("#chat_"+nick).attr("class","chatWindow activeChat");
        }

        function activeClient(nick){
            var newHtml = "<a class=\"nick\" onClick=\"activeChat('"+nick+"')\" id=\"active_"+nick+"\">"+nick+"</a>";
            $("#activeClientList").append(newHtml);
        }



        function startPolling(){
            var nick = $("#nick").val();
            jQuery.post("SocketConnection",{"nick":nick},
            function(data){
                if(data == 'true'){
                    $("#messages").html("Connesso");
                }else{
                    $("#messages").html("Nick già esistente");
                }
                refreshData();
            })
        }

        function refreshData(){
            //$('#chatText').load('SessionReader',function() {
            //    reloadChatText();
            //});

            jQuery.post("SessionReader",
            function(data){
                parseData(data);
                refreshData();
            });
        }

        var commands = {
            COMMAND : "COMMAND",
            MESSAGE : "MESSAGE"
        };


        function parseData(data){
            if(data != ""){
                eval(data);
                if(e.type == commands.COMMAND){
                    if(e.name == 'ADDUSER' && e.nick != null){
                        for(i=0;i > -1;i++){
                            if(e.nick[i] != null && e.nick[i] != undefined){
                                var newHtml = "<p class=\"nick\" onClick=\"createChatWith('"+e.nick[i]+"')\" id=\""+e.nick[i]+"\">"+e.nick[i]+"</p>";
                                $("#clientList").append(newHtml);
                            }else{
                                break;
                            }
                        }
                    }else if(e.name == 'REMOVEUSER' && e.nick != null){
                        //for(i=0;i > -1;i++){
                        //    if(e.nick[i] != null && e.nick[i] != undefined){
                        //alert("nick da rimuovere : "+e.nick);
                        //alert("remove element : "+$("#"+e.nick));
                        $("#"+e.nick).remove();
                        $("#active_"+e.nick).remove();
                        //     }else{
                        //         break;
                        //     }
                        // }
                    }
                }else if(e.type == commands.MESSAGE){
                    if($("#chat_"+e.sender).val() == null){
                        createChatWith(e.sender,e.text);
                    }else{
                        var newHtml = e.text;
                        $("#chat_"+e.sender+" .chatText").append(e.sender+" scrive : "+newHtml+"<br>");
                    }
                }
            }
        }

    </script>
</html>
