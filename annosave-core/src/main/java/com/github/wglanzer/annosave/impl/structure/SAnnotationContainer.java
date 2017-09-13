package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.*;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotationContainer implements IAnnotationContainer
{

  private String name;
  private SAnnotation[] annotations;
  private SAnnotationContainer[] children;

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public IAnnotation[] getAnnotations()
  {
    return annotations;
  }

  @Override
  public IAnnotationContainer[] getChildren()
  {
    return children;
  }

  public void setName(String pName)
  {
    name = pName;
  }

  public void setAnnotations(SAnnotation[] pAnnotations)
  {
    annotations = pAnnotations;
  }

  public void setChildren(SAnnotationContainer[] pChildren)
  {
    children = pChildren;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    SAnnotationContainer that = (SAnnotationContainer) pO;
    return Objects.equals(name, that.name) &&
        Arrays.equals(annotations, that.annotations) &&
        Arrays.equals(children, that.children);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(name, annotations, children);
  }
}
