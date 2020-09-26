package dev.procrastineyaz.watchlist.ui.main.item_view

import android.os.Bundle
import android.text.format.DateUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Result
import dev.procrastineyaz.watchlist.databinding.FragmentItemDetailsBinding
import kotlinx.android.synthetic.main.fragment_item_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ItemDetailsFragment : Fragment() {
    private val args: ItemDetailsFragmentArgs by navArgs()
    private val vm: ItemDetailsViewModel by viewModel()
    private var deleteSnackbar: Snackbar? = null
    private lateinit var binding: FragmentItemDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this::binding.isInitialized) return binding.root
        binding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        vm.setItem(args.item)
        val relativeTime =
            args.item.createdAt?.time?.let { DateUtils.getRelativeTimeSpanString(it) }
        binding.relativeTime = (if (relativeTime != null) "Добавлено $relativeTime" else "")
        binding.vm = vm
        binding.item = args.item
        binding.readonly = args.readonly
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_poster.load(args.item.posterUrl) {
            placeholder(R.drawable.no_movie_poster)
            error(R.drawable.no_movie_poster)
            transformations(RoundedCornersTransformation(12f))
        }

        vm.updateResult.observe(viewLifecycleOwner) { result ->
            if (result is Result.Success) {
                Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
            } else if (result is Result.Error) {
                Toast.makeText(context, "Произошла ошибка при сохранении", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        if (!args.readonly)
            vm.deleteResult.observe(viewLifecycleOwner) { result ->
                if (result is Result.Error) {
                    binding.readonly = false
                    Toast.makeText(context, "При удалении произошла ошибка", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        if (!args.readonly) btn_remove.setOnClickListener {
            showRemoveSnackbar()
        }
    }

    private fun showRemoveSnackbar() {
        deleteSnackbar =
            Snackbar.make(requireView(), "Элемент \"${args.item.nameRu}\" удален", 3200)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            vm.deleteItem()
                        }
                        deleteSnackbar = null
                    }

                    override fun onShown(sb: Snackbar?) {
                        binding.readonly = true
                    }
                })
                .setAction("Отмена") {
                    binding.readonly = false
                }
        deleteSnackbar?.apply {
            val bottomMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                48f,
                resources.displayMetrics
            )
//            view.layoutParams = (view.layoutParams as ViewGroup.MarginLayoutParams).let {
//
//                it.setMargins(4, 0, 4, bottomMargin)
//                it
//            }
            view.translationY = -bottomMargin
            show()
        }
    }

    override fun onPause() {
        super.onPause()
        vm.onSave()
    }

    override fun onStop() {
        super.onStop()
        deleteSnackbar?.let {
            vm.deleteItem()
            it.dismiss()
            deleteSnackbar = null
        }
    }
}
