package com.github.wglanzer.annosave.api;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CloseShieldOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * Contains API-Methods to compress multiple classes to a .zip-file
 *
 * @author W.Glanzer, 13.09.2017
 */
public class AnnoSaveGZip
{

  /**
   * Saves all annotation-descriptions from all classes
   * inside a .zip-File which is located at pZipFile
   *
   * @param pClasses All classes which should be stored
   * @param pZipFile Path to zip-file
   * @return all created containers
   */
  public static IAnnotationContainer[] write(@NotNull Class<?>[] pClasses, @NotNull File pZipFile)
  {
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(pZipFile)))
    {
      IAnnotationContainer[] containers = new IAnnotationContainer[pClasses.length];
      for (int i = 0; i < pClasses.length; i++)
      {
        zipOutputStream.putNextEntry(new ZipEntry(pClasses[i].getName() + ".json"));
        IAnnotationContainer container = AnnoSave.write(pClasses[i], new CloseShieldOutputStream(zipOutputStream));
        zipOutputStream.closeEntry();
        containers[i] = container;
      }
      return containers;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Saves all annotation-descriptions
   * inside a .zip-File which is located at pZipFile
   *
   * @param pObjects   All objects which should be stored
   * @param pConverter Converter to convert T-objects to saveable IAnnotation-Objects
   * @param pZipFile   Path to zip-file
   * @return all created containers
   */
  public static <T> IAnnotationContainer[] write(T[] pObjects, IAnnoSaveConverter<T> pConverter, @NotNull File pZipFile)
  {
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(pZipFile)))
    {
      IAnnotationContainer[] containers = new IAnnotationContainer[pObjects.length];
      for (int i = 0; i < pObjects.length; i++)
      {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
          IAnnotationContainer container = AnnoSave.write(pObjects[i], pConverter, baos);
          zipOutputStream.putNextEntry(new ZipEntry(container.getName() + ".json"));
          IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), zipOutputStream);
          zipOutputStream.closeEntry();
          containers[i] = container;
        }
      }
      return containers;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Reads all annotation-descriptions from a .zip-File
   *
   * @param pZipFile File, which should be read
   * @return all containers
   */
  public static IAnnotationContainer[] read(@NotNull File pZipFile)
  {
    List<IAnnotationContainer> containers = new ArrayList<>();

    try (ZipFile zipFile = new ZipFile(pZipFile))
    {
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements())
      {
        InputStream inputStream = zipFile.getInputStream(entries.nextElement());
        containers.add(AnnoSave.read(inputStream));
      }
      return containers.toArray(new IAnnotationContainer[containers.size()]);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

}
