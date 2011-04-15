

<%@ page import="powertac.weather.server.WeatherSet" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'weatherSet.label', default: 'WeatherSet')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${weatherSetInstance}">
            <div class="errors">
                <g:renderErrors bean="${weatherSetInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${weatherSetInstance?.id}" />
                <g:hiddenField name="version" value="${weatherSetInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="cloudCoverColumnName"><g:message code="weatherSet.cloudCoverColumnName.label" default="Cloud Cover Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'cloudCoverColumnName', 'errors')}">
                                    <g:textField name="cloudCoverColumnName" value="${weatherSetInstance?.cloudCoverColumnName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dbHost"><g:message code="weatherSet.dbHost.label" default="Db Host" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbHost', 'errors')}">
                                    <g:textField name="dbHost" value="${weatherSetInstance?.dbHost}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dbName"><g:message code="weatherSet.dbName.label" default="Db Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbName', 'errors')}">
                                    <g:textField name="dbName" value="${weatherSetInstance?.dbName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dbPass"><g:message code="weatherSet.dbPass.label" default="Db Pass" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbPass', 'errors')}">
                                    <g:textField name="dbPass" value="${weatherSetInstance?.dbPass}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dbtableName"><g:message code="weatherSet.dbtableName.label" default="Dbtable Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbtableName', 'errors')}">
                                    <g:textField name="dbtableName" value="${weatherSetInstance?.dbtableName}" />
                                </td>
                            </tr>
                        
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
                                  <label for="games"><g:message code="weatherSet.games.label" default="Games" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'games', 'errors')}">
                                    <g:select name="games" from="${powertac.weather.server.GameModel.list()}" multiple="yes" optionKey="id" size="5" value="${weatherSetInstance?.games*.id}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="idColumnName"><g:message code="weatherSet.idColumnName.label" default="Id Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'idColumnName', 'errors')}">
                                    <g:textField name="idColumnName" value="${weatherSetInstance?.idColumnName}" />
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
                                  <label for="reportString"><g:message code="weatherSet.reportString.label" default="Report String" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'reportString', 'errors')}">
                                    <g:textField name="reportString" value="${weatherSetInstance?.reportString}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="reports"><g:message code="weatherSet.reports.label" default="Reports" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'reports', 'errors')}">
                                    <g:select name="reports" from="${powertac.weather.server.WeatherReport.list()}" multiple="yes" optionKey="id" size="5" value="${weatherSetInstance?.reports*.id}" />
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
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="tempColumnName"><g:message code="weatherSet.tempColumnName.label" default="Temp Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'tempColumnName', 'errors')}">
                                    <g:textField name="tempColumnName" value="${weatherSetInstance?.tempColumnName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="windDirColumnName"><g:message code="weatherSet.windDirColumnName.label" default="Wind Dir Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'windDirColumnName', 'errors')}">
                                    <g:textField name="windDirColumnName" value="${weatherSetInstance?.windDirColumnName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="windSpeedColumnName"><g:message code="weatherSet.windSpeedColumnName.label" default="Wind Speed Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'windSpeedColumnName', 'errors')}">
                                    <g:textField name="windSpeedColumnName" value="${weatherSetInstance?.windSpeedColumnName}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
