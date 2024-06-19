@file:Suppress("HEADER_MISSING_IN_NON_SINGLE_CLASS_FILE")

package com.sanyavertolet.chess.buildutils

import org.gradle.api.Project
import java.io.File

/**
 * @param propertyName
 * @param envName
 * @return value of a property as [String] or null if no such property exists
 */
@Suppress("unused")
fun Project.readFromPropertyOrEnv(
    propertyName: String,
    envName: String,
) = readFromFileOrEnv(findProperty(propertyName) as String?, envName)

/**
 * @param filePath
 * @return value of a property as [String] or null if no such property exists
 */
fun readFromFile(filePath: String?) = filePath?.let { File(it) }
    ?.takeIf { it.exists() }
    ?.readLines()
    ?.joinToString("\n")

/**
 * @param filePath
 * @param envName
 * @return value of a property as [String] or null if no such property exists
 */
fun readFromFileOrEnv(
    filePath: String?,
    envName: String,
): String? = readFromFile(filePath) ?: System.getenv(envName)
