package com.github.wglanzer.annosave.api;

import org.jetbrains.annotations.NotNull;

/**
 * A single parameter/method of an annotation with the value set
 *
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotationParameter
{

  /**
   * @return Name of the method/parameter
   */
  String getName();

  /**
   * @return Returntype of the parameter
   */
  @NotNull
  IType getType();

  /**
   * @return Specific value set in this annotationparameter.
   * It can be cast to the type given in getType()
   */
  Object getValue();

}
