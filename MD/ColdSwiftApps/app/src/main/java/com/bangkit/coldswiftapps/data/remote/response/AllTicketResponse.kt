package com.bangkit.coldswiftapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllTicketResponse(

	@field:SerializedName("AllTicketResponse")
	val allTicketResponse: List<AllTicketResponseItem?>? = null
)

data class AllTicketResponseItem(

	@field:SerializedName("eventId")
	val eventId: Int? = null,

	@field:SerializedName("eventImageURL")
	val eventImageURL: String? = null,

	@field:SerializedName("eventLocation")
	val eventLocation: String? = null,

	@field:SerializedName("purchasedAt")
	val purchasedAt: String? = null,

	@field:SerializedName("eventName")
	val eventName: String? = null,

	@field:SerializedName("ticketId")
	val ticketId: String? = null,

	@field:SerializedName("eventDate")
	val eventDate: String? = null
)
