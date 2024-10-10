package com.althaaf.dicodingevents.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.althaaf.dicodingevents.databinding.FragmentSettingBinding
import com.althaaf.dicodingevents.helper.ViewModelFactory
import java.util.UUID
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private var isListenerActive : Boolean = true

    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workManager = WorkManager.getInstance(requireContext())
        binding.swDarkMode.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if(isChecked) {
                binding.swDarkMode.isChecked = true
                settingViewModel.setDarkMode(true)
            } else {
                binding.swDarkMode.isChecked = false
                settingViewModel.setDarkMode(false)
            }
        }

        binding.swReminder.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if(isListenerActive) {
                if(isChecked) {
                    settingViewModel.setDailyReminder(true)
                    startPeriodicTask()
                    Toast.makeText(requireContext(), "Daily reminder diaktifkan", Toast.LENGTH_SHORT).show()
                } else {
                    settingViewModel.setDailyReminder(false)
                    cancelPeriodicTask()
                    Toast.makeText(requireContext(), "Daily reminder dinonaktifkan", Toast.LENGTH_SHORT).show()
                }
            }
        }


        settingViewModel.getNotificationSetting().observe(viewLifecycleOwner) { isEnable ->
            isListenerActive = false
            binding.swReminder.isChecked = isEnable
            isListenerActive = true
        }

        settingViewModel.getDarkModeSetting().observe(viewLifecycleOwner) { isEnable ->
            if(isEnable) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.swDarkMode.isChecked = isEnable
        }
    }

    private fun cancelPeriodicTask() {
        var periodicWorkRequestId: UUID
        settingViewModel.getPeriodicKey().observe(viewLifecycleOwner) { id ->
            periodicWorkRequestId = UUID.fromString(id)
            workManager.cancelWorkById(periodicWorkRequestId)
        }
    }

    private fun startPeriodicTask() {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        periodicWorkRequest = PeriodicWorkRequest.Builder(ReminderWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()

        val periodicWorkRequestId = periodicWorkRequest.id
        settingViewModel.savePeriodicKey(periodicWorkRequestId.toString())

        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id).observe(viewLifecycleOwner) { workInfo ->
            if (workInfo.state == WorkInfo.State.ENQUEUED) {
                Toast.makeText(requireContext(), "Akan kembali diingatkan setiap 15 menit sekali", Toast.LENGTH_SHORT).show()
            }
        }
    }
}