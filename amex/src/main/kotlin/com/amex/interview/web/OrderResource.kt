package com.amex.interview.web

import com.amex.interview.exception.NotFoundException
import com.amex.interview.model.OrderSummary
import com.amex.interview.model.UserOrder
import com.amex.interview.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/order")
class OrderResource(val service: OrderService) {

    @PostMapping
    fun createOrder(@RequestBody userOrder: UserOrder) : OrderSummary  {
        return service.createUserOrder(userOrder)
    }

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: String) : OrderSummary  {
        val uuid = UUID.fromString(orderId)
        return service.getUserOrderByUUID(uuid) ?:
            throw NotFoundException("Can't find an order with UUID: $uuid")
    }

    // An unconstrained invocation isn't the best practice...
    // Normally the size of the items returned would be constrained (by the service).
    //
    // Assuming a paging management though, that parameter (what page, how many per page, etc.)
    // would be specified here.
    //
    // Also with real entities we might transform the results to not expose the
    // internal entity (OrderSummary) to a smaller/more secure Data Transfer Object.
    // (e.g. audit fields and other sensitive parameters (suspected Fraud markers?)
    // that might normally exist would be dropped on shippment).
    //
    // In the interest of time and given the directions, I'm not doing that...
    @GetMapping
    fun getAllOrders() : List<OrderSummary> = service.getAllOrders()

}
