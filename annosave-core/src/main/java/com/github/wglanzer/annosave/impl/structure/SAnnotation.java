package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotation implements IAnnotation
{

  private String name;
  private SType type;
  private SAnnotationParameter[] parameters;

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
  public IAnnotationParameter[] getParameters()
  {
    return parameters;
  }

  public void setName(String pName)
  {
    name = pName;
  }

  public void setType(SType pType)
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
        Objects.equals(name, that.name) &&
        Arrays.equals(parameters, that.parameters);
  }

  @Override
  public int hashCode()
  {
    return Arrays.deepHashCode(new Object[]{type, name, parameters});
  }
}
