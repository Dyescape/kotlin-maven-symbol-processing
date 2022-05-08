# kotlin-maven-symbol-processing

Extension for the `kotlin-maven-plugin` to support [Kotlin Symbol Processing][ksp] (KSP).

## Usage

To use this extension, add the dependency to the `kotlin-maven-plugin`:

```xml

<dependency>
    <groupId>com.dyescape</groupId>
    <artifactId>kotlin-maven-symbol-processing</artifactId>
    <version>${ksp.version}</version>
</dependency>
```

> You can find the latest version [here][maven-search].

Then configure the `kotlin-maven-plugin` to use the `ksp` compiler plugin:

```xml

<configuration>
    <compilerPlugins>
        <compilerPlugin>ksp</compilerPlugin>
    </compilerPlugins>
</configuration>
```

To now add a symbol processor, simply add the module containing the symbol processor provider to the `dependencies` of
the `kotlin-maven-plugin`.

> The releases for this project are published to the central maven repository.

### Example `kotlin-maven-plugin` configuration

```xml

<plugin>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <version>${kotlin.version}</version>
    <executions>
        <execution>
            <id>compile</id>
            <phase>compile</phase>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <compilerPlugins>
            <compilerPlugin>ksp</compilerPlugin>
        </compilerPlugins>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.dyescape</groupId>
            <artifactId>kotlin-maven-symbol-processing</artifactId>
            <version>${ksp.version}</version>
        </dependency>
        <dependency>
            <groupId><!-- symbol processor group id --></groupId>
            <artifactId><!-- symbol processor artifact id --></artifactId>
            <version><!-- symbol processor version --></version>
        </dependency>
    </dependencies>
</plugin>
```

### Passing options to the compiler plugin

It is also possible to pass options to KSP by specifying them under `pluginOptions`:

```xml

<pluginOptions>
    <option>ksp:kotlinOutputDir=/my/output/dir</option>
</pluginOptions>
```

Options can also be passed to the actual annotation processors using the repeatable `apoption` option:

```xml

<pluginOptions>
    <option>ksp:apoption=key=value</option>
</pluginOptions>
```

[ksp]: https://goo.gle/ksp
[maven-search]: https://search.maven.org/artifact/com.dyescape/kotlin-maven-symbol-processing
