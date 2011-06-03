/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an
* "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
* either express or implied. See the License for the specific language
* governing permissions and limitations under the License.
*/
package powertac.weather.server


import org.joda.time.Instant
import org.powertac.common.Competition
import org.powertac.common.Timeslot
import org.powertac.common.TimeService
import org.powertac.common.ClockDriveJob

/**
 * This is the weather controller. 
 * @author Erik Onarheim
 */
class WeatherService {

  static transactional = false
  Competition competition
  
  int timeslotPhaseCount = 3 // # of phases/timeslot
  boolean running = false
  
  //def quartzScheduler
  //def clockDriveJob
  //def timeService // inject simulation time service dependency
  
  //def phaseRegistrations
  int timeslotCount = 0
  long timeslotMillis
  
  
  /**
  * Runs the initialization process and starts the simulation.
  */
  void init ()
  {
    // to enhance testability, initialization is split into a static phase
    // followed by starting the clock
	log.info("WeatherService Activated")
    if (setup() == false){
 	  return
    }else{
      start((long)(competition.timeslotLength * TimeService.MINUTE / competition.simulationRate))
    }
  }
  
  
  /**
  * Starts the simulation.
  */
  void start (long scheduleMillis)
  {
    quartzScheduler.start()
    // wait for start time
    long now = new Date().getTime()
    long start = now + scheduleMillis * 2 - now % scheduleMillis

    timeService.start = start
   
    // Start up the clock at the correct time
    Thread.sleep(start - new Date().getTime())
    ClockDriveJob.schedule(scheduleMillis)
    timeService.updateTime()
   
    running = true
    scheduleStep()
  }
  
  /**
  * Runs a step of the simulation
  */
  void step ()
  {
    if (!running) {
 	 log.info("Stop simulation") 
 	 return
    }
    //def time = timeService.currentTime
    log.info "step at $time"
   
    scheduleStep()
    
 }
  
  
  /**
  * Stops the simulation.
  */
  void stop ()
  {
    running = false
  }
   
  /**
  * Schedules a step of the simulation
  */
  void scheduleStep ()
  {
    timeService.addAction(new Instant(timeService.currentTime.millis + timeslotMillis),
	{ this.step() })
  }
  
  
  boolean setup()
  {
	log.info("attempted dep inject...")
	timeService.currentTime = competition.simulationBaseTime
	timeService.currentDateTime = currentDateTime
	timeService.base = competition.simulationBaseTime.millis
	timeService.rate = competition.simulationRate
    timeService.modulo = competition.timeslotLength * TimeService.MINUTE
	return true
  }
 
}
