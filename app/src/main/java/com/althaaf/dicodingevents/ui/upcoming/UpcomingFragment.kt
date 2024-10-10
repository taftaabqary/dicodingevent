package com.althaaf.dicodingevents.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.dicodingevents.R
import com.althaaf.dicodingevents.adapter.ListEventAdapter
import com.althaaf.dicodingevents.data.model.ApiResult
import com.althaaf.dicodingevents.data.response.ListEventsItem
import com.althaaf.dicodingevents.databinding.FragmentUpcomingBinding
import com.althaaf.dicodingevents.helper.ViewModelFactory

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel: UpcomingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRetry.setOnClickListener {
            setupListEvent()
        }

        setupListEvent()
    }

    private fun setupListEvent() {
        upcomingViewModel.getListEvent(active = 1, query = null, limit = null).observe(viewLifecycleOwner) {
            it?.let { result ->
                when(result) {
                    is ApiResult.Success -> {
                        binding.loadingState.visibility = View.GONE
                        setupRecyclerView(result.data)
                    }

                    is ApiResult.Error -> {
                        binding.loadingState.visibility = View.GONE
                        binding.tvError.text = result.errorMessage
                        binding.tvError.visibility = View.VISIBLE
                        binding.btnRetry.visibility = View.VISIBLE
                    }

                    is ApiResult.Empty -> {
                        binding.loadingState.visibility = View.GONE
                        setupRecyclerView(listOf())
                    }

                    is ApiResult.Loading -> {
                        binding.loadingState.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(data: List<ListEventsItem>) {
        if(data.isEmpty()) {
            binding.rvEventUpcoming.visibility = View.GONE
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = getString(R.string.empty_list)
            return
        }

        val adapter = ListEventAdapter(data)
        binding.rvEventUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventUpcoming.setHasFixedSize(true)
        binding.rvEventUpcoming.adapter = adapter
        binding.rvEventUpcoming.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}