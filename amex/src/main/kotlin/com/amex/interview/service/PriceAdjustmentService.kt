package com.amex.interview.service

import com.amex.interview.model.OrderSummary
import com.amex.interview.model.PriceAdjustmentSummary
import com.amex.interview.service.pricing.BuyNForThePriceOf
import org.springframework.stereotype.Service

@Service
class PriceAdjustmentService {

    private val currentDeals =
        listOf(
            BuyNForThePriceOf(1,2,1, "Buy one Apple get one free!"),
            BuyNForThePriceOf(2,3,2, "Buy 3 Oranges for the price of 2!")
        )

    fun applyCurrentDeals(orderSummary: OrderSummary): OrderSummary {
        if (orderSummary.priceAdjustments.isNotEmpty()) {
            throw IllegalArgumentException("Requires that no adjustments have already been made!")
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

    private fun getCurrentDeals() : List<PriceAdjuster> = currentDeals
}

interface PriceAdjuster {
    fun getPriceAdjustments(userOrderSummary: OrderSummary): List<PriceAdjustmentSummary>
}