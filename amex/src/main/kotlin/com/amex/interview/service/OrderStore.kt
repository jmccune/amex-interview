package com.amex.interview.service

import com.amex.interview.model.OrderSummary
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderStore {

    // Do an in memory store -- in reality this would be persisted somewhere (DB/File/NoSQL...)
    private val store = mutableMapOf<UUID, OrderSummary>()

    fun save(order:OrderSummary) = store.put(order.orderId,order)
    fun findById(orderId:UUID) = store[orderId]
    fun findAll() : List<OrderSummary> = store.values.toList()

}