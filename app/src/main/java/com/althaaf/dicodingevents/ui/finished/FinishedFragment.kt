package com.althaaf.dicodingevents.ui.finished

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.dicodingevents.adapter.ListEventAdapter
import com.althaaf.dicodingevents.data.response.ListEventsItem
import com.althaaf.dicodingevents.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel: FinishedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "Dibuat")
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Dibuat Created")

        finishedViewModel.stateConnection.observe(viewLifecycleOwner) { state ->
            state.data?.let { setupRecyclerView(it) }
            setupStateLoading(state.isLoading)
            state.error?.let {
                setupStateError(it)
            }
        }

        binding.btnRetry.setOnClickListener {
            finishedViewModel.getFinishedEvents()
        }
    }

    private fun setupRecyclerView(list: List<ListEventsItem>) {
        if(list.isNotEmpty()) {
            binding.rvEventFinished.layoutManager = LinearLayoutManager(requireContext())
            binding.rvEventFinished.setHasFixedSize(true)
            binding.rvEventFinished.adapter = ListEventAdapter(list)
            binding.tvError.visibility = View.GONE
            binding.btnRetry.visibility = View.GONE
        } else {
            binding.rvEventFinished.visibility = View.GONE
            binding.tvError.text = "Data List Kosong"
            binding.tvError.visibility = View.VISIBLE
        }
    }

    private fun setupStateLoading(loading: Boolean) {
        if(loading) {
            binding.loadingState.visibility = View.VISIBLE
        } else {
            binding.loadingState.visibility = View.GONE
        }
    }

    private fun setupStateError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
        binding.btnRetry.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Dihancurkan")
        _binding = null
    }

    companion object {
        private const val TAG = "FinishedFragment"
    }
}