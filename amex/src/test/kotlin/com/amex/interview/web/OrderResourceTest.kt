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

    @Autowired
    private lateinit var objectMapper: ObjectMapper

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
            .andExpect(MockMvcResultMatchers.jsonPath("$.totalPriceInCents",`is`(470)))
            // (For debugging if desired)
            .andDo(MockMvcResultHandlers.print())
    }

}