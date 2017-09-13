package com.github.wglanzer.annosave.api;

import com.github.wglanzer.annosave.impl.*;
import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * Contains all common API-Methods for AnnoSave
 *
 * @author W.Glanzer, 13.09.2017
 */
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
  public static IAnnotationContainer write(@NotNull Class<?> pClass, @NotNull OutputStream pOutputStream)
  {
    SAnnotationContainer container = AnnoSaveConverter.convert(pClass);
    new AnnoWriter(pOutputStream).write(container);
    return container;
  }

  /**
   * Reads an inputStream and converts the JSON back to an IAnnotationContainer
   *
   * @param pInputStream Stream which should be read completely. Closed afterwards.
   * @return the resulting container
   */
  @NotNull
  public static IAnnotationContainer read(@NotNull InputStream pInputStream)
  {
    return new AnnoReader(pInputStream).read();
  }

}
