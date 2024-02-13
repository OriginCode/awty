package edu.uw.ischool.kaiyaw.awty

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast

class MReceiver(val activity: Activity) : BroadcastReceiver() {
    private lateinit var txtToastCaption: TextView
    private lateinit var txtToastMsg: TextView
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("awty", "Received repeating task!")
        // Custom Toast
//        val layout =
//            (context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
//                R.layout.custom_toast,
//                null
//            )
//        txtToastCaption = layout.findViewById(R.id.txtToastCaption)
//        txtToastMsg = layout.findViewById(R.id.txtToastMsg)
//        txtToastCaption.text =
//            context.getString(R.string.toast_caption, intent?.getStringExtra("phone_num"))
//        txtToastMsg.text = intent?.getStringExtra("message")
//        Toast(context).apply {
//            duration = Toast.LENGTH_LONG
//            view = layout
//        }.show()
        Toast.makeText(
            context,
            "Texting ${intent?.getStringExtra("phone_num")}: ${intent?.getStringExtra("message")}",
            Toast.LENGTH_LONG
        ).show()
    }
}
