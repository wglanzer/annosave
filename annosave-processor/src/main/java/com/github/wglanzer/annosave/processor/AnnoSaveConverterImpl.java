package com.github.wglanzer.annosave.processor;

import com.github.wglanzer.annosave.api.*;
import com.github.wglanzer.annosave.impl.util.TypeFactory;
import com.google.auto.common.AnnotationMirrors;
import com.google.common.base.Predicates;
import com.google.common.primitives.Primitives;
import org.jetbrains.annotations.*;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * @author W.Glanzer, 15.09.2017
 */
class AnnoSaveConverterImpl implements IAnnoSaveConverter<Element>
{
  @Override
  public IAnnotationContainer createContainer(Element pElement)
  {
    if (_getChildren(pElement).length == 0 && _getAnnotations(pElement).length == 0)
      return null;

    return new IAnnotationContainer()
    {
      @NotNull
      @Override
      public String getName()
      {
        return pElement.getSimpleName().toString();
      }

      @NotNull
      @Override
      public IType getType()
      {
        String className;
        if(ElementUtil.isField(pElement))
          className = ElementUtil.getClassName(pElement.asType());
        else if(ElementUtil.isMethod(pElement))
          className = ElementUtil.getClassName(((ExecutableElement) pElement).getReturnType());
        else
          className = pElement.toString();
        return TypeFactory.create(className);
      }

      @NotNull
      @Override
      public EContainerType getContainerType()
      {
        if (ElementUtil.isMethod(pElement))
          return EContainerType.METHOD;
        else if (ElementUtil.isField(pElement))
          return EContainerType.FIELD;
        return EContainerType.CLASS;
      }

      @Override
      public IAnnotation[] getAnnotations()
      {
        return _getAnnotations(pElement);
      }

      @Override
      public IAnnotationContainer[] getChildren()
      {
        return _getChildren(pElement);
      }
    };
  }

  @NotNull
  private IAnnotation[] _getAnnotations(Element pElement)
  {
    ArrayList<IAnnotation> annos = new ArrayList<>();
    for (AnnotationMirror annotationMirror : pElement.getAnnotationMirrors())
    {
      IAnnotation anno = _toAnno(annotationMirror);
      if (anno != null)
        annos.add(anno);
    }
    return annos.toArray(new IAnnotation[annos.size()]);
  }

  @NotNull
  private IAnnotationContainer[] _getChildren(Element pElement)
  {
    return pElement.getEnclosedElements().stream()
        .filter(Predicates.not(ElementUtil::isConstructor))
        .map(this::createContainer)
        .filter(Objects::nonNull)
        .toArray(IAnnotationContainer[]::new);
  }

  @Nullable
  private IAnnotation _toAnno(AnnotationMirror pMirror)
  {
    if (!new PersistenceFilter().test(pMirror))
      return null;

    return new IAnnotation()
    {
      @NotNull
      @Override
      public String getName()
      {
        return pMirror.getAnnotationType().toString();
      }

      @NotNull
      @Override
      public IType getType()
      {
        return TypeFactory.create(pMirror.getAnnotationType().toString());
      }

      @Override
      public IAnnotationParameter[] getParameters()
      {
        ArrayList<IAnnotationParameter> params = new ArrayList<>();
        AnnotationMirrors.getAnnotationValuesWithDefaults(pMirror).forEach((pName, pValue) -> params.add(_toParameter(pName, pValue)));
        return params.toArray(new IAnnotationParameter[params.size()]);
      }
    };
  }

  @NotNull
  private IAnnotationParameter _toParameter(ExecutableElement pElement, AnnotationValue pValue)
  {
    return new IAnnotationParameter()
    {
      @Override
      public String getName()
      {
        return pElement.getSimpleName().toString();
      }

      @NotNull
      @Override
      public IType getType()
      {
        return TypeFactory.create(ElementUtil.getClassName(pElement.getReturnType()));
      }

      @Override
      public Object getValue()
      {
        return _extractValueFromAnnotationValue(pValue);
      }

      private Object _extractValueFromAnnotationValue(AnnotationValue pValue)
      {
        Object value = pValue.getValue();
        if (value instanceof List)
        {
          if (((List) value).get(0) instanceof AnnotationMirror)
          {
            return ((List<AnnotationMirror>) value).stream()
                .map(pMirror -> _toAnno(pMirror))
                .toArray(IAnnotation[]::new);
          }
          else if (((List) value).get(0) instanceof AnnotationValue)
          {
            return ((List<AnnotationValue>) value).stream()
                .map(this::_extractValueFromAnnotationValue)
                .toArray(Object[]::new);
          }
        }

        // Maybe it is a Class-Type? (int.class, String.class, ...)
        if(value instanceof TypeMirror)
          return ElementUtil.getClassName((TypeMirror) value);

        // Already a correct value?
        if (value instanceof String ||
            value.getClass().isPrimitive() ||
            Primitives.isWrapperType(value.getClass()))
          return value;

        return value.toString();
      }
    };
  }
}
