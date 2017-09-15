package com.github.wglanzer.annosave.api;

import org.jetbrains.annotations.*;

/**
 * An AnnotationContainer is a container which contains annotations, cpt. obvious :)
 * It can be a Java-Class, -SubClass, -Method or -Field.
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotationContainer
{

  /**
   * @return Type of this container. Mainly the class-reference to the object before.
   * <tt>null</tt> if it could be resolved correctly
   */
  @Nullable
  Class<?> getType();

  /**
   * @return Name of the container. Mainly the class-, method- or fieldname
   */
  @NotNull
  String getName();

  /**
   * @return all annotations on the container
   */
  IAnnotation[] getAnnotations();

  /**
   * @return child-classes
   */
  IAnnotationContainer[] getChildren();

}
