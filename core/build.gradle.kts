plugins {
    // Allow blossom to mark sources root of templates
    idea
    alias(libs.plugins.indra.git)
    alias(libs.plugins.blossom)
    id("com.rtm516.mcxboxbroadcast.java-conventions")
}

dependencies {
    api(libs.gson)
    api(libs.bundles.jackson)
    api(libs.nimbus.jose.jwt)
    api(libs.java.websocket)
    api(libs.methanol)
    api(libs.minecraftauth)
}

sourceSets {
    main {
        blossom {
            val info = GitInfo()
            javaSources {
                property("version", info.version)
                property("gitVersion", info.gitVersion)
                property("buildNumber", info.buildNumber.toString())
                property("branch", info.branch)
                property("commit", info.commit)
                property("repository", info.repository)
            }
        }
    }
}

// Modified from https://github.com/GeyserMC/Geyser/blob/ca2312c7f68c54f32314c40c2c1db5d9cda5a0b2/core/build.gradle.kts#L111-L143
inner class GitInfo {
    val branch: String
    val commit: String
    val commitAbbrev: String

    val gitVersion: String
    val version: String
    val buildNumber: Int

    val commitMessage: String
    val repository: String

    init {
        branch = indraGit.branchName() ?: "DEV"

        val commit = indraGit.commit()
        this.commit = commit?.name ?: "0".repeat(40)
        commitAbbrev = commit?.name?.substring(0, 7) ?: "0".repeat(7)

        gitVersion = "git-${branch}-${commitAbbrev}"
        buildNumber = (System.getenv("BUILD_NUMBER"))?.let { Integer.parseInt(it) } ?: -1

        if (buildNumber == -1) {
            version = "DEV ($gitVersion)"
        } else {
            version = "build ${buildNumber} ($gitVersion)"
        }

        val git = indraGit.git()
        commitMessage = git?.commit()?.message ?: ""
        repository = git?.repository?.config?.getString("remote", "origin", "url") ?: ""
    }
}

description = "core"
