package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.IAnnotationParameter;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotationParameter implements IAnnotationParameter
{

  private String name;
  private Class<?> type;
  private Object value;

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public Class<?> getType()
  {
    return type;
  }

  @Override
  public Object getValue()
  {
    return value;
  }

  public void setName(String pName)
  {
    name = pName;
  }

  public void setType(Class<?> pType)
  {
    type = pType;
  }

  public void setValue(Object pValue)
  {
    value = pValue;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    SAnnotationParameter that = (SAnnotationParameter) pO;
    return Objects.equals(name, that.name) &&
        Objects.equals(type, that.type) &&
        (type.isArray() ?
            Arrays.equals((Object[]) value, (Object[]) that.value) :
            Objects.equals(value, that.value));
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(name, type, value);
  }
}
