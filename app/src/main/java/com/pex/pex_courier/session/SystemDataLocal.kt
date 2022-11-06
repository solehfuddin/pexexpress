package com.pex.pex_courier.session

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.login.LoginDTO
import com.pex.pex_courier.dto.login.UsersDTO
import com.pex.pex_courier.dto.users.DataProfileDTO

class SystemDataLocal(private val context: Context?) {

    private val sharedPref: SharedPreferences = context!!.getSharedPreferences(context.getString(R.string.user_key), Context.MODE_PRIVATE)
    private val gson = Gson()

    fun userAdd(loginStatus:Boolean, userData:UsersDTO,loginDTO: LoginDTO){
        with(sharedPref.edit()){
            val stringJson = gson.toJson(userData)
            putString("user_obj", stringJson)
            putBoolean("login_status",loginStatus)
            putString("token",loginDTO.token)
            apply()
        }
    }

    fun userUpdate(usersData: DataProfileDTO){
        with(sharedPref.edit()){
            val stringJson = gson.toJson(usersData)
            putString("user_obj",stringJson)
            apply()
        }
    }

    fun fetchLoginStatus(): Boolean {
        return sharedPref.getBoolean("login_status", false)
    }

    fun fetchToken(): String{
        return sharedPref.getString("token","")!!
    }


    @SuppressLint("CommitPrefEdits")
    fun editLogout(){
        with(sharedPref.edit()){
            clear()
//            putBoolean("login_status",false)
            apply()
        }
    }

    fun userFetch(): UsersDTO {
        val stringJson = sharedPref.getString("user_obj", "")
        return gson.fromJson(stringJson, UsersDTO::class.java)
    }

//    fun setSessionLogin() {
//        val shared_Pref: SharedPreferences = context!!.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = shared_Pref.edit()
//        editor.putString("id", usersData?.id.toString())
//        editor.putString("nik", usersData?.nik.toString())
//        editor.putString("namadepan", usersData?.namadepan.toString())
//        editor.putString("namabelakang", usersData?.namabelakang.toString())
//        editor.putBoolean("login", true)
//        editor.apply()
//    }
//    fun save_String(key_name: String, text: String) {
//        val editor: SharedPreferences.Editor = shared_Pref.edit()
//        editor.putString(key_name, text)
//        editor.commit()
//    }
//
//    fun getPreferenceString(key_name: String): String? {
//        return shared_Pref.getString(key_name, null)
//    }
//
//    fun clearSharedPreference() {
//        val editor: SharedPreferences.Editor = shared_Pref.edit()
//        editor.clear()
//        editor.commit()
//    }
//
////    fun getLoginData(): DataUsers? {
//////        sharedPreferences = context!!.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE)
////        val shared_Pref: SharedPreferences = context!!.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE)
////        val id = shared_Pref.getString("id","")
////        val nik = shared_Pref.getString("nik", "")
////        val nama_depan = shared_Pref.getString("nama_depan", "")
////        val nama_belakang = shared_Pref.getString("nama_belakang", "")
////        return DataUsers(nik = nik.toString(), namadepan = nama_depan.toString(), namabelakang = nama_belakang.toString(),id = 0,alamat = "", jeniskelamin = "", departemen = "",email = "",telepon = "",jabatan = "",penempatan = "",imei = "", tglbergabung = "", iconprofile = "", usercreate = "", tglcreate = "", usermodify = "", rolesid = "", password = "", kode = "", position_name = "")
////    }
//
//    fun getCheckLogin(): Boolean {
////        sharedPreferences = context!!.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE)
//        val shared_Pref: SharedPreferences = context!!.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE)
//        return shared_Pref.getBoolean("login", false)
//    }
    companion object {
        private const val KEY_USER = "User"
    }


}