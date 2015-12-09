
<meta name="layout" content="ui"/>

<h1>${ params.id}</h1>
<hr/>
<g:form action="sendData">
    <input name="id" value="${params.id}" type="hidden"/>
    <input name="endpoint" value="${endpoint}" type="hidden"/>
    <table style="font-size:1em; text-align:left;">
        <tr><th>Service</th><td>${serviceName}</td></tr>
        <tr><th>Endpoint</th><td>${endpoint}</td></tr>
        <tr><th>URL</th><td><input type="text" name="url" value="${url}" style="height:40px; width:700px; font-size:0.8em"/></td></tr>
        <tr><th>Content</th><td><textarea name="content" rows="10" cols="120"></textarea></td></tr>
    </table>
    <button type="submit">Request</button>
</g:form>
<hr/>
