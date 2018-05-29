package com.eury.touristai.utils

import android.util.Log

class Logger

//======================================================================================
//CONSTRUCTORS
//======================================================================================
private constructor() {

    //======================================================================================
    //FIELDS
    //======================================================================================
    private var sb: StringBuilder? = null
    //======================================================================================
    //ENUMS
    //======================================================================================

    /**
     * **LoggerDepth Enum** <br></br>
     *
     *  * ACTUAL_METHOD(4)
     *  * LOGGER_METHOD(3)
     *  * STACK_TRACE_METHOD(1)
     *  * JVM_METHOD(0)
     *
     */
    enum class LOGGER_DEPTH private constructor(val value: Int) {
        ACTUAL_METHOD(4),
        LOGGER_METHOD(3),
        STACK_TRACE_METHOD(1),
        JVM_METHOD(0)
    }

    init {
        if (LoggerLoader.instance != null) {
            Log.e(personalTAG, "Error: Logger already instantiated")
            throw IllegalStateException("Already Instantiated")
        } else {
            this.sb = StringBuilder(255)
        }
    }

    /**
     * Method that creates the tag automatically
     * @param depth (Defines the depth of the Logging)
     * @return
     */
    private fun getTag(depth: LOGGER_DEPTH): String? {
        try {
            val className = Thread.currentThread().stackTrace[depth.value].className
            sb!!.append(className.substring(className.lastIndexOf(".") + 1))
            sb!!.append("[")
            sb!!.append(Thread.currentThread().stackTrace[depth.value].methodName)
            sb!!.append("] - ")
            sb!!.append(Thread.currentThread().stackTrace[depth.value].lineNumber)
            return sb!!.toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e(personalTAG, ex.message)
        } finally {
            sb!!.setLength(0)
        }
        return null
    }

    /**
     * Simple d Method will log in default depth ACTUAL_METHOD
     * @param msg
     */
    fun d(msg: String) {
        try {
            Log.d(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg)
        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * d Method that takes LOGGER_DEPTH as the second parameter, will log custom depth level
     * @param msg
     * @param depth
     */
    fun d(msg: String, depth: LOGGER_DEPTH) {
        try {
            Log.d(getTag(depth), msg)
        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * d Method with Throwable that takes LOGGER_DEPTH as the third parameter, will log custom depth level
     * @param msg
     * @param t
     * @param depth
     */
    fun d(msg: String, t: Throwable, depth: LOGGER_DEPTH) {
        try {
            Log.d(getTag(depth), msg, t)
        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * Simple e Method will log in default depth ACTUAL_METHOD
     * @param msg
     */
    fun e(msg: String) {
        try {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * e Method that takes LOGGER_DEPTH as the second parameter, will log custom depth level
     * @param msg
     * @param depth
     */
    fun e(msg: String, depth: LOGGER_DEPTH) {
        try {
            Log.e(getTag(depth), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * e Method with Throwable that takes LOGGER_DEPTH as the third parameter, will log custom depth level
     * @param msg
     * @param t
     * @param depth
     */
    fun e(msg: String, t: Throwable, depth: LOGGER_DEPTH) {
        try {
            Log.e(getTag(depth), msg, t)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * Simple w Method will log in default depth ACTUAL_METHOD
     * @param msg
     */
    fun w(msg: String) {
        try {
            Log.w(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * w Method that takes LOGGER_DEPTH as the second parameter, will log custom depth level
     * @param msg
     * @param depth
     */
    fun w(msg: String, depth: LOGGER_DEPTH) {
        try {
            Log.w(getTag(depth), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * w Method with Throwable that takes LOGGER_DEPTH as the third parameter, will log custom depth level
     * @param msg
     * @param t
     * @param depth
     */
    fun w(msg: String, t: Throwable, depth: LOGGER_DEPTH) {
        try {
            Log.w(getTag(depth), msg, t)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * Simple v Method will log in default depth ACTUAL_METHOD
     * @param msg
     */
    fun v(msg: String) {
        try {
            Log.v(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * v Method that takes LOGGER_DEPTH as the second parameter, will log custom depth level
     * @param msg
     * @param depth
     */
    fun v(msg: String, depth: LOGGER_DEPTH) {
        try {
            Log.v(getTag(depth), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * v Method with Throwable that takes LOGGER_DEPTH as the third parameter, will log custom depth level
     * @param msg
     * @param t
     * @param depth
     */
    fun v(msg: String, t: Throwable, depth: LOGGER_DEPTH) {
        try {
            Log.v(getTag(depth), msg, t)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * Simple i Method will log in default depth ACTUAL_METHOD
     * @param msg
     */
    fun i(msg: String) {
        try {
            Log.i(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * i Method that takes LOGGER_DEPTH as the second parameter, will log custom depth level
     * @param msg
     * @param depth
     */
    fun i(msg: String, depth: LOGGER_DEPTH) {
        try {
            Log.i(getTag(depth), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * i Method with Throwable that takes LOGGER_DEPTH as the third parameter, will log custom depth level
     * @param msg
     * @param t
     * @param depth
     */
    fun i(msg: String, t: Throwable, depth: LOGGER_DEPTH) {
        try {
            Log.i(getTag(depth), msg, t)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * Simple wtf Method will log in default depth ACTUAL_METHOD
     * @param msg
     */
    fun wtf(msg: String) {
        try {
            Log.wtf(getTag(LOGGER_DEPTH.ACTUAL_METHOD), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * wtf Method that takes LOGGER_DEPTH as the second parameter, will log custom depth level
     * @param msg
     * @param depth
     */
    fun wtf(msg: String, depth: LOGGER_DEPTH) {
        try {
            Log.wtf(getTag(depth), msg)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }

    /**
     * wtf Method with Throwable that takes LOGGER_DEPTH as the third parameter, will log custom depth level
     * @param msg
     * @param t
     * @param depth
     */
    fun wtf(msg: String, t: Throwable, depth: LOGGER_DEPTH) {
        try {
            Log.wtf(getTag(depth), msg, t)

        } catch (exception: Exception) {
            Log.e(getTag(LOGGER_DEPTH.ACTUAL_METHOD), "Logger failed, exception: " + exception.message)
        }

    }


    //======================================================================================
    //INNER CLASSES
    //======================================================================================

    private object LoggerLoader {
        val instance = Logger()
    }

    companion object {

        //======================================================================================
        //CONSTANTS
        //======================================================================================
        private val personalTAG = "Logger"

        //======================================================================================
        //METHODS
        //======================================================================================
        val logger: Logger
            get() = LoggerLoader.instance
    }
}