package com.github.wglanzer.annosave.api;

import org.jetbrains.annotations.*;

/**
 * Represents a single annotation-interface
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotation
{

  /**
   * @return Type of the annotation.
   * Can be <tt>null</tt> if the type could not be resolved
   */
  @Nullable
  Class<?> getType();

  /**
   * @return Name of the annotation. Mainly the classname
   */
  @NotNull
  String getName();

  /**
   * @return all available parameters/methods of the representing annotation
   */
  IAnnotationParameter[] getParameters();

}
