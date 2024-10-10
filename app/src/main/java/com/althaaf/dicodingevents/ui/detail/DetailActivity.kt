package com.althaaf.dicodingevents.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.parseAsHtml
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.althaaf.dicodingevents.R
import com.althaaf.dicodingevents.data.response.Event
import com.althaaf.dicodingevents.databinding.ActivityDetailBinding
import com.althaaf.dicodingevents.helper.ViewModelFactory
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var detailEventEntity: com.althaaf.dicodingevents.data.local.EventEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getIntExtra(ID_EVENT, 0)

        detailViewModel.getDetailEvent(idEvent = id)
        detailViewModel.stateConnection.observe(this) { detail ->
            detail.data?.let { setupDetailEvent(it) }
            setupStateLoading(detail.isLoading)
            detail.error?.let {
                setupStateError(it)
            }
        }

        detailViewModel.checkEventFavorite(id)
        detailViewModel.isFavorite.observe(this) {
            it?.let {
                setStateFavorite(it)
            }
        }
    }

    private fun setStateFavorite(isFavorite: Boolean) {
        if(isFavorite) {
            binding.fabFavorite.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.icon_fav_full))
            binding.fabFavorite.setOnClickListener {
                detailViewModel.deleteEventFavorite(detailEventEntity)
                detailViewModel.checkEventFavorite(detailEventEntity.id)
                Toast.makeText(this, "Event dihapus ke dalam favorite", Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.fabFavorite.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.icon_fav_line))
            binding.fabFavorite.setOnClickListener {
                detailViewModel.setEventFavorite(detailEventEntity)
                detailViewModel.checkEventFavorite(detailEventEntity.id)
                Toast.makeText(this, "Event ditambahkan ke dalam favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDetailEvent(data: Event) {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(data.beginTime)
        val df = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).format(dateFormat!!)

        detailEventEntity = com.althaaf.dicodingevents.data.local.EventEntity(
            id = data.id,
            title = data.name,
            location = data.cityName,
            image = data.mediaCover,
            category = data.category,
            owner = data.ownerName,
            date = df
        )

        binding.contentDetail.apply {
            tvDetailCategory.text = data.category
            tvDetailDescription.text = data.description.parseAsHtml()
            tvDetailLocation.text = data.cityName
            tvDetailEventOwner.text = "Diselenggarakan oleh: ${data.ownerName}"
            tvDetailEventTitle.text = data.name
            tvDetailTime.text = df
            tvDetailDuration.text = "60m"
            tvPesertaEvent.text = "Pendaftaran sudah ditutup."
        }


        Glide.with(this)
            .load(data.mediaCover)
            .into(binding.contentDetail.ivDetailPhoto)

        binding.contentDetail.btnWeb.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse(data.link)
            startActivity(intent)
        }
    }

    private fun setupStateLoading(loading: Boolean) {
        if(loading) {
            binding.contentDetail.loadingState.visibility = View.VISIBLE
        } else {
            binding.contentDetail.loadingState.visibility = View.GONE
        }
    }

    private fun setupStateError(message: String) {
        binding.contentDetail.tvError.text = message
        binding.contentDetail.tvError.visibility = View.VISIBLE
        binding.contentDetail.btnRetry.visibility = View.VISIBLE
    }

    companion object {
        const val ID_EVENT = "id_event"
    }
}