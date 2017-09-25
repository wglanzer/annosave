package com.github.wglanzer.annosave.processor;

import com.github.wglanzer.annosave.api.*;
import com.github.wglanzer.annosave.api.containers.IMethodContainer;
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

    if (ElementUtil.isMethod(pElement))
      return new _MethodContainer((ExecutableElement) pElement);
    return new _AnnotationContainer(pElement);
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

    return new _Annotation(pMirror);
  }

  @NotNull
  private IAnnotationParameter _toParameter(ExecutableElement pElement, AnnotationValue pValue)
  {
    return new _AnnotationParameter(pElement, pValue);
  }

  /**
   * IAnnotation-Impl
   */
  private class _Annotation implements IAnnotation
  {
    private final AnnotationMirror mirror;

    public _Annotation(AnnotationMirror pMirror)
    {
      mirror = pMirror;
    }

    @NotNull
    @Override
    public String getName()
    {
      return mirror.getAnnotationType().toString();
    }

    @NotNull
    @Override
    public IType getType()
    {
      return TypeFactory.create(mirror.getAnnotationType().toString());
    }

    @NotNull
    @Override
    public IAnnotationParameter[] getParameters()
    {
      ArrayList<IAnnotationParameter> params = new ArrayList<>();
      AnnotationMirrors.getAnnotationValuesWithDefaults(mirror).forEach((pName, pValue) -> params.add(_toParameter(pName, pValue)));
      return params.toArray(new IAnnotationParameter[params.size()]);
    }
  }

  /**
   * IAnnotationParameter-Impl
   */
  private class _AnnotationParameter implements IAnnotationParameter
  {
    private final ExecutableElement element;
    private final AnnotationValue value;

    public _AnnotationParameter(ExecutableElement pElement, AnnotationValue pValue)
    {
      element = pElement;
      value = pValue;
    }

    @Override
    public String getName()
    {
      return element.getSimpleName().toString();
    }

    @NotNull
    @Override
    public IType getType()
    {
      return TypeFactory.create(ElementUtil.getClassName(element.getReturnType()));
    }

    @Override
    public Object getValue()
    {
      return _extractValueFromAnnotationValue(value);
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
      if (value instanceof TypeMirror)
        return ElementUtil.getClassName((TypeMirror) value);

      // Already a correct value?
      if (value instanceof String ||
          value.getClass().isPrimitive() ||
          Primitives.isWrapperType(value.getClass()))
        return value;

      return value.toString();
    }
  }

  /**
   * IAnnotationContainer-Impl
   */
  private class _AnnotationContainer implements IAnnotationContainer
  {
    private final Element element;

    public _AnnotationContainer(Element pElement)
    {
      element = pElement;
    }

    @NotNull
    @Override
    public String getName()
    {
      return element.getSimpleName().toString();
    }

    @NotNull
    @Override
    public IType getType()
    {
      String className;
      if (ElementUtil.isField(element))
        className = ElementUtil.getClassName(element.asType());
      else if (ElementUtil.isMethod(element))
        className = ElementUtil.getClassName(((ExecutableElement) element).getReturnType());
      else
        className = ElementUtil.flatName(element, element.toString());
      return TypeFactory.create(className);
    }

    @NotNull
    @Override
    public IAnnotation[] getAnnotations()
    {
      return _getAnnotations(element);
    }

    @NotNull
    @Override
    public IAnnotationContainer[] getChildren()
    {
      return _getChildren(element);
    }
  }

  /**
   * IMethodContainer-Impl
   */
  private class _MethodContainer extends _AnnotationContainer implements IMethodContainer
  {
    private final ExecutableElement element;

    public _MethodContainer(ExecutableElement pElement)
    {
      super(pElement);
      element = pElement;
    }

    @NotNull
    @Override
    public IType[] getMethodParameters()
    {
      return element.getParameters().stream()
          .map(Element::asType)
          .map(pEle -> TypeFactory.create(ElementUtil.getClassName(pEle)))
          .toArray(IType[]::new);
    }
  }

}
