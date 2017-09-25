package com.github.wglanzer.annosave.impl.converter;

import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;

import java.util.List;

/**
 * @author W.Glanzer, 15.09.2017
 */
public interface ISerializationConverter<Root>
{

  List<SAnnotationContainer> convert(Root pType);

}
