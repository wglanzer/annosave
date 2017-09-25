package com.github.wglanzer.annosave.impl;

import com.github.wglanzer.annosave.impl.structure.SAnnotationContainer;
import com.github.wglanzer.annosave.impl.util.GsonUtil;
import com.google.gson.*;

import java.io.*;
import java.util.*;

/**
 * Reads an IAnnotationContainer from JSON
 *
 * @author W.Glanzer, 13.09.2017
 */
public class AnnoReader
{

  private final InputStream stream;

  public AnnoReader(InputStream pStream)
  {
    stream = pStream;
  }

  public List<SAnnotationContainer> read()
  {
    Gson gson = GsonUtil.createGson();
    JsonArray rootArray = gson.fromJson(new InputStreamReader(stream), JsonArray.class);
    List<SAnnotationContainer> containers = new ArrayList<>();
    for (JsonElement element : rootArray)
      containers.add(gson.fromJson(element, SAnnotationContainer.class));
    return containers;
  }

}
