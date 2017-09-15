package com.github.wglanzer.annosave.impl.converter;

import com.github.wglanzer.annosave.api.IAnnoSaveConverter;

/**
 * @author W.Glanzer, 15.09.2017
 */
public class ConverterFactory
{

  public static ISerializationConverter<Class<?>> createDefaultConverter()
  {
    return new DefaultSerializationConverter();
  }

  public static <T> ISerializationConverter<T> createConverter(IAnnoSaveConverter<T> pConverter)
  {
    return new WrappedSerializationConverter<>(pConverter);
  }

}
