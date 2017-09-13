package com.github.wglanzer.annosave.api;

/**
 * Represents a single annotation-interface
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotation
{

  /**
   * @return Type of the annotation
   */
  Class<?> getType();

  /**
   * @return all available parameters/methods of the representing annotation
   */
  IAnnotationParameter[] getParameters();

}
