/*
 * Copyright 2015 www.hyberbin.com.
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
package org.jplus.hyberbin.excel.adapter;

import org.jplus.hyberbin.excel.utils.DicCodePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Hyberbin
 * Date: 13-12-4
 * Time: 下午2:04
 */
public abstract class IOAdapter {
    protected final transient Logger log = LoggerFactory.getLogger(getClass());
//    protected Logger log = LoggerFactory.getLogger(getClass());
    protected DicCodePool dicCodePool;

    public IOAdapter(DicCodePool dicCodePool) {
        this.dicCodePool = dicCodePool;
    }

    public void finish() {
        dicCodePool.clear();
    }

}
