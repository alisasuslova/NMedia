package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.ActivityAppBinding


class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*//проверяем входящий intent
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
        }*/
        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) { //если текст пустой, или пробел, создаем Snackbar
                Snackbar.make(
                    binding.root,
                    R.string.error_empty_content,
                    LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {//установка кнопки, стандартно через android
                    finish()  //действие по кнопке "ОК"
                }
                    .show() //показать Snackbar
                return@let
            }
        }
        //переход на фрагмент после того, как пришел интент(1й параметр - id перехода, 2й параметр(сам текст, который передаем в интенте)):
        findNavController(R.id.nav_host).navigate(
            R.id.action_feedFragment_to_newPostFragment,
            Bundle().apply {
                textArg = text
            })

    }
}