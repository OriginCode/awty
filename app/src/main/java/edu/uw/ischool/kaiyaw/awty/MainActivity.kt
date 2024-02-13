package edu.uw.ischool.kaiyaw.awty

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import android.widget.TextView

const val PERM_POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"

class MainActivity : AppCompatActivity() {
    private lateinit var edtMsg: EditText
    private lateinit var edtPhoneNum: EditText
    private lateinit var edtInterval: EditText
    private lateinit var btnStartStop: Button


    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private var receiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (this.checkSelfPermission(PERM_POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED
            && this.shouldShowRequestPermissionRationale(PERM_POST_NOTIFICATIONS)
        ) {
            this.requestPermissions(
                arrayOf(PERM_POST_NOTIFICATIONS),
                0
            )
        }

        // Load UI Views
        edtMsg = findViewById(R.id.edtMsg)
        edtPhoneNum = findViewById(R.id.edtPhoneNum)
        edtInterval = findViewById(R.id.edtInterval)
        btnStartStop = findViewById(R.id.btnStartStop)

        btnStartStop.setOnClickListener {
            if (btnStartStop.text == getString(R.string.btn_start)) {
                if (edtMsg.text.isEmpty()) {
                    Toast.makeText(this, "Please enter something to send!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (edtPhoneNum.text.isEmpty() || edtPhoneNum.text.toString()
                        .toIntOrNull() == null
                ) {
                    Toast.makeText(this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (edtInterval.text.isEmpty() || edtInterval.text.toString()
                        .toIntOrNull() == null
                ) {
                    Toast.makeText(this, "Invalid Interval!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val interval = edtInterval.text.toString().toLong() * 60L * 1000L
                pendingIntent = PendingIntent.getBroadcast(
                    this, 0, Intent("awty").apply {
                        putExtra("phone_num", edtPhoneNum.text.toString())
                        putExtra("message", edtMsg.text.toString())
                    }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                if (receiver == null) {
                    receiver = MReceiver(this@MainActivity)
                }
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent
                )
                registerReceiver(receiver, IntentFilter("awty"), RECEIVER_EXPORTED)
                btnStartStop.text = getString(R.string.btn_stop)
            } else if (btnStartStop.text == getString(R.string.btn_stop)) {
                alarmManager.cancel(pendingIntent)
                if (receiver != null) {
                    unregisterReceiver(receiver)
                    receiver = null
                }
                btnStartStop.text = getString(R.string.btn_start)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::pendingIntent.isInitialized) {
            alarmManager.cancel(pendingIntent)
        }
        if (receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }
    }
}