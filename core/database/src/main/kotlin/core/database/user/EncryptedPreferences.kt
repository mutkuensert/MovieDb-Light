package core.database.user

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager

class EncryptedPreferences(context: Context, fileName: String) {
    private val sharedPreferences =
        context.getSharedPreferences("encrpyted_preferences", Context.MODE_PRIVATE)

    init {
        AeadConfig.register()
    }

    val encryption: Aead = AndroidKeysetManager.Builder()
        .withSharedPref(context, "master_keyset", fileName)
        .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
        .withMasterKeyUri("android-keystore://master_key")
        .build()
        .keysetHandle
        .getPrimitive(RegistryConfiguration.get(), Aead::class.java)

    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun putString(key: String, value: String): Boolean {
        val encryptedText = encryption.encrypt(value.toByteArray(), null)
        return sharedPreferences.edit()
            .putString(key, Base64.encodeToString(encryptedText, Base64.DEFAULT)).commit()
    }

    fun getString(key: String): String? {
        val encryptedText = sharedPreferences.getString(key, null) ?: return null
        return encryption.decrypt(
            Base64.decode(encryptedText, Base64.DEFAULT),
            null
        ).toString(Charsets.UTF_8)
    }

    fun remove(key: String): Boolean {
        return sharedPreferences.edit().remove(key).commit()
    }
}