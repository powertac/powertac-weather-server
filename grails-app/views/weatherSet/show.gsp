
<%@ page import="powertac.weather.server.WeatherSet" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'weatherSet.label', default: 'WeatherSet')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "id")}</td>
                            
                        </tr>
                    
                        
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.dbHost.label" default="Db Host" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "dbHost")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.dbName.label" default="Db Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "dbName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.dbPass.label" default="Db Pass" /></td>
                            
                            <td valign="top" class="value"><i>hidden</i></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.dbtableName.label" default="Db Table Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "dbtableName")}</td>
                            
                        </tr>
                    
                                            
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.startId.label" default="Start ID" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "startId")} </td>
                            
                        </tr>
                        
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.numberDays.label" default="Number Days" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "numberDays")}</td>
                            
                        </tr>
                        
                         <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.idColumnName.label" default="Id Column Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "idColumnName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.tempColumnName.label" default="Temp Column Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "tempColumnName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.windDirColumnName.label" default="Wind Dir Column Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "windDirColumnName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.windSpeedColumnName.label" default="Wind Speed Column Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "windSpeedColumnName")}</td>
                            
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="weatherSet.cloudCoverColumnName.label" default="Cloud Cover Column Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: weatherSetInstance, field: "cloudCoverColumnName")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${weatherSetInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
