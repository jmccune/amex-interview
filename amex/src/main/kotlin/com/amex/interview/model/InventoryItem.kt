package com.amex.interview.model

class InventoryItem(
    var id: Long,
    var name: String,
    var unitPriceInCents: Int,
    var numberAvailable: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InventoryItem

        if (id != other.id) return false
        if (name != other.name) return false
        if (unitPriceInCents != other.unitPriceInCents) return false
        if (numberAvailable != other.numberAvailable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + unitPriceInCents.hashCode()
        result = 31 * result + numberAvailable
        return result
    }
}
