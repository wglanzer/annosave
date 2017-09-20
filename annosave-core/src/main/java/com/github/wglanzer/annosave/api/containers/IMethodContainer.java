package com.github.wglanzer.annosave.api.containers;

import com.github.wglanzer.annosave.api.*;
import org.jetbrains.annotations.NotNull;

/**
 * A specific AnnotationContainer for methods
 *
 * @author W.Glanzer, 20.09.2017
 */
public interface IMethodContainer extends IAnnotationContainer
{

  /**
   * @return an array of all used parameters sorted by declaration order
   */
  @NotNull
  IType[] getMethodParameters();

}
