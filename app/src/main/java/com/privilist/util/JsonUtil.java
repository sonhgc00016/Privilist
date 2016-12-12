package com.privilist.util;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author minhtdh
 */
public class JsonUtil {
    private static final String tag = "JsonUtil";
    private static ObjectMapper configMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .withSetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .withCreatorVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY));
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * convert overlaybg object from overlaybg json string
     *
     * @param json
     * @param returnType
     * @return
     */
    public static <T> T fromString(String json, Class<T> returnType) {
        T ret = null;
        if (json != null) {
            ObjectMapper mapper = configMapper();
            try {
                ret = mapper.readValue(json, returnType);
            } catch (JsonParseException e) {
                Log.e(tag, Log.getStackTraceString(e));
                return null;
            } catch (JsonMappingException e) {
                Log.e(tag, Log.getStackTraceString(e));
                return null;
            } catch (IOException e) {
                Log.e(tag, Log.getStackTraceString(e));
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return ret;
    }

    public static <T> T fromFile(File file, Class<T> returnType) {
        T ret = null;
        if (file.exists()) {
            ObjectMapper mapper = configMapper();
            try {
                ret = mapper.readValue(file, returnType);
            } catch (JsonParseException e) {
                Log.e(tag, Log.getStackTraceString(e));
                return null;
            } catch (JsonMappingException e) {
                Log.e(tag, Log.getStackTraceString(e));
                return null;
            } catch (IOException e) {
                Log.e(tag, Log.getStackTraceString(e));
                return null;
            } catch (Exception e) {
                Log.e(tag, Log.getStackTraceString(e));
                return null;
            }
        }
        return ret;
    }

    public static <T> ArrayList<T> fromStringToList(String json, Class<T> item_class) {
        if (json == null)
            return null;

        ArrayList<T> ret;
        ObjectMapper mapper = configMapper();
        mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            JavaType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, item_class);
            ret = mapper.readValue(json, type);
        } catch (JsonParseException e) {
            Log.e(tag, Log.getStackTraceString(e));
            return null;
        } catch (JsonMappingException e) {
            Log.e(tag, Log.getStackTraceString(e));
            return null;
        } catch (IOException e) {
            Log.e(tag, Log.getStackTraceString(e));
            return null;
        } catch (Exception e) {
            Log.e(tag, Log.getStackTraceString(e));
            return null;
        }
        return ret;
    }

    public static String writeString(Object o) {
        String ret = null;
        if (o != null) {
            ObjectMapper om = new ObjectMapper();
            try {
                ret = om.writeValueAsString(o);
            } catch (JsonGenerationException e) {
                Log.e(tag, Log.getStackTraceString(e));
            } catch (JsonMappingException e) {
                Log.e(tag, Log.getStackTraceString(e));
            } catch (IOException e) {
                Log.e(tag, Log.getStackTraceString(e));
            }
        }
        return ret;
    }

    /**
     * COMMENTS : write object as file
     *
     * @param obj
     * @param folderPath
     * @param fileName
     * @return
     */
    public static boolean writeFile(Object obj, String folderPath, String fileName) {
        boolean ret = false;
        if (obj != null) {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if (folder.exists() && folder.isDirectory()) {
                File file = new File(folder, fileName);
                ObjectMapper om = new ObjectMapper();
                try {
                    om.writeValue(file, obj);
                    ret = true;
                } catch (JsonGenerationException e) {
                    Log.e(tag, Log.getStackTraceString(e));
                } catch (JsonMappingException e) {
                    Log.e(tag, Log.getStackTraceString(e));
                } catch (IOException e) {
                    Log.e(tag, Log.getStackTraceString(e));
                }
            }
        }
        return ret;
    }
}
