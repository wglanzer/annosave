package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.IType;
import com.google.common.primitives.Primitives;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author W.Glanzer, 20.09.2017
 */
public class SType implements IType
{

  private String name;

  @NotNull
  @Override
  public String getClassName()
  {
    return name;
  }

  @Override
  public boolean isPrimitive()
  {
    return Primitives.allPrimitiveTypes().stream()
        .anyMatch(pPrimClass -> pPrimClass.getName().equals(name));
  }

  @Override
  public boolean isArray()
  {
    return name.startsWith("[");
  }

  @Override
  public Class<?> getInstance()
  {
    try
    {
      if (isPrimitive())
      {
        return Primitives.allPrimitiveTypes().stream()
            .filter(pPrimClass -> pPrimClass.getName().equals(name))
            .findFirst().orElseThrow(IllegalArgumentException::new);
      }
      else
        // "normal" Object
        return Class.forName(name);
    }
    catch (ClassNotFoundException cnfe)
    {
      // Class could not be found
      return null;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void setName(String pName)
  {
    name = pName;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    SType that = (SType) pO;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(name);
  }

  @Override
  public String toString()
  {
    return "SType{" +
        "name='" + name + '\'' +
        '}';
  }
}
