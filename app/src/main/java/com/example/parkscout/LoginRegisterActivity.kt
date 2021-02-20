package com.example.parkscout

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
//        printhashkey()
    }

//    public fun printhashkey() {
//        try {
//            val info = packageManager.getPackageInfo("com.example.parkscout", PackageManager.GET_SIGNING_CERTIFICATES)
//            for (signature in info.signatures)
//            {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.e("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        }
//        catch (e:NoSuchAlgorithmException) {
//            Log.e("KEYHASH", "ERROR")
//        }
//        catch (e:PackageManager.NameNotFoundException) {
//            Log.e("KEYHASH", "NameNotFoundException")
//        }
//    }

}