package com.bendrisstarek.dynamicrecyclerview.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * this class represents string utils
 */
object StringUtils {
    const val UTF8 = "UTF-8"

    /**
     * this function returns the month abreviation from a number
     * @param i
     * @return
     */
    @JvmStatic
    fun transformToValidString(s: String): String {
        var result = s

        if(result[0] == '\"' && result[result.length-1] == '\"')
            result = result.substring(1,result.length-1)

        if(isValidDate(result)) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val d = sdf.parse(result)
            sdf.applyPattern("yyyy-MM-dd")
            result = sdf.format(d!!)
        }

        return result
    }

    fun isValidDate(inDate: String): Boolean {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        dateFormat.isLenient = false
        try {
            dateFormat.parse(inDate.trim { it <= ' ' })
        } catch (pe: ParseException) {
            val dateFormat =
                SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss:ms", Locale.US)
            dateFormat.isLenient = false
            try {
                dateFormat.parse(inDate.trim { it <= ' ' })
            } catch (pe: ParseException) {
                return false
            }
        }
        return true
    }

}