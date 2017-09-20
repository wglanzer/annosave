package com.github.wglanzer.annosave.api;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a single annotation-interface
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotation
{

  /**
   * @return Name of the annotation. Mainly the classname
   */
  @NotNull
  String getName();

  /**
   * @return Type of this annotation
   */
  @NotNull
  IType getType();

  /**
   * @return all available parameters/methods of the representing annotation
   */
  IAnnotationParameter[] getParameters();

}
