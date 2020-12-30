# IntelliJ P4 language plugin

This plugin provides P4 language support by IntelliJ IDE

## Features supported:
 - Syntax highlighting
 - Basic grammar check
 - P4 module support
 - P4 settings (Include paths setting)

## Work in progress:
 - Type check from include path
 - Code completed

## How to install:
Download jar from [release](https://github.com/TakeshiTseng/IntelliJ-P4-Plugin/releases) and install manually or download from Intellij plugin repository.

## How to configure

To set the `p4-include-path`, create a file name `.p4plugin.cfg` in your home directory with the following contents: 
```
p4-include-path=<your custom P4 include path>
```

## Development

### Create idea project file

```
gradle idea
```

### Debug the plugin with IDE

```
gradle runIde
```

### Build the plugin

```
gradle buildPlugin [-PideaVersion=2019.3]
```

After build, you will get the plugin zip file in `build/distributions/` 


## Screenshot:
![Screenshot](https://raw.githubusercontent.com/TakeshiTseng/IntelliJ-P4-Plugin/master/screenshot/p4-plugin-hightlight.png)

### License:
Apache License
