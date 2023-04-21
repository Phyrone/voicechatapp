package de.phyrone.voicechatapp.server.api

import org.atteo.classindex.IndexAnnotated

/**
 * Marks a class as a subcommand for the start parameter
 *
 * DO NOT USE THIS ANNOTATION FOR REGULAR PLUGIN CLASSES AS THEY ARE LOADED AFTER SUBCOMMANDS ARE
 * LOADED
 */
@IndexAnnotated annotation class Subcommand
