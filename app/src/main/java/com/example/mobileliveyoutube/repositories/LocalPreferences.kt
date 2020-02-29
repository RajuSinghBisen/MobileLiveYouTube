package com.example.mobileliveyoutube.repositories

import android.content.Context
import com.example.mobileliveyoutube.utils.SingletonHolder


class LocalPreferences private constructor(val context: Context) {
    companion object: SingletonHolder<LocalPreferences, Context>(::LocalPreferences) {

        const val PREFNAME = "mobilelivepref"
        const val EMAIL = "email"
        const val DISPLAYNAME = "displayname"
        const val FAMILYNAME = "familyname"
        const val GIVENNAME = "givenname"
    }
    private fun getPrefs() =
        context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE)

    fun storeEmail(
        email: String?,
        displayName: String?,
        familyName: String?,
        givenName: String?
    ){
        if (email!= null) {
            getPrefs()
                .edit()
                .putString(EMAIL, email)
                .apply()
        }
        if (displayName!= null) {
            getPrefs()
                .edit()
                .putString(DISPLAYNAME, displayName)
                .apply()
        }
        if (familyName!= null) {
            getPrefs()
                .edit()
                .putString(FAMILYNAME, familyName)
                .apply()
        }
        if (givenName!= null) {
            getPrefs()
                .edit()
                .putString(GIVENNAME, givenName)
                .apply()
        }
    }

    fun getEmailStored(): String? =
        getPrefs().getString(EMAIL,"")

    fun getDisplayNameStored(): String? =
        getPrefs().getString(DISPLAYNAME,"")

    fun getFamilyNameStored(): String? =
        getPrefs().getString(FAMILYNAME,"")

    fun getGivenNameStored(): String? =
        getPrefs().getString(GIVENNAME,"")

    fun clearUserDataStored(){
        getPrefs()
            .edit()
            .remove(EMAIL)
            .remove(DISPLAYNAME)
            .remove(FAMILYNAME)
            .remove(GIVENNAME)
            .apply()
    }
}