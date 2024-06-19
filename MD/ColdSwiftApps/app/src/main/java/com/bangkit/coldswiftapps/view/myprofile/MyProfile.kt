package com.bangkit.coldswiftapps.view.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.coldswiftapps.R
import com.bangkit.coldswiftapps.databinding.FragmentMyProfileBinding
import com.bangkit.coldswiftapps.view.ViewModelFactory
import com.bangkit.coldswiftapps.view.myticket.MyTicket

/**
 * A simple [Fragment] subclass.
 * Use the [MyProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProfile : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var viewModel: MyProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[MyProfileViewModel::class.java]

        viewModel.getProfile()

        viewModel.profile.observe(viewLifecycleOwner){profile ->
            if(profile != null){
                binding.username.text = profile.name
                binding.userEmail.text = profile.email
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            showToast(it)
        }

        binding.btnMytiket.setOnClickListener {
            val fragment = MyTicket()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}