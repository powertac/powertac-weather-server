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
			// Grab the WeatherDatabaseService
			def WeatherDatabaseService wds = new WeatherDatabaseService()
			
			// Default connection to tac05, other users wont be able to access
			// this database unless they are behind the firewall.
			wds.defaultRegister()
			wds.connect()
			List result = wds.executeQuery(wds.defaultQuery)
			result.each {row -> render "[" +"id_date:"+ row[0] +", id_hrmn:" + row[1] + ", temp:"+ row[2]+", wind_spd:" +row[3]+", wind_dir:"+row[4]+", cloud_cvr:"+row[5] +" ]\n"}
			
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
