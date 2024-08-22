package com.pools.store.core

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.pools.store.R


class DialogClaimSuccess(private var textBody : String, private var actionSuccessInterface  :ActionSuccessInterface): DialogFragment() {

    private lateinit var btnOk: TextView
    private lateinit var tvBody: TextView
    private lateinit var tvTitle: TextView
    private lateinit var rootViewDialogActionSuccess: RelativeLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = layoutInflater.inflate(R.layout.dialog_action_success, null)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        tvBody = root.findViewById(R.id.tvBody)
        tvTitle = root.findViewById(R.id.tvTitle)
        btnOk = root.findViewById(R.id.btnOk)
        rootViewDialogActionSuccess = root.findViewById(R.id.rootViewDialogActionSuccess)
        btnOk.setOnClickListener {
//            actionSuccessInterface.click()
            dismiss()
        }
//        tvBody.text = textBody
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return dialog
    }


    interface ActionSuccessInterface {
        fun click()
    }
}