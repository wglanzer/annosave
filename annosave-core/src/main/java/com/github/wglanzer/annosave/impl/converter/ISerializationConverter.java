package com.github.wglanzer.annosave.impl.converter;

import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;

/**
 * @author W.Glanzer, 15.09.2017
 */
public interface ISerializationConverter<Root>
{

  SAnnotationContainer convert(Root pType);

}
