package com.github.wglanzer.annosave.api;

import org.jetbrains.annotations.NotNull;

/**
 * An AnnotationContainer is a container which contains annotations, cpt. obvious :)
 * It can be a Java-Class, -SubClass, -Method or -Field.
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotationContainer
{

  /**
   * @return Name of the container
   */
  @NotNull
  String getName();

  /**
   * @return Type of this container
   */
  @NotNull
  IType getType();

  /**
   * @return Type of the container
   */
  @NotNull
  EContainerType getContainerType();

  /**
   * @return all annotations on the container
   */
  IAnnotation[] getAnnotations();

  /**
   * @return child-classes
   */
  IAnnotationContainer[] getChildren();

}
