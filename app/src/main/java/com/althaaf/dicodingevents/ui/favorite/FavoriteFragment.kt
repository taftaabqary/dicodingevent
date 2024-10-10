package com.althaaf.dicodingevents.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.althaaf.dicodingevents.adapter.ListEventAdapter
import com.althaaf.dicodingevents.data.local.EventEntity
import com.althaaf.dicodingevents.databinding.FragmentFavoriteBinding
import com.althaaf.dicodingevents.helper.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel.getFavoriteEvent().observe(viewLifecycleOwner) {
            binding.loadingState.visibility = View.VISIBLE
            it?.let { data ->
                setupRecyclerView(data)
            }
        }
    }

    private fun setupRecyclerView(data: List<EventEntity>) {
        if(data.isEmpty()) {
            binding.loadingState.visibility = View.GONE
            binding.rvEventFavorite.visibility = View.GONE
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = "You don't have any favorites"

            return
        }

        val adapter = ListEventAdapter(data)
        binding.rvEventFavorite.adapter = adapter
        binding.rvEventFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventFavorite.setHasFixedSize(true)
        binding.tvError.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
        binding.loadingState.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Favorite Fragment", "Dihancurkan")
        _binding = null
    }


}