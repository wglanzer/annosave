package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotation implements IAnnotation
{

  private Class<?> type;
  private String name;
  private SAnnotationParameter[] parameters;

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

  @Override
  public IAnnotationParameter[] getParameters()
  {
    return parameters;
  }

  public void setType(Class<?> pType)
  {
    type = pType;
  }

  public void setName(String pName)
  {
    name = pName;
  }

  public void setParameters(SAnnotationParameter[] pParameters)
  {
    parameters = pParameters;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    SAnnotation that = (SAnnotation) pO;
    return Objects.equals(type, that.type) &&
        Objects.equals(name, that.name) &&
        Arrays.equals(parameters, that.parameters);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(type, parameters);
  }
}
