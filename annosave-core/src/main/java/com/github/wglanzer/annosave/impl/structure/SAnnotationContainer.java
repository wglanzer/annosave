package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotationContainer implements IAnnotationContainer
{

  private String name;
  private SType type;
  private SAnnotation[] annotations;
  private SAnnotationContainer[] children;

  @NotNull
  @Override
  public String getName()
  {
    return name;
  }

  @NotNull
  @Override
  public IType getType()
  {
    return type;
  }

  @NotNull
  @Override
  public IAnnotation[] getAnnotations()
  {
    return annotations;
  }

  @NotNull
  @Override
  public IAnnotationContainer[] getChildren()
  {
    return children;
  }

  public void setName(String pName)
  {
    name = pName;
  }

  public void setType(SType pType)
  {
    type = pType;
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
    return Objects.equals(type, that.type) &&
        Objects.equals(name, that.name) &&
        Arrays.equals(annotations, that.annotations) &&
        Arrays.equals(children, that.children);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(type, name, annotations, children);
  }
}
