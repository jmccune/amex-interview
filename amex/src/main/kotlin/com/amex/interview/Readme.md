
# AMEX Interview Exercise

This was an exercise for an interview
with American Express

## Problem:

Coding Task for Software Engineer Candidates
The following task will allow you to demonstrate your ability to deliver readable, maintainable and testable code. As an agile organization, we are applying equal emphasis on assessing both your problem solving and testing abilities.
Be prepared to present your solution over conversation that will lead to questions around design decisions, rationale and future expansion.

The exercise is to demo your technical skills and takes most candidates a few hours; please don’t spend a huge amount of time making it perfect.
* Please complete the following task with the language and libraries you feel comfortable with.
* Use git to version control the code and once complete, send us a link to the Github (or similar) repository via the recruitment agent

Instructions: Complete the steps in order. At each step build the simplest possible solution which meets the requirement. Tag and git commit after each step so that your approach is clear.

### Step 1: Build an Orders Service
* Build a service that’s able to receive simple orders of shopping goods via a REST API
* Apples cost 60 cents and oranges cost 25 cents
* The service should return a summary of the order, including the cost of the order
* Add unit tests that validate your code

### Step 2: Simple offer
* The shop decides to introduce two new offers
* buy one get one free on Apples
* 3 for the price of 2 on Oranges
* Update your functions & unit tests accordingly

### Step 3: Store and retrieve orders
* The service should now store the orders that a customer submits
* There should be an endpoint to get a particular order based on its ID
* There should be an endpoint to get all orders
* This store does not have to be to disk
* Update your functions & unit tests accordingly


## NOTES:

### Approach --
I decided to do this solution as if it were being built into a real service
with future extensions.  This might have been in violation of the requirement
to build "the simplest possible" service.  For example:
* it seemed odd to just code the deals into the OrderService itself.  In all likelihood we'd need to
have a deals/price-adjustment service, so that was represented.
* I also included _the beginning_ of  an inventory service.  It seemed weird to me to assume there were infinite apples \
and oranges.  So I constrained the problem the way I would in a real...
* etc.

That said (For interest of time and possibly going in the wrong direction) I didn't
make those anything other than maybe minimally functional and not complete,
the inventory  system doesn't decrement the inventory-- I could pretend that was handled elsewhere
even though that may not make sense...)


### Error Handling
I took a simplistic approach -- anything that didn't seem to work was a full error
that would be reported back to the client.  I didn't bother messing with the
status codes & error message format -- as these are usually system designed.
(E.g. Not Found would be 404, BadRequest 400)
However, error handling is often a design choice.  If I don't find one of 3 order items
is the whole summary invalid, or do I place a partial order and return the errors,
or do we do a workflow and pause the order until the user resolves the issues--
perhaps the item was just delisted and we just need the UI/user to remove the problematic
item).

### Future Expansion
* If this were a real service, then we'd need to flesh out the inventoryService,
  the OrderStore, etc.
* Round out the tests... for complete coverage if that's the goal
* Figure out what the actual error handling protocol is and implement it.
* Figure out whether we are paging things or not, or how we're constraining
  the "get all orders" from Step3 (or if we're not implemement a streaming solution maybe)
  ...
* Etc.
 



