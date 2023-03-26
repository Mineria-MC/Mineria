# Mineria

Mineria is a vanilla+ oriented mod that aims to add brand-new ores, tools and uses to improve the
Minecraft experience.

---
## For Developers

Welcome to Mineria's GitHub repository! Here you can find the code that runs behind the scenes the *incredible*
mod that is Mineria. If you have any issue, any suggestions or any minor changes/improvements to bring to the mod
feel free to open an issue/pull request.

### Mineria is on GitHub Packages!

Want to use Mineria for one of your projects? For an addon? Or just to resolve a compatibility issue
with your mod? You can include Mineria as a maven dependency in your gradle project with the following code:

In `build.gradle`:
```
repositories {
    [...]
    maven {
        name "Mineria (GitHub)"
        url "https://maven.pkg.github.com/Mineria-MC/Mineria"
        credentials {
            username findProperty("github_username")
            password findProperty("github_token")
        }
    }
}

dependencies {
    [...]
    
    implementation fg.deobf('io.github.mineria_mc:mineria:<version>-mc<mc_version>')
}
```

**IMPORTANT**: as Mineria is using GitHub Packages to host its artifacts, you will need to authenticate in
order to query them. Therefore, we recommend that you create a `gradle.properties` file either in your
`GRADLE_HOME` directory (usually located at `USER_HOME/.gradle`) or in your project's `.gradle` directory
defining the `github_username` and `github_token` properties we use above:

In `gradle.properties`:
```
github_username=<Your GitHub Username>
github_token=<Your GitHub personal access token>
```

**Make sure that this file is excluded from any source control indexing!**
If you don't know how to generate a GitHub personal access token, you can check [this tutorial](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).

And voilà! Enjoy working with Mineria!