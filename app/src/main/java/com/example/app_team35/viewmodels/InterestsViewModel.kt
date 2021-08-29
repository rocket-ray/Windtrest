package com.example.app_team35.viewmodels

import androidx.lifecycle.ViewModel
import com.example.app_team35.models.interests.Interests

/**
 * Defines the rules for displaying conditions
 */
class InterestsViewModel: ViewModel() {
    private val basisuser = Interests("Basisbruker", -40, 40, 20, 0, 40)
    private val tripsTraining = Interests("Tur og trening", -20, 30, 15, 0, 40)
    private val windsurfing = Interests("Vindsurfing", 0, 40, 20, 6, 15)
    private val paragliding = Interests("Paragliding", -20, 40, 20, 0, 8)
    private val drone = Interests("Droneflyging", -10, 30, 20, 0, 15)
    private val user_interests: MutableList<Interests> = mutableListOf(basisuser, tripsTraining, windsurfing, paragliding, drone)

    fun getInterests(): MutableList<Interests>{
        return user_interests
    }
    fun checkConditions(interest: String, temp: Double, wind: Double): String {
        val t = temperatureCondition(temp)
        var conditionDescr: String = ""
        if (interest == "Basisbruker"){
            conditionDescr = conditionBasis(t,wind)
        } else if (interest == "Tur og trening") {
            conditionDescr = conditionTurTrening(t, wind)
        } else if (interest == "Vindsurfing") {
            conditionDescr = conditionWind(t, wind)
        } else if (interest == "Paragliding") {
            conditionDescr = conditionPara(t, wind)
        } else if (interest == "Droneflyvning") {
            conditionDescr = conditionDrone(t, wind)
        }
        return conditionDescr
    }

    /**
     * In general, wind, temperature and gust is classified from 11-44 (where 11 is to cold, 22  too warm , 33 is ok conditions, 44 is optimal conditions)
     * If any of the interests have conditions of 5 we should advice them not to do the activity
     */

    fun temperatureCondition(temp: Double): Int {
        var tc: Int
        if (temp <= basisuser.minTemp) {
            tc = 11
        } else if (temp >= basisuser.maxTemp) {
            tc = 22
        } else if (temp >= basisuser.optimalTemp - 5 && temp <= basisuser.optimalTemp + 5) {
            tc = 44
        } else {
            tc = 33
        }
        return tc
    }

    fun conditionBasis(t: Int, wind: Double): String {
        if(wind == 0.0){
            return "Nå er det helt vindstille!"
        }else if(wind > 32.7){
            return "Det er virkelig uvær, det er vind\nav orkan styrke!"
        } else if (wind > 20.8){
            return "Det er uvær, det er storm!"
        } else if (wind > 10.8){
            return "Det er bevegelse i luften.\nNå er det kuling!"
        } else if (wind > 5.5){
            return "Nå er det kun en liten vind i form av bris!"
        } else{
            return "Nå er det så vidt litt bevegelse\ni trærne av vinden!"
        }
    }

    fun conditionTurTrening(t: Int, wind: Double): String {
        if (t == 44 && wind <= 20) {
            return "Det er veldig fine forhold for tur og trening!"
        } else if (t == 22 && wind <= 10) {
            return "Det er veldig varmt og lite vind,\ni dette været anbefaler vi ikke\ntur eller trening!"
        } else if (t == 11) {
            return "Det er litt i kaldeste laget for tur\nog trening ute i dag!"
        } else if (t == 33 && wind <= basisuser.maxWind) {
            return "Det er greie forhold for tur og trening i dag!"
        } else {
            return "Det er ikke de beste forholdene for tur og trening.\nOm du drar ut si alltid ifra hvor du\ngår eller sykler!"
        }
    }

    fun conditionWind(t: Int, wind: Double): String {
        if (wind < windsurfing.minWind) {
            return "Det er for lite vind for vindsurfing!"
        } else if (wind > windsurfing.maxWind){
            return "Det er mye vind, det er ikke anbefalt å\nvindsurfe med denne vinden!"
        } else if(wind >= windsurfing.minWind+2 && wind <= windsurfing.maxWind-3 && (t == 33 || t == 44)) {
            return "Det er supre forhold for vindsurfing!"
        } else {
            return "Hadde nok prøvd meg på noe annet\nenn vindsurfing i dag!"
        }

    }
    fun conditionPara(t: Int, wind: Double): String{
        if (wind > paragliding.maxWind){
            return "Det er mye vind, det er ikke anbefalt å\nparaglide i denne vinden!"
        } else if ((t==33 || t == 44) && wind < paragliding.maxWind){
            return "Det er supre forhold for paragliding!"
        } else if((wind < paragliding.maxWind) && t == 22){
            return "Det er fine vindforhold for paragliding,\nmen tenk på varmen!"
        } else if (wind < paragliding.maxWind && t == 11){
            return "Det er fine vindforhold for paragliding,\nmen veldig kaldt!"
        } else {
            return "Se ann forholdene!"
        }
    }

    fun conditionDrone(t: Int, wind: Double): String{
        if (wind > drone.maxWind){
            return "Det er for mye vind for en liten til medium drone,\nstiv kuling er ikke dronevær!"
        } else if (wind < drone.maxWind && (t == 44)){
            return "Det er veldig fine forhold for droneflyging!"
        } else if (wind < drone.maxWind && (t == 33)){
            return "Det er greie forhold for droneflyging!"
        } else{
            return "Se ann forholdene og droneferdighetene dine!"
        }
    }





}