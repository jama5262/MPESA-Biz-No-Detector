package com.jama.mpesa_biz_no_detector.fuzzySearch

import com.jama.mpesa_biz_no_detector.BizNoType
import com.jama.mpesa_biz_no_detector.models.DetectedBizNo
import com.jama.mpesa_biz_no_detector.utils.Constants
import com.jama.mpesa_biz_no_detector.utils.cleanBizNo
import me.xdrop.fuzzywuzzy.FuzzySearch

class SearchBizNo(private val choices: List<String>) {

    private companion object

    fun search(): DetectedBizNo? {
        var type = BizNoType.TILL_NUMBER
        var businessNoMatchedIndex = findMatchedIndex(Constants.TILL_NO_QUERIES)
        var accountNoMatchedIndex = -1

        if (businessNoMatchedIndex == -1) {
            businessNoMatchedIndex = findMatchedIndex(Constants.PAYBILL_NO_QUERIES)
            accountNoMatchedIndex = findMatchedIndex(Constants.ACCOUNT_NO_QUERIES)
            type = BizNoType.PAYBILL_NUMBER
        }

        val businessNo = if (businessNoMatchedIndex != -1) {
            choices.getOrNull(businessNoMatchedIndex + 1)
        } else null

        val accountNo = if (accountNoMatchedIndex != -1) {
            choices.getOrNull(accountNoMatchedIndex + 1)
        } else null

        if (businessNo == null) return null

        return DetectedBizNo(
            type,
            businessNo.cleanBizNo(),
            accountNo
        )
    }

    private fun performFuzzySearchIndex(query: String): Int {
        val matchedIndex = FuzzySearch.extractSorted(
            query,
            choices,
            Constants.CUTOFF
        ).map { it.index }
        return matchedIndex.getOrElse(0) { -1 }
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
