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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
//import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg

        /*private const val TEXT_KEY = "TEXT_KEY"
        var Bundle.textArg: String?
            set(value) = putString(TEXT_KEY, value)
            get() = getString(TEXT_KEY)*/
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
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

        arguments?.textArg
            ?.let(binding.edit::setText)  //слайд 35

        //arguments?.textArg?.let(binding.edit.setText(it))

        binding.save.setOnClickListener {
                   viewModel.changeContentAndSave(binding.edit.context.toString()) // запись текста поста уже в самом фрагменте
                   AndroidUtils.hideKeyboard(requireView())
                   findNavController().navigateUp() //вызов прошлого состояния, аналогично нажатию на кнопку назад
               }

        return binding.root


        /*binding.edit.requestFocus()
        binding.save.setOnClickListener {
            val intent = Intent()
            if (TextUtils.isEmpty(binding.edit.context.toString())) {  // ?!?!?!?
                activity?.setResult(Activity.RESULT_CANCELED, intent)
            } else {

                val content = binding.edit.context.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                activity?.setResult(Activity.RESULT_OK, intent)
            }
            findNavController()
        }*/

    }

}

object StringArg : ReadWriteProperty<Bundle, String?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
        thisRef.putString(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
        thisRef.getString(property.name)


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

/* object NewPostContract : ActivityResultContract<Unit, String?>() {
     override fun createIntent(context: Context, input: Unit) =
         Intent(context, NewPostFragment::class.java)

     override fun parseResult(resultCode: Int, intent: Intent?) =
         intent?.getStringExtra(Intent.EXTRA_TEXT)

 }*/