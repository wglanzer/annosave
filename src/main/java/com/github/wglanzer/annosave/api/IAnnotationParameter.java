package com.github.wglanzer.annosave.api;

/**
 * @author W.Glanzer, 13.09.2017
 */
public interface IAnnotationParameter
{

  String getName();

  Class<?> getType();

  Object getValue();

}
