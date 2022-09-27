package com.amex.interview.service

import com.amex.interview.model.InventoryItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class InventoryOrderServiceTest {
    @Autowired
    lateinit var service:InventoryService

    companion object {
        val APPLE_INVENTORY = InventoryItem(1L, "Apple", 60, 1000)
        val ORANGE_INVENTORY = InventoryItem(2L, "Orange", 25, 500)
    }

    @Test
    fun testServiceReturnsAllTheItems() {
        val items = service.lookupItemsByIds(listOf(1L,2L))
        assertEquals(2,items.size)
        assertEquals(APPLE_INVENTORY,items[0])
        assertEquals(ORANGE_INVENTORY,items[1])
    }

    @Test
    fun testFiltersCorrectly() {
        val items = service.lookupItemsByIds(listOf(2L,99999L))
        assertEquals(1,items.size)
        assertEquals(ORANGE_INVENTORY,items[0])
    }
}