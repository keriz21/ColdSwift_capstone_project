package com.bangkit.coldswiftapps.view.myticket

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.coldswiftapps.adapter.TiketAdapter
import com.bangkit.coldswiftapps.databinding.FragmentMyTicketBinding
import com.bangkit.coldswiftapps.view.ViewModelFactory

class MyTicket : Fragment() {

    private lateinit var binding: FragmentMyTicketBinding
    private lateinit var ticketViewModel: MyTicketViewModel
    private lateinit var ticketAdapter: TiketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        ticketViewModel = ViewModelProvider(this, factory).get(MyTicketViewModel::class.java)

        setupRecyclerView()

        observeTickets()
        ticketViewModel.getAllTickets()
    }

    private fun setupRecyclerView() {
        binding.rvItemList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeTickets() {
        ticketViewModel.allTickets.observe(viewLifecycleOwner, Observer { tickets ->
            ticketAdapter = TiketAdapter(tickets)
            binding.rvItemList.adapter = ticketAdapter
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

//    private fun showLoading(isLoading: Boolean) {
//        if (isLoading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

