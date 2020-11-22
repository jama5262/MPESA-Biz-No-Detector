package com.jama.mpesa_biz_no_detector.fuzzySearch

import android.util.Log
import com.jama.mpesa_biz_no_detector.BizNoType
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.utils.cleanBizNo
import me.xdrop.fuzzywuzzy.FuzzySearch

class SearchBizNo {

    private val cutOff = 70
    val foo = listOf(
        "BUY AIRTIME",

                "WITH MPESA",

                "LIPA NA m~",

    "PESA",

    "PAYBILL NO.",

    "7 70 71 1",

    "Acc",

    "airtel",

    "Telkom",

    "Safaricom",

    "Twaweza",

    "TELSAN NETWORKS",

    "Call, Text or WhatsApp 0700900092"
    )

    private val tillNoQueries = listOf("buy goods till", "buy goods", "till")
    private val paybillNoQueries = listOf("paybill", "pay bill")
    private val accountNoQueries = listOf("account", "acc.", "acc")

    fun search(): DetectedBizNo? {
        var type = BizNoType.TILL_NUMBER
        var businessNoMatchedIndex: Int
        var accountNoMatchedIndex = -1

        businessNoMatchedIndex = findMatchedIndex(tillNoQueries)
        if (businessNoMatchedIndex == -1) {
            businessNoMatchedIndex = findMatchedIndex(paybillNoQueries)
            accountNoMatchedIndex = findMatchedIndex(accountNoQueries)
            type = BizNoType.PAYBILL_NUMBER
        }

        val businessNo = if (businessNoMatchedIndex != -1) foo[businessNoMatchedIndex + 1] else null
        val accountNo = if (accountNoMatchedIndex != -1) foo[accountNoMatchedIndex + 1] else null

        if (businessNo == null) return null

        return DetectedBizNo(
            type,
            businessNo.cleanBizNo(),
            accountNo
        )
    }

    private fun performFuzzySearchIndex(query: String): Int {
        val matchedIndex = FuzzySearch.extractSorted(query, foo, cutOff).map { it.index }
        return if (matchedIndex.isNotEmpty()) matchedIndex[0] else -1
    }

    private fun findMatchedIndex(queries: List<String>): Int {
        var matchedIndex = -1
        for (query in queries) {
            matchedIndex = performFuzzySearchIndex(query)
            if (matchedIndex != -1) break
        }
        return matchedIndex
    }
}