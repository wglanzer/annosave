package com.github.wglanzer.annosave.api;

/**
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotation
{

  Class<?> getType();

  IAnnotationParameter[] getParameters();

}
