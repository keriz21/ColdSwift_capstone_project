package com.bangkit.coldswiftapps.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.coldswiftapps.adapter.EventAdapter
import com.bangkit.coldswiftapps.data.remote.response.ListEventResponse
import com.bangkit.coldswiftapps.databinding.FragmentHomeBinding
import com.bangkit.coldswiftapps.view.ViewModelFactory
import com.bangkit.coldswiftapps.view.detail.DetailEventActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        homeViewModel.events.observe(viewLifecycleOwner, Observer { events ->
            getEvents(events)
        })


        homeViewModel.getAllEvents()

        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            showLoading(it)
        })
    }

    private fun getEvents(listEvent: List<ListEventResponse>) {
        eventAdapter = EventAdapter(listEvent)
        binding.rvItemList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        eventAdapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListEventResponse) {
                showSelectedStory(data)
            }
        })

    }

    private fun showSelectedStory(data: ListEventResponse) {
        val goDetailIntent = Intent(activity, DetailEventActivity::class.java).apply {
            putExtra(DetailEventActivity.EXTRA_EVENT_ITEM, data.eventId.toString())
        }
        startActivity(goDetailIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}
