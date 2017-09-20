package com.github.wglanzer.annosave.impl.converter;

import com.github.wglanzer.annosave.api.*;
import com.github.wglanzer.annosave.impl.structure.*;
import com.github.wglanzer.annosave.impl.util.TypeFactory;

import java.util.Arrays;

/**
 * @author W.Glanzer, 15.09.2017
 */
class WrappedSerializationConverter<Type> implements ISerializationConverter<Type>
{

  private final IAnnoSaveConverter<Type> converter;

  public WrappedSerializationConverter(IAnnoSaveConverter<Type> pConverter)
  {
    converter = pConverter;
  }

  @Override
  public SAnnotationContainer convert(Type pType)
  {
    IAnnotationContainer originalContainer = converter.createContainer(pType);
    return _create(originalContainer);
  }

  private SAnnotationContainer _create(IAnnotationContainer pContainer)
  {
    SAnnotationContainer container = new SAnnotationContainer();
    container.setName(pContainer.getName());
    container.setType(_create(pContainer.getType()));
    container.setContainerType(pContainer.getContainerType());
    container.setAnnotations(Arrays.stream(pContainer.getAnnotations())
                                 .map(this::_create)
                                 .toArray(SAnnotation[]::new));
    container.setChildren(Arrays.stream(pContainer.getChildren())
                              .map(this::_create)
                              .toArray(SAnnotationContainer[]::new));
    return container;
  }

  private SAnnotation _create(IAnnotation pAnnotation)
  {
    SAnnotation annotation = new SAnnotation();
    annotation.setName(pAnnotation.getName());
    annotation.setType(_create(pAnnotation.getType()));
    annotation.setParameters(Arrays.stream(pAnnotation.getParameters())
                                 .map(this::_create)
                                 .toArray(SAnnotationParameter[]::new));
    return annotation;
  }

  private SAnnotationParameter _create(IAnnotationParameter pParameter)
  {
    SAnnotationParameter parameter = new SAnnotationParameter();
    parameter.setName(pParameter.getName());
    parameter.setType(_create(pParameter.getType()));

    Object value = pParameter.getValue();
    if(value instanceof IAnnotation[])
    {
      value = Arrays.stream((IAnnotation[]) value)
          .map(this::_create)
          .toArray(SAnnotation[]::new);
    }

    parameter.setOriginalValue(value);
    return parameter;
  }

  private SType _create(IType pSource)
  {
    SType source = TypeFactory.create(pSource.getClassName());
    if(source.isPrimitive() != pSource.isPrimitive() ||
        source.isArray() != pSource.isArray())
      throw new IllegalArgumentException("IType given in converter is invalid");
    return source;
  }

}
