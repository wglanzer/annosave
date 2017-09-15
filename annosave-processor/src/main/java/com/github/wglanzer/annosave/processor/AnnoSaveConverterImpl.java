package com.github.wglanzer.annosave.processor;

import com.github.wglanzer.annosave.api.*;
import com.google.common.base.Predicates;
import com.google.common.primitives.Primitives;
import org.jetbrains.annotations.*;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import java.util.*;

/**
 * @author W.Glanzer, 15.09.2017
 */
class AnnoSaveConverterImpl implements IAnnoSaveConverter<Element>
{
  @Override
  public IAnnotationContainer createContainer(Element pElement)
  {
    if(_getChildren(pElement) == null)
      return null;
    
    return new IAnnotationContainer()
    {
      @Nullable
      @Override
      public Class<?> getType()
      {
        return null;
      }

      @NotNull
      @Override
      public String getName()
      {
        return pElement.getSimpleName().toString();
      }

      @Override
      public IAnnotation[] getAnnotations()
      {
        ArrayList<IAnnotation> annos = new ArrayList<>();
        for (AnnotationMirror annotationMirror : pElement.getAnnotationMirrors())
        {
          IAnnotation anno = _toAnno(annotationMirror);
          if(anno != null)
            annos.add(anno);
        }
        return annos.toArray(new IAnnotation[annos.size()]);
      }

      @Override
      public IAnnotationContainer[] getChildren()
      {
        return _getChildren(pElement);
      }
    };
  }

  @Nullable
  private IAnnotationContainer[] _getChildren(Element pElement)
  {
    return pElement.getEnclosedElements().stream()
        .filter(Predicates.not(Predicates.in(ElementFilter.constructorsIn(pElement.getEnclosedElements()))))
        .map(element -> {
          IAnnotationContainer container = createContainer(element);
          if (container.getChildren() != null)
            return container;
          return null;
        })
        .filter(Objects::nonNull)
        .toArray(IAnnotationContainer[]::new);
  }

  @Nullable
  private IAnnotation _toAnno(AnnotationMirror pMirror)
  {
    if(!new PersistenceFilter().test(pMirror))
      return null;

    return new IAnnotation()
    {
      @Override
      public Class<?> getType()
      {
        return null;
      }

      @NotNull
      @Override
      public String getName()
      {
        return pMirror.getAnnotationType().toString();
      }

      @Override
      public IAnnotationParameter[] getParameters()
      {
        ArrayList<IAnnotationParameter> params = new ArrayList<>();
        pMirror.getElementValues().forEach((pName, pValue) -> params.add(_toParameter(pName, pValue)));
        return params.toArray(new IAnnotationParameter[params.size()]);
      }
    };
  }

  @NotNull
  private IAnnotationParameter _toParameter(ExecutableElement pName, AnnotationValue pValue)
  {
    return new IAnnotationParameter()
    {
      @Override
      public String getName()
      {
        return pName.getSimpleName().toString();
      }

      @Override
      public Class<?> getType()
      {
        return null;
      }

      @Override
      public Object getValue()
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
                .map(pMirror -> pMirror.getValue().toString())
                .toArray(Object[]::new);
          }
        }

        if (value instanceof String ||
            value.getClass().isPrimitive() ||
            Primitives.isWrapperType(value.getClass()))
          return value;

        return pValue.toString();
      }
    };
  }
}
