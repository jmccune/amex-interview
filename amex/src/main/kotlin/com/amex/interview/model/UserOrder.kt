package com.amex.interview.model

import com.amex.interview.annotations.NoArgConstructor

@NoArgConstructor
class UserOrder(
    var orderedItems: List<PricedItemOrder>
)

