package cn.cheney.uploader

import cn.cheney.uploader.extension.MavenExtension
import cn.cheney.uploader.util.Logger
import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

class MavenPlugin implements Plugin<Project> {

    private Instantiator instantiator

    private Project mProject

    private MavenExtension mavenExtension


    @Override
    void apply(Project project) {
        this.mProject = project
        this.instantiator = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator.class)
        this.mavenExtension = project.extensions.create("pluginMaven", MavenExtension.class, instantiator)
        project.apply([plugin: 'maven'])
        project.afterEvaluate {
            if (!mavenExtension.validate()) {
                return
            }
            if (!project.plugins.hasPlugin('com.android.library')) {
                Logger.e("Not com.android.library ~~")
                return
            }
            project.uploadArchives {
                repositories {
                    mavenDeployer {
                        pom.groupId = mavenExtension.groupId
                        pom.artifactId = mavenExtension.artifactId
                        pom.version = mavenExtension.version
                        repository(url: mavenExtension.repository.url) {
                            if (mavenExtension.repository.auth) {
                                authentication(userName: mavenExtension.repository.userName,
                                        password: mavenExtension.repository.password)
                            }
                        }
                        snapshotRepository(url: mavenExtension.snapshotRepository.url) {
                            if (mavenExtension.snapshotRepository.auth) {
                                authentication(userName: mavenExtension.snapshotRepository.userName,
                                        password: mavenExtension.snapshotRepository.password)
                            }
                        }
                        pom.project {
                            name = mavenExtension.artifactId
                            packaging = "aar"
                        }
                    }
                }
            }
            project.android.libraryVariants.all { LibraryVariant variant ->
                Logger.d("LibraryVariant NAME =" + variant.name)
                if (variant.name == "release") {
                    project.artifacts {
                        archives androidSourcesJarTask(project, mavenExtension.artifactId, variant)
                        archives androidJavadocsJarTask(project, mavenExtension.artifactId, variant)
                    }
                }
            }

        }
    }

    private static Task androidSourcesJarTask(Project project, String publicationName, LibraryVariant variant) {
        def sourcePaths = variant.sourceSets.collect { it.javaDirectories }.flatten()
        return sourcesJarTask(project, publicationName, sourcePaths)
    }

    private static Task androidJavadocsJarTask(Project project, String publicationName, LibraryVariant variant) {
        Javadoc javadoc = project.task("javadoc${publicationName.capitalize()}", type: Javadoc) { Javadoc javadoc ->
            javadoc.source = variant.javaCompileProvider.get().source
            javadoc.classpath = variant.javaCompileProvider.get().classpath
        } as Javadoc
        return javadocsJarTask(project, publicationName, javadoc)
    }

    protected static Task sourcesJarTask(Project project, String publicationName, def ... sourcePaths) {
        return project.task("sourcesJarFor${publicationName.capitalize()}", type: Jar) { Jar jar ->
            jar.archiveClassifier.set("sources")
            jar.from sourcePaths
        }
    }

    protected static Task javadocsJarTask(Project project, String publicationName, Javadoc javadoc) {
        return project.task("javadocsJarFor${publicationName.capitalize()}", type: Jar) { Jar jar ->
            jar.archiveClassifier.set("javadoc")
            jar.exclude("**/*.kt")
            jar.from project.files(javadoc)
        }
    }

}