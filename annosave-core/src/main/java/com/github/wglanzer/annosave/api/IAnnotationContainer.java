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
    try
    {
      return getAnnotation(pType);
    }
    catch(Exception e)
    {
      return null;
    }
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
    try
    {
      return getAnnotation(pType);
    }
    catch(Exception e)
    {
      return null;
    }
  }

  /**
   * Returns an annotation with the given type
   *
   * @param pType Type of the annotation
   * @return the Annotation
   * @throws NullPointerException if nothing was found
   */
  @NotNull
  default IAnnotation getAnnotation(@NotNull Class<?> pType)
  {
    return Arrays.stream(getAnnotations())
        .filter(pAnnotation -> Objects.equals(pAnnotation.getType().getInstance(), pType))
        .findFirst().orElseThrow(NullPointerException::new);
  }

  /**
   * Returns an annotation with the given type
   *
   * @param pType Type of the annotation
   * @return the Annotation
   * @throws NullPointerException if nothing was found
   */
  @NotNull
  default IAnnotation getAnnotation(@NotNull IType pType)
  {
    return Arrays.stream(getAnnotations())
        .filter(pAnnotation -> Objects.equals(pAnnotation.getType(), pType))
        .findFirst().orElseThrow(NullPointerException::new);
  }

  /**
   * Returns <tt>true</tt> if an annotation with pType was found on this container
   *
   * @param pType Type of the annotation
   * @return <tt>true</tt> if it is available
   */
  default boolean hasAnnotation(@NotNull Class<?> pType)
  {
    return findAnnotation(pType) != null;
  }

  /**
   * Returns <tt>true</tt> if an annotation with pType was found on this container
   *
   * @param pType Type of the annotation
   * @return <tt>true</tt> if it is available
   */
  default boolean hasAnnotation(@NotNull IType pType)
  {
    return findAnnotation(pType) != null;
  }

}
