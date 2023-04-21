import java.io.File
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object JRTExtractor {
    fun extract(
        output: File
    ) {
        output.parentFile?.mkdirs()
        val fs = FileSystems.getFileSystem(URI.create("jrt:/"))
        ZipOutputStream(
            output.outputStream()
        ).use { zipStream ->
            Files.walk(fs.getPath("/")).forEach { p: Path ->
                if (!Files.isRegularFile(p)) {
                    return@forEach
                }
                try {
                    val data = Files.readAllBytes(p)
                    val list: MutableList<String> = ArrayList()
                    p.iterator()
                        .forEachRemaining { p2: Path ->
                            list.add(
                                p2.toString()
                            )
                        }
                    assert(list.removeAt(0) == "modules")
                    if (list[list.size - 1] != "module-info.class") {
                        list.removeAt(0)
                    }
                    val outPath = java.lang.String.join("/", list)
                    val ze = ZipEntry(outPath)
                    zipStream.putNextEntry(ze)
                    zipStream.write(data)
                } catch (t: Throwable) {
                    throw RuntimeException(t)
                }
            }
        }
    }
}