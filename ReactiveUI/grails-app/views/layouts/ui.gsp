
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <g:layoutHead/>
</head>

<body>

<div style="margin-left:40px; font-size:2.5em">
    <div style="float:left;width:300px; padding-top:200px; border-right 1px solid black; height:800px;">

        <g:link controller="dashboard" action="index">Dashboard</g:link><br/>
        <g:link controller="dashboard" action="event">Send Event</g:link><br/>

    </div>
    <div style="float:left;">
        <g:layoutBody/>
    </div>
</div>
</body>
</html>