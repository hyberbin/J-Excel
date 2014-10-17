/*
 * Copyright 2014 Hyberbin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Email:hyberbin@qq.com
 */
package org.jplus.hyberbin.excel.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: Hyberbin Date: 13-12-3 Time: 下午1:37
 */
public class DicCodePool {

    protected Map<String, Map<String, String>> dicKeyPool = new HashMap<String, Map<String, String>>();
    protected Map<String, Map<String, String>> dicValuePool = new HashMap<String, Map<String, String>>();
    protected Map<String, Set<String>> dicKeySet = new HashMap<String, Set<String>>();
    protected Map<String, Set<String>> dicValueSet = new HashMap<String, Set<String>>();

    /**
     * 添加自定义字典 格式必须是和serviceCombox的数据一致 List<Map>,Map中必须有key和value属性
     * @param mapName
     * @param mapList
     */
    public void addMap(String mapName, List<Map> mapList) {
        if (ObjectHelper.isNotEmpty(mapList)) {
            Map<String, String> keyDicBeanMap = new HashMap<String, String>();
            Map<String, String> valueDicBeanMap = new HashMap<String, String>();
            Set keySet = new LinkedHashSet();
            Set valueSet = new LinkedHashSet();
            for (Map<String, String> map : mapList) {
                String key = map.get("key");
                String value = map.get("value");
                keyDicBeanMap.put(key, value);
                keySet.add(key);
                valueSet.add(value);
                valueDicBeanMap.put(value, key);
            }
            dicKeySet.put(mapName, keySet);
            dicValueSet.put(mapName, valueSet);
            dicKeyPool.put(mapName, keyDicBeanMap);
            dicValuePool.put(mapName, valueDicBeanMap);
        }
    }

    public void addMap(String mapName, String key, String value) {
        Set<String> keySet = dicKeySet.get(mapName);
        if (ObjectHelper.isEmpty(keySet)) {
            keySet = new LinkedHashSet<String>();
            dicKeySet.put(mapName, keySet);
        }
        keySet.add(key);
        Set<String> valueSet = dicValueSet.get(mapName);
        if (ObjectHelper.isEmpty(valueSet)) {
            valueSet = new LinkedHashSet<String>();
            dicValueSet.put(mapName, valueSet);
        }
        valueSet.add(value);
        Map<String, String> keyDicBeanMap = dicKeyPool.get(mapName);
        Map<String, String> valueDicBeanMap = dicValuePool.get(mapName);
        if (ObjectHelper.isEmpty(keyDicBeanMap)) {
            keyDicBeanMap = new HashMap<String, String>();
            dicKeyPool.put(mapName, keyDicBeanMap);
        }
        keyDicBeanMap.put(key, value);
        if (ObjectHelper.isEmpty(valueDicBeanMap)) {
            valueDicBeanMap = new HashMap<String, String>();
            dicValuePool.put(mapName, valueDicBeanMap);
        }
        valueDicBeanMap.put(value,key);        
    }

    /**
     * 根据KEY找字典项
     * @param dicCode
     * @param key
     * @return
     */
    public String getByKey(String dicCode, String key) {
        Map<String, String> keyPool = dicKeyPool.get(dicCode);
        if (ObjectHelper.isEmpty(keyPool)) {
            keyPool = dicKeyPool.get(dicCode);
            if (ObjectHelper.isEmpty(keyPool)) {
                return key;
            }
        }
        return dicKeyPool.get(dicCode).get(key);
    }

    /**
     * 根据VALUE找字典项
     * @param dicCode
     * @param value
     * @return
     */
    public String getByValue(String dicCode, String value) {
        Map<String, String> valuePool = dicValuePool.get(dicCode);
        if (ObjectHelper.isEmpty(valuePool)) {
            valuePool = dicValuePool.get(dicCode);
            if (ObjectHelper.isEmpty(valuePool)) {
                return value;
            }
        }
        return dicValuePool.get(dicCode).get(value);
    }

    public Map<String, Map<String, String>> getDicKeyPool() {
        return dicKeyPool;
    }

    public void setDicKeyPool(Map<String, Map<String, String>> dicKeyPool) {
        this.dicKeyPool = dicKeyPool;
    }

    public Map<String, Set<String>> getDicKeySet() {
        return dicKeySet;
    }

    public void setDicKeySet(Map<String, Set<String>> dicKeySet) {
        this.dicKeySet = dicKeySet;
    }

    public Map<String, Map<String, String>> getDicValuePool() {
        return dicValuePool;
    }

    public void setDicValuePool(Map<String, Map<String, String>> dicValuePool) {
        this.dicValuePool = dicValuePool;
    }

    public Map<String, Set<String>> getDicValueSet() {
        return dicValueSet;
    }

    public void setDicValueSet(Map<String, Set<String>> dicValueSet) {
        this.dicValueSet = dicValueSet;
    }

    public void clear() {
        dicValuePool = null;
        dicValueSet = null;
        dicKeyPool = null;
        dicKeySet = null;
    }

}
