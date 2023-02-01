package my.tick_tac_toe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.tick_tac_toe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Tick_Tac_Toe)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.toNewGame.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.toSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.toContinueGame.setOnClickListener{
            val data = getInfoAboutGame()
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(EXTRA_TIME, data.time)
                putExtra(EXTRA_GAME_FIELD, data.gameField)
            }
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun getInfoAboutGame(): InfoGame {
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)) {
            val time = getLong("time", 0L)
            val gameField = getString("gameField", "")

            return if(gameField != null) {
                InfoGame(time, gameField)
            } else {
                InfoGame(0L, "")
            }
        }
    }

    data class InfoGame(val time:Long, val gameField: String)

    companion object {
        const val EXTRA_TIME = "extra_time"
        const val EXTRA_GAME_FIELD = "extra_game_field"
    }
}