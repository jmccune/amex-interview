package com.amex.interview.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/** Somewhat of an integration test across layers. */
@SpringBootTest
@AutoConfigureMockMvc
class OrderResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc;

    companion object {
        const val CREATE_ENTRY_JSON ="""
            {
                "orderedItems": [ 
                    { 
                      "itemId": 1,
                      "itemName": "Apple (human readable what the front end thinks...)",
                      "unitPriceInCents": 60,
                      "numUnits": 7
                    },
                    {
                      "itemId": 2,
                      "itemName": "Orange (human readable what the front end thinks...)",
                      "unitPriceInCents": 25,
                      "numUnits": 2
                    }
                ]      
            }
        """
    }
    @Test
    fun createOrder() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_ENTRY_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems.length()",`is`(2)))

            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[0].itemId",`is`(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[0].itemName",`is`("Apple")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[0].unitPriceInCents",`is`(60)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[0].numUnits",`is`(7)))

            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[1].itemId",`is`(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[1].itemName",`is`("Orange")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[1].unitPriceInCents",`is`(25)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pricedItems[1].numUnits",`is`(2)))

            .andExpect(MockMvcResultMatchers.jsonPath("$.priceAdjustments.length()",`is`(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.priceAdjustments[0].summary",
                `is`("Buy one Apple get one free! x 3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.priceAdjustments[0].priceChangeInCents",
                `is`(-180)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPriceInCents",`is`(290)))

            // (For debugging if desired)
            .andDo(MockMvcResultHandlers.print())
    }


}