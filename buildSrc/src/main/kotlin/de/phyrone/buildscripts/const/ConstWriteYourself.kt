package de.phyrone.buildscripts.const

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.eclipse.jgit.lib.Constants as JGitConstants
import org.eclipse.jgit.lib.ObjectId
import java.io.File
import kotlin.reflect.KClass

object ConstWriteYourself {

    fun writeBuildInfo(
        outDIR: File,
        props: Map<String, Any>
    ) {

        val properties = props.map { (key, value) ->


            val isPrimitive = value is String || value is Number || value is Boolean
            val builder = PropertySpec.builder(key.uppercase(), value::class)
            if (isPrimitive)
                builder.addModifiers(KModifier.CONST)

            when(value){
                is Number -> builder.initializer("%L", value)
                is Boolean -> builder.initializer("%L", value)
                is String -> builder.initializer("%S", value)
                //TODO add more types
                else -> throw IllegalArgumentException("Unknown Type: ${value::class}")
            }


            builder.build()
        }

        val propertyObjectSpec = TypeSpec.objectBuilder("BuildInfo")
            .addProperties(properties)
            .build()
        FileSpec.builder("de.phyrone.voicechatapp", "BuildInfo")
            .addType(propertyObjectSpec)
            .build()
            .writeTo(outDIR)
    }


    private const val GIT_PROPERTY_COMMIT_HASH = "git_commit_hash"
    private const val GIT_PROPERTY_COMMIT_BRANCH = "bit_commit_branch"

    private const val GIT_PROPERTY_VALUE_UNKNOWN = "unknown"
    fun getGitInfo(rootDIR: File): Map<String, String> {

        try {
            val git = Git.open(rootDIR)
            val branch = git.repository.branch
            val head = git.repository.resolve(JGitConstants.HEAD)
            val commitHash: String = head.name()

            return mapOf(
                GIT_PROPERTY_COMMIT_BRANCH to branch,
                GIT_PROPERTY_COMMIT_HASH to commitHash
            )

        } catch (e: RepositoryNotFoundException) {
            return mapOf(
                GIT_PROPERTY_COMMIT_BRANCH to GIT_PROPERTY_VALUE_UNKNOWN,
                GIT_PROPERTY_COMMIT_HASH to GIT_PROPERTY_VALUE_UNKNOWN
            )
        }

    }


}