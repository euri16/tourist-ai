package com.eury.touristai.ui.main.fragments.dialogs

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eury.touristai.R
import kotlinx.android.synthetic.main.text_search_dialog.*

/**
 * Created by euryperez on 5/18/18.
 * Property of Instacarro.com
 */
class TextSearchDialogFragment : DialogFragment() {

    private var mListener: OnTextSearchListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.text_search_dialog, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnTextSearchListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnTextSearchListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //dialog.window.requestFeature(Window.FEATURE_NO_TITLE)

        btnSearch.setOnClickListener {
            mListener?.onTextSubmitted(etText.text.toString())
            this.dismiss()
        }

        btnCancel.setOnClickListener {
            this.dismiss()
        }
    }


    interface OnTextSearchListener {
        fun onTextSubmitted(text: String)
    }

    companion object {

        fun newInstance(): TextSearchDialogFragment {
            return TextSearchDialogFragment()
        }
    }
}