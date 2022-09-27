package com.amex.interview.service.pricing

import com.amex.interview.model.OrderSummary
import com.amex.interview.model.PriceAdjustmentSummary
import com.amex.interview.service.PriceAdjuster
import java.lang.IllegalArgumentException

class BuyNForThePriceOf(
    private val itemId: Long,
    private val buyThisMany: Int,
    private val forThePriceOfThisMany: Int,
    private val rebateMessage: String
) : PriceAdjuster {

    init {
        if (buyThisMany <1 ||
            forThePriceOfThisMany < 1 ||
            buyThisMany <= forThePriceOfThisMany) {
            throw IllegalArgumentException("This must be a properly specified price reduction and isn't!")
        }
    }


    override fun getPriceAdjustments(userOrderSummary: OrderSummary): List<PriceAdjustmentSummary> {
        return userOrderSummary.pricedItems
            .filter { it.itemId==itemId }
            .map {
                // Integer division- floor is intentional (they only secure the rebate for whole units)
                val numRebates = it.numUnits / buyThisMany
                val savingsPerRebate = (buyThisMany * it.unitPriceInCents) - forThePriceOfThisMany * it.unitPriceInCents
                getPriceAdjustmentSummary(numRebates, savingsPerRebate)

            }.filterNotNull()
    }

    private fun getPriceAdjustmentSummary(numRebates: Int, savingsPerRebate: Int) : PriceAdjustmentSummary? {
        if (numRebates==0) {
            return null
        }

        val fullRebateMessage = if (numRebates > 1) {
            "$rebateMessage x $numRebates"
        } else {
            rebateMessage
        }
        return PriceAdjustmentSummary(fullRebateMessage, savingsPerRebate * numRebates * -1)
    }
}