
<meta name="layout" content="ui"/>

<h1>Dispatch Event</h1>

<g:if test="${message}">
    <p>
        ${message}
    </p>
</g:if>

<hr/>
<g:form action="event">
    <table style="font-size:1em; text-align:left;">
        <tr><th>Event Type</th><td><input type="text" name="eventType" style="height:40px; width:700px; font-size:0.8em"/></td></tr>
        <tr><th>Content</th><td><textarea name="content" rows="10" cols="120"></textarea></td></tr>
    </table>
    <button type="submit">Dispatch Event</button>
</g:form>
