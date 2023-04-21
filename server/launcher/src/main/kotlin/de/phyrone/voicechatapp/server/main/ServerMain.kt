/**
 * This file is The Default Entry Point of the Server it is called by the JVM It installs the
 * AnsiConsole unless set to false by the jansi.enable property The it launches the
 * [StartupParameters] class
 */
@file:JvmName("ServerMain")

package de.phyrone.voicechatapp.server.main

import de.phyrone.voicechatapp.server.api.Subcommand
import de.phyrone.voicechatapp.server.api.logger
import kotlin.system.exitProcess
import org.atteo.classindex.ClassIndex
import org.fusesource.jansi.AnsiConsole
import picocli.CommandLine

private val LOGGER = logger()

fun main(args: Array<String>) {
  if (System.getProperty("jansi.enable")?.toBooleanStrict() != false) {
    AnsiConsole.systemInstall()
  }
  LOGGER.atFine().log("Reached Main...")

  val commandLine = CommandLine(StartupParameters::class.java)
  commandLine.colorScheme = CommandLine.Help.defaultColorScheme(CommandLine.Help.Ansi.ON)
  ClassIndex.getAnnotated(Subcommand::class.java).forEach { commandLine.addSubcommand(it) }

  exitProcess(commandLine.execute(*args))
}
