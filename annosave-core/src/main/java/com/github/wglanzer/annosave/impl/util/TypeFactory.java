package com.github.wglanzer.annosave.impl.util;

import com.github.wglanzer.annosave.impl.structure.SType;

/**
 * @author W.Glanzer, 20.09.2017
 */
public class TypeFactory
{

  public static SType create(String pClassName)
  {
    SType source = new SType();
    source.setName(pClassName);
    return source;
  }

}
