package com.github.wglanzer.annosave.api;

import org.jetbrains.annotations.*;

import java.util.*;

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
  @NotNull
  IAnnotation[] getAnnotations();

  /**
   * @return child-classes
   */
  @NotNull
  IAnnotationContainer[] getChildren();

  /**
   * Returns an annotation with the given type
   *
   * @param pType Type of the annotation
   * @return the Annotation or <tt>null</tt> if nothing is found
   */
  @Nullable
  default IAnnotation findAnnotation(@NotNull Class<?> pType)
  {
    return Arrays.stream(getAnnotations())
        .filter(pAnnotation -> Objects.equals(pAnnotation.getType().getInstance(), pType))
        .findFirst().orElse(null);
  }

  /**
   * Returns an annotation with the given type
   *
   * @param pType Type of the annotation
   * @return the Annotation or <tt>null</tt> if nothing is found
   */
  @Nullable
  default IAnnotation findAnnotation(@NotNull IType pType)
  {
    return Arrays.stream(getAnnotations())
        .filter(pAnnotation -> Objects.equals(pAnnotation.getType(), pType))
        .findFirst().orElse(null);
  }

}
