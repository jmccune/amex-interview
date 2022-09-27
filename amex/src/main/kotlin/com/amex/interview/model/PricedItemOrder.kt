package com.amex.interview.model

class PricedItemOrder(
    var itemId: Long,
    var itemName: String,
    var unitPriceInCents: Int,
    var numUnits: Int,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PricedItemOrder

        if (itemId != other.itemId) return false
        if (itemName != other.itemName) return false
        if (unitPriceInCents != other.unitPriceInCents) return false
        if (numUnits != other.numUnits) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + itemName.hashCode()
        result = 31 * result + unitPriceInCents.hashCode()
        result = 31 * result + numUnits
        return result
    }

    override fun toString(): String {
        return "PricedItemOrder(itemId=$itemId, itemName='$itemName', unitPriceInCents=$unitPriceInCents, numUnits=$numUnits)"
    }
}