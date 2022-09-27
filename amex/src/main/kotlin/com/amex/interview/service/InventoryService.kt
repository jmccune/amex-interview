package com.amex.interview.service

import com.amex.interview.model.InventoryItem
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


class InventoryItemListTypeRef : TypeReference<List<InventoryItem>>()
@Service
class InventoryService {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    val inventoryItems  = lazy {
        objectMapper.readValue(INVENTORY_AS_JSON,InventoryItemListTypeRef())
    }


    fun lookupItemsByIds(ids: Collection<Long>) : List<InventoryItem> {
        return inventoryItems.value.filter { ids.contains(it.id) }
    }

    companion object {
        val INVENTORY_AS_JSON = """
            [
              { "id": 1,
                "name": "Apple",
                "unitPriceInCents": 60,
                "numberAvailable": 1000
              },
              { "id": 2,
                "name": "Orange",
                "unitPriceInCents": 25,
                "numberAvailable": 500
              }
            ]

        """.trimIndent()
    }
}