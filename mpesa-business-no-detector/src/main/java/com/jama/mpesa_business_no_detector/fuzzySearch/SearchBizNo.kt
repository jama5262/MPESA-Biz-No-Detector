package com.jama.mpesa_business_no_detector.fuzzySearch

import android.util.Log
import me.xdrop.fuzzywuzzy.FuzzySearch

class SearchBizNo {

    private val cutOff = 70
    val foo = listOf(
        "LIPA NA MEPESA",

                "YWCA-PARKVIEW SUITES",

                "Paybill Number",

                "53540",

                "CLIENT'S NAME - RM NO."
    )

    private val tillNoQueries = listOf("buy goods till", "buy goods", "till")
    private val paybillNoQueries = listOf("paybill", "pay bill")
    private val accountNoQueries = listOf("account", "acc")

    fun search() {
        var businessNoMatchedIndex = -1
        var accountNoMatchedIndex = -1
        businessNoMatchedIndex = getMatchedIndex(tillNoQueries)
        if (businessNoMatchedIndex == -1) {
            businessNoMatchedIndex = getMatchedIndex(paybillNoQueries)
            accountNoMatchedIndex = getMatchedIndex(accountNoQueries)
        }
        val businessNo = if (businessNoMatchedIndex != -1) foo[businessNoMatchedIndex + 1] else "not found"
        val accountNo = if (accountNoMatchedIndex != -1) foo[accountNoMatchedIndex + 1] else "not found"

        Log.e("jjj", "business No -> $businessNo and acc no -> $accountNo")
    }

    private fun fuzzySearchIndex(query: String): Int {
        val matchedIndex = FuzzySearch.extractSorted(query, foo, cutOff).map { it.index }
        return if (matchedIndex.isNotEmpty()) matchedIndex[0] else -1
    }

    private fun getMatchedIndex(queries: List<String>): Int {
        var matchedIndex = -1
        for (query in queries) {
            matchedIndex = fuzzySearchIndex(query)
            if (matchedIndex != -1) break
        }
        return matchedIndex
    }

}