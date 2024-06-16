package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding

class IntentHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityIntentHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //проверяем входящий intent
        intent?.let {
            if (it.action == Intent.ACTION_SEND) {
                val text = it.getStringExtra(Intent.EXTRA_TEXT)
                if (text.isNullOrBlank()) { //если текст пустой, или пробел, создаем Snackbar
                    Snackbar.make(
                        binding.root,
                        R.string.error_empty_content,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(android.R.string.ok) {//установка кнопки, стандартно через android
                        finish()  //действие по кнопке "ОК"
                    }
                        .show() //показать Snackbar
                }
            }
        }
    }
}