package com.jama.mpesa_business_no_detector.fuzzySearch

import android.util.Log
import me.xdrop.fuzzywuzzy.FuzzySearch

class SearchBizNo {

    fun search() {
        val foo = listOf("jama", "mohamed", "wardere")
        val s = FuzzySearch.extractOne("jama", foo)
        Log.e("jjj", "$s")
    }

}