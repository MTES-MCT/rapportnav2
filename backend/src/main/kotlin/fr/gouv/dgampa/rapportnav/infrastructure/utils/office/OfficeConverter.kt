package fr.gouv.dgampa.rapportnav.infrastructure.utils.office

import fr.gouv.dgampa.rapportnav.domain.repositories.office.IOfficeConverter
import jakarta.annotation.PreDestroy
import org.jodconverter.core.DocumentConverter
import org.jodconverter.core.office.OfficeManager
import org.jodconverter.local.LocalConverter
import org.jodconverter.local.office.LocalOfficeManager
import org.jodconverter.remote.RemoteConverter
import org.jodconverter.remote.office.RemoteOfficeManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

/**
 * Converts office documents (docx -> odt, xlsx -> ods) with LibreOffice.
 *
 * Two modes, selected by `rapportnav.office.mode`:
 * - `local`: a LibreOffice process installed in the same container, started once and reused
 * - `remote`: a LibreOffice conversion server running as a separate container (sidecar),
 *   reached over HTTP with the LibreOffice Online API (`POST {url}/{format}`, multipart field `data`)
 *
 * The office manager is started lazily on the first conversion, so the application
 * (and the test context) can boot on machines without LibreOffice.
 */
@Component
class OfficeConverter(
    @param:Value("\${rapportnav.office.mode:local}") private val mode: String,
    @param:Value("\${rapportnav.office.remote.url:}") private val remoteUrl: String,
    @param:Value("\${rapportnav.office.remote.timeout-ms:30000}") private val remoteTimeoutMs: Long,
) : IOfficeConverter {

    private val logger: Logger = LoggerFactory.getLogger(OfficeConverter::class.java)

    private val officeManagerDelegate = lazy { startOfficeManager() }
    private val officeManager: OfficeManager by officeManagerDelegate

    private val converter: DocumentConverter by lazy {
        if (isRemote()) RemoteConverter.make(officeManager) else LocalConverter.make(officeManager)
    }

    override fun convert(sourcePath: String, destinationPath: String): String {
        converter.convert(File(sourcePath)).to(File(destinationPath)).execute()
        logger.info("[OfficeConverter::convert] ($mode) Successfully converted $sourcePath to $destinationPath")
        return destinationPath
    }

    @PreDestroy
    fun stop() {
        if (!officeManagerDelegate.isInitialized()) return
        try {
            officeManager.stop()
        } catch (e: Exception) {
            logger.error("[OfficeConverter::stop] error on officeManager.stop() : ${e.message}")
        }
    }

    private fun isRemote(): Boolean = mode.equals("remote", ignoreCase = true)

    private fun startOfficeManager(): OfficeManager {
        val manager = if (isRemote()) {
            require(remoteUrl.isNotBlank()) { "rapportnav.office.remote.url must be set when rapportnav.office.mode=remote" }
            RemoteOfficeManager.builder()
                .urlConnection(remoteUrl)
                .connectTimeout(remoteTimeoutMs)
                .socketTimeout(remoteTimeoutMs)
                .build()
        } else {
            LocalOfficeManager.builder().build()
        }
        try {
            manager.start()
        } catch (e: Exception) {
            // stop the half-started manager (soffice process, temp profile) before the next lazy attempt
            runCatching { manager.stop() }
            throw e
        }
        logger.info("[OfficeConverter] office manager started in $mode mode${if (isRemote()) " ($remoteUrl)" else ""}")
        return manager
    }
}
