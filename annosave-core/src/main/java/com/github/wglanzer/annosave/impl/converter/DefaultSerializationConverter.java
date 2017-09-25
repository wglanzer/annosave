package com.github.wglanzer.annosave.impl.converter;

import com.github.wglanzer.annosave.impl.structure.*;
import com.github.wglanzer.annosave.impl.util.TypeFactory;

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
  public List<SAnnotationContainer> convert(Class<?> pClass)
  {
    SAnnotationContainer container = new SAnnotationContainer();
    container.setName(pClass.getName());
    container.setType(TypeFactory.create(pClass.getName()));
    container.setAnnotations(Arrays.stream(pClass.getDeclaredAnnotations())
                                 .map(this::_convert)
                                 .toArray(SAnnotation[]::new));

    List<SAnnotationContainer> children = new ArrayList<>();
    for (Field field : pClass.getDeclaredFields())
      children.add(_convert(field));
    for (Method method : pClass.getDeclaredMethods())
      children.add(_convert(method));
    container.setChildren(children.toArray(new SAnnotationContainer[children.size()]));

    List<SAnnotationContainer> result = new ArrayList<>();
    for (Class<?> clazz : pClass.getDeclaredClasses())
      result.addAll(convert(clazz));
    result.add(container);
    return result;
  }

  private SAnnotationContainer _convert(Field pField)
  {
    SAnnotationContainer container = new SAnnotationContainer();
    container.setName(pField.getName());
    container.setType(TypeFactory.create(pField.getType().getName()));
    container.setAnnotations(Arrays.stream(pField.getDeclaredAnnotations())
                                 .map(this::_convert)
                                 .toArray(SAnnotation[]::new));
    return container;
  }

  private SAnnotationContainer _convert(Method pMethod)
  {
    SMethodContainer container = new SMethodContainer();
    container.setName(pMethod.getName());
    container.setType(TypeFactory.create(pMethod.getReturnType().getName()));
    container.setAnnotations(Arrays.stream(pMethod.getDeclaredAnnotations())
                                 .map(this::_convert)
                                 .toArray(SAnnotation[]::new));
    container.setMethodParameters(Arrays.stream(pMethod.getParameters())
                                      .map(pParameter -> TypeFactory.create(pParameter.getType().getName()))
                                      .toArray(SType[]::new));
    return container;
  }

  private SAnnotation _convert(Annotation pAnnotation)
  {
    SAnnotation annotation = new SAnnotation();
    annotation.setType(TypeFactory.create(pAnnotation.annotationType().getName()));
    annotation.setName(pAnnotation.annotationType().getName());

    List<SAnnotationParameter> parameters = new ArrayList<>();
    for (Method method : pAnnotation.annotationType().getDeclaredMethods())
    {
      try
      {
        method.setAccessible(true);
        Object result = method.invoke(pAnnotation);
        SAnnotationParameter parameter = new SAnnotationParameter();
        parameter.setName(method.getName());
        parameter.setType(TypeFactory.create(method.getReturnType().getName()));

        if (result.getClass().isArray())
        {
          if (result instanceof Annotation[])
          {
            result = Arrays.stream((Object[]) result)
                .map(pClass -> _convert((Annotation) pClass))
                .toArray(SAnnotation[]::new);
          }
        }

        parameter.setOriginalValue(result);
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
