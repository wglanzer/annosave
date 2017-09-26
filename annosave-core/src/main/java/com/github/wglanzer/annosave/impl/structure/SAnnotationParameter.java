package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.*;
import com.github.wglanzer.annosave.impl.util.TypeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class SAnnotationParameter implements IAnnotationParameter
{

  private String name;
  private SType type;
  private Object value; //ignored in Json
  private Object originalValue;

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

  @Override
  public Object getValue()
  {
    if(value == null)
    {
      if (originalValue instanceof String && Class.class.equals(type.getInstance()))
      {
        value = TypeFactory.create(originalValue.toString()).getInstance();
      }
      else if ((originalValue instanceof Object[] && !(originalValue instanceof Class[])) &&
          Class[].class.equals(type.getInstance()))
      {
        value = Arrays.stream((Object[]) originalValue)
            .map(pObject -> TypeFactory.create(String.valueOf(pObject)).getInstance())
            .toArray(Class[]::new);
      }
      else
        value = originalValue;
    }

    return value;
  }

  public Object getOriginalValue()
  {
    return originalValue;
  }

  public void setName(String pName)
  {
    name = pName;
  }

  public void setType(SType pType)
  {
    type = pType;
  }

  public void setOriginalValue(Object pOriginalValue)
  {
    originalValue = pOriginalValue;
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
            Arrays.equals((Object[]) originalValue, (Object[]) that.originalValue) :
            Objects.equals(originalValue, that.originalValue));
  }

  @Override
  public int hashCode()
  {
    return Arrays.deepHashCode(new Object[]{type, name, type.isArray() ? (Object[]) originalValue : originalValue});
  }
}
