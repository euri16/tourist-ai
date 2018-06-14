package com.eury.touristai.repository.remote.models

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
data class VisionLandmarkRequest (var requests: List<VisionLandmarkRequestDetail>) {

    constructor(image:String, features:Array<String>) : this(arrayListOf<VisionLandmarkRequestDetail>()) {
        val visionFeatures = features.map { VisionLandmarkFeature(type = it) }
        val requestDetail = VisionLandmarkRequestDetail(image = VisionLandmarkImage(image), features = visionFeatures)
        requests = arrayListOf(requestDetail)
    }

    data class VisionLandmarkRequestDetail (val image:VisionLandmarkImage, val features:List<VisionLandmarkFeature>, val imageContext: ImageContext = ImageContext())
    data class VisionLandmarkImage (val content:String)
    data class VisionLandmarkFeature (val type:String)
    data class ImageContext(val webDetectionParams: WebDetectionParams = WebDetectionParams(true))
    data class WebDetectionParams(val includeGeoResults:Boolean = true)
}