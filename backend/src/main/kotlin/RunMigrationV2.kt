import java.io.File
import java.sql.DriverManager

fun main(args: Array<String>) {
    val dbUrl = System.getenv("ENV_DB_URL")
        ?: throw IllegalArgumentException("ENV_DB_URL environment variable is not set!")

    if (args.isEmpty()) {
        throw IllegalArgumentException("No SQL file paths provided!")
    }

    val sqlFiles = args.map { File(it) }

    // Validate all files exist before starting
    sqlFiles.forEach { file ->
        if (!file.exists()) {
            throw IllegalArgumentException("SQL file not found: ${file.absolutePath}")
        }
    }

    println("Found ${sqlFiles.size} SQL file(s) to execute")
    println("Connecting to database: ${dbUrl.substringBefore("?")}")

    DriverManager.getConnection(dbUrl).use { conn ->
        conn.autoCommit = false
        try {
            sqlFiles.forEachIndexed { index, sqlFile ->
                println("\n[${index + 1}/${sqlFiles.size}] Executing: ${sqlFile.name}")
                conn.createStatement().use { stmt ->
                    val sql = sqlFile.readText()
                    println("  Size: ${sqlFile.length() / 1024} KB")
                    stmt.execute(sql)
                    println("  ✓ Completed")
                }
            }
            conn.commit()
            println("\n✓ All ${sqlFiles.size} migration(s) executed successfully!")
        } catch (e: Exception) {
            println("\n✗ Migration failed, rolling back all changes...")
            conn.rollback()
            throw e
        }
    }
}
