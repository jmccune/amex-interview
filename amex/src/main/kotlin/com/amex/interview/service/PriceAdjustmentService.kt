package com.amex.interview.service

import com.amex.interview.model.OrderSummary
import com.amex.interview.model.PriceAdjustmentSummary
import com.amex.interview.model.PricedItemOrder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class PriceAdjustmentService {

    fun applyCurrentDeals(orderSummary: OrderSummary): OrderSummary {
        if (orderSummary.priceAdjustments.isNotEmpty()) {
            throw IllegalStateException("Requires that no adjustments have already been made!")
        }

        val priceAdjustments =
            getCurrentDeals().flatMap { it.getPriceAdjustments(orderSummary) }
        val totalAdjustment = priceAdjustments.map { it.priceChangeInCents}.sum()

        var adjustedFinalPrice = orderSummary.totalPriceInCents + totalAdjustment;
        if (adjustedFinalPrice < 0 ) {
            adjustedFinalPrice = 0
        }

        return OrderSummary(
            orderSummary.orderId, orderSummary.pricedItems,
            priceAdjustments, adjustedFinalPrice )

    }

    private fun getCurrentDeals() : List<PriceAdjuster> = listOf(
        BuyNForThePriceOf(1,2,1, "Buy one Apple get one free!"),
        BuyNForThePriceOf(2,3,2, "Buy 3 Oranges for the price of 2!")
    )
}

interface PriceAdjuster {
    fun getPriceAdjustments(userOrderSummary: OrderSummary): List<PriceAdjustmentSummary>
}


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