package com.amex.interview.service

import com.amex.interview.model.OrderSummary
import com.amex.interview.model.PriceAdjustmentSummary
import com.amex.interview.model.PricedItemOrder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.util.*
import java.util.Collections.emptyList


class PriceAdjustmentServiceTest {

    var service = PriceAdjustmentService()

    // THis suggests a condition that doesn't actually apply to the
    // code as written, but how it would likely work for a real service.
    // (e.g. where deals are temporary/expire, etc.)
    @Test
    fun testWhereNoOpportunityForDeals() {
        ReflectionTestUtils.setField(service, "currentDeals", emptyList<PriceAdjuster>())
        val orderSummary = OrderSummary(UUID.randomUUID(), APPLES_ORDER, emptyList(), APPLES_ORDER_PRICE)
        val orderSummaryWithRebates = service.applyCurrentDeals(orderSummary)
        assertTrue(orderSummaryWithRebates.priceAdjustments.isEmpty())
        assertEquals(orderSummary, orderSummaryWithRebates)
    }

    @Test
    fun testWhereNoDealsApply() {
        val orderSummary = OrderSummary(UUID.randomUUID(),
            listOf(ONE_APPLE), emptyList(), 60)
        val orderSummaryWithRebates = service.applyCurrentDeals(orderSummary)
        assertTrue(orderSummaryWithRebates.priceAdjustments.isEmpty())
        assertEquals(orderSummary, orderSummaryWithRebates)
    }

    @Test
    fun testWithMultipleRebates() {
        val orderSummary = OrderSummary(UUID.randomUUID(),
            MIXED_ORDER, emptyList(), MIXED_ORDER_NONREBATE_PRICE)

        val orderSummaryWithRebates = service.applyCurrentDeals(orderSummary)

        assertEquals(MIXED_ORDER_REBATE_PRICE, orderSummaryWithRebates.totalPriceInCents)

        assertEquals(2,orderSummaryWithRebates.priceAdjustments.size)
        val rebate1= orderSummaryWithRebates.priceAdjustments[0]
        assertEquals(MO_REBATE_1,rebate1.priceChangeInCents)
        assertEquals(MO_REBATE_1_MESSAGE,rebate1.summary)

        val rebate2= orderSummaryWithRebates.priceAdjustments[1]
        assertEquals(MO_REBATE_2,rebate2.priceChangeInCents)
        assertEquals(MO_REBATE_2_MESSAGE,rebate2.summary)
    }

    @Test
    fun failsWhenPreExistingRebates() {

        val preExistingRebates = listOf(PriceAdjustmentSummary("FakeRebate",-50))
        val orderSummary = OrderSummary(UUID.randomUUID(), APPLES_ORDER,
            preExistingRebates, APPLES_ORDER_PRICE )

        val ex = Assertions.assertThrows(IllegalArgumentException::class.java) {
            service.applyCurrentDeals(orderSummary)
        }
        assertEquals("Requires that no adjustments have already been made!", ex.message)
    }

    @Test
    fun rebatesDoNotTakePastZero() {
        ReflectionTestUtils.setField(service, "currentDeals", listOf(RebatePastZero()))
        val orderSummary = OrderSummary(UUID.randomUUID(), APPLES_ORDER, emptyList(), APPLES_ORDER_PRICE)
        val orderSummaryWithRebates = service.applyCurrentDeals(orderSummary)
        assertEquals(1,orderSummaryWithRebates.priceAdjustments.size)
        assertEquals(0,orderSummaryWithRebates.totalPriceInCents)

        val rebate1= orderSummaryWithRebates.priceAdjustments[0]
        assertEquals(-1000,rebate1.priceChangeInCents)
        assertEquals("Rebate past zero",rebate1.summary)
    }

    companion object {
        private val ONE_APPLE =     PricedItemOrder(1,"Apple",60,1)
        private val FIVE_APPLES =     PricedItemOrder(1,"Apple",60,5)
        private val FOUR_ORANGES =     PricedItemOrder(2,"Orange",25,4)

        val APPLES_ORDER = listOf( FIVE_APPLES )
        const val APPLES_ORDER_PRICE = 300

        val MIXED_ORDER = listOf(FOUR_ORANGES, FIVE_APPLES)
        const val MIXED_ORDER_NONREBATE_PRICE = 400
        const val MO_REBATE_1_MESSAGE = "Buy one Apple get one free! x 2"
        const val MO_REBATE_1 = -2 * 60
        const val MO_REBATE_2_MESSAGE = "Buy 3 Oranges for the price of 2!"
        const val MO_REBATE_2 = -1 * 25
        const val MIXED_ORDER_REBATE_PRICE = MIXED_ORDER_NONREBATE_PRICE + MO_REBATE_1 + MO_REBATE_2

    }
}

class RebatePastZero: PriceAdjuster {
    override fun getPriceAdjustments(userOrderSummary: OrderSummary): List<PriceAdjustmentSummary> {
        return listOf(PriceAdjustmentSummary("Rebate past zero", -1000))
    }
}

