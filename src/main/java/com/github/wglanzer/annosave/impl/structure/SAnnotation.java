package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.*;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotation implements IAnnotation
{

  private Class<?> type;
  private SAnnotationParameter[] parameters;

  @Override
  public Class<?> getType()
  {
    return type;
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
        Arrays.equals(parameters, that.parameters);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(type, parameters);
  }
}
