package my.tick_tac_toe

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import my.tick_tac_toe.databinding.ActivityGameBinding
import my.tick_tac_toe.databinding.DiaglogPopupMenuBinding
import org.w3c.dom.Text

@Suppress("DEPRECATION")
class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private lateinit var gameField: Array<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        binding.toPopupMenu.setOnClickListener {
            showPopupMenu()

        }

        binding.toCloseGame.setOnClickListener {
            onBackPressed()

        }

        binding.cell11.setOnClickListener {
            makeStepOfUser(0, 0)

        }

        binding.cell12.setOnClickListener {
            makeStepOfUser(0, 1)

        }

        binding.cell13.setOnClickListener {
            makeStepOfUser(0, 2)

        }

        binding.cell21.setOnClickListener {
            makeStepOfUser(1, 0)

        }

        binding.cell22.setOnClickListener {
            makeStepOfUser(1, 1)

        }

        binding.cell23.setOnClickListener {
            makeStepOfUser(1, 2)

        }

        binding.cell31.setOnClickListener {
            makeStepOfUser(2, 0)

        }

        binding.cell32.setOnClickListener {
            makeStepOfUser(2, 1)

        }

        binding.cell33.setOnClickListener {
            makeStepOfUser(2, 2)

        }


        setContentView(binding.root)

        initGameField()
    }

    private fun initGameField() {
        gameField = Array(3) { Array(3) { " " } }
    }

    private fun makeStep(row: Int, column: Int, symbol: String) {
        gameField[row][column] = symbol

        makeStepUI("$row$column", symbol)
    }

    private fun makeStepUI(position: String, symbol: String) {
        val resId = when (symbol) {
            "X" -> R.drawable.cross
            "0" -> R.drawable.zero
            else -> return
        }

        when (position) {
            "00" -> binding.cell11.setImageResource(resId)
            "01" -> binding.cell12.setImageResource(resId)
            "02" -> binding.cell13.setImageResource(resId)
            "10" -> binding.cell21.setImageResource(resId)
            "11" -> binding.cell22.setImageResource(resId)
            "12" -> binding.cell23.setImageResource(resId)
            "20" -> binding.cell31.setImageResource(resId)
            "21" -> binding.cell32.setImageResource(resId)
            "22" -> binding.cell33.setImageResource(resId)
        }
    }

    private fun makeStepOfUser(row: Int, column: Int) {
        if (isEmptyField(row, column)) {
            makeStep(row, column, "X")

            val status = checkGameField(row, column, "X")
            if(status.status) {
                showGameStatus(STATUS_PLAYER_WIN)
                return
            }

            if(!isFilledGameField()) {
                makeStepOfAI()

                val statusAI = checkGameField(row, column, "0")

                if (statusAI.status) {
                    showGameStatus(STATUS_PLAYER_LOSE)
                    return
                }

                if(isFilledGameField()) {
                    showGameStatus(STATUS_PLAYER_DRAW)
                    return
                }

            } else {
                showGameStatus(STATUS_PLAYER_DRAW)
                return
            }


        } else {
            Toast.makeText(this, "You can't do this", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isEmptyField(row: Int, column: Int): Boolean {
        return gameField[row][column] == " "
    }

    private fun makeStepOfAI() {
        var randRow = 0
        var randColumn = 0

        do {

            randRow = (0..2).random()
            randColumn = (0..2).random()
        } while (isEmptyField(randRow, randColumn))

        makeStep(randRow, randColumn, "0")

    }

    private fun checkGameField(x: Int, y: Int, symbol: String): StatusInfo {
        var row = 0
        var column = 0
        var leftDiagonal = 0
        var rightDiagonal = 0
        var n = gameField.size

        for (i in 0..2) {
            if(gameField[x][i] == symbol)
                column++
            if (gameField[i][y] == symbol)
                row++
            if (gameField[i][i] == symbol)
                leftDiagonal++
            if (gameField[i][n-i-1] == symbol)
                rightDiagonal++
        }

        return if (column == n || row == n || leftDiagonal == n || rightDiagonal == n)
            StatusInfo(true, symbol)
        else
            StatusInfo(false, "")

    }

    data class StatusInfo(val status: Boolean, val side: String)

    private fun showGameStatus(status: Int) {
        val dialog = Dialog(this, R.style.Theme_Tick_Tac_Toe)
        with(dialog) {
            window?.setBackgroundDrawable(ColorDrawable(Color.argb(50, 0, 0, 0)))
            setContentView(R.layout.dialog_popup_status_game)
            setCancelable(true)
        }

        val image = dialog.findViewById<ImageView>(R.id.dialog_image)
        val text = dialog.findViewById<TextView>(R.id.dialog_txt)
        val button = dialog.findViewById<TextView>(R.id.dialog_ok)


        button.setOnClickListener{
            onBackPressed()
        }

        when (status) {
            STATUS_PLAYER_WIN -> {
                image.setImageResource(R.drawable.win)
                text.text = getString(R.string.dialog_status_win)
            }
            STATUS_PLAYER_LOSE -> {
                image.setImageResource(R.drawable.lose)
                text.text = getString(R.string.dialog_status_lose)
            }
            STATUS_PLAYER_DRAW -> {
                image.setImageResource(R.drawable.draw)
                text.text = getString(R.string.dialog_status_draw)
            }
        }

        dialog.show()

    }


    private fun showPopupMenu() {
        val dialog = Dialog(this, R.style.Theme_Tick_Tac_Toe)
        with(dialog) {
            window?.setBackgroundDrawable(ColorDrawable(Color.argb(50, 0, 0, 0)))
            setContentView(R.layout.diaglog_popup_menu)
            setCancelable(true)
        }

        val toContinue = dialog.findViewById<TextView>(R.id.dialog_continue)
        val toSettings = dialog.findViewById<TextView>(R.id.diaglog_settings)
        val toExit = dialog.findViewById<TextView>(R.id.dialog_exit)

        toContinue.setOnClickListener {
            dialog.hide()

        }

        toSettings.setOnClickListener {
            dialog.hide()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)

        }

        toExit.setOnClickListener {
            dialog.dismiss()
            onBackPressed()

        }

        dialog.show()

    }

    private fun isFilledGameField(): Boolean {
        gameField.forEach { strings ->
            if(strings.find{it == " "} != null)
                return false
        }
        return true
    }


    companion object {
        const val STATUS_PLAYER_WIN = 1
        const val STATUS_PLAYER_LOSE = 2
        const val STATUS_PLAYER_DRAW = 3
    }





}