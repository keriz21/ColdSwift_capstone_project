package com.bangkit.coldswiftapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class BuyTiketResponse(

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null

)
