package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(
        ownerProducer = :: requireParentFragment
        )

        arguments?.textArg.let(binding.content::setText)

        binding.content.requestFocus()
        binding.save.setOnClickListener {

            val intent = Intent()

            if(binding.content.text.isNotBlank()) {
                viewModel.changeContent(binding.content.text.toString())
                viewModel.save()
                //viewModel.changeContentAndSave()

            }

            viewModel.postCreated.observe(viewLifecycleOwner) {
                findNavController().navigateUp()
                viewModel.load()
            }

        }

        return binding.root
    }

    //св-во расширения

    companion object {
        var Bundle.textArg : String? by StringArg
    }

}
