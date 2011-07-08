

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
                        		<tr>
                        			<td colspan="2">
                        				<label><h2>Database Connection</h2></label>
                        			</td>
                        		</tr>
                        	</tr>
                            <tr class="prop">
                            	
                                <td valign="top" class="name">
                                	
                                    <label for="dbHost"><g:message code="weatherSet.dbHost.label" default="Db Host" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbHost', 'errors')}">
                                    <g:textField name="dbHost" value="db.itlabs.umn.edu:3313" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dbName"><g:message code="weatherSet.dbName.label" default="Db Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbName', 'errors')}">
                                    <g:textField name="dbName" value="powertac" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dbPass"><g:message code="weatherSet.dbPass.label" default="Db Pass" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbPass', 'errors')}">
                                    <g:passwordField name="dbPass" value="somepassword" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dbtableName"><g:message code="weatherSet.dbtableName.label" default="Db Table Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'dbtableName', 'errors')}">
                                    <g:textField name="dbtableName" value="historical_weather_data_minneapolis" />
                                </td>
                            </tr>
                        	<tr class="prop">
                        		<tr>
                        			<td colspan="2">
                        				<label><h2>Simulation Setup</h2></label>
                        			</td>
                        		</tr>
                        	</tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="startId"><g:message code="weatherSet.startId.label" default="Start Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'startId', 'errors')}">
                                    <g:textField name="startId" value="1"  />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="numberDays"><g:message code="weatherSet.numberDays.label" default="Number Days" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'numberDays', 'errors')}">
                                    <g:textField name="numberDays" value="24" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                        		<tr>
                        			<td colspan="2">
                        				<label><h2>Column Setup</h2></label>
                        			</td>
                        		</tr>
                        	</tr>
                                                    
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="idColumnName"><g:message code="weatherSet.idColumnName.label" default="Id Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'idColumnName', 'errors')}">
                                    <g:textField name="idColumnName" value="id_weather" />
                                </td>
                            </tr>
                        
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="tempColumnName"><g:message code="weatherSet.tempColumnName.label" default="Temp Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'tempColumnName', 'errors')}">
                                    <g:textField name="tempColumnName" value="temp" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="windDirColumnName"><g:message code="weatherSet.windDirColumnName.label" default="Wind Dir Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'windDirColumnName', 'errors')}">
                                    <g:textField name="windDirColumnName" value="wind_dir" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="windSpeedColumnName"><g:message code="weatherSet.windSpeedColumnName.label" default="Wind Speed Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'windSpeedColumnName', 'errors')}">
                                    <g:textField name="windSpeedColumnName" value="wind_spd" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="cloudCoverColumnName"><g:message code="weatherSet.cloudCoverColumnName.label" default="Cloud Cover Column Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: weatherSetInstance, field: 'cloudCoverColumnName', 'errors')}">
                                    <g:textField name="cloudCoverColumnName" value="0.0" />
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
