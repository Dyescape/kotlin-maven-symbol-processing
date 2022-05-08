package com.dyescape.ksp.maven

import com.google.devtools.ksp.KspCliOption
import org.apache.maven.plugin.MojoExecution

// from org.jetbrains.kotlin.maven.KotlinCompileMojoBase.OPTION_PATTERN
private val pattern = Regex("([^:]+):([^=]+)=(.*)")

private fun parseUserOption(option: String): UserOption? {
    val match = pattern.matchEntire(option) ?: return null

    val (name, key, value) = match.destructured

    return UserOption(name, key, value)
}


private data class UserOption(val pluginName: String, val key: String, val value: String)

private val availableOptions = KspCliOption.values().associateBy { it.optionName }

fun MojoExecution.findKspOptions(): Map<KspCliOption, List<String>> {
    val pluginOptions = configuration.getChild("pluginOptions") ?: return emptyMap()
    val rawOptions = pluginOptions.children.map { it.value }

    return rawOptions.mapNotNull { parseUserOption(it) }
        .filter { it.pluginName == KSP_PLUGIN_NAME }
        .filter { it.key in availableOptions }
        .groupBy({ availableOptions.getValue(it.key) }, UserOption::value)
}
