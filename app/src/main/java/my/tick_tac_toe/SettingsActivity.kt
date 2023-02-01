package my.tick_tac_toe

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import my.tick_tac_toe.databinding.ActivityMainBinding
import my.tick_tac_toe.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private var currentSound: Int = 0
    private var currentLvl: Int = 0
    private var currentRules: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        val data = getSettingsInfo()

        currentSound = data.soundValue
        currentLvl = data.lvl
        currentRules = data.rules



        if (currentLvl == 0) {
            binding.prevLvl.visibility = View.INVISIBLE
        } else if (currentLvl == 2) {
            binding.nextLvl.visibility = View.INVISIBLE
        }

        binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLvl]
        binding.soundBar.progress = currentSound

        when(currentRules) {
            1 -> binding.checkBoxVer.isChecked = true
            2 -> binding.checkBoxHor.isChecked = true
            3 -> {
                binding.checkBoxVer.isChecked = true
                binding.checkBoxHor.isChecked = true
            }
            4 -> binding.checkBoxRow.isChecked = true
            5 -> {
                binding.checkBoxRow.isChecked = true
                binding.checkBoxVer.isChecked = true
            }
            6 -> {
                binding.checkBoxRow.isChecked = true
                binding.checkBoxHor.isChecked = true
            }
            7 -> {
                binding.checkBoxRow.isChecked = true
                binding.checkBoxHor.isChecked = true
                binding.checkBoxVer.isChecked = true
            }
        }


        binding.prevLvl.setOnClickListener {
            currentLvl--

            if (currentLvl == 0) {
                binding.prevLvl.visibility = View.INVISIBLE
            } else if (currentLvl == 1) {
                binding.nextLvl.visibility = View.VISIBLE
            }

            binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLvl]

            updateLvl(currentLvl)

        }

        binding.nextLvl.setOnClickListener {
            currentLvl++

            if (currentLvl == 2) {
                binding.nextLvl.visibility = View.INVISIBLE
            } else if (currentLvl == 1) {
                binding.prevLvl.visibility = View.VISIBLE
            }

            binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLvl]

            updateLvl(currentLvl)
        }


        binding.toBack.setOnClickListener {
            onBackPressed()
        }


        binding.soundBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                currentSound = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateSoundValue(currentSound)

            }
        })

        binding.checkBoxVer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentRules += 2
            } else {
                currentRules -= 2
            }

            updateRules(currentRules)
        }

        binding.checkBoxHor.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentRules += 1
            } else {
                currentRules -= 1
            }
            updateRules(currentRules)
        }


        binding.checkBoxRow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentRules += 4
            } else {
                currentRules -= 4
            }
            updateRules(currentRules)
        }

        setContentView(binding.root)
    }

    private fun updateSoundValue(value: Int) {
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()) {
            putInt(PREF_SOUND, value)
            apply()
        }
    }

    private fun updateLvl(lvl: Int) {
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()) {
            putInt(PREF_LVL, lvl)
            apply()
        }
    }

    private fun updateRules(rules: Int) {
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()) {
            putInt(PREF_RULES, rules)
            apply()
        }
    }

    private fun getSettingsInfo(): SettingsInfo {
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)) {
            val soundValue = getInt(PREF_SOUND, 0)
            val lvl = getInt(PREF_LVL, 0)
            val rules = getInt(PREF_RULES, 0)

            return SettingsInfo(soundValue, lvl, rules)
        }
    }

    companion object {
        const val PREF_SOUND = "pref_sound_value"
        const val PREF_LVL = "pref_lvl"
        const val PREF_RULES = "pref_rules"
    }

    data class SettingsInfo(val soundValue: Int, val lvl:Int, val rules: Int)
}