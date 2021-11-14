# kotlin-maven-symbol-processing

Extension for the `kotlin-maven-plugin` to support [Kotlin Symbol Processing][ksp] (KSP).

## Usage

To use this extension, add the dependency to the `kotlin-maven-plugin`:

```xml

<dependency>
    <groupId>com.dyescape</groupId>
    <artifactId>kotlin-maven-symbol-processing</artifactId>
    <version>1.0</version>
</dependency>
```

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

[ksp]: https://goo.gle/ksp
