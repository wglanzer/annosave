package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotationContainer implements IAnnotationContainer
{

  private Class<?> type;
  private String name;
  private EContainerType containerType;
  private SAnnotation[] annotations;
  private SAnnotationContainer[] children;

  @Nullable
  @Override
  public Class<?> getType()
  {
    return type;
  }

  @NotNull
  @Override
  public String getName()
  {
    return name;
  }

  @NotNull
  @Override
  public EContainerType getContainerType()
  {
    return containerType;
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

  public void setType(Class<?> pType)
  {
    type = pType;
  }

  public void setContainerType(EContainerType pContainerType)
  {
    containerType = pContainerType;
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
        Objects.equals(containerType, that.containerType) &&
        Arrays.equals(annotations, that.annotations) &&
        Arrays.equals(children, that.children);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(type, name, containerType, annotations, children);
  }
}
