package cn.cheney.uploader.extension

import cn.cheney.uploader.util.Logger
import cn.cheney.uploader.util.StringUtils
import org.gradle.api.Action
import org.gradle.internal.reflect.Instantiator

class MavenExtension {

    String groupId
    String artifactId
    String version

    MavenRepositoryExtension repository
    MavenRepositoryExtension snapshotRepository

    MavenExtension(Instantiator instantiator) {
        repository = instantiator.newInstance(MavenRepositoryExtension.class)
        snapshotRepository = instantiator.newInstance(MavenRepositoryExtension.class)
    }

    void repository(Action<MavenRepositoryExtension> action) {
        action.execute(repository)
    }

    void snapshotRepository(Action<MavenRepositoryExtension> action) {
        action.execute(snapshotRepository)
    }


    boolean validate() {
        String extensionError = ""
        if (StringUtils.isNullOrEmpty(groupId)) {
            extensionError += "Missing groupId. "
        }

        if (StringUtils.isNullOrEmpty(artifactId)) {
            extensionError += "Missing artifactId. "
        }

        if (StringUtils.isNullOrEmpty(version)) {
            extensionError += "Missing version. "
        }
        if (!checkRepository() && !checkSnapshotRepository()) {
            extensionError += "Missing Repository Url . "
        }
        if (extensionError) {
            String prefix = "Have you created the  maven? "
            Logger.e(prefix + extensionError)
            return false
        } else {
            Logger.d("groupId =" + groupId + " artifactId=" + artifactId + " version=" + version)
            Logger.d("Target  MavenRepository =$targetRepositoryEx")
            return true
        }
    }


    MavenRepositoryExtension getTargetRepositoryEx() {
        if (StringUtils.isNullOrEmpty(version)) {
            return null
        }
        return version.endsWith('-SNAPSHOT') ? snapshotRepository : repository
    }

    boolean checkRepository() {
        if (!repository) {
            repository = new MavenRepositoryExtension()
        }
        return !StringUtils.isNullOrEmpty(repository.url)
    }

    boolean checkSnapshotRepository() {
        if (!snapshotRepository) {
            snapshotRepository = new MavenRepositoryExtension()
        }
        return !StringUtils.isNullOrEmpty(snapshotRepository.url)
    }


}