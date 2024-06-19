package com.bangkit.coldswiftapps.view.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.data.remote.response.BuyTiketResponse
import com.bangkit.coldswiftapps.data.remote.response.DetailEventResponse
import com.bangkit.coldswiftapps.databinding.ActivityDetailEventBinding
import com.bangkit.coldswiftapps.utils.DateFormatter
import com.bangkit.coldswiftapps.view.ViewModelFactory
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.util.TimeZone

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        val eventId = intent.getStringExtra(EXTRA_EVENT_ITEM)
        Log.d("DetailEventActivity", "Event ID: $eventId")

        if (eventId != null) {
            viewModel.getDetailEvent(eventId)
        } else {
            showToast("Gagal memuat : Event ID Kosong")
        }

        viewModel.eventDetail.observe(this) { result ->
            result.onSuccess { event ->
                setupDetailEvent(event)
            }.onFailure {
                showToast("Gagal memuat event")
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnBuy.setOnClickListener{
            showConfirmPurchase(eventId!!)
        }
    }

    private fun showConfirmPurchase(id: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.buy_confirmation, null)
        val btnYes = dialogView.findViewById<View>(R.id.btn_yes)
        val btnCancel = dialogView.findViewById<View>(R.id.btn_no)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnYes.setOnClickListener {
            viewModel.purchaseTiket(id)
            viewModel.buyTiket.observe(this, Observer { result ->
                result.onSuccess {
                    showToast(it.message!!)

                }.onFailure {error ->
                    handleFailure(error)
                }
            })
            alertDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun handleFailure(error: Throwable) {
        when (error) {
            is HttpException -> {
                val errorBody = error.response()?.errorBody()?.string()
                try {
                    val json = JSONObject(errorBody!!)
                    val errorMessage = json.getString("error")
                    showToast(errorMessage)
                } catch (e: JSONException) {
                    showToast("Unknown error occurred")
                }
            }
            else -> {
                showToast("Network Error: ${error.message}")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    @SuppressLint("NewApi")
    private fun setupDetailEvent(event: DetailEventResponse) {
        binding.apply {
            Glide.with(this@DetailEventActivity)
                .load(event.eventImageURL)
                .into(ivImage)
            tvEventName.text = event.eventName
            eventOrganizer.text = event.vendor
            tvPrice.text = DateFormatter.formatCurrency(event.price!!.toDouble())
            tvDate.text = DateFormatter.formatDate(event.eventDate, TimeZone.getDefault().id)
            tvLocation.text = event.eventLocation
            tvDescription.text = event.eventDescription
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_EVENT_ITEM = "event_item"
    }
}
