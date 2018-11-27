package net.android.anko.model.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.android.anko.BuildConfig
import net.android.anko.helper.GithubConfigHelper
import java.util.*

@Parcelize
data class AuthModel(
        var clientId: String = "",
        var clientSecret: String = "",
        var redirectUri: String = "",
        var state: String = "",
        var code: String = "",
        var noteUrl: String = "",
        var note: String = "",
        //Arrays.asList("user", "repo", "gist", "notifications", "read:org")
        var scopes: List<String> = listOf()

) : Parcelable