package com.example.shesoul.features.auth.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class GoogleSignInManager(private val context: Context) {
    
    private val auth: FirebaseAuth = Firebase.auth
    
    // Configure Google Sign-In to request the user's ID, email address, and basic profile
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("514708419997-vcasulnfnbjpgr2h6shgrb3952gup0rj.apps.googleusercontent.com")
        .requestEmail()
        .build()
    
    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
    
    fun getSignInIntent(): Intent {
        Log.d("GoogleSignIn", "Getting sign-in intent...")
        Log.d("GoogleSignIn", "GoogleSignInClient initialized: ${googleSignInClient != null}")
        val intent = googleSignInClient.signInIntent
        Log.d("GoogleSignIn", "Sign-in intent created successfully")
        return intent
    }
    
    suspend fun handleSignInResult(data: Intent?): GoogleSignInResult {
        return try {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            
            // Firebase authentication with Google
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user
            
            if (user != null) {
                GoogleSignInResult.Success(
                    idToken = account.idToken ?: "",
                    displayName = user.displayName ?: "",
                    email = user.email ?: ""
                )
            } else {
                GoogleSignInResult.Error("Authentication failed")
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
            GoogleSignInResult.Error("Sign-in failed: ${e.message}")
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error during sign-in", e)
            GoogleSignInResult.Error("Sign-in failed: ${e.message}")
        }
    }
    
    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}

sealed class GoogleSignInResult {
    data class Success(
        val idToken: String,
        val displayName: String,
        val email: String
    ) : GoogleSignInResult()
    
    data class Error(val message: String) : GoogleSignInResult()
    object Cancelled : GoogleSignInResult()
}
