package com.github.wglanzer.annosave.impl.converter;

import com.github.wglanzer.annosave.api.*;
import com.github.wglanzer.annosave.api.containers.IMethodContainer;
import com.github.wglanzer.annosave.impl.structure.*;
import com.github.wglanzer.annosave.impl.util.TypeFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author W.Glanzer, 15.09.2017
 */
class WrappedSerializationConverter<Type> implements ISerializationConverter<Type>
{

  private final IAnnoSaveConverter<Type> converter;

  WrappedSerializationConverter(IAnnoSaveConverter<Type> pConverter)
  {
    converter = pConverter;
  }

  @Override
  public List<SAnnotationContainer> convert(Type pType)
  {
    List<IAnnotationContainer> originalContainer = converter.createContainer(pType);
    return originalContainer.stream()
        .map(this::_create)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private SAnnotationContainer _create(IAnnotationContainer pContainer)
  {
    SAnnotationContainer container = pContainer instanceof IMethodContainer ? new SMethodContainer() : new SAnnotationContainer();
    container.setName(pContainer.getName());
    container.setType(_create(pContainer.getType()));
    container.setAnnotations(Arrays.stream(pContainer.getAnnotations())
                                 .map(this::_create)
                                 .toArray(SAnnotation[]::new));
    container.setChildren(Arrays.stream(pContainer.getChildren())
                              .map(this::_create)
                              .toArray(SAnnotationContainer[]::new));

    if(pContainer instanceof IMethodContainer)
      ((SMethodContainer) container).setMethodParameters(Arrays.stream(((IMethodContainer) pContainer).getMethodParameters())
                                        .map(this::_create)
                                        .toArray(SType[]::new));

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
    if (value instanceof IAnnotation[])
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
    if (source.isPrimitive() != pSource.isPrimitive() ||
        source.isArray() != pSource.isArray())
      throw new IllegalArgumentException("IType given in converter is invalid");
    return source;
  }

}
