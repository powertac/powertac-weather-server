

<%@ page import="powertac.weather.server.WeatherSet" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'weatherSet.label', default: 'WeatherSet')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${weatherSetInstance}">
            <div class="errors">
                <g:renderErrors bean="${weatherSetInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fetched"><g:message code="weatherSet.fetched.label" default="Fetched" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'fetched', 'errors')}">
                                    <g:checkBox name="fetched" value="${weatherSetInstance?.fetched}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="numberDays"><g:message code="weatherSet.numberDays.label" default="Number Days" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'numberDays', 'errors')}">
                                    <g:textField name="numberDays" value="${fieldValue(bean: weatherSetInstance, field: 'numberDays')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="startDate"><g:message code="weatherSet.startDate.label" default="Start Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'startDate', 'errors')}">
                                    <g:datePicker name="startDate" precision="day" value="${weatherSetInstance?.startDate}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
