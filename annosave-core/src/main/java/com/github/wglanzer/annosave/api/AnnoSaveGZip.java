package com.github.wglanzer.annosave.api;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.output.CloseShieldOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.zip.*;

/**
 * Contains API-Methods to compress multiple classes to a .zip-file
 *
 * @author W.Glanzer, 13.09.2017
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
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
  @NotNull
  public static IAnnotationContainer[] write(@NotNull Class<?>[] pClasses, @NotNull File pZipFile)
  {
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(pZipFile)))
    {
      List<IAnnotationContainer> containers = new ArrayList<>();
      for (Class<?> pClass : pClasses)
      {
        zipOutputStream.putNextEntry(new ZipEntry(pClass.getName() + ".json"));
        List<IAnnotationContainer> container = AnnoSave.write(pClass, new CloseShieldOutputStream(zipOutputStream));
        zipOutputStream.closeEntry();
        containers.addAll(container);
      }
      return containers.toArray(new IAnnotationContainer[containers.size()]);
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
  @NotNull
  public static <T> IAnnotationContainer[] write(@NotNull T[] pObjects, @NotNull IAnnoSaveConverter<T> pConverter, @NotNull File pZipFile)
  {
    // Nothing to write -> no zip-file, empty array
    if(pObjects.length == 0)
      return new IAnnotationContainer[0];

    try
    {
      HashMap<String, String> env = new HashMap<>();
      env.put("create", String.valueOf(!pZipFile.exists()));
      env.put("encoding", "UTF-8");
      try (FileSystem zipFs = FileSystems.newFileSystem(URI.create("jar:" + pZipFile.toURI()), env))
      {
        List<IAnnotationContainer> containers = new ArrayList<>();
        for (T pObject : pObjects)
        {
          try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
          {
            List<IAnnotationContainer> container = AnnoSave.write(pObject, pConverter, baos);
            if (container.isEmpty())
              continue;

            Path file = zipFs.getPath("/" + container.get(0).getName() + ".json");
            Files.copy(new ByteArrayInputStream(baos.toByteArray()), file, StandardCopyOption.REPLACE_EXISTING);
            containers.addAll(container);
          }
        }
        return containers.toArray(new IAnnotationContainer[containers.size()]);
      }
    }
    catch(Exception e)
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
  @NotNull
  public static IAnnotationContainer[] read(@NotNull File pZipFile)
  {
    List<IAnnotationContainer> containers = new ArrayList<>();

    try (ZipFile zipFile = new ZipFile(pZipFile))
    {
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements())
      {
        InputStream inputStream = zipFile.getInputStream(entries.nextElement());
        containers.addAll(AnnoSave.read(inputStream));
      }
      return containers.toArray(new IAnnotationContainer[containers.size()]);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Reads all annotation-descriptions from an InputStream
   *
   * @param pStream Stream, which should be read
   * @return all containers
   */
  @NotNull
  public static IAnnotationContainer[] read(@NotNull InputStream pStream)
  {
    List<IAnnotationContainer> containers = new ArrayList<>();

    try (ZipInputStream zipFile = new ZipInputStream(pStream))
    {
      while (zipFile.getNextEntry() != null)
        containers.addAll(AnnoSave.read(new CloseShieldInputStream(zipFile)));
      return containers.toArray(new IAnnotationContainer[containers.size()]);
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
   * @return all containers in a map with name as key
   */
  @NotNull
  public static Map<String, IAnnotationContainer> readAsMap(@NotNull File pZipFile)
  {
    return Stream.of(read(pZipFile)).collect(Collectors.toMap(IAnnotationContainer::getName, pE -> pE));
  }

  /**
   * Reads all annotation-descriptions from an InputStream
   *
   * @param pStream Stream, which should be read
   * @return all containers in a map with name as key
   */
  @NotNull
  public static Map<String, IAnnotationContainer> readAsMap(@NotNull InputStream pStream)
  {
    return Stream.of(read(pStream)).collect(Collectors.toMap(IAnnotationContainer::getName, pE -> pE));
  }

}
