package com.bangkit.coldswiftapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailEventResponse(

	@field:SerializedName("eventId")
	val eventId: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("eventImageURL")
	val eventImageURL: String? = null,

	@field:SerializedName("eventCategory")
	val eventCategory: String? = null,

	@field:SerializedName("vendor")
	val vendor: String? = null,

	@field:SerializedName("eventLocation")
	val eventLocation: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("eventDescription")
	val eventDescription: String? = null,

	@field:SerializedName("eventName")
	val eventName: String? = null,

	@field:SerializedName("ticketAvailable")
	val ticketAvailable: Int? = null,

	@field:SerializedName("eventDate")
	val eventDate: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
