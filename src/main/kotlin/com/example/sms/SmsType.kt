package com.example.sms

import com.example.sms.Variable.PROOF_CODE
import com.example.sms.Variable.REGISTRATION_CODE

enum class SmsType(
    val customizable: Boolean,
    vararg val variables: Variable
) {
    REGISTRATION(false, REGISTRATION_CODE),
    DELIVERY_PICKUP(true),
    DELIVERY_IM_HERE(true, PROOF_CODE);
}

enum class Variable {
    REGISTRATION_CODE,
    PROOF_CODE
}
