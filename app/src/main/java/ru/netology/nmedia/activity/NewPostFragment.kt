package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
//import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.databinding.FragmentFeedBinding

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFeedBinding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        binding.edit.requestFocus()
        binding.save.setOnClickListener {
            val intent = Intent()
            if (TextUtils.isEmpty(binding.content)) {
                activity?.setResult(Activity.RESULT_CANCELED, intent)
            } else {

            val content = binding.edit.context.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                activity?.setResult(Activity.RESULT_OK, intent)
            }
            findNavController()
        }

        return binding.root
}
    /*
        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if(text.isBlank()) {
                setResult(AppCompatActivity.RESULT_CANCELED)
            }
            else{
                setResult(AppCompatActivity.RESULT_OK, Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                })
            }
            finish()
        }*/

// контракт

object NewPostContract : ActivityResultContract<Unit, String?> () {
    override fun createIntent(context: Context, input: Unit) =
        Intent(context, NewPostFragment::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)

}