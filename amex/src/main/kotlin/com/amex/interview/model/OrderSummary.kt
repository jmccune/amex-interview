package com.amex.interview.model

import java.util.*

class OrderSummary(
    var orderId: UUID,
    var pricedItems: List<PricedItemOrder>,
    var priceAdjustments: List<PriceAdjustmentSummary>,
    var totalPriceInCents:  Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderSummary

        if (orderId != other.orderId) return false
        if (pricedItems != other.pricedItems) return false
        if (priceAdjustments != other.priceAdjustments) return false
        if (totalPriceInCents != other.totalPriceInCents) return false

        return true
    }

    override fun hashCode(): Int {
        var result = orderId.hashCode()
        result = 31 * result + pricedItems.hashCode()
        result = 31 * result + priceAdjustments.hashCode()
        result = 31 * result + totalPriceInCents
        return result
    }

    override fun toString(): String {
        return "OrderSummary(orderId=$orderId, pricedItems=$pricedItems, priceAdjustments=$priceAdjustments, totalPriceInCents=$totalPriceInCents)"
    }


}


/** Note: Rebates & savings should be negative. */
class PriceAdjustmentSummary(val summary:String, val priceChangeInCents: Int)