<%-- 
    Document   : chat
    Created on : 10-lug-2009, 14.25.04
    Author     : Luigi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%
        String nick = request.getParameter("nick");
        request.setAttribute("nick", nick);
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="chat_${nick}" class="chatWindow">
            <h1>Chat con ${nick}</h1>
            <div class="chatText">

            </div>
            <div class="inputText">
                <textarea class="newMessage"></textarea>
                <input type="button" id="send" value="Invia"onclick="send()">
            </div>
        </div>
    </body>
    <script type="text/javascript">
        function send(){
            var text = $("#chat_${nick} .newMessage").val();
            $("#chat_${nick} .newMessage").val("");
            var nickSt = '${nick}';
            //mi metto in attesa e scrittura
            jQuery.post("MessageSender",{'text' : text, 'nick' : nickSt},
            function(){
                $("#chat_${nick} .chatText").append("Tu scrivi : "+text+"<br>");
            });
        }
        $(document).unload(function(){
            alert("quit");
        });
       
        function verifyButton(){
            $("#send").focus();
        }
    </script>
</html>
