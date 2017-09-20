package com.github.wglanzer.annosave.impl.util;

import com.github.wglanzer.annosave.impl.structure.*;
import com.google.common.primitives.Primitives;
import com.google.gson.*;

import java.util.*;

/**
 * @author W.Glanzer, 13.09.2017
 */
public class GsonUtil
{

  /**
   * @return Creates the Gson-Object with all typeAdapters registered
   */
  public static Gson createGson()
  {
    GsonBuilder b = new GsonBuilder();

    // Type-Adapter for "Class"-Type
    b.registerTypeAdapter(Class.class, (JsonSerializer<Class>) (src, typeOfSrc, context) -> {
      JsonObject obj = new JsonObject();
      obj.add("name", context.serialize(Primitives.wrap(src).getName()));
      obj.add("primitive", context.serialize(src.isPrimitive()));
      return obj;
    }).registerTypeAdapter(Class.class, (JsonDeserializer<Class>) (src, typeOfSrc, context) -> {
      try
      {
        JsonObject obj = src.getAsJsonObject();
        String name = context.deserialize(obj.get("name"), String.class);
        Boolean primitive = context.deserialize(obj.get("primitive"), Boolean.class);
        Class<?> clazz = Class.forName(name);
        if(Boolean.TRUE.equals(primitive))
          clazz = Primitives.unwrap(clazz);
        return clazz;
      }
      catch (ClassNotFoundException pE)
      {
        pE.printStackTrace();
        return null;
      }
    });

    // Type-Adapter for "SAnnotationParameter"-Type because of additional "type"-Field
    b.registerTypeAdapter(SAnnotationParameter.class, (JsonSerializer<SAnnotationParameter>) (src, typeOfSrc, context) -> {
      JsonObject obj = new JsonObject();
      obj.add("name", context.serialize(src.getName()));
      obj.add("classsource", context.serialize(src.getType()));
      obj.add("valuetype", context.serialize(src.getOriginalValue().getClass()));
      obj.add("value", context.serialize(src.getOriginalValue()));
      return obj;
    }).registerTypeAdapter(SAnnotationParameter.class, (JsonDeserializer<SAnnotationParameter>) (src, typeOfSrc, context) -> {
      JsonObject obj = src.getAsJsonObject();
      SAnnotationParameter parameter = new SAnnotationParameter();
      parameter.setName(context.deserialize(obj.get("name"), String.class));
      parameter.setType(context.deserialize(obj.get("classsource"), SType.class));
      Class valuetype = context.deserialize(obj.get("valuetype"), Class.class);
      parameter.setOriginalValue(context.deserialize(obj.get("value"), valuetype));
      return parameter;
    });

    // SAnnotationContainer[] to support multiple container-classes
    b.registerTypeAdapter(SAnnotationContainer[].class, (JsonSerializer<SAnnotationContainer[]>) (src, typeOfSrc, context) -> {
      JsonArray array = new JsonArray();
      for (SAnnotationContainer container : src)
      {
        JsonObject ele;
        if(container instanceof SMethodContainer)
          ele = context.serialize(container, SMethodContainer.class).getAsJsonObject();
        else
          ele = context.serialize(container, SAnnotationContainer.class).getAsJsonObject();
        ele.add("__type", context.serialize(container.getClass().getSimpleName(), String.class));
        array.add(ele);
      }
      return array;
    }).registerTypeAdapter(SAnnotationContainer[].class, (JsonDeserializer<SAnnotationContainer[]>) (src, typeOfSrc, context) -> {
      List<SAnnotationContainer> containers = new ArrayList<>();
      for (JsonElement innerEle : src.getAsJsonArray())
      {
        JsonObject obj = innerEle.getAsJsonObject();
        String type = context.deserialize(obj.remove("__type"), String.class);
        if(type.equals(SMethodContainer.class.getSimpleName()))
          containers.add(context.deserialize(obj, SMethodContainer.class));
        else
          containers.add(context.deserialize(obj, SAnnotationContainer.class));
      }
      return containers.toArray(new SAnnotationContainer[containers.size()]);
    });

    b.setPrettyPrinting();
    return b.create();
  }

}
