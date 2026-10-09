package com.example.financeapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine

interface AuthRepository {
    val currentUser: Flow<FirebaseUser?>
    fun isLoggedIn(): Boolean
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser>
    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser>
    fun signOut()
    fun getUid(): String?
}

class AuthRepositoryImpl : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override val currentUser: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return suspendCancellableCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    if (result.user != null) cont.resume(Result.success(result.user!!))
                    else cont.resume(Result.failure(Exception("Login failed")))
                }
                .addOnFailureListener { e ->
                    cont.resume(Result.failure(e))
                }
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser> {
        return suspendCancellableCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    if (result.user != null) cont.resume(Result.success(result.user!!))
                    else cont.resume(Result.failure(Exception("Signup failed")))
                }
                .addOnFailureListener { e ->
                    cont.resume(Result.failure(e))
                }
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getUid(): String? {
        return auth.currentUser?.uid
    }
}
