package com.amex.interview.exception

import java.lang.RuntimeException

class BadRequestException(message: String?) : RuntimeException(message)
class NotFoundException(message: String?) : RuntimeException(message)