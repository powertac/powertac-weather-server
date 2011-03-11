
<%@ page import="powertac.weather.server.WeatherSet" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'weatherSet.label', default: 'WeatherSet')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'weatherSet.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="currentDay" title="${message(code: 'weatherSet.currentDay.label', default: 'Current Day')}" />
                        
                            <g:sortableColumn property="currentTime" title="${message(code: 'weatherSet.currentTime.label', default: 'Current Time')}" />
                        
                            <g:sortableColumn property="fetched" title="${message(code: 'weatherSet.fetched.label', default: 'Fetched')}" />
                        
                            <g:sortableColumn property="numberDays" title="${message(code: 'weatherSet.numberDays.label', default: 'Number Days')}" />
                        
                            <th><g:message code="weatherSet.set.label" default="Set" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${weatherSetInstanceList}" status="i" var="weatherSetInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${weatherSetInstance.id}">${fieldValue(bean: weatherSetInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: weatherSetInstance, field: "currentDay")}</td>
                        
                            <td>${fieldValue(bean: weatherSetInstance, field: "currentTime")}</td>
                        
                            <td><g:formatBoolean boolean="${weatherSetInstance.fetched}" /></td>
                        
                            <td>${fieldValue(bean: weatherSetInstance, field: "numberDays")}</td>
                        
                            <td>${fieldValue(bean: weatherSetInstance, field: "set")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${weatherSetInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
