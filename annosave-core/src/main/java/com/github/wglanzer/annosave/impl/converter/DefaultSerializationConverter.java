package com.github.wglanzer.annosave.impl.converter;

import com.github.wglanzer.annosave.impl.structure.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
class DefaultSerializationConverter implements ISerializationConverter<Class<?>>
{

  /**
   * Converts a class to the SAnnotationContainer-Class for serializing with gson
   *
   * @param pClass class which should be converted
   * @return container
   */
  public SAnnotationContainer convert(Class<?> pClass)
  {
    SAnnotationContainer container = new SAnnotationContainer();
    container.setName(pClass.getSimpleName());
    container.setAnnotations(Arrays.stream(pClass.getDeclaredAnnotations())
                                 .map(this::_convert)
                                 .toArray(SAnnotation[]::new));

    List<SAnnotationContainer> children = new ArrayList<>();
    for (Class<?> clazz : pClass.getDeclaredClasses())
      children.add(convert(clazz));
    for (Field field : pClass.getDeclaredFields())
      children.add(_convert(field));
    for (Method method : pClass.getDeclaredMethods())
      children.add(_convert(method));
    container.setChildren(children.toArray(new SAnnotationContainer[children.size()]));

    return container;
  }

  private SAnnotationContainer _convert(Field pField)
  {
    SAnnotationContainer container = new SAnnotationContainer();
    container.setName(pField.getName());
    container.setAnnotations(Arrays.stream(pField.getDeclaredAnnotations())
                                 .map(this::_convert)
                                 .toArray(SAnnotation[]::new));
    return container;
  }

  private SAnnotationContainer _convert(Method pMethod)
  {
    SAnnotationContainer container = new SAnnotationContainer();
    container.setName(pMethod.getName());
    container.setAnnotations(Arrays.stream(pMethod.getDeclaredAnnotations())
                                 .map(this::_convert)
                                 .toArray(SAnnotation[]::new));
    return container;
  }

  private SAnnotation _convert(Annotation pAnnotation)
  {
    SAnnotation annotation = new SAnnotation();
    annotation.setType(pAnnotation.annotationType());

    List<SAnnotationParameter> parameters = new ArrayList<>();
    for (Method method : pAnnotation.annotationType().getDeclaredMethods())
    {
      try
      {
        method.setAccessible(true);
        Object result = method.invoke(pAnnotation);
        SAnnotationParameter parameter = new SAnnotationParameter();
        parameter.setName(method.getName());
        parameter.setType(method.getReturnType());

        if (result.getClass().isArray())
        {
          if (result instanceof Annotation[])
          {
            result = Arrays.stream((Object[]) result)
                .map(pClass -> _convert((Annotation) pClass))
                .toArray(SAnnotation[]::new);
          }
        }

        parameter.setValue(result);
        parameters.add(parameter);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    annotation.setParameters(parameters.toArray(new SAnnotationParameter[parameters.size()]));

    return annotation;
  }

}
