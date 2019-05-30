package br.com.gizmin.bitstore.knowbitstore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.gizmin.bitstore.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_know_bit_store_page.*

class KnowBitStorePageFragment : Fragment() {

    companion object {
        private const val KEY_MESSAGE = "KM"
        private const val KEY_DRAWABLE = "KD"
        private const val KEY_EXISTS_BUTTON = "KEB"

        fun newInstance(@StringRes message : Int,
                        @DrawableRes photo : Int,
                        existsButton: Boolean = false) : KnowBitStorePageFragment {
            val args = Bundle()
            args.putInt(KEY_MESSAGE, message)
            args.putInt(KEY_DRAWABLE,photo)
            args.putBoolean(KEY_EXISTS_BUTTON, existsButton)

            val kbspf = KnowBitStorePageFragment()
            kbspf.arguments = args

            return kbspf
        }
    }

    private val imageView by lazy { imageview }
    private val textView by lazy { textview }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_know_bit_store_page,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let { args ->
            imageView.setImageResource(args.getInt(KEY_DRAWABLE))
            textView.setText(args.getInt(KEY_MESSAGE))

            Observable
                    .just(args.getBoolean(KEY_EXISTS_BUTTON))
                    .filter { it }
                    .doOnNext { createButton() }
                    .subscribe()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createButton() {
        button.visibility = View.VISIBLE
        button.setOnClickListener {

        }
    }
}
