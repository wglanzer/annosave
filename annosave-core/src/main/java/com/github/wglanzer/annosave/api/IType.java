package com.github.wglanzer.annosave.api;

import com.github.wglanzer.annosave.impl.util.TypeFactory;
import org.jetbrains.annotations.*;

/**
 * An IType can hold "class-references" identified by name
 *
 * @author W.Glanzer, 20.09.2017
 */
public interface IType
{

  /**
   * @return the classname, not <tt>null</tt>
   */
  @NotNull
  String getClassName();

  /**
   * @return <tt>true</tt> if the type represents a primitive type (int, double, float, ...)
   */
  boolean isPrimitive();

  /**
   * @return <tt>true</tt> if the type represents an array ("[I", "[Ljava.langString;", ...)
   */
  boolean isArray();

  /**
   * @return a new instance of the capsuled class-reference. <tt>null</tt> if the class was not found on classpath
   */
  @Nullable
  Class<?> getInstance();

  /**
   * Creates a new IType-instance representing a class with the given name
   *
   * @param pClassName classname, it does not have to be on classpath
   * @return the type
   */
  @NotNull
  default IType of(@NotNull String pClassName)
  {
    return TypeFactory.create(pClassName);
  }

  /**
   * Creates a new IType-instance representing the given class
   *
   * @param pClass Class to represent
   * @return the type
   */
  @NotNull
  default IType of(@NotNull Class<?> pClass)
  {
    return of(pClass.getName());
  }

}
