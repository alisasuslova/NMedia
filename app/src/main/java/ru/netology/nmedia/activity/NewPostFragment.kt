package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment()  {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let(binding.edit::setText)

        binding.edit.requestFocus()
        /*binding.ok.setOnClickListener {
            val intent = Intent()
            if (TextUtils.isEmpty(binding.edit.toString())) {
                activity?.setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                activity?.setResult(Activity.RESULT_OK, intent)
            }
            findNavController()
        }*/

        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                activity?.setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                activity?.setResult(Activity.RESULT_OK, intent)
            }
            findNavController()
        }
        return binding.root
    }

    //сво-во расширения здесь!!!
    companion object {
        var Bundle.textArg : String? by StringArg
    }

}