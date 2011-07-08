package powertac.weather.server

import java.awt.image.renderable.RenderableImage;

import groovy.sql.Sql
import powertac.weather.server.WeatherService

class WeatherSetController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }
	
	
	def weatherRequest = {
		if(WeatherSet.findById(params.get("id")) != null){
			
			render "Invalid ID, cannot retrieve weather tuples. Please check your queryString."
			
		}else{
			int weatherIdQ = 1
			int nDaysOut = 3	
		
			if(params.get("weather_id") != null && params.get("weather_days") != null){
				weatherIdQ = params.get("weather_id").toInteger()
				nDaysOut = params.get("weather_days").toInteger()
			}
			
			if(weatherIdQ > 900 && params.get("setname")=="default"){
					weatherIdQ = weatherIdQ%901
			}
			
				
		
			// Grab the WeatherDatabaseService
			def WeatherDatabaseService wds = new WeatherDatabaseService()
			
			// Default connection to tac05, other users wont be able to access
			// this database unless they are behind the firewall.
			if(params.get("setname")=="default"){
				wds.defaultRegister()
				wds.connect()
				List reportResult = wds.executeQuery(wds.genDefaultQuery(weatherIdQ))
				
				def query = wds.genWeatherQuery(weatherIdQ,nDaysOut)
				println query
				List forecastResult = wds.executeQuery(query)
				List forecastMultipliers = wds.genForecastMultipliers(nDaysOut)
				
				float prevTemp = 0
				reportResult.each {row -> 
					 render "[" +
					 "id_weather:"+ row[0] +
					 ", temp:"+ ((row[1] == 999.9f) ? prevTemp : row[1]) +
					 ", wind_spd:" +row[2]+
					 ", wind_dir:"+row[3]+
					 ", cloud_cvr:"+row[4] +
					 " ]\n"
					 
					 prevTemp = ((row[1] != 999.9f) ? row[1] : prevTemp) 
					 }
				
				render "---Forecast Data---\n"
				
				int i = 0
				forecastResult.each{row -> 
					def tmpRow = ((row[1] == 999.9f) ? prevTemp : row[1])
					render "["+
					"id_weather:"+row[0] +
					", temp:"+ (tmpRow+tmpRow*forecastMultipliers[i]) +
					", wind_spd:" + (row[2]+row[2]*forecastMultipliers[i]) +
					", wind_dir:"+ ((row[3]+row[3]*forecastMultipliers[i])%360) +
					", cloud_cvr:"+ (row[4]+row[4]*forecastMultipliers[i++]) +
					" ]\n"
					
					prevTemp = ((row[1] != 999.9f) ? row[1] : prevTemp)
					}
			}else{
				render "Error -- No Permissions"
			}
			
			
		}
	}

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [weatherSetInstanceList: WeatherSet.list(params), weatherSetInstanceTotal: WeatherSet.count()]
    }

    def create = {
        def weatherSetInstance = new WeatherSet()
        weatherSetInstance.properties = params
        return [weatherSetInstance: weatherSetInstance]
    }

    def save = {
        def weatherSetInstance = new WeatherSet(params)
        if (weatherSetInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), weatherSetInstance.id])}"
            redirect(action: "show", id: weatherSetInstance.id)
        }
        else {
            render(view: "create", model: [weatherSetInstance: weatherSetInstance])
        }
    }

    def show = {
        def weatherSetInstance = WeatherSet.get(params.id)
        if (!weatherSetInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), params.id])}"
            redirect(action: "list")
        }
        else {
            [weatherSetInstance: weatherSetInstance]
        }
    }

    def edit = {
        def weatherSetInstance = WeatherSet.get(params.id)
        if (!weatherSetInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [weatherSetInstance: weatherSetInstance]
        }
    }

    def update = {
        def weatherSetInstance = WeatherSet.get(params.id)
        if (weatherSetInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (weatherSetInstance.version > version) {
                    
                    weatherSetInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'weatherSet.label', default: 'WeatherSet')] as Object[], "Another user has updated this WeatherSet while you were editing")
                    render(view: "edit", model: [weatherSetInstance: weatherSetInstance])
                    return
                }
            }
            weatherSetInstance.properties = params
            if (!weatherSetInstance.hasErrors() && weatherSetInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), weatherSetInstance.id])}"
                redirect(action: "show", id: weatherSetInstance.id)
            }
            else {
                render(view: "edit", model: [weatherSetInstance: weatherSetInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def weatherSetInstance = WeatherSet.get(params.id)
        if (weatherSetInstance) {
            try {
                weatherSetInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'weatherSet.label', default: 'WeatherSet'), params.id])}"
            redirect(action: "list")
        }
    }
}
