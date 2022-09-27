package com.amex.interview.model

import java.util.*

class OrderSummary(
    var orderId: UUID,
    var pricedItems: List<PricedItemOrder>,
    var totalPriceInCents:  Int
)