package com.github.wglanzer.annosave.api;

import com.github.wglanzer.annosave.impl.*;
import com.github.wglanzer.annosave.impl.converter.ConverterFactory;
import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains all common API-Methods for AnnoSave
 *
 * @author W.Glanzer, 13.09.2017
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class AnnoSave
{

  /**
   * Writes all annotations for a class-object (including subclasses) to an outputStream.
   * Those descriptions are made with JSON.
   *
   * @param pClass        Class which should be read and written to stream
   * @param pOutputStream Consuming stream. Closed after return.
   * @return a container representing all annotations inside pClass
   */
  @NotNull
  public static List<IAnnotationContainer> write(@NotNull Class<?> pClass, @NotNull OutputStream pOutputStream)
  {
    List<SAnnotationContainer> containers = ConverterFactory.createDefaultConverter().convert(pClass);
    new AnnoWriter(pOutputStream).write(containers);
    return Collections.unmodifiableList(containers);
  }

  /**
   * Writes all annotations for a object to an outputStream.
   * Those descriptions are made with JSON.
   *
   * @param pRoot         Root-Object which should be read and written to stream
   * @param pConverter    Converter which describes, how the given object is disassembled to an serializable object
   * @param pOutputStream Consuming stream. Closed after return.
   * @return a container representing all annotations inside pClass
   */
  @NotNull
  public static <T> List<IAnnotationContainer> write(T pRoot, IAnnoSaveConverter<T> pConverter, @NotNull OutputStream pOutputStream)
  {
    List<SAnnotationContainer> containers = ConverterFactory.createConverter(pConverter).convert(pRoot);
    new AnnoWriter(pOutputStream).write(containers);
    return Collections.unmodifiableList(containers);
  }

  /**
   * Reads an inputStream and converts the JSON back to an IAnnotationContainer
   *
   * @param pInputStream Stream which should be read completely. Closed afterwards.
   * @return the resulting container
   */
  @NotNull
  public static List<IAnnotationContainer> read(@NotNull InputStream pInputStream)
  {
    return Collections.unmodifiableList(new AnnoReader(pInputStream).read());
  }

  /**
   * Reads an inputStream and converts the JSON back to an IAnnotationContainer
   *
   * @param pInputStream Stream which should be read completely. Closed afterwards.
   * @return the resulting containers as map with name as key
   */
  @NotNull
  public static Map<String, IAnnotationContainer> readAsMap(@NotNull InputStream pInputStream)
  {
    List<IAnnotationContainer> list = read(pInputStream);
    return list.stream().collect(Collectors.toMap(IAnnotationContainer::getName, pE -> pE));
  }

}
