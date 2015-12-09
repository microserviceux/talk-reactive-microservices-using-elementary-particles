
<meta name="layout" content="ui"/>

<h1>${ introspect.serviceName} Introspection</h1>
<hr/>
<table style="font-size:1em; text-align:left;">
    <tr><th>Muon Protocols</th><td>${introspect.protocols*.protocolScheme.join(",")}</td></tr>
    <tr><th>Tags</th><td>${discovery.tags.join(",")}</td></tr>
    <tr><th>Supported Codecs</th><td>${discovery.codecs.join(",")}</td></tr>
    <tr><th>Transport Connection URLs</th><td>${discovery.connectionUrls*.toString().join(",")}</td></tr>
</table>

<hr/>
<h3>Request / Response Endpoints</h3>
<p>${introspect.protocols.find {it.protocolScheme == "request"}.description}</p>

<g:if test="${introspect.protocols.find {it.protocolScheme == "request"}.operations}">
<table style="font-size: 1em">
    <tr><th>Endpoint</th><th>Documentation (if exists)</th></tr>
    <g:each in="${introspect.protocols.find {it.protocolScheme == "request"}.operations}">
        <tr><td>${it.resource}</td>
            <td>${it.doc}</td>
            <td><g:link action="data" id="${discovery.identifier}" params="[endpoint:it.resource]">Interact</g:link></td></tr>
    </g:each>
</table>
</g:if>
<g:else>
    <b>There are no request/ response endpoints</b>
</g:else>

<hr/>
<h3>Reactive Stream Endpoints</h3>
<p>${introspect.protocols.find {it.protocolScheme == "reactive-stream"}.description}</p>

<g:if test="${introspect.protocols.find {it.protocolScheme == "reactive-stream"}.operations}">

<table style="font-size: 1em">
    <tr><th>Endpoint</th><th>Documentation (if exists)</th></tr>
    <g:each in="${introspect.protocols.find {it.protocolScheme == "reactive-stream"}.operations}">
        <tr><td>${it.resource}</td><td>${it.doc}</td></tr>
    </g:each>
</table>
</g:if>
<g:else>
    <b>There are no reactive stream endpoints</b>
</g:else>