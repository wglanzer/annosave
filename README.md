# AnnoSave 
[![Build Status](https://travis-ci.org/wglanzer/annosave.svg?branch=master)](https://travis-ci.org/wglanzer/annosave)

AnnoSave is a Java framework which persists annotations in a .json-file so that you can process them without having access to the original class-file at runtime.

The dependency is available in maven-central:
 ```xml
 <dependency>
     <groupId>com.github.wglanzer.annosave</groupId>
     <artifactId>annosave-core</artifactId>
     <version>1.2.2</version>
 </dependency>
 ````
 
 ## Annotation Processor
 AnnoSave provides a simple processor which iterates all annotations annotated with "@AnnoPersist" at compile time.
 If an annotation contains a "serialize"-Method which returns "false" -> It will be ignored.
 The processor generates an "annosave.zip"-File in the target directory containing all those annotations.
 

 Maven dependency:
  ```xml
  <dependency>
      <groupId>com.github.wglanzer.annosave</groupId>
      <artifactId>annosave-processor</artifactId>
      <version>1.2.2</version>
  </dependency>
  ````