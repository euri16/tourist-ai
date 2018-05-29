package com.eury.touristai.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import android.support.media.ExifInterface
import android.support.media.ExifInterface.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ExifUtil {

    fun rotateBitmap(absolutePath: String, bitmap: Bitmap): Bitmap {
        try {
            val orientation = getExifOrientation(absolutePath)

            if (orientation == ORIENTATION_NORMAL) {
                return bitmap
            }

            val matrix = Matrix()
            when (orientation) {
                ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)

                ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }

                ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }

                else -> return bitmap
            }


            return try {
                val oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                oriented
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                bitmap
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    fun resizeBitmap(path: String, quality: Int, newWidth: Int = 600, newHeight: Int = 600): Bitmap  {

        //resizing picture to a less pixel quality
        var picResize = BitmapFactory.decodeFile(path, null)
        picResize = Bitmap.createScaledBitmap(picResize, newWidth, newHeight, false)

        val bos = ByteArrayOutputStream()
        picResize.compress(Bitmap.CompressFormat.JPEG, quality, bos)
        val bitmapData = bos.toByteArray()

        //saving original image orientation
        val oldExif = ExifInterface(path)
        val exifOrientation = oldExif.getAttribute(TAG_ORIENTATION)

        //creating new temp file
        val newImageFile = File(path)
        val fos = FileOutputStream(newImageFile)
        fos.write(bitmapData)
        fos.close()

        //applying  original orientation to new resize file
        if (exifOrientation != null) {
            val newExif = ExifInterface(newImageFile.path)
            newExif.setAttribute(TAG_ORIENTATION, exifOrientation)
            newExif.saveAttributes()
        }

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        return BitmapFactory.decodeFile(newImageFile.absolutePath, options)
    }


    private fun getExifOrientation(src: String): Int {
        val orientation = ORIENTATION_NORMAL

        try {
            val exifInterface = ExifInterface(src)
            return exifInterface.getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return orientation
    }
}