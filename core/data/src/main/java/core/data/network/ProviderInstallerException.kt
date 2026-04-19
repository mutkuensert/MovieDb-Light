package core.data.network

import java.io.IOException

class ProviderInstallerException(override val message: String) : IOException(message)