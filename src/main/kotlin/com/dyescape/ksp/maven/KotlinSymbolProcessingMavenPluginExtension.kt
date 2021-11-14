package com.dyescape.ksp.maven

import com.google.devtools.ksp.KspCliOption
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest
import org.apache.maven.plugin.MojoExecution
import org.apache.maven.project.MavenProject
import org.apache.maven.repository.RepositorySystem
import org.codehaus.plexus.component.annotations.Component
import org.codehaus.plexus.component.annotations.Requirement
import org.jetbrains.kotlin.maven.KotlinMavenPluginExtension
import org.jetbrains.kotlin.maven.PluginOption
import java.io.File

const val KSP_PLUGIN_ID = "com.google.devtools.ksp.symbol-processing"

@Component(role = KotlinMavenPluginExtension::class, hint = "ksp")
class KotlinSymbolProcessingMavenPluginExtension : KotlinMavenPluginExtension {
    @Requirement
    lateinit var system: RepositorySystem

    override fun isApplicable(project: MavenProject, execution: MojoExecution) = true

    override fun getCompilerPluginId() = KSP_PLUGIN_ID

    override fun getPluginOptions(project: MavenProject, execution: MojoExecution): List<PluginOption> {
        val options = mutableMapOf<KspCliOption, String>()

        options[KspCliOption.CLASS_OUTPUT_DIR_OPTION] = project.build.outputDirectory
        options[KspCliOption.JAVA_OUTPUT_DIR_OPTION] = project.generatedSourcesDir("ksp-java").path
        options[KspCliOption.KOTLIN_OUTPUT_DIR_OPTION] = project.generatedSourcesDir("ksp").path
        options[KspCliOption.RESOURCE_OUTPUT_DIR_OPTION] = project.build.outputDirectory
        options[KspCliOption.PROJECT_BASE_DIR_OPTION] = project.basedir.path
        options[KspCliOption.KSP_OUTPUT_DIR_OPTION] = project.buildDir("ksp").path
        options[KspCliOption.PROCESSOR_CLASSPATH_OPTION] = execution.constructProcessorClassPath()
        options[KspCliOption.CACHES_DIR_OPTION] = system.cacheDir("ksp").path
        options[KspCliOption.WITH_COMPILATION_OPTION] = "true"

        project.addCompileSourceRoot(options[KspCliOption.JAVA_OUTPUT_DIR_OPTION])
        project.addCompileSourceRoot(options[KspCliOption.KOTLIN_OUTPUT_DIR_OPTION])

        return options.map { (option, value) -> PluginOption("ksp", KSP_PLUGIN_ID, option.optionName, value) }
    }

    private fun MojoExecution.constructProcessorClassPath(): String {
        val classPath = plugin.dependencies.flatMap {
            val artifact = system.createDependencyArtifact(it)
            val resolved = system.resolve(ArtifactResolutionRequest().setArtifact(artifact))

            resolved.artifacts.mapNotNull(Artifact::getFile).filter(File::exists)
        }

        return classPath.joinToString(separator = File.pathSeparator) { it.path }
    }

    private fun MavenProject.buildDir(name: String): File {
        return File(build.directory, name)
    }

    private fun MavenProject.generatedSourcesDir(name: String): File {
        return File(buildDir("generated-sources"), name)
    }

    private fun RepositorySystem.cacheDir(name: String): File {
        return File(createDefaultLocalRepository().basedir, ".cache").resolve(name)
    }
}
