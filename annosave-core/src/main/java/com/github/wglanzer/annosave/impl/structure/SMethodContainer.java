package com.github.wglanzer.annosave.impl.structure;

import com.github.wglanzer.annosave.api.IType;
import com.github.wglanzer.annosave.api.containers.IMethodContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author W.Glanzer, 20.09.2017
 */
public class SMethodContainer extends SAnnotationContainer implements IMethodContainer
{

  private SType[] methodParameters;

  @NotNull
  @Override
  public IType[] getMethodParameters()
  {
    return methodParameters;
  }

  public void setMethodParameters(SType[] pMethodParameters)
  {
    methodParameters = pMethodParameters;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    if (!super.equals(pO)) return false;
    SMethodContainer that = (SMethodContainer) pO;
    return Arrays.equals(methodParameters, that.methodParameters);
  }

  @Override
  public int hashCode()
  {
    return 31 * super.hashCode() +
        Arrays.deepHashCode(new Object[]{methodParameters});
  }

}
