package com.pools.store.core

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.pools.store.R

class DialogNotice(): DialogFragment() {

    private lateinit var tvYes: TextView
    private lateinit var tvNo: TextView
    private lateinit var ivClose: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = layoutInflater.inflate(R.layout.dialog_notice, null)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        tvYes = root.findViewById(R.id.tvYes)
        tvNo = root.findViewById(R.id.tvNo)
        ivClose = root.findViewById(R.id.ivClose)

        ivClose.setOnClickListener {
            dismiss()
        }
        tvYes.setOnClickListener {
            dismiss()
        }
        tvNo.setOnClickListener {
            dismiss()
        }
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return dialog
    }

}