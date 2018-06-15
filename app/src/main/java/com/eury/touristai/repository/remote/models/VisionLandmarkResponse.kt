package com.eury.touristai.repository.remote.models

import java.util.*

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */

data class VisionResponse (val responses: LinkedList<VisionLandmarkResponse>) {

    data class VisionLandmarkResponse (val webDetection:WebDetectionDetail?)

    data class WebDetectionDetail(val visuallySimilarImages:List<SimilarImage>,
                                  val bestGuessLabels:List<GuessLabel>,
                                  val webEntities:List<WebEntity>)
    data class SimilarImage(val url:String)
    data class GuessLabel(val label:String)
    data class WebEntity(val description: String, val score:Float)
}