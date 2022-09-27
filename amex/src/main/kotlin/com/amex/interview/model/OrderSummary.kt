package com.amex.interview.model

import java.util.*

class OrderSummary(
    var orderId: UUID,
    var pricedItems: List<PricedItemOrder>,
    var priceAdjustments: List<PriceAdjustmentSummary>,
    var totalPriceInCents:  Int
)

/** Note: Rebates & savings should be negative. */
class PriceAdjustmentSummary(val summary:String, val priceChangeInCents: Int)