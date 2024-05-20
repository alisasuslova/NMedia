package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if(text.isBlank()) {
                setResult(RESULT_CANCELED)
            }
            else{
                setResult(RESULT_OK, Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                })
            }
            finish()
        }
    }
}

// контракт на редактирование
object EditPostContract : ActivityResultContract<String, String?> () {
    //TODO добавить сам текст поста во входящий параметр content.text.toString()
    override fun createIntent(context: Context, input: String) = Intent(context, EditPostActivity::class.java).putExtra(input) //?!?!?!
    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getStringExtra(Intent.EXTRA_TEXT)

}