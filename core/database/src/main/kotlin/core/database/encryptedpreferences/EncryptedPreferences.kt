package core.database.encryptedpreferences

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.integration.android.AndroidKeystoreKmsClient
import core.domain.ProfileFeatureAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val FILE_NAME = "encrypted_preferences"

@Singleton
class EncryptedPreferences @Inject constructor(
    @ApplicationContext context: Context,
) : ProfileFeatureAvailability {
    private val applicationContext = context.applicationContext
    private val sharedPreferences =
        applicationContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    private var isAeadConfigRegistered = false

    init {
        try {
            AeadConfig.register()
            isAeadConfigRegistered = true
        } catch (exception: Exception) {
            Timber.e(exception)
            isAeadConfigRegistered = false
        }
    }

    private val encryption: Aead? = createEncryption()

    override val available get() = isAeadConfigRegistered && encryption != null

    fun contains(key: String): Boolean {
        return getString(key) != null
    }

    fun putString(key: String, value: String): Boolean {
        if (encryption == null) return false

        val encryptedText = encryption.encrypt(
            value.toByteArray(Charsets.UTF_8),
            key.toByteArray(Charsets.UTF_8)
        )
        return sharedPreferences.edit()
            .putString(key, Base64.encodeToString(encryptedText, Base64.NO_WRAP)).commit()
    }

    fun getString(key: String): String? {
        if (encryption == null) return null

        val encryptedText = sharedPreferences.getString(key, null) ?: return null
        return try {
            encryption.decrypt(
                Base64.decode(encryptedText, Base64.DEFAULT),
                key.toByteArray(Charsets.UTF_8)
            ).toString(Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }

    fun remove(key: String): Boolean {
        return sharedPreferences.edit().remove(key).commit()
    }

    private fun createEncryption(): Aead? {
        return try {
            AndroidKeystoreKmsClient.getOrGenerateNewAeadKey(MASTER_KEY_URI)

            val keysetManager = AndroidKeysetManager.Builder()
                .withSharedPref(applicationContext, KEYSET_NAME, FILE_NAME)
                .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()

            check(keysetManager.isUsingKeystore) {
                "Refuse to use a keyset that is not protected by Android Keystore"
            }
            keysetManager.keysetHandle
                .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }
    }

    private companion object {
        const val KEYSET_NAME = "master_keyset"
        const val MASTER_KEY_URI = "android-keystore://master_key"
    }
}
