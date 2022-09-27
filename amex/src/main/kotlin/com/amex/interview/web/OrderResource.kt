package com.amex.interview.web

import com.amex.interview.model.OrderSummary
import com.amex.interview.model.UserOrder
import com.amex.interview.service.OrderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderResource(val service: OrderService) {

    @PostMapping
    fun createOrder(@RequestBody userOrder: UserOrder) : OrderSummary  {
        return service.createUserOrder(userOrder)
    }
}
